package com.ytplayer.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sonu on 10/11/17.
 * class to set and get the video id, title and duration for a video
 */

public class YTVideoModel implements Serializable {
    private String videoId, title, duration;
    private String nextPageToken, description, publishedAt;
    private String image;
    private String channelId;
    private String channelTitle;
    private String totalResults;
    private String error;
    private ArrayList<YTVideoModel> list;

    public String getChannelId() {
        return channelId;
    }


    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public ArrayList<YTVideoModel> getList() {
        return list;
    }

    public void setList(ArrayList<YTVideoModel> list) {
        this.list = list;
    }

    public String getError() {
        return error;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setError(String error) {
        this.error = error;
    }

    public static YTVideoModel Builder() {
        return new YTVideoModel();
    }


    public String getNextPageToken() {
        return nextPageToken;
    }

    public YTVideoModel setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public YTVideoModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public YTVideoModel setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
        return this;
    }

    public String getVideoId() {
        return videoId;
    }

    public YTVideoModel setVideoId(String videoId) {
        this.videoId = videoId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public YTVideoModel setTitle(String title) {
        this.title = title;
        return this;
    }


    public String getDuration() {
        return duration;
    }

    public YTVideoModel setDuration(String duration) {
        this.duration = duration;
        return this;
    }

    @Override
    public String toString() {
        return "YoutubeVideoModel{" +
                "videoId='" + videoId + '\'' +
                ", title='" + title + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }
}
