package com.droidhelios.ytplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ytplayer.YTPlayer;
import com.ytplayer.adapter.YTVideoModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    static final String YOUTUBE_VIDEO_ID = "KN5XtpD-jKw";
    static final String YOUTUBE_PLAYLIST = "RDHxNTDNJ7Ndo";
    private YTPlayer ytPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        (findViewById(R.id.btnPlaySingle1)).setOnClickListener(this);
        (findViewById(R.id.btnPlaySingle2)).setOnClickListener(this);
        (findViewById(R.id.btn_open_internal)).setOnClickListener(this);
        (findViewById(R.id.btn_open_external)).setOnClickListener(this);

        ytPlayer = YTPlayer.getInstance(this, DeveloperKey.DEVELOPER_KEY)
                .setPlayerType(YTPlayer.VideoType.OPEN_INTERNAL_PLAYER);

    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.btnPlaySingle1:
                ytPlayer.setPlayerType(YTPlayer.VideoType.OPEN_INTERNAL_PLAYER);
                ytPlayer.openVideo(YOUTUBE_VIDEO_ID);
                break;
            case R.id.btn_open_internal:
                ytPlayer.openPlaylist("Youtube",generateDummyVideoList());
                break;
            case R.id.btnPlaySingle2:
                ytPlayer.setPlayerType(YTPlayer.VideoType.OPEN_EXTERNAL);
                ytPlayer.openVideo(YOUTUBE_VIDEO_ID);
                break;
            case R.id.btn_open_external:
                ytPlayer.openPlaylist(YOUTUBE_PLAYLIST);
                break;
            default:
        }
    }

    private ArrayList<YTVideoModel> generateDummyVideoList() {
        ArrayList<YTVideoModel> youtubeVideoModelArrayList = new ArrayList<>();

        //get the video id array, title array and duration array from strings.xml
        String[] videoIDArray = getResources().getStringArray(R.array.video_id_array);
        String[] videoTitleArray = getResources().getStringArray(R.array.video_title_array);
        String[] videoDurationArray = getResources().getStringArray(R.array.video_duration_array);

        //loop through all items and add them to arraylist
        for (int i = 0; i < videoIDArray.length; i++) {

            YTVideoModel playList = YTVideoModel.Builder()
                    .setVideoId(videoIDArray[i])
                    .setTitle(videoTitleArray[i])
                    .setDuration(videoDurationArray[i]);

            youtubeVideoModelArrayList.add(playList);

        }

        return youtubeVideoModelArrayList;
    }
}