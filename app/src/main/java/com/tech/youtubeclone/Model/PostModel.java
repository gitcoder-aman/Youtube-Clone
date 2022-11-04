package com.tech.youtubeclone.Model;

public class PostModel{
    private String postId;
    private int postVideo;
    private String postedBy;
    private String postDescription;
    private long postedAt;
    private int postLike;

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    private int commentCount;

    public PostModel(String postId, int postVideo, String postedBy, String postDescription, long postedAt) {
        this.postId = postId;
        this.postVideo = postVideo;
        this.postedBy = postedBy;
        this.postDescription = postDescription;
        this.postedAt = postedAt;
    }

    public PostModel() {
    }

    public int getPostLike() {
        return postLike;
    }

    public void setPostLike(int postLike) {
        this.postLike = postLike;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public int getPostVideo() {
        return postVideo;
    }

    public void setPostVideo(int postImage) {
        this.postVideo = postVideo;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public long getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(long postedAt) {
        this.postedAt = postedAt;
    }
}