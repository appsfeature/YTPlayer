package com.ytplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.ytplayer.activity.playlist.YTChannelPlaylistActivity;
import com.ytplayer.activity.search.YTSearchActivity;
import com.ytplayer.activity.single.YTSlidingActivity;
import com.ytplayer.activity.playlist.YTPlaylistActivity;
import com.ytplayer.adapter.YTVideoModel;
import com.ytplayer.activity.single.YTPlayerActivity;
import com.ytplayer.util.YTConstant;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class YTUtility {
    static final String GOOGLE_API_KEY = "AIzaSyAD86z35Tv5BN2eIk9JSOvcPYEuC2-zIa4";
    static final String YOUTUBE_VIDEO_ID = "HxNTDNJ7Ndo";
    static final String YOUTUBE_PLAYLIST = "RDHxNTDNJ7Ndo";


    public static void showKeyboard(View view, Context activity) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
//                imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View f = activity.getCurrentFocus();
        if (null != f && null != f.getWindowToken() && EditText.class.isAssignableFrom(f.getClass()))
            imm.hideSoftInputFromWindow(f.getWindowToken(), 0);
        else
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public static void openExternalYoutubeVideoPlayer(Activity activity, String googleApiKey, String youtubeVideoId) {
        activity.startActivity(YouTubeStandalonePlayer.createVideoIntent(activity, googleApiKey, youtubeVideoId, 0, true, false));
    }

    public static void openExternalYoutubePlaylistPlayer(Activity activity, String googleApiKey, String playlistId) {
        activity.startActivity(YouTubeStandalonePlayer.createPlaylistIntent(activity, googleApiKey, playlistId, 0, 0, true, true));

    }

    public static void openInternalYoutubePlayer(Activity activity) {
        activity.startActivity(new Intent(activity, YTPlayerActivity.class));
    }

    public static void openInternalYoutubePlayer(Activity activity, boolean isDetailVisible) {
        Intent intent = new Intent(activity, YTSlidingActivity.class);
        intent.putExtra(YTConstant.VIDEO_DETAIL, isDetailVisible);
        activity.startActivity(intent);
    }

    public static void openInternalYoutubeSlidingPanel(Activity activity) {
        activity.startActivity(new Intent(activity, YTSlidingActivity.class));
    }

    public static void openInternalYoutubePlaylistPlayer(Activity activity, String playerName, ArrayList<YTVideoModel> playlist) {
        Intent intent = new Intent(activity, YTPlaylistActivity.class);
        intent.putExtra(YTConstant.PLAYER_NAME, playerName);
        intent.putExtra(YTConstant.PLAYLIST, playlist);
        activity.startActivity(intent);
    }

    public static void openInternalYoutubeByPlaylistId(Activity activity, String playerName, String playListId) {
        Intent intent = new Intent(activity, YTPlaylistActivity.class);
        intent.putExtra(YTConstant.PLAYER_NAME, playerName);
        intent.putExtra(YTConstant.PLAYLIST_ID, playListId);
        activity.startActivity(intent);
    }

    public static void openInternalYoutubeByChannelId(Activity activity, String playerName, String channelId) {
        Intent intent = new Intent(activity, YTChannelPlaylistActivity.class);
        intent.putExtra(YTConstant.PLAYER_NAME, playerName);
        intent.putExtra(YTConstant.CHANNEL_ID, channelId);
        activity.startActivity(intent);
    }

    /**
     * @param playerName Toolbar title name
     * @param playListIds Playlist ids in comma separated
     */
    public static void openInternalYoutubeByPlayListMultipleIds(Activity activity, String playerName, String playListIds) {
        Intent intent = new Intent(activity, YTChannelPlaylistActivity.class);
        intent.putExtra(YTConstant.PLAYER_NAME, playerName);
        intent.putExtra(YTConstant.PLAYLIST_ID, playListIds);
        activity.startActivity(intent);
    }

    public static void openInternalYoutubePlaylistPlayer(Activity activity, String channelId) {
        Intent intent = new Intent(activity, YTSearchActivity.class);
        intent.putExtra(YTConstant.CHANNEL_ID, channelId);
        activity.startActivity(intent);
    }


    public static int parseInt(String totalResults) {
        try {
            return Integer.parseInt(totalResults);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void log(String message) {
        if (YTConstant.isLogEnabled) {
            Log.d("@ytplayer", message);
        }
    }

    // input date is PT1H1M13S
    public static String getValidDuration(String youtubeDuration) {
        Calendar c = new GregorianCalendar();
        try {
            DateFormat df = new SimpleDateFormat("'PT'mm'M'ss'S'", Locale.US);
            Date d = df.parse(youtubeDuration);
            c.setTime(d);
        } catch (ParseException e) {
            try {
                DateFormat df = new SimpleDateFormat("'PT'hh'H'mm'M'ss'S'", Locale.US);
                Date d = df.parse(youtubeDuration);
                c.setTime(d);
            } catch (ParseException e1) {
                try {
                    DateFormat df = new SimpleDateFormat("'PT'ss'S'", Locale.US);
                    Date d = df.parse(youtubeDuration);
                    c.setTime(d);
                } catch (ParseException e2) {
                }
            }
        }
        c.setTimeZone(TimeZone.getDefault());

        String time = "";
        if ( c.get(Calendar.HOUR) > 0 ) {
            if ( String.valueOf(c.get(Calendar.HOUR)).length() == 1 ) {
                time += "0" + c.get(Calendar.HOUR);
            }
            else {
                time += c.get(Calendar.HOUR);
            }
            time += ":";
        }
        // test minute
        if ( String.valueOf(c.get(Calendar.MINUTE)).length() == 1 ) {
            time += "0" + c.get(Calendar.MINUTE);
        }
        else {
            time += c.get(Calendar.MINUTE);
        }
        time += ":";
        // test second
        if ( String.valueOf(c.get(Calendar.SECOND)).length() == 1 ) {
            time += "0" + c.get(Calendar.SECOND);
        }
        else {
            time += c.get(Calendar.SECOND);
        }
        return time ;
    }
}
