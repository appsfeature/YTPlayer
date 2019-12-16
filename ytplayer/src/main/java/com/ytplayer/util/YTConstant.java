package com.ytplayer.util;

import com.ytplayer.BuildConfig;

public interface YTConstant {
    String PLAYLIST = "playlist";
    String PLAYER_NAME = "playerName";
    String PLAYLIST_ID = "playlistId";
    String CHANNEL_ID = "channelId";
    String VIDEO_DETAIL = "videoDetail";
    boolean isLogEnabled = BuildConfig.DEBUG;

    int MAX_RESULTS_COUNT = 50;

    int TOTAL_RESULT_SIZE = 35;

    boolean IS_LOAD_VIDEO_STATICS = true;
}
