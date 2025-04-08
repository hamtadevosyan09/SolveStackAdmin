package com.myproject.solvestackadmin;

public class ReportedPost {
    private String description;
    private String postId;
    private String reportReason;
    private String reporterId;
    private String title;

    public ReportedPost(String description, String postId, String reportReason, String reporterId, String title) {
        this.description = description;
        this.postId = postId;
        this.reportReason = reportReason;
        this.reporterId = reporterId;
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public String getPostId() {
        return postId;
    }

    public String getReportReason() {
        return reportReason;
    }

    public String getReporterId() {
        return reporterId;
    }

    public String getTitle() {
        return title;
    }
}
