package com.ytplayer.adapter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sonu on 10/11/17.
 * class to set and get the video id, title and duration for a video
 */

public class YTVideoStatistics {
    private String viewCount, likeCount, dislikeCount, favoriteCount, commentCount;
    private String error;

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(String dislikeCount) {
        this.dislikeCount = dislikeCount;
    }

    public String getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(String favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "YTVideoStatistics{" +
                "viewCount='" + viewCount + '\'' +
                ", likeCount='" + likeCount + '\'' +
                ", dislikeCount='" + dislikeCount + '\'' +
                ", favoriteCount='" + favoriteCount + '\'' +
                ", commentCount='" + commentCount + '\'' +
                '}';
    }
}
