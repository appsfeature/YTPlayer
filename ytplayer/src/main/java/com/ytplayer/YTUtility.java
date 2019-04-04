package com.ytplayer;

import android.app.Activity;
import android.content.Intent;

import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.ytplayer.activity.DataPlaylistActivity;
import com.ytplayer.activity.YoutubeActivity;
import com.ytplayer.activity.YoutubePlaylistActivity;
import com.ytplayer.adapter.YTVideoModel;
import com.ytplayer.player.YoutubePlayerActivity;
import com.ytplayer.util.YTConstant;

import java.util.ArrayList;

class YTUtility {
    static final String GOOGLE_API_KEY = "AIzaSyAD86z35Tv5BN2eIk9JSOvcPYEuC2-zIa4";
    static final String YOUTUBE_VIDEO_ID = "HxNTDNJ7Ndo";
    static final String YOUTUBE_PLAYLIST = "RDHxNTDNJ7Ndo";

    public static void openExternalYoutubeVideoPlayer(Activity activity, String googleApiKey, String youtubeVideoId) {
        activity.startActivity(YouTubeStandalonePlayer.createVideoIntent(activity, googleApiKey, youtubeVideoId, 0, true, false));
    }

    public static void openExternalYoutubePlaylistPlayer(Activity activity, String googleApiKey, String playlistId) {
        activity.startActivity(YouTubeStandalonePlayer.createPlaylistIntent(activity, googleApiKey, playlistId, 0, 0, true, true));

    }

    public static void openInternalYoutubePlayer(Activity activity) {
        activity.startActivity(new Intent(activity, YoutubePlayerActivity.class));
    }

    public static void openInternalYoutubeSlidingPanel(Activity activity) {
        activity.startActivity(new Intent(activity, YoutubeActivity.class));
    }

    public static void openInternalYoutubePlaylistPlayer(Activity activity, String playerName, ArrayList<YTVideoModel> playlist) {
        Intent intent =new Intent(activity, YoutubePlaylistActivity.class);
        intent.putExtra(YTConstant.PLAYER_NAME, playerName);
        intent.putExtra(YTConstant.PLAYLIST, playlist);
        activity.startActivity(intent);
    }

    public static void openInternalYoutubePlaylistPlayer(Activity activity, String playerName,String channelId) {
        Intent intent =new Intent(activity, DataPlaylistActivity.class);
        intent.putExtra(YTConstant.PLAYER_NAME, playerName);
        intent.putExtra(YTConstant.CHANNEL_ID, channelId);
        activity.startActivity(intent);
    }


}
