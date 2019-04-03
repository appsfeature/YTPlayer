package com.ytplayer.activity;

import android.os.Bundle;

import com.ytplayer.R;
import com.ytplayer.util.YTConfig;

public class YoutubeActivity extends YTBaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yt_activity_youtube);
    }


    @Override
    public void initYTPlayer() {
        dragView = findViewById(R.id.dragView);
        slidingLayout = findViewById(R.id.sliding_layout);
        playerView = findViewById(R.id.youtubePlayerView);
    }

    @Override
    public void onInitializationSuccess() {
        playVideo(YTConfig.getVideoId());
    }
}
