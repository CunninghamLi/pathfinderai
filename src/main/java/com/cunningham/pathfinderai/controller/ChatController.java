package com.cunningham.pathfinderai.controller;

import com.cunningham.pathfinderai.dto.ChatRequest;
import com.cunningham.pathfinderai.dto.ChatResponse;
import com.cunningham.pathfinderai.dto.JobDto;
import com.cunningham.pathfinderai.service.JobSearchService;
import com.cunningham.pathfinderai.service.SkillExtractor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final JobSearchService jobSearchService;
    private final SkillExtractor skillExtractor;

    public ChatController(JobSearchService jobSearchService, SkillExtractor skillExtractor) {
        this.jobSearchService = jobSearchService;
        this.skillExtractor = skillExtractor;
    }

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        String userMessage = request.getMessage();

        // 1) Extract skills from the user message
        List<String> skills = skillExtractor.extractSkills(userMessage);

        // 2) Get mocked jobs based on the skills / message
        List<JobDto> jobs = jobSearchService.searchJobs(userMessage);

        // 3) Build a simple reply text (no OpenAI)
        String reply = buildSimpleReply(userMessage, skills, jobs);

        // 4) Return reply + jobs list
        ChatResponse response = new ChatResponse(reply, jobs);
        return ResponseEntity.ok(response);
    }

    private String buildSimpleReply(String userMessage, List<String> skills, List<JobDto> jobs) {
        StringBuilder sb = new StringBuilder();

        if (!skills.isEmpty()) {
            sb.append("You mentioned these skills or technologies: ")
                    .append(String.join(", ", skills))
                    .append(".\n");
        } else {
            sb.append("I did not detect any specific skills in your message yet.\n");
        }

        sb.append("Based on this, here are some jobs that could fit you:\n\n");

        int index = 1;
        for (JobDto job : jobs) {
            sb.append(index++)
                    .append(". ")
                    .append(job.getTitle())
                    .append(" at ")
                    .append(job.getCompany())
                    .append(" (")
                    .append(job.getLocation())
                    .append(")\n");
        }

        sb.append("\nIf you tell me more about your experience level or location preferences, I can refine the recommendations.");

        return sb.toString();
    }
}
