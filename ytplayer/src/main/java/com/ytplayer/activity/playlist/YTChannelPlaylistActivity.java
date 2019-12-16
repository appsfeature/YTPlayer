package com.ytplayer.activity.playlist;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ytplayer.R;
import com.ytplayer.YTUtility;
import com.ytplayer.adapter.OnItemClickListener;
import com.ytplayer.adapter.YTVideoAdapter;
import com.ytplayer.adapter.YTVideoModel;
import com.ytplayer.network.ApiCall;
import com.ytplayer.network.JsonParser;
import com.ytplayer.network.KeyValuePair;
import com.ytplayer.network.ParamBuilder;
import com.ytplayer.network.YTNetwork;
import com.ytplayer.util.YTConfig;
import com.ytplayer.util.YTConstant;
import com.ytplayer.util.YTType;

import java.util.ArrayList;
import java.util.List;

public class YTChannelPlaylistActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<YTVideoModel> playList = new ArrayList<>();
    private String playerName;
    private ProgressBar progressbar;
    private String channelId;
    private String playListIds;
//    private String playListIds = "PLWz5rJ2EKKc8jQfNAUu5reIGFNNqpn26X,PLWz5rJ2EKKc-lJo_RGGXL2Psr8vVCTWjM";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(YTConstant.PLAYLIST, playList);
        outState.putString(YTConstant.PLAYER_NAME, playerName);
        outState.putString(YTConstant.CHANNEL_ID, channelId);
        outState.putString(YTConstant.PLAYLIST_ID, playListIds);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yt_activity_channel_playlist);
        progressbar = findViewById(R.id.progressbar);

        setUpRecyclerView();
        if (savedInstanceState == null) {
            getBundle(getIntent());
        } else {
            playerName = savedInstanceState.getString(YTConstant.PLAYER_NAME);
            channelId = savedInstanceState.getString(YTConstant.CHANNEL_ID);
            playListIds = savedInstanceState.getString(YTConstant.PLAYLIST_ID);
            loadData();
        }
        setupToolBar();
    }

    private void loadData() {
        try {
            if(channelId!=null) {
                getPlaylistByChannelId(channelId);
            }else if(playListIds!=null) {
                getPlaylistByChannelId(playListIds);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getBundle(Intent intent) {
        try {
            playerName = intent.getStringExtra(YTConstant.PLAYER_NAME);
            channelId = intent.getStringExtra(YTConstant.CHANNEL_ID);
            playListIds = intent.getStringExtra(YTConstant.PLAYLIST_ID);
            loadData();
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


    private void setUpRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void populateRecyclerView(ArrayList<YTVideoModel> tempList) {
        playList.addAll(tempList);
        YTVideoAdapter adapter = new YTVideoAdapter(this, YTType.PLAYLIST, playList, new OnItemClickListener<YTVideoModel>() {
            @Override
            public void onItemClick(YTVideoModel item, YTType ytType) {
                YTUtility.openInternalYoutubeByPlaylistId(YTChannelPlaylistActivity.this, item.getTitle(), item.getVideoId());
            }
        });
        recyclerView.setAdapter(adapter);
    }



    private void getPlaylistByChannelId(String channelOrPlayListIds) {
        new VideoListTask().execute(channelOrPlayListIds);
    }

    private class VideoListTask extends AsyncTask<String, Void, YTVideoModel> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected YTVideoModel doInBackground(String... channelId) {

            String part = "snippet,contentDetails";
            String maxResults = "50";
            String key = YTConfig.getApiKey();
            List<KeyValuePair> paramBuilder;
            if(playListIds!=null) {
                paramBuilder = ParamBuilder.getMultiplePlayListDetails(part, channelId[0], maxResults, key);
            }else {
                paramBuilder = ParamBuilder.getChannelItems(part, channelId[0], maxResults, key);
            }
            String response = ApiCall.GET(YTNetwork.getPlayListByChannelId
                    , paramBuilder);

            return JsonParser.parsePlayList(response);
        }

        @Override
        protected void onPostExecute(YTVideoModel ytVideoModel) {
            super.onPostExecute(ytVideoModel);
            progressbar.setVisibility(View.GONE);
            if (TextUtils.isEmpty(ytVideoModel.getError())) {
                populateRecyclerView(ytVideoModel.getList());
            } else {
                Toast.makeText(YTChannelPlaylistActivity.this, ytVideoModel.getError(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
