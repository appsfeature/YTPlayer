package com.ytplayer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.ytplayer.R;
import com.ytplayer.adapter.OnItemClickListener;
import com.ytplayer.adapter.YoutubeVideoAdapter;
import com.ytplayer.adapter.YoutubeVideoModel;
import com.ytplayer.util.YTConfig;
import com.ytplayer.util.YTConstant;

import java.util.ArrayList;

public class YoutubePlaylistActivity extends YTBaseActivity {

    private RecyclerView recyclerView;
    private ArrayList<YoutubeVideoModel> playList;
    private String playerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yt_activity_youtube_playlist);

        setUpRecyclerView();
        getBundle(getIntent());
    }

    private void getBundle(Intent intent) {
        try {
            playerName = intent.getStringExtra(YTConstant.PLAYER_NAME);
            playList = (ArrayList<YoutubeVideoModel>) intent.getSerializableExtra(YTConstant.PLAYLIST);
            setupToolBar();
            if (playList != null) {
                populateRecyclerView();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setupToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (!TextUtils.isEmpty(playerName)) {
            toolbar.setTitle(playerName);
            toolbar.setNavigationOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onBackPressed();
                        }
                    }
            );
        } else {
            toolbar.setVisibility(View.GONE);
        }

    }


    @Override
    public void initYTPlayer() {
        dragView = findViewById(R.id.dragView);
        slidingLayout = findViewById(R.id.sliding_layout);
        playerView = findViewById(R.id.youtubePlayerView);
    }

    @Override
    public void onInitializationSuccess() {

    }

    private void setUpRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void populateRecyclerView() {
        YoutubeVideoAdapter adapter = new YoutubeVideoAdapter(YTConfig.getApiKey(), playList, new OnItemClickListener<YoutubeVideoModel>() {
            @Override
            public void onItemClick(YoutubeVideoModel item) {
                playVideo(item);
            }
        });
        recyclerView.setAdapter(adapter);
    }


//    private ArrayList<YoutubeVideoModel> generateDummyVideoList() {
//        ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList = new ArrayList<>();
//
//        //get the video id array, title array and duration array from strings.xml
//        String[] videoIDArray = getResources().getStringArray(R.array.video_id_array);
//        String[] videoTitleArray = getResources().getStringArray(R.array.video_title_array);
//        String[] videoDurationArray = getResources().getStringArray(R.array.video_duration_array);
//
//        //loop through all items and add them to arraylist
//        for (int i = 0; i < videoIDArray.length; i++) {
//
//            YoutubeVideoModel youtubeVideoModel = new YoutubeVideoModel();
//            youtubeVideoModel.setVideoId(videoIDArray[i]);
//            youtubeVideoModel.setTitle(videoTitleArray[i]);
//            youtubeVideoModel.setDuration(videoDurationArray[i]);
//
//            youtubeVideoModelArrayList.add(youtubeVideoModel);
//
//        }
//
//        return youtubeVideoModelArrayList;
//    }
}
