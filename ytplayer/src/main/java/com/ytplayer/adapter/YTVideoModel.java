package com.ytplayer.adapter;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sonu on 10/11/17.
 * class to set and get the video id, title and duration for a video
 */

public class YTVideoModel implements Parcelable {
    private String videoId, title, duration;
    private String nextPageToken, description, publishedAt;
    private String image;
    private String channelId;
    private String channelTitle;
    private String totalResults;
    private String error;
    private ArrayList<YTVideoModel> list;
    private List<String> videoIds;
    private YTVideoStatistics statistics;



    public YTVideoStatistics getStatistics() {
        return statistics;
    }

    public void setStatistics(YTVideoStatistics statistics) {
        this.statistics = statistics;
    }

    public List<String> getVideoIds() {
        return videoIds;
    }

    public void setVideoIds(List<String> videoIds) {
        this.videoIds = videoIds;
    }

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

    public YTVideoModel() {
    }

    public String getDuration() {
        return duration;
    }

    public YTVideoModel setDuration(String duration) {
        this.duration = duration;
        return this;
    }

    protected YTVideoModel(Parcel in) {
        videoId = in.readString();
        title = in.readString();
        duration = in.readString();
        nextPageToken = in.readString();
        description = in.readString();
        publishedAt = in.readString();
        image = in.readString();
        channelId = in.readString();
        channelTitle = in.readString();
        totalResults = in.readString();
        error = in.readString();
        list = in.createTypedArrayList(YTVideoModel.CREATOR);
        videoIds = in.createStringArrayList();
    }

    public static final Creator<YTVideoModel> CREATOR = new Creator<YTVideoModel>() {
        @Override
        public YTVideoModel createFromParcel(Parcel in) {
            return new YTVideoModel(in);
        }

        @Override
        public YTVideoModel[] newArray(int size) {
            return new YTVideoModel[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(videoId);
        parcel.writeString(title);
        parcel.writeString(duration);
        parcel.writeString(nextPageToken);
        parcel.writeString(description);
        parcel.writeString(publishedAt);
        parcel.writeString(image);
        parcel.writeString(channelId);
        parcel.writeString(channelTitle);
        parcel.writeString(totalResults);
        parcel.writeString(error);
        parcel.writeTypedList(list);
        parcel.writeStringList(videoIds);
    }
}
