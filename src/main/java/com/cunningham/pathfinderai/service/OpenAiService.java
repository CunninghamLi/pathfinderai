package com.cunningham.pathfinderai.service;

import com.cunningham.pathfinderai.dto.JobDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpenAiService {

    private final SkillExtractor skillExtractor;

    public OpenAiService(SkillExtractor skillExtractor) {
        this.skillExtractor = skillExtractor;
    }

    public String buildAiReply(String userMessage, List<JobDto> jobs) {
        // 1) Extract skills from the user message
        List<String> skills = skillExtractor.extractSkills(userMessage);

        StringBuilder sb = new StringBuilder();

        if (skills.isEmpty()) {
            sb.append("I did not detect specific skills in your message, ")
                    .append("but I will still show you some jobs that could be relevant.\n\n");
        } else {
            sb.append("You mentioned these skills or technologies: ")
                    .append(String.join(", ", skills))
                    .append(".\n");
            sb.append("Based on this, here are some jobs that could fit you:\n\n");
        }

        if (jobs == null || jobs.isEmpty()) {
            sb.append("Right now I could not find any matching jobs. ")
                    .append("Try adding more details about your skills, preferred technologies, or location.");
            return sb.toString();
        }

        for (int i = 0; i < jobs.size(); i++) {
            JobDto job = jobs.get(i);

            sb.append(i + 1)
                    .append(". ")
                    .append(job.getTitle())
                    .append(" at ")
                    .append(job.getCompany())
                    .append(" (")
                    .append(job.getLocation())
                    .append(")\n");

            sb.append("   Why it might fit you: ");

            if (skills.isEmpty()) {
                sb.append("This role looks related to the general interests you described.");
            } else {
                sb.append("This role likely uses some of the skills you mentioned, such as ")
                        .append(String.join(", ", skills))
                        .append(".");
            }

            sb.append("\n\n");
        }

        sb.append("If you tell me more about your experience level or location preferences, ")
                .append("I can refine the recommendations.");

        return sb.toString();
    }
}
