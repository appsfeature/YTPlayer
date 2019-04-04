package com.ytplayer.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ytplayer.R;
import com.ytplayer.adapter.OnItemClickListener;
import com.ytplayer.adapter.YTVideoAdapter;
import com.ytplayer.adapter.YTVideoModel;
import com.ytplayer.network.ApiCall;
import com.ytplayer.network.JsonParser;
import com.ytplayer.network.ParamBuilder;
import com.ytplayer.network.YTNetwork;
import com.ytplayer.util.YTConfig;
import com.ytplayer.util.YTConstant;

import java.util.ArrayList;

public class YoutubePlaylistActivity extends YTBaseActivity {

    private RecyclerView recyclerView;
    private ArrayList<YTVideoModel> playList = new ArrayList<>();
    private String playerName;
    private ProgressBar progressbar;
    private String channelId;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("playList",playList);
        outState.putSerializable("playerName",playerName);
        outState.putSerializable("channelId",channelId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yt_activity_youtube_playlist);
        progressbar = findViewById(R.id.progressbar);

        setUpRecyclerView();
        if(savedInstanceState == null) {
            getBundle(getIntent());
        }else{
            playerName = savedInstanceState.getString("playerName");
            channelId = savedInstanceState.getString("channelId");
            playList = (ArrayList<YTVideoModel>) savedInstanceState.getSerializable("playList");
            if(playList!=null) {
                populateRecyclerView(playList);
            }
        }
        setupToolBar();
    }

    private void getBundle(Intent intent) {
        try {
            playerName = intent.getStringExtra(YTConstant.PLAYER_NAME);
            channelId = intent.getStringExtra(YTConstant.CHANNEL_ID);
            ArrayList<YTVideoModel> tempList = (ArrayList<YTVideoModel>) intent.getSerializableExtra(YTConstant.PLAYLIST);
            if (tempList != null) {
                populateRecyclerView(tempList);
            }else if (channelId != null) {
                getPlaylistByChannelId(channelId);
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

    private void populateRecyclerView(ArrayList<YTVideoModel> tempList) {
        playList.addAll(tempList);
        YTVideoAdapter adapter = new YTVideoAdapter(this,YTConfig.getApiKey(), playList, new OnItemClickListener<YTVideoModel>() {
            @Override
            public void onItemClick(YTVideoModel item) {
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


    private void getPlaylistByChannelId(String channelId) {
        new VideoListTask().execute(channelId);
    }

    private class VideoListTask extends AsyncTask<String, Void, YTVideoModel> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected YTVideoModel doInBackground(String... playlistId) {

            String part = "snippet,contentDetails";
            String maxResults = "25";
            String key = YTConfig.getApiKey();
            String response = ApiCall.GET(YTNetwork.getPlayListItemsByPlayListId
                    , ParamBuilder.getPlayListItems(part, playlistId[0], maxResults, key));

            return JsonParser.parsePlayListItems(response);
        }

        @Override
        protected void onPostExecute(YTVideoModel ytVideoModel) {
            super.onPostExecute(ytVideoModel);
            progressbar.setVisibility(View.GONE);
            if(TextUtils.isEmpty(ytVideoModel.getError())){
                populateRecyclerView(ytVideoModel.getList());
            }else{
                Toast.makeText(YoutubePlaylistActivity.this, ytVideoModel.getError(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
