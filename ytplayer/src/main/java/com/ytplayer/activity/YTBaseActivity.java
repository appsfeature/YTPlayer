package com.ytplayer.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.slidinguppanel.SlidingUpPanelLayout;
import com.ytplayer.R;
import com.ytplayer.adapter.YTVideoModel;
import com.ytplayer.util.AnimUtil;
import com.ytplayer.util.Logger;
import com.ytplayer.util.SizeUtil;
import com.ytplayer.util.YTConfig;

import java.text.DecimalFormat;


public abstract class YTBaseActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, SlidingUpPanelLayout.PanelSlideListener {

    protected YouTubePlayerView playerView;
    protected YouTubePlayer youTubePlayer;
    protected int playerWidth;
    protected int playerHeight;
    protected LinearLayout dragView;
    protected SlidingUpPanelLayout slidingLayout;
    protected SlidingUpPanelLayout.PanelState currentState;
    private boolean wasRestored;
    protected String videoId;
    private TextView tvVideoDuration, tvVideoTitle;
    private FrameLayout frameLayout;
    private boolean isFullScreen;
    private int mSlideOffset;
    private int videoTime;

    public abstract void initYTPlayer();

    public abstract void onInitializationSuccess();

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        bundle.putString("videoId",videoId);
        bundle.putInt("videoTime",youTubePlayer.getCurrentTimeMillis());
        super.onSaveInstanceState(bundle);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if(bundle!=null) {
            videoId = bundle.getString("videoId");
            videoTime = bundle.getInt("videoTime");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    public void playVideo(YTVideoModel model) {
        this.videoId = model.getVideoId();
        this.videoTime = 0;
        tvVideoTitle.setText(model.getTitle());
        tvVideoDuration.setText(model.getDuration());
        playVideo(videoId);
    }

    public void playVideo(String mVideoId) {
        this.videoId = mVideoId;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (youTubePlayer != null) {
                    dragView.setVisibility(View.VISIBLE);
                    youTubePlayer.cueVideo(videoId, videoTime);

                    if (slidingLayout != null) {
                        currentState = SlidingUpPanelLayout.PanelState.EXPANDED;
                        slidingLayout.setPanelState(currentState);
                    }
                } else {
                    initializeYoutubePlayer();
                }
            }
        },500);

    }

    private void init() {
        if (playerView == null) {
            initYTPlayer();
            if (dragView == null && slidingLayout == null && playerView == null) {
                Toast.makeText(this, "Initialization failed.", Toast.LENGTH_SHORT).show();
                return;
            }
            frameLayout = findViewById(R.id.frameLayout);
            tvVideoDuration = findViewById(R.id.video_duration_label);
            tvVideoTitle = findViewById(R.id.video_title_label);
            (findViewById(R.id.btnClose)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeYoutubePlayer();
                }
            });

            initializeYoutubePlayer();
        }
    }

    private void closeYoutubePlayer() {
        slidingLayout.setShadowHeight(0);
        dragView.setVisibility(View.GONE);
        resetPadding();
    }

    private void initializeYoutubePlayer() {
        playerView.initialize(YTConfig.getApiKey(), this);
        dragView.setVisibility(View.GONE);
        if (slidingLayout != null) {
            slidingLayout.addPanelSlideListener(this);
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        this.youTubePlayer = youTubePlayer;
        this.wasRestored = wasRestored;
        onInitializationSuccess();
        Logger.log("onInitializationSuccess");

        youTubePlayer.setPlaybackEventListener(playbackEventListener);
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);

        youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);

        if (!TextUtils.isEmpty(videoId)) {
            playVideo(videoId);
        }
        getPlayerSize();

        youTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
            @Override
            public void onFullscreen(boolean b) {
                isFullScreen = b;
            }
        });
    }

    private void getPlayerSize() {
        try {
//            int[] size = SizeUtil.getScreenWidthAndHeight(this);
            playerWidth = playerView.getMeasuredWidth();
            playerHeight = playerView.getMeasuredHeight();
        } catch (Exception e) {
            playerWidth = 0;
            playerHeight = 0;
        }
    }


    //Toast pop up messaging to show errors.
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        final int REQUEST_CODE = 1;

        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, REQUEST_CODE).show();

        } else {
            String errorMessage = String.format("There was an error initializing the YoutubePlayer (&1$s)", youTubeInitializationResult.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }

    }

    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onPlaying() {
            Logger.log("onPlaying");
        }

        @Override
        public void onPaused() {
            Logger.log("onPaused");
            videoTime = youTubePlayer.getCurrentTimeMillis();
        }

        @Override
        public void onStopped() {
            Logger.log("onStopped");
        }

        @Override
        public void onBuffering(boolean b) {
            Logger.log("onBuffering");
        }

        @Override
        public void onSeekTo(int i) {
            Logger.log("onSeekTo");
        }
    };

    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onLoading() {
            Logger.log("onLoading");
        }

        @Override
        public void onLoaded(String s) {
            Logger.log("onLoaded");
            youTubePlayer.play();
        }

        @Override
        public void onAdStarted() {
            Logger.log("onAdStarted");
        }

        @Override
        public void onVideoStarted() {
            Logger.log("onVideoStarted");
        }

        @Override
        public void onVideoEnded() {
            Logger.log("onVideoEnded");
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {
            Logger.log("onError");
        }
    };

    @Override
    public void onPanelSlide(View panel, float slideOffset) {
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        float twoDigitsF = Float.valueOf(decimalFormat.format(slideOffset));
        updateYoutubeLayout((int) (twoDigitsF * 100));
    }

    @Override
    public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
        currentState = newState;
        if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
            Log.d("@onPanelStateChanged", "EXPANDED" );
            updateLayoutParams(YouTubePlayerView.LayoutParams.MATCH_PARENT, YouTubePlayerView.LayoutParams.WRAP_CONTENT);
            youTubePlayer.play();
            currentState = SlidingUpPanelLayout.PanelState.EXPANDED;
            dragView.setBackgroundColor(Color.TRANSPARENT);
            resetPadding();
        } else if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            Log.d("@onPanelStateChanged", "COLLAPSED");
            try {
                updateLayoutParams(SizeUtil.dpToPx(125), SizeUtil.dpToPx(85));
            }catch (Exception e){
                e.printStackTrace();
            }
            youTubePlayer.pause();
            currentState = SlidingUpPanelLayout.PanelState.COLLAPSED;
            slidingLayout.setShadowHeight(10);
            dragView.setBackgroundColor(Color.WHITE);
            frameLayout.setPadding(0, 0, 0, 350);
        }
    }


    private void updateYoutubeLayout(int slideOffset) {
        this.mSlideOffset = slideOffset;
        Log.d("@SlidingUpPanelLayout", "" + slideOffset);
        SlidingUpPanelLayout.PanelState panelState = slidingLayout.getPanelState();
        if (panelState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            Log.d("@SlidingUpPanelLayout", "COLLAPSED-" + slideOffset);

        } else if (panelState == SlidingUpPanelLayout.PanelState.DRAGGING) {
            Log.d("@SlidingUpPanelLayout", "DRAGGING-" + slideOffset);
            dragView.setBackgroundColor(Color.TRANSPARENT);

        } else if (panelState == SlidingUpPanelLayout.PanelState.EXPANDED) {
            Log.d("@SlidingUpPanelLayout", "EXPANDED-" + slideOffset);
        } else if (panelState == SlidingUpPanelLayout.PanelState.ANCHORED) {
            Log.d("@SlidingUpPanelLayout", "ANCHORED-" + slideOffset);
        }
//        try {
//            if (slideOffset == 0) {
//                updateLayoutParams(SizeUtil.dpToPx(125), SizeUtil.dpToPx(85));
//                youTubePlayer.pause();
//                currentState = SlidingUpPanelLayout.PanelState.COLLAPSED;
//                slidingLayout.setShadowHeight(10);
//                dragView.setBackgroundColor(Color.WHITE);
//            } else if (slideOffset == 100) {
//                updateLayoutParams(YouTubePlayerView.LayoutParams.MATCH_PARENT, YouTubePlayerView.LayoutParams.WRAP_CONTENT);
//                youTubePlayer.play();
//                currentState = SlidingUpPanelLayout.PanelState.EXPANDED;
//                dragView.setBackgroundColor(Color.TRANSPARENT);
//            }
////            else {
////                updateLayoutParams(slideOffset);
////            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onBackPressed() {
        if (isFullScreen) {
            youTubePlayer.setFullscreen(false);
            slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        } else {
            if (slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                dragView.setVisibility(View.GONE);
                slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                resetPadding();
            } else if (slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.HIDDEN) {
                super.onBackPressed();
            } else {
                slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        }

    }

    private void resetPadding() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                frameLayout.setPadding(0, 0, 0, 0);
            }
        }, 100);
    }

    private void updateLayoutParams(int slideOffset) throws Exception {
        int width = 0, height = 0;
        int currentPlayerWidth = playerView.getMeasuredWidth();
        int currentPlayerHeight = playerView.getMeasuredHeight();
        if (currentState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            width = playerWidth + getLayoutSize(playerWidth, slideOffset);
            height = playerHeight + getLayoutSize(playerHeight, slideOffset);
        } else if (currentState == SlidingUpPanelLayout.PanelState.EXPANDED) {
            width = playerWidth - getLayoutSize(playerWidth, slideOffset);
            height = playerHeight - getLayoutSize(playerHeight, slideOffset);
        }
        updateLayoutParams(SizeUtil.pxToDp(width), SizeUtil.pxToDp(height));
    }

    private int getLayoutSize(int screenSize, int slideOffset) {
        return (screenSize * slideOffset) / 100;
    }

    private void updateLayoutParams(int width, int height) {
        int fromWidth = playerView.getMeasuredWidth();
        int fromHeight = playerView.getMeasuredHeight();
        if(width ==YouTubePlayerView.LayoutParams.MATCH_PARENT){
            AnimUtil.resizeView(playerView, fromWidth, fromHeight, playerWidth, playerHeight);
        }else{
            AnimUtil.resizeView(playerView, fromWidth, fromHeight, width, height);
        }
        YouTubePlayerView.LayoutParams params = playerView.getLayoutParams();
        params.width = width;
        params.height = height;
        playerView.setLayoutParams(params);
    }

}
