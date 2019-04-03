package com.ytplayer.util;

public enum YTConfig {
    instance;

    private String googleApiKey;
    private String videoId;
    private String playlistId;

    public static void setApiKey(String googleApiKey) {
        instance.googleApiKey = googleApiKey;
    }

    public static String getApiKey() {
        return instance.googleApiKey;
    }

    public static void setVideoId(String videoId) {
        instance.videoId =videoId;
    }

    public static String getVideoId() {
        return instance.videoId;
    }

    public static void setPlaylistId(String playlistId) {
        instance.playlistId = playlistId;
    }

    public static String getPlaylistId() {
        return instance.playlistId;
    }
}
