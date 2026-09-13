package com.resumematch.controller.user;

import com.resumematch.common.R;
import com.resumematch.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/optimize/{resumeId}")
    public R<?> optimizeResume(@PathVariable Long resumeId) {
        return aiService.optimizeResume(resumeId);
    }

    @PostMapping("/polish")
    public R<?> polishText(@RequestBody Map<String, String> body) {
        String text = body.get("text");
        return aiService.polishText(text);
    }

    @GetMapping("/keywords/{resumeId}")
    public R<?> extractKeywords(@PathVariable Long resumeId) {
        return aiService.extractKeywords(resumeId);
    }

    @GetMapping("/skills/{resumeId}")
    public R<?> extractCategorizedSkills(@PathVariable Long resumeId) {
        return aiService.extractCategorizedSkills(resumeId);
    }

    @GetMapping("/similarity/{resumeId}")
    public R<?> checkSimilarity(@PathVariable Long resumeId) {
        return aiService.checkSimilarity(resumeId);
    }
}
