package com.ytplayer.activity.search;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ytplayer.R;
import com.ytplayer.YTPlayer;
import com.ytplayer.YTUtility;
import com.ytplayer.activity.playlist.YTPlaylistActivity;
import com.ytplayer.adapter.OnItemClickListener;
import com.ytplayer.adapter.PaginationAdapterCallback;
import com.ytplayer.adapter.YTPaginationAdapter;
import com.ytplayer.adapter.YTVideoAdapter;
import com.ytplayer.adapter.YTVideoModel;
import com.ytplayer.adapter.pagination.PaginationScrollListener;
import com.ytplayer.network.ApiCall;
import com.ytplayer.network.JsonParser;
import com.ytplayer.network.ParamBuilder;
import com.ytplayer.network.YTNetwork;
import com.ytplayer.util.YTConfig;
import com.ytplayer.util.YTConstant;
import com.ytplayer.util.YTType;

import java.util.ArrayList;

public class YTSearchActivity extends AppCompatActivity implements PaginationAdapterCallback {

    private static final int PAGE_START = 1;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES = 100;
    private int currentPage = PAGE_START;

    private RecyclerView recyclerView;
    private ArrayList<YTVideoModel> playList = new ArrayList<>();
    private ProgressBar progressbar;
    private String channelId;
    private EditText etSearch;
    private String pageToken = "";
    private YTPaginationAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private YTVideoModel mYTVideoModel;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("playList", playList);
        outState.putSerializable("channelId", channelId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yt_activity_search);
        etSearch = findViewById(R.id.et_search);
        progressbar = findViewById(R.id.progressbar);

        setUpRecyclerView();
        if (savedInstanceState == null) {
            getBundle(getIntent());
        } else {
            channelId = savedInstanceState.getString("channelId");
            playList = (ArrayList<YTVideoModel>) savedInstanceState.getSerializable("playList");
            if (playList != null) {
                populateRecyclerView(playList);
            }
        }
        setupToolBar();

        etSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                boolean handled = false;
                if (actionId == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // Handle pressing "Enter" key here
                    searchTask(etSearch.getText().toString(), pageToken);
                    YTUtility.hideKeyboard(YTSearchActivity.this);
                    handled = true;
                }
                return handled;
            }
        });
    }


    private void getBundle(Intent intent) {
        try {
            channelId = intent.getStringExtra(YTConstant.CHANNEL_ID);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setupToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        CharSequence playerName = "Search";
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

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new YTPaginationAdapter(this, YTType.PLAYLIST, YTConfig.getApiKey(), playList, new OnItemClickListener<YTVideoModel>() {
            @Override
            public void onItemClick(YTVideoModel item, YTType ytType) {
                if (ytType == YTType.VIDEO) {
                    YTConfig.setVideoId(item.getVideoId());
                    YTUtility.openInternalYoutubePlayer(YTSearchActivity.this);
                } else {
                    openInternalYoutubePlaylistPlayer(YTSearchActivity.this, item.getTitle(), item.getVideoId());
                }
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                loadNextPage();
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

    }

    private void populateRecyclerView(ArrayList<YTVideoModel> tempList) {
        TOTAL_PAGES = tempList.size();
        playList.clear();
        playList.addAll(tempList);
        adapter.notifyDataSetChanged();
    }

    private void updateRecyclerView(ArrayList<YTVideoModel> tempList, boolean isFirstPage, YTType ytType) {
        if (!isFirstPage) {
            adapter.setYtType(ytType);
            adapter.removeLoadingFooter();
            isLoading = false;
        }else{
            playList.clear();
            adapter.clear();
        }
        playList.addAll(tempList);
        adapter.notifyDataSetChanged();

        if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
        else isLastPage = true;
    }

    public static void openInternalYoutubePlaylistPlayer(Activity activity, String playerName, String channelId) {
        Intent intent = new Intent(activity, YTPlaylistActivity.class);
        intent.putExtra(YTConstant.PLAYER_NAME, playerName);
        intent.putExtra(YTConstant.CHANNEL_ID, channelId);
        activity.startActivity(intent);
    }


    @Override
    public void retryPageLoad() {
        loadNextPage();
    }

    private void loadNextPage() {
        if (mYTVideoModel != null) {
            searchTask(etSearch.getText().toString(), mYTVideoModel.getNextPageToken());
        }
    }

    private void searchTask(String searchWord, String pageToken) {
        new SearchTask(searchWord, pageToken).execute();
    }

    private class SearchTask extends AsyncTask<String, Void, YTVideoModel> {


        private final String searchWord, pageToken;

        public SearchTask(String searchWord, String pageToken) {
            this.searchWord = searchWord;
            this.pageToken = pageToken;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (pageToken.equals("")) {
                progressbar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected YTVideoModel doInBackground(String... Void) {
            String part = "snippet";
            String maxResults = "10";
            String type = "video";
            String key = YTConfig.getApiKey();
            String response = ApiCall.GET(YTNetwork.getVideoListBySearchQuery
                    , ParamBuilder.getSearchParam(part, maxResults, searchWord, pageToken, type, key));
            return JsonParser.parseSearchList(response);
        }

        @Override
        protected void onPostExecute(YTVideoModel ytVideoModel) {
            super.onPostExecute(ytVideoModel);
            progressbar.setVisibility(View.GONE);
            if (TextUtils.isEmpty(ytVideoModel.getError())) {
                mYTVideoModel = ytVideoModel;
                TOTAL_PAGES = YTUtility.parseInt(ytVideoModel.getTotalResults());
                boolean isFirstPage;
                if (pageToken.equals("")) {
                    isFirstPage = true;
                } else {
                    isFirstPage = false;
                }
                updateRecyclerView(ytVideoModel.getList(), isFirstPage, YTType.VIDEO);
            } else {
                adapter.showRetry(true, ytVideoModel.getError());
                Toast.makeText(YTSearchActivity.this, ytVideoModel.getError(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
