package com.ytplayer.activity.playlist;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.slidinguppanel.SlidingUpPanelLayout;
import com.ytplayer.R;
import com.ytplayer.activity.YTBaseActivity;
import com.ytplayer.adapter.OnItemClickListener;
import com.ytplayer.adapter.YTVideoAdapter;
import com.ytplayer.adapter.YTVideoModel;
import com.ytplayer.adapter.YTVideoStatistics;
import com.ytplayer.network.ApiCall;
import com.ytplayer.network.JsonParser;
import com.ytplayer.network.ParamBuilder;
import com.ytplayer.network.YTNetwork;
import com.ytplayer.util.YTConfig;
import com.ytplayer.util.YTConstant;
import com.ytplayer.util.YTType;

import java.util.ArrayList;

public class YTPlaylistActivity extends YTBaseActivity implements YTVideoAdapter.LoadMoreItems {

    int maxCount = YTConfig.getMaxResultsCount();
    int totalResultSize = YTConstant.TOTAL_RESULT_SIZE;

    private ArrayList<YTVideoModel> playList = new ArrayList<>();
    private String playerName;
    private ProgressBar progressbar;
    private String playListId;
    private YTVideoModel mYTVideoModel;
    private YTVideoAdapter mAdapter;
    private String mNextPageToken;
    private YTType itemType;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(YTConstant.PLAYLIST, playList);
        outState.putString(YTConstant.PLAYER_NAME, playerName);
        outState.putString(YTConstant.PLAYLIST_ID, playListId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yt_activity_youtube_playlist);
        progressbar = findViewById(R.id.progressbar);

        setUpRecyclerView();
        if (savedInstanceState == null) {
            getBundle(getIntent());
        } else {
            playerName = savedInstanceState.getString(YTConstant.PLAYER_NAME);
            playListId = savedInstanceState.getString(YTConstant.PLAYLIST_ID);
            ArrayList<YTVideoModel> tempList = savedInstanceState.getParcelableArrayList(YTConstant.PLAYLIST);
            if (tempList != null) {
                playList.addAll(tempList);
                itemType = YTType.LIST;
            } else {
                itemType = YTType.PLAYLIST_ID;
            }
            loadData();
        }
        setupToolBar();
    }

    private void getBundle(Intent intent) {
        try {
            playerName = intent.getStringExtra(YTConstant.PLAYER_NAME);
            playListId = intent.getStringExtra(YTConstant.PLAYLIST_ID);
            ArrayList<YTVideoModel> tempList = intent.getParcelableArrayListExtra(YTConstant.PLAYLIST);
            if (tempList != null) {
                playList.addAll(tempList);
                itemType = YTType.LIST;
            } else {
                itemType = YTType.PLAYLIST_ID;
            }
            loadData();

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadData() {
        if (itemType == YTType.LIST) {
            if (playList != null && playList.size() > 0) {
                populateRecyclerView(playList);
            }
        } else {
            if (playListId != null) {
                getPlaylistByChannelId(playListId);
            }
        }
    }


    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new YTVideoAdapter(this, YTType.VIDEO, playList, new OnItemClickListener<YTVideoModel>() {
            @Override
            public void onItemClick(YTVideoModel item, YTType ytType) {
                playVideo(item);
            }
        }).setMinimumItemCount(YTConstant.MAX_RESULTS_COUNT)
                .setOnLoadMoreListener(this);

        recyclerView.setAdapter(mAdapter);
    }


    /**
     * mYTVideoModel : response getting from server
     * playList : videos list
     * totalResultSize : total video list count available on Playlist getting from server
     * maxCount : list pagination maximum count
     */
    @Override
    public void onLoadMore() {
        if (mYTVideoModel != null) {
            if (playList.size() < totalResultSize) {
                maxCount = (totalResultSize - playList.size()) > maxCount ? maxCount : (totalResultSize - playList.size());
                loadData();
            } else {
                mAdapter.stop();
            }
        }
    }

    @Override
    public void onProgressUpdateBar(int visibility) {
        progressbar.setVisibility(visibility);
    }



    private void populateRecyclerView(ArrayList<YTVideoModel> tempList) {
        mAdapter.finish();
        playList.addAll(tempList);
        mAdapter.notifyDataSetChanged();
    }


    private int getTotalResultCount() {
        try {
            return Integer.parseInt(mYTVideoModel.getTotalResults());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return playList.size();
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
        scrollView = findViewById(R.id.scroll_view_video_detail);
    }

    @Override
    public void onInitializationSuccess() {

    }

    @Override
    public void onPanelStateChanged(SlidingUpPanelLayout.PanelState newState) {

    }

    private void getPlaylistByChannelId(String channelId) {
        new VideoListTask().execute(channelId);
    }

    private class VideoListTask extends AsyncTask<String, Void, YTVideoModel> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onProgressUpdateBar(View.VISIBLE);
        }

        @Override
        protected YTVideoModel doInBackground(String... playlistId) {

            String part;
            String key;
            String response;

            part = "snippet,contentDetails";
            key = YTConfig.getApiKey();
            response = ApiCall.GET(YTNetwork.getPlayListItemsByPlayListId
                    , ParamBuilder.getPlayListItems(part, playlistId[0], maxCount, key, mNextPageToken));

            YTVideoModel playlistDetail = JsonParser.parsePlayListItems(response);

            if (YTConstant.IS_LOAD_VIDEO_STATICS && playlistDetail.getError() == null) {
                String videoIds = TextUtils.join(",", playlistDetail.getVideoIds());

                part = "snippet,contentDetails,statistics";
                key = YTConfig.getApiKey();
                response = ApiCall.GET(YTNetwork.getVideoStatistics
                        , ParamBuilder.getStatistics(part, videoIds, key));

                YTVideoStatistics videoList = JsonParser.parseStatistics(response);
                if (videoList.getError() == null && videoList.getList() != null) {
                    for (YTVideoModel items : playlistDetail.getList()) {
                        for (YTVideoStatistics video : videoList.getList()) {
                            if (items.getVideoId().equalsIgnoreCase(video.getVideoId())) {
                                items.setStatistics(video);
                            }
                        }
                    }
                }
            }

            return playlistDetail;
        }

        @Override
        protected void onPostExecute(YTVideoModel ytVideoModel) {
            super.onPostExecute(ytVideoModel);
            onProgressUpdateBar(View.GONE);
            if (TextUtils.isEmpty(ytVideoModel.getError())) {
                mYTVideoModel = ytVideoModel;
                totalResultSize = getTotalResultCount();
                mNextPageToken = mYTVideoModel.getNextPageToken();
                populateRecyclerView(ytVideoModel.getList());
            } else {
                Toast.makeText(YTPlaylistActivity.this, ytVideoModel.getError(), Toast.LENGTH_SHORT).show();
            }
        }
    }
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



//    if ((totalResultSize - playList.size()) > maxCount) {
//        loadData();
//    } else {
//        maxCount = (totalResultSize - playList.size());
//        loadData();
//    }
