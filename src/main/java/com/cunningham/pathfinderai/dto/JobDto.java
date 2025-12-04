package com.cunningham.pathfinderai.dto;

public class JobDto {

    private String title;
    private String company;
    private String location;
    private String description;
    private String url;

    public JobDto() {
    }

    public JobDto(String title, String company, String location, String description, String url) {
        this.title = title;
        this.company = company;
        this.location = location;
        this.description = description;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getCompany() {
        return company;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
