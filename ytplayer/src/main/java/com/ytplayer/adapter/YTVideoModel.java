package com.ytplayer.adapter;

import java.io.Serializable;

/**
 * Created by sonu on 10/11/17.
 * class to set and get the video id, title and duration for a video
 */

public class YTVideoModel implements Serializable {
    private String videoId, title,duration;

    public static YTVideoModel Builder() {
        return new YTVideoModel();
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
