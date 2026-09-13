package com.resumematch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.resumematch.common.Constants;
import com.resumematch.common.R;
import com.resumematch.dto.ResumeDTO;
import com.resumematch.entity.Resume;
import com.resumematch.entity.ResumeSkill;
import com.resumematch.entity.ResumeVersion;
import com.resumematch.entity.User;
import com.resumematch.mapper.ResumeMapper;
import com.resumematch.mapper.ResumeSkillMapper;
import com.resumematch.mapper.ResumeVersionMapper;
import com.resumematch.mapper.UserMapper;
import com.resumematch.service.ResumeService;
import com.resumematch.ai.KeywordExtractor;
import com.resumematch.util.*;
import com.resumematch.vo.ResumeVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final ResumeMapper resumeMapper;
    private final ResumeVersionMapper resumeVersionMapper;
    private final ResumeSkillMapper resumeSkillMapper;
    private final UserMapper userMapper;
    private final OssUtil ossUtil;
    private final FileUtil fileUtil;
    private final PdfUtil pdfUtil;
    private final ExcelUtil excelUtil;
    private final KeywordExtractor keywordExtractor;

    @Override
    @Transactional
    public R uploadResume(Long userId, MultipartFile file, String title) {
        if (file == null || file.isEmpty()) {
            return R.fail("请选择要上传的文件");
        }
        try {
            // 上传到OSS
            String fileUrl = ossUtil.uploadFile(file, "resume");
            // 解析文件内容
            String content = fileUtil.extractText(file);
            // 文件信息
            String originalFilename = file.getOriginalFilename();
            String fileType = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileType = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            }

            // 保存简历记录
            Resume resume = new Resume();
            resume.setUserId(userId);
            resume.setTitle(title);
            resume.setFileName(originalFilename);
            resume.setFileUrl(fileUrl);
            resume.setFileSize(file.getSize());
            resume.setFileType(fileType);
            resume.setContent(content);
            resume.setCurrentVersion(1);
            resume.setIsPublic(Constants.PRIVATE);
            resume.setMatchScore(0);
            resumeMapper.insert(resume);

            // 创建第一个版本记录
            ResumeVersion version = new ResumeVersion();
            version.setResumeId(resume.getId());
            version.setVersion(1);
            version.setFileName(originalFilename);
            version.setFileUrl(fileUrl);
            version.setContent(content);
            version.setIsCurrent(Constants.VERSION_CURRENT);
            resumeVersionMapper.insert(version);

            // 提取技能关键词
            List<String> keywords = pdfUtil.extractKeywords(content);
            for (String keyword : keywords) {
                ResumeSkill resumeSkill = new ResumeSkill();
                resumeSkill.setResumeId(resume.getId());
                resumeSkill.setSkillName(keyword);
                resumeSkillMapper.insert(resumeSkill);
            }

            return R.ok(resume.getId());
        } catch (Exception e) {
            log.error("简历上传失败", e);
            return R.fail("简历上传失败: " + e.getMessage());
        }
    }

    @Override
    public R listMyResumes(Long userId, Integer page, Integer size) {
        Page<Resume> pageObj = new Page<>(page, size);
        LambdaQueryWrapper<Resume> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Resume::getUserId, userId);
        wrapper.orderByDesc(Resume::getCreateTime);
        resumeMapper.selectPage(pageObj, wrapper);

        List<ResumeVO> voList = pageObj.getRecords().stream().map(resume -> {
            ResumeVO vo = new ResumeVO();
            vo.setId(resume.getId());
            vo.setUserId(resume.getUserId());
            vo.setUserName(getUserName(resume.getUserId()));
            vo.setTitle(resume.getTitle());
            vo.setFileName(resume.getFileName());
            vo.setFileUrl(resume.getFileUrl());
            vo.setFileSize(resume.getFileSize());
            vo.setFileType(resume.getFileType());
            vo.setContent(resume.getContent());
            vo.setCurrentVersion(resume.getCurrentVersion());
            vo.setIsPublic(resume.getIsPublic());
            vo.setMatchScore(resume.getMatchScore());
            vo.setCreateTime(resume.getCreateTime());
            vo.setUpdateTime(resume.getUpdateTime());

            LambdaQueryWrapper<ResumeSkill> skillWrapper = new LambdaQueryWrapper<>();
            skillWrapper.eq(ResumeSkill::getResumeId, resume.getId());
            List<ResumeSkill> skills = resumeSkillMapper.selectList(skillWrapper);
            vo.setSkills(skills.stream().map(ResumeSkill::getSkillName).collect(Collectors.toList()));
            return vo;
        }).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("records", voList);
        result.put("total", pageObj.getTotal());
        return R.ok(result);
    }

    @Override
    public R getResumeDetail(Long resumeId) {
        Resume resume = resumeMapper.selectById(resumeId);
        if (resume == null) {
            return R.fail("简历不存在");
        }

        ResumeVO vo = new ResumeVO();
        vo.setId(resume.getId());
        vo.setUserId(resume.getUserId());
        vo.setUserName(getUserName(resume.getUserId()));
        vo.setTitle(resume.getTitle());
        vo.setFileName(resume.getFileName());
        vo.setFileUrl(resume.getFileUrl());
        vo.setFileSize(resume.getFileSize());
        vo.setFileType(resume.getFileType());
        vo.setContent(resume.getContent());
        vo.setCurrentVersion(resume.getCurrentVersion());
        vo.setIsPublic(resume.getIsPublic());
        vo.setMatchScore(resume.getMatchScore());
        vo.setCreateTime(resume.getCreateTime());
        vo.setUpdateTime(resume.getUpdateTime());

        // 获取技能列表
        LambdaQueryWrapper<ResumeSkill> skillWrapper = new LambdaQueryWrapper<>();
        skillWrapper.eq(ResumeSkill::getResumeId, resume.getId());
        List<ResumeSkill> skills = resumeSkillMapper.selectList(skillWrapper);
        vo.setSkills(skills.stream().map(ResumeSkill::getSkillName).collect(Collectors.toList()));

        return R.ok(vo);
    }

   /* @Override
    public R updateResume(ResumeDTO resumeDTO) {
        Resume resume = resumeMapper.selectById(resumeDTO.getId());
        if (resume == null) {
            return R.fail("简历不存在");
        }
        if (resumeDTO.getTitle() != null) {
            resume.setTitle(resumeDTO.getTitle());
        }
        if (resumeDTO.getIsPublic() != null) {
            resume.setIsPublic(resumeDTO.getIsPublic());
        }
        resumeMapper.updateById(resume);
        return R.ok("简历更新成功");
    }*/
   @Override
   @Transactional
   public R updateResume(ResumeDTO resumeDTO) {
       Resume resume = resumeMapper.selectById(resumeDTO.getId());
       if (resume == null) {
           return R.fail("简历不存在");
       }

       // 1. 更新字段
       if (resumeDTO.getTitle() != null) {
           resume.setTitle(resumeDTO.getTitle());
       }
       if (resumeDTO.getContent() != null) {
           resume.setContent(resumeDTO.getContent());
       }
       if (resumeDTO.getIsPublic() != null) {
           resume.setIsPublic(resumeDTO.getIsPublic());
       }

       // 2. 创建新版本（关键！）
       // 把旧版本标记为历史
       LambdaQueryWrapper<ResumeVersion> currentWrapper = new LambdaQueryWrapper<>();
       currentWrapper.eq(ResumeVersion::getResumeId, resume.getId());
       currentWrapper.eq(ResumeVersion::getIsCurrent, Constants.VERSION_CURRENT);
       ResumeVersion currentVersion = resumeVersionMapper.selectOne(currentWrapper);
       if (currentVersion != null) {
           currentVersion.setIsCurrent(Constants.VERSION_HISTORY);
           resumeVersionMapper.updateById(currentVersion);
       }

       // 新版本号 +1
       int newVersion = resume.getCurrentVersion() + 1;

       // 插入新版本
       ResumeVersion newVersionRecord = new ResumeVersion();
       newVersionRecord.setResumeId(resume.getId());
       newVersionRecord.setVersion(newVersion);
       newVersionRecord.setFileName(resume.getFileName());
       newVersionRecord.setFileUrl(resume.getFileUrl());
       newVersionRecord.setContent(resume.getContent());
       newVersionRecord.setIsCurrent(Constants.VERSION_CURRENT);
       resumeVersionMapper.insert(newVersionRecord);

       // 3. 更新主表版本号
       resume.setCurrentVersion(newVersion);
       resumeMapper.updateById(resume);

       return R.ok("保存成功，已生成新版本 v" + newVersion);
   }

    @Override
    @Transactional
    public R deleteResume(Long resumeId) {
        Resume resume = resumeMapper.selectById(resumeId);
        if (resume == null) {
            return R.fail("简历不存在");
        }
        // 删除OSS文件
        ossUtil.deleteFile(resume.getFileUrl());
        // 删除版本文件
        LambdaQueryWrapper<ResumeVersion> versionWrapper = new LambdaQueryWrapper<>();
        versionWrapper.eq(ResumeVersion::getResumeId, resumeId);
        List<ResumeVersion> versions = resumeVersionMapper.selectList(versionWrapper);
        for (ResumeVersion version : versions) {
            if (version.getFileUrl() != null) {
                ossUtil.deleteFile(version.getFileUrl());
            }
        }
        // 删除
        resumeMapper.deleteById(resumeId);
        resumeVersionMapper.delete(versionWrapper);
        LambdaQueryWrapper<ResumeSkill> skillWrapper = new LambdaQueryWrapper<>();
        skillWrapper.eq(ResumeSkill::getResumeId, resumeId);
        resumeSkillMapper.delete(skillWrapper);
        return R.ok("简历已删除");
    }

    @Override
    @Transactional
    public R uploadNewVersion(Long resumeId, MultipartFile file) {
        Resume resume = resumeMapper.selectById(resumeId);
        if (resume == null) {
            return R.fail("简历不存在");
        }
        if (file == null || file.isEmpty()) {
            return R.fail("请选择要上传的文件");
        }
        try {
            // 上传新版文件到OSS
            String fileUrl = ossUtil.uploadFile(file, "resume");
            String content = fileUtil.extractText(file);
            String originalFilename = file.getOriginalFilename();

            // 将当前版本标记为历史
            LambdaQueryWrapper<ResumeVersion> currentWrapper = new LambdaQueryWrapper<>();
            currentWrapper.eq(ResumeVersion::getResumeId, resumeId);
            currentWrapper.eq(ResumeVersion::getIsCurrent, Constants.VERSION_CURRENT);
            ResumeVersion currentVersion = resumeVersionMapper.selectOne(currentWrapper);
            if (currentVersion != null) {
                currentVersion.setIsCurrent(Constants.VERSION_HISTORY);
                resumeVersionMapper.updateById(currentVersion);
            }

            // 获取新版本号
            int newVersionNum = resume.getCurrentVersion() + 1;

            // 创建新版本
            ResumeVersion newVersion = new ResumeVersion();
            newVersion.setResumeId(resumeId);
            newVersion.setVersion(newVersionNum);
            newVersion.setFileName(originalFilename);
            newVersion.setFileUrl(fileUrl);
            newVersion.setContent(content);
            newVersion.setIsCurrent(Constants.VERSION_CURRENT);
            resumeVersionMapper.insert(newVersion);

            // 更新简历
            resume.setCurrentVersion(newVersionNum);
            resume.setContent(content);
            resume.setFileName(originalFilename);
            resume.setFileUrl(fileUrl);
            resumeMapper.updateById(resume);

            // 更新技能
            LambdaQueryWrapper<ResumeSkill> skillWrapper = new LambdaQueryWrapper<>();
            skillWrapper.eq(ResumeSkill::getResumeId, resumeId);
            resumeSkillMapper.delete(skillWrapper);

            List<String> keywords = pdfUtil.extractKeywords(content);
            for (String keyword : keywords) {
                ResumeSkill resumeSkill = new ResumeSkill();
                resumeSkill.setResumeId(resumeId);
                resumeSkill.setSkillName(keyword);
                resumeSkillMapper.insert(resumeSkill);
            }

            return R.ok(newVersionNum);
        } catch (Exception e) {
            log.error("新版本上传失败", e);
            return R.fail("版本上传失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public R saveOptimizedVersion(Long resumeId, String content) {
        Resume resume = resumeMapper.selectById(resumeId);
        if (resume == null) {
            return R.fail("简历不存在");
        }

        // 将当前版本标记为历史
        LambdaQueryWrapper<ResumeVersion> currentWrapper = new LambdaQueryWrapper<>();
        currentWrapper.eq(ResumeVersion::getResumeId, resumeId);
        currentWrapper.eq(ResumeVersion::getIsCurrent, Constants.VERSION_CURRENT);
        ResumeVersion currentVersion = resumeVersionMapper.selectOne(currentWrapper);
        if (currentVersion != null) {
            currentVersion.setIsCurrent(Constants.VERSION_HISTORY);
            resumeVersionMapper.updateById(currentVersion);
        }

        int newVersionNum = resume.getCurrentVersion() + 1;

        ResumeVersion newVersion = new ResumeVersion();
        newVersion.setResumeId(resumeId);
        newVersion.setVersion(newVersionNum);
        newVersion.setFileName(resume.getFileName());
        newVersion.setFileUrl(resume.getFileUrl());
        newVersion.setContent(content);
        newVersion.setIsCurrent(Constants.VERSION_CURRENT);
        resumeVersionMapper.insert(newVersion);

        resume.setCurrentVersion(newVersionNum);
        resume.setContent(content);
        resumeMapper.updateById(resume);

        // 重新提取技能关键词
        LambdaQueryWrapper<ResumeSkill> skillWrapper = new LambdaQueryWrapper<>();
        skillWrapper.eq(ResumeSkill::getResumeId, resumeId);
        resumeSkillMapper.delete(skillWrapper);
        List<String> keywords = pdfUtil.extractKeywords(content);
        for (String keyword : keywords) {
            ResumeSkill resumeSkill = new ResumeSkill();
            resumeSkill.setResumeId(resumeId);
            resumeSkill.setSkillName(keyword);
            resumeSkillMapper.insert(resumeSkill);
        }

        return R.ok(newVersionNum);
    }

    @Override
    public R getVersions(Long resumeId) {
        LambdaQueryWrapper<ResumeVersion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ResumeVersion::getResumeId, resumeId);
        wrapper.orderByDesc(ResumeVersion::getVersion);
        List<ResumeVersion> versions = resumeVersionMapper.selectList(wrapper);
        return R.ok(versions);
    }

    @Override
    @Transactional
    public R switchVersion(Long resumeId, Integer version) {
        Resume resume = resumeMapper.selectById(resumeId);
        if (resume == null) {
            return R.fail("简历不存在");
        }

        LambdaQueryWrapper<ResumeVersion> targetWrapper = new LambdaQueryWrapper<>();
        targetWrapper.eq(ResumeVersion::getResumeId, resumeId);
        targetWrapper.eq(ResumeVersion::getVersion, version);
        ResumeVersion targetVersion = resumeVersionMapper.selectOne(targetWrapper);
        if (targetVersion == null) {
            return R.fail("指定版本不存在");
        }

        // 将所有版本标记为非当前
        LambdaQueryWrapper<ResumeVersion> allWrapper = new LambdaQueryWrapper<>();
        allWrapper.eq(ResumeVersion::getResumeId, resumeId);
        List<ResumeVersion> allVersions = resumeVersionMapper.selectList(allWrapper);
        for (ResumeVersion v : allVersions) {
            v.setIsCurrent(Constants.VERSION_HISTORY);
            resumeVersionMapper.updateById(v);
        }

        // 设置目标版本为当前
        targetVersion.setIsCurrent(Constants.VERSION_CURRENT);
        resumeVersionMapper.updateById(targetVersion);

        // 更新简历主体
        resume.setCurrentVersion(version);
        resume.setContent(targetVersion.getContent());
        resume.setFileName(targetVersion.getFileName());
        resume.setFileUrl(targetVersion.getFileUrl());
        resumeMapper.updateById(resume);

        return R.ok("版本切换成功");
    }

    @Override
    public R checkSimilarity(Long resumeId) {
        Resume resume = resumeMapper.selectById(resumeId);
        if (resume == null) {
            return R.fail("简历不存在");
        }

        // 获取所有其他公开简历的内容
        LambdaQueryWrapper<Resume> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(Resume::getId, resumeId);
        wrapper.eq(Resume::getIsPublic, Constants.PUBLIC);
        wrapper.isNotNull(Resume::getContent);
        List<Resume> otherResumes = resumeMapper.selectList(wrapper);

        List<String> otherContents = otherResumes.stream()
                .map(Resume::getContent)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Map<String, Object> similarityResult = keywordExtractor.checkSimilarity(resume.getContent(), otherContents);
        return R.ok(similarityResult);
    }

    @Override
    public R listAllResumes(Integer page, Integer size) {
        Page<Resume> pageObj = new Page<>(page, size);
        resumeMapper.selectPage(pageObj, null);

        List<ResumeVO> voList = pageObj.getRecords().stream().map(resume -> {
            ResumeVO vo = new ResumeVO();
            vo.setId(resume.getId());
            vo.setUserId(resume.getUserId());
            vo.setUserName(getUserName(resume.getUserId()));
            vo.setTitle(resume.getTitle());
            vo.setFileName(resume.getFileName());
            vo.setFileUrl(resume.getFileUrl());
            vo.setFileSize(resume.getFileSize());
            vo.setFileType(resume.getFileType());
            vo.setContent(resume.getContent());
            vo.setCurrentVersion(resume.getCurrentVersion());
            vo.setIsPublic(resume.getIsPublic());
            vo.setMatchScore(resume.getMatchScore());
            vo.setCreateTime(resume.getCreateTime());
            vo.setUpdateTime(resume.getUpdateTime());

            LambdaQueryWrapper<ResumeSkill> skillWrapper = new LambdaQueryWrapper<>();
            skillWrapper.eq(ResumeSkill::getResumeId, resume.getId());
            List<ResumeSkill> skills = resumeSkillMapper.selectList(skillWrapper);
            vo.setSkills(skills.stream().map(ResumeSkill::getSkillName).collect(Collectors.toList()));
            return vo;
        }).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("records", voList);
        result.put("total", pageObj.getTotal());
        return R.ok(result);
    }

    @Override
    public R exportResumes(HttpServletResponse response) {
        List<Resume> resumes = resumeMapper.selectList(null);
        excelUtil.export(response, "简历数据", "简历列表", Resume.class, resumes);
        return R.ok();
    }

    private String getUserName(Long userId) {
        if (userId == null) return null;
        User user = userMapper.selectById(userId);
        return user != null ? user.getRealName() : null;
    }
}
