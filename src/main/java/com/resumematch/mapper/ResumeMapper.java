package com.resumematch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.resumematch.entity.Resume;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ResumeMapper extends BaseMapper<Resume> {
}
