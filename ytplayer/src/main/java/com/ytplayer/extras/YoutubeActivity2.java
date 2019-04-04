package com.ytplayer.extras;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.slidinguppanel.SlidingUpPanelLayout;
import com.ytplayer.R;
import com.ytplayer.util.Logger;
import com.ytplayer.util.SizeUtil;
import com.ytplayer.util.YTConfig;

import java.text.DecimalFormat;

public class YoutubeActivity2 extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private YouTubePlayerView playerView;
    private YouTubePlayer youTubePlayer;
    private Context context;
    private int screenWidth;
    private int screenHeight;
    private SlidingUpPanelLayout slidingLayout;
    private SlidingUpPanelLayout.PanelState currentState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yt_activity_youtube);
        context = this;
//        //Initializing the Layout which is yt_activity_youtube.
//        ConstraintLayout layout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.yt_activity_youtube,null);
//        setContentView(layout);
//
//
//        //Using YoutubePLayerView Object class
//        //Initialize youtube video for this frame.
//        playerView = new YouTubePlayerView(this);
//        playerView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
////        layout.setPadding(20,20,20,20);
//        layout.addView(playerView);

        slidingLayout = findViewById(R.id.sliding_layout);
        playerView = findViewById(R.id.youtubePlayerView);
        playerView.initialize(YTConfig.getApiKey(), this);

        currentState = SlidingUpPanelLayout.PanelState.EXPANDED;
        slidingLayout.setPanelState(currentState);
        slidingLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                DecimalFormat decimalFormat = new DecimalFormat("#.#");
                float twoDigitsF = Float.valueOf(decimalFormat.format(slideOffset));
                updateYoutubeLayout((int) (twoDigitsF * 100));
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

            }
        });


    }

    private void updateYoutubeLayout(int slideOffset) {
        Log.d("@SlidingUpPanelLayout", "" + slideOffset);
        SlidingUpPanelLayout.PanelState panelState = slidingLayout.getPanelState();
        if (panelState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            Log.d("@SlidingUpPanelLayout", "COLLAPSED-" + slideOffset);

        } else if (panelState == SlidingUpPanelLayout.PanelState.DRAGGING) {
            Log.d("@SlidingUpPanelLayout", "DRAGGING-" + slideOffset);

        } else if (panelState == SlidingUpPanelLayout.PanelState.EXPANDED) {
            Log.d("@SlidingUpPanelLayout", "EXPANDED-" + slideOffset);
        } else if (panelState == SlidingUpPanelLayout.PanelState.ANCHORED) {
            Log.d("@SlidingUpPanelLayout", "ANCHORED-" + slideOffset);
        }
        try {
            if (slideOffset == 0) {
                updateLayoutParams(SizeUtil.dpToPx(125), SizeUtil.dpToPx(85));
                youTubePlayer.pause();
                currentState = SlidingUpPanelLayout.PanelState.COLLAPSED;
            } else if (slideOffset == 100) {
                updateLayoutParams(YouTubePlayerView.LayoutParams.MATCH_PARENT, YouTubePlayerView.LayoutParams.WRAP_CONTENT);
                youTubePlayer.play();
                currentState = SlidingUpPanelLayout.PanelState.EXPANDED;
            }
//            else {
//                updateLayoutParams(slideOffset);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if(slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            super.onBackPressed();
        }else{
            slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
    }

    private void updateLayoutParams(int slideOffset) throws Exception {
        int width = 0, height = 0;
        int currentPlayerWidth = playerView.getMeasuredWidth();
        int currentPlayerHeight = playerView.getMeasuredHeight();
        if (currentState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            width = screenWidth + getLayoutSize(screenWidth, slideOffset);
            height = screenHeight + getLayoutSize(screenHeight, slideOffset);
        } else if (currentState == SlidingUpPanelLayout.PanelState.EXPANDED) {
            width = screenWidth - getLayoutSize(screenWidth, slideOffset);
            height = screenHeight - getLayoutSize(screenHeight, slideOffset);
        }
        updateLayoutParams(SizeUtil.pxToDp(width), SizeUtil.pxToDp(height));
    }

    private int getLayoutSize(int screenSize, int slideOffset) {
        return (screenSize * slideOffset) / 100;
    }

    private void updateLayoutParams(int width, int height) {
        YouTubePlayerView.LayoutParams params = playerView.getLayoutParams();
        params.width = width;
        params.height = height;
        playerView.setLayoutParams(params);
    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        this.youTubePlayer = youTubePlayer;
        Logger.log("onInitializationSuccess");

        youTubePlayer.setPlaybackEventListener(playbackEventListener);
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);

        youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);

        if (!wasRestored) {
            youTubePlayer.cueVideo(YTConfig.getVideoId());
        }
        getPlayerSize();

        youTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
            @Override
            public void onFullscreen(boolean b) {

            }
        });
    }

    private void getPlayerSize() {
        try {
//            int[] size = SizeUtil.getScreenWidthAndHeight(this);
            screenWidth = playerView.getMeasuredWidth();
            screenHeight = playerView.getMeasuredHeight();
        } catch (Exception e) {
            screenWidth = 0;
            screenHeight = 0;
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
}
