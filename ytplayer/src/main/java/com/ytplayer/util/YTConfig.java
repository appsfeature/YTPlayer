package com.ytplayer.util;

public enum YTConfig {
    instance;

    private String googleApiKey;
    private String videoId;
    private String playlistId;
    private int maxResultsCount = YTConstant.MAX_RESULTS_COUNT;

    public static void setApiKey(String googleApiKey) {
        instance.googleApiKey = googleApiKey;
    }

    public static int getMaxResultsCount() {
        return instance.maxResultsCount;
    }

    public static void setMaxResultsCount(int maxResultsCount) {
        instance.maxResultsCount = maxResultsCount;
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
