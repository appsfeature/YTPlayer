package com.ytplayer;

import android.app.Activity;

import com.ytplayer.adapter.YTVideoModel;
import com.ytplayer.util.YTConfig;

import java.util.ArrayList;

public class YTPlayer {

    public enum VideoType {
        OPEN_INTERNAL_PLAYER,
        OPEN_EXTERNAL
    }

    private static YTPlayer instance;
    private final Activity activity;
    private final String googleApiKey;
    private VideoType playerType;

    public YTPlayer(Activity activity, String googleApiKey) {
        this.activity = activity;
        this.googleApiKey = googleApiKey;
        this.playerType = VideoType.OPEN_INTERNAL_PLAYER;
        YTConfig.setApiKey(googleApiKey);
    }

    public static YTPlayer getInstance(Activity activity, String googleApiKey) {
        if (instance == null) {
            instance = new YTPlayer(activity, googleApiKey);
        }
        return instance;
    }

    public void openVideo(String videoId) {
        YTConfig.setVideoId(videoId);
        if (playerType == VideoType.OPEN_INTERNAL_PLAYER) {
            YTUtility.openInternalYoutubePlayer(activity);
        } else {
            YTUtility.openExternalYoutubeVideoPlayer(activity, YTConfig.getApiKey(), YTConfig.getVideoId());
        }
    }

    public YTPlayer setPlayerType(VideoType playerType) {
        this.playerType = playerType;
        return this;
    }

    public void openPlaylist(String playlistId) {
        YTConfig.setPlaylistId(playlistId);
        YTUtility.openExternalYoutubePlaylistPlayer(activity, YTConfig.getApiKey(), YTConfig.getPlaylistId());
    }

    public void openPlaylist(ArrayList<YTVideoModel> playlist) {
        openPlaylist(null, playlist);
    }

    public void openPlaylist(String playerName, ArrayList<YTVideoModel> playlist) {
        YTUtility.openInternalYoutubePlaylistPlayer(activity, playerName, playlist);
    }

    public void openPlaylist(String playerName, String channelId) {
        YTUtility.openInternalYoutubePlaylistPlayer(activity, playerName, channelId);
    }

}
