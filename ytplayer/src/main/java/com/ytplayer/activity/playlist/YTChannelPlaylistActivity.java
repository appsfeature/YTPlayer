package com.ytplayer.activity.playlist;

import android.app.Activity;
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
import com.ytplayer.adapter.OnItemClickListener;
import com.ytplayer.adapter.YTVideoAdapter;
import com.ytplayer.adapter.YTVideoModel;
import com.ytplayer.network.ApiCall;
import com.ytplayer.network.JsonParser;
import com.ytplayer.network.ParamBuilder;
import com.ytplayer.network.YTNetwork;
import com.ytplayer.util.YTConfig;
import com.ytplayer.util.YTConstant;
import com.ytplayer.util.YTType;

import java.util.ArrayList;

public class YTChannelPlaylistActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<YTVideoModel> playList = new ArrayList<>();
    private String playerName;
    private ProgressBar progressbar;
    private String channelId;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("playList", playList);
        outState.putSerializable("playerName", playerName);
        outState.putSerializable("channelId", channelId);
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
            playerName = savedInstanceState.getString("playerName");
            channelId = savedInstanceState.getString("channelId");
            playList = (ArrayList<YTVideoModel>) savedInstanceState.getSerializable("playList");
            if (playList != null) {
                populateRecyclerView(playList);
            }
        }
        setupToolBar();
    }

    private void getBundle(Intent intent) {
        try {
            playerName = intent.getStringExtra(YTConstant.PLAYER_NAME);
            channelId = intent.getStringExtra(YTConstant.CHANNEL_ID);
            getPlaylistByChannelId(channelId);
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
        YTVideoAdapter adapter = new YTVideoAdapter(this, YTType.PLAYLIST, YTConfig.getApiKey(), playList, new OnItemClickListener<YTVideoModel>() {
            @Override
            public void onItemClick(YTVideoModel item, YTType ytType) {
                openInternalYoutubePlaylistPlayer(YTChannelPlaylistActivity.this, item.getTitle(), item.getVideoId());
            }
        });
        recyclerView.setAdapter(adapter);
    }

    public static void openInternalYoutubePlaylistPlayer(Activity activity, String playerName, String channelId) {
        Intent intent = new Intent(activity, YTPlaylistActivity.class);
        intent.putExtra(YTConstant.PLAYER_NAME, playerName);
        intent.putExtra(YTConstant.CHANNEL_ID, channelId);
        activity.startActivity(intent);
    }

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
        protected YTVideoModel doInBackground(String... channelId) {

            String part = "snippet,contentDetails";
            String maxResults = "50";
            String key = YTConfig.getApiKey();
            String response = ApiCall.GET(YTNetwork.getPlayListByChannelId
                    , ParamBuilder.get(part, channelId[0], maxResults, key));

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
