package com.cunningham.pathfinderai.service;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SkillExtractor {

    private static final List<String> KNOWN_SKILLS = List.of(
            "java",
            "javascript",
            "typescript",
            "python",
            "c#",
            "c++",
            "c",
            "go",
            "golang",
            "rust",
            "kotlin",
            "swift",
            "php",
            "ruby",
            "sql",
            "postgresql",
            "mysql",
            "mongodb",
            "html",
            "css",
            "react",
            "angular",
            "vue",
            "node",
            "node.js",
            "spring",
            "spring boot",
            "docker",
            "kubernetes",
            "git",
            "azure",
            "aws",
            "gcp"
    );

    public List<String> extractSkills(String userMessage) {
        if (userMessage == null || userMessage.isBlank()) {
            return List.of();
        }

        String lower = userMessage.toLowerCase(Locale.ROOT);
        List<String> found = new ArrayList<>();

        for (String skill : KNOWN_SKILLS) {
            String skillLower = skill.toLowerCase(Locale.ROOT);

            // \b = word boundary, Pattern.quote() to escape +, #, .
            Pattern pattern = Pattern.compile("\\b" + Pattern.quote(skillLower) + "\\b");
            Matcher matcher = pattern.matcher(lower);

            if (matcher.find()) {
                found.add(formatSkill(skillLower));
            }
        }

        // Remove duplicates and keep order
        return new ArrayList<>(new LinkedHashSet<>(found));
    }

    private String formatSkill(String raw) {
        return switch (raw) {
            case "c#" -> "C#";
            case "c++" -> "C++";
            case "sql" -> "SQL";
            case "html" -> "HTML";
            case "css" -> "CSS";
            case "aws" -> "AWS";
            case "gcp" -> "GCP";
            case "node.js", "node" -> "Node.js";
            case "spring boot" -> "Spring Boot";
            case "postgresql" -> "PostgreSQL";
            default -> {
                if (raw.isEmpty()) {
                    yield raw;
                }
                yield raw.substring(0, 1).toUpperCase(Locale.ROOT) + raw.substring(1);
            }
        };
    }
}
