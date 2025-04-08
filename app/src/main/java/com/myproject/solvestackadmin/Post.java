package com.myproject.solvestackadmin;

public class Post {
    private String postId;
    private String title;
    private String description;
    private String userId;
    private String creatorUserId;
    private String difficultyLevel;

    public Post() {
    }

    public Post(String title, String description, String userId, String creatorUserId) {
        this.title = title;
        this.description = description;
        this.userId = userId;
        this.creatorUserId = creatorUserId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(String creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }
}
