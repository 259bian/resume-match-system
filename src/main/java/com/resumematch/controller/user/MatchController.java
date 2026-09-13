package com.resumematch.controller.user;

import com.resumematch.common.R;
import com.resumematch.dto.MatchRequestDTO;
import com.resumematch.service.MatchService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @PostMapping("/single")
    public R<?> matchResumeToJob(@RequestBody MatchRequestDTO dto) {
        return matchService.matchResumeToJob(dto);
    }

    @PostMapping("/batch/{resumeId}")
    public R<?> batchMatch(@PathVariable Long resumeId) {
        return matchService.batchMatch(resumeId);
    }

    @GetMapping("/history")
    public R<?> getMatchHistory(HttpServletRequest request,
                                @RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = "10") Integer size) {
        Long userId = (Long) request.getAttribute("userId");
        return matchService.getMatchHistory(userId, page, size);
    }

    @GetMapping("/{id}")
    public R<?> getMatchDetail(@PathVariable Long id) {
        return matchService.getMatchDetail(id);
    }
}
