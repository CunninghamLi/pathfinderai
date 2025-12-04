package com.cunningham.pathfinderai.dto;

import java.util.List;

public class ChatResponse {

    private String reply;
    private List<JobDto> jobs;

    public ChatResponse() {
    }

    public ChatResponse(String reply, List<JobDto> jobs) {
        this.reply = reply;
        this.jobs = jobs;
    }

    public String getReply() {
        return reply;
    }

    public List<JobDto> getJobs() {
        return jobs;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public void setJobs(List<JobDto> jobs) {
        this.jobs = jobs;
    }
}
