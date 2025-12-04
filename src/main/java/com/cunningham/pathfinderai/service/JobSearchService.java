package com.cunningham.pathfinderai.service;

import com.cunningham.pathfinderai.dto.JobDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class JobSearchService {

    private final WebClient webClient;

    public JobSearchService(WebClient.Builder webClientBuilder) {
        // Remotive public jobs API
        this.webClient = webClientBuilder
                .baseUrl("https://remotive.com/api")
                .build();
    }

    public List<JobDto> searchJobs(String userMessage) {
        try {
            String query = (userMessage == null || userMessage.isBlank())
                    ? "developer"
                    : userMessage;

            Mono<Map> responseMono = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/remote-jobs")
                            .queryParam("search", query)
                            .build())
                    .retrieve()
                    .bodyToMono(Map.class);

            Map<String, Object> response = responseMono.block();
            if (response == null) {
                return Collections.emptyList();
            }

            Object jobsObj = response.get("jobs");
            if (!(jobsObj instanceof List<?> jobsList)) {
                return Collections.emptyList();
            }

            List<JobDto> result = new ArrayList<>();

            // Limit to first 5 jobs
            int limit = Math.min(jobsList.size(), 5);

            for (int i = 0; i < limit; i++) {
                Object jobObj = jobsList.get(i);
                if (!(jobObj instanceof Map<?, ?> jobMap)) {
                    continue;
                }

                String title = safeString(jobMap.get("title"));
                String company = safeString(jobMap.get("company_name"));
                String location = safeString(jobMap.get("candidate_required_location"));
                String description = safeString(jobMap.get("description"));
                String url = safeString(jobMap.get("url"));

                JobDto dto = new JobDto();
                dto.setTitle(title.isBlank() ? "Unknown title" : title);
                dto.setCompany(company.isBlank() ? "Unknown company" : company);
                dto.setLocation(location.isBlank() ? "Remote" : location);
                dto.setDescription(description.isBlank()
                        ? "No description provided by the API."
                        : description);
                dto.setUrl(url.isBlank() ? "https://remotive.com/remote-jobs" : url);

                result.add(dto);
            }

            if (result.isEmpty()) {
                // Fallback if API returned nothing usable
                return List.of(
                        new JobDto(
                                "No matching jobs found",
                                "Remotive API",
                                "Remote",
                                "The jobs API did not return any jobs for your query.",
                                "https://remotive.com/remote-jobs"
                        )
                );
            }

            return result;

        } catch (Exception e) {
            e.printStackTrace();

            // Hard fallback: one fake job if the API call fails
            JobDto fallback = new JobDto(
                    "Junior Backend Developer (mocked)",
                    "Demo Jobs API",
                    "Montreal, QC",
                    "This is a placeholder job shown because the real jobs API call failed.",
                    "https://example.com/job/1"
            );
            return List.of(fallback);
        }
    }

    private String safeString(Object value) {
        return value == null ? "" : value.toString().trim();
    }
}
