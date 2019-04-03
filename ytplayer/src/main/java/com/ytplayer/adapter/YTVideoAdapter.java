package com.ytplayer.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.ytplayer.R;

import java.util.ArrayList;

/**
 * Created by sonu on 10/11/17.
 */

public class YTVideoAdapter extends RecyclerView.Adapter<YTVideoAdapter.YoutubeViewHolder> {
    private static final String TAG = YTVideoAdapter.class.getSimpleName();
    private final OnItemClickListener<YTVideoModel> listener;
    private ArrayList<YTVideoModel> youtubeVideoModelArrayList;
    private String developerKey;


    public YTVideoAdapter(String developerKey, ArrayList<YTVideoModel> youtubeVideoModelArrayList, OnItemClickListener<YTVideoModel> listener) {
        this.developerKey = developerKey;
        this.youtubeVideoModelArrayList = youtubeVideoModelArrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public YoutubeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.yt_video_custom_layout, parent, false);
        return new YoutubeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YoutubeViewHolder holder, final int position) {

        final YTVideoModel item = youtubeVideoModelArrayList.get(position);

        if(!TextUtils.isEmpty(item.getTitle())) {
            holder.videoTitle.setVisibility(View.VISIBLE);
            holder.videoTitle.setText(item.getTitle());
        }else{
            holder.videoTitle.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(item.getDuration())) {
            holder.videoDuration.setVisibility(View.VISIBLE);
            holder.videoDuration.setText(item.getDuration());
        }else {
            holder.videoDuration.setVisibility(View.GONE);
        }


        /*  initialize the thumbnail image view , we need to pass Developer Key */
        holder.videoThumbnailImageView.initialize(developerKey, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                //when initialization is sucess, set the video id to thumbnail to load
                youTubeThumbnailLoader.setVideo(item.getVideoId());

                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        //when thumbnail loaded successfully release the thumbnail loader as we are showing thumbnail in adapter
                        youTubeThumbnailLoader.release();
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                        //print or show error when thumbnail load failed
                        Log.e(TAG, "Youtube Thumbnail Error");
                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                //print or show error when initialization failed
                Log.e(TAG, "Youtube Initialization Failure");

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(item);
            }
        });

    }

    @Override
    public int getItemCount() {
        return youtubeVideoModelArrayList != null ? youtubeVideoModelArrayList.size() : 0;
    }

    class YoutubeViewHolder extends RecyclerView.ViewHolder {

        private YouTubeThumbnailView videoThumbnailImageView;
        private TextView videoTitle, videoDuration;

        YoutubeViewHolder(View itemView) {
            super(itemView);
            videoThumbnailImageView = itemView.findViewById(R.id.video_thumbnail_image_view);
            videoTitle = itemView.findViewById(R.id.video_title_label);
            videoDuration = itemView.findViewById(R.id.video_duration_label);
        }
    }

}
