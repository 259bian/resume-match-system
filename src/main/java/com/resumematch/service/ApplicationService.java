package com.resumematch.service;

import com.resumematch.common.R;
import com.resumematch.dto.ApplicationDTO;
import jakarta.servlet.http.HttpServletResponse;

public interface ApplicationService {

    R apply(Long userId, ApplicationDTO applicationDTO);

    R listMyApplications(Long userId, Integer page, Integer size, Integer status);

    R getApplicationDetail(Long id);

    R updateStatus(Long id, Integer status, String remark);

    R withdrawApplication(Long id, Long userId);

    R listAllApplications(Integer page, Integer size, Integer status);

    R exportApplications(HttpServletResponse response);
}
