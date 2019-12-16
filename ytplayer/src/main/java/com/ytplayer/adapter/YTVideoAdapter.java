package com.ytplayer.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ytplayer.R;
import com.ytplayer.util.YTConstant;
import com.ytplayer.util.YTType;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by sonu on 10/11/17.
 */

public class YTVideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = YTVideoAdapter.class.getSimpleName();
    private final OnItemClickListener<YTVideoModel> listener;
    private int minimumItemCount = YTConstant.MAX_RESULTS_COUNT;
    private boolean isLoading;
    private final Context context;
    private YTType ytType;
    private ArrayList<YTVideoModel> youtubeVideoModelArrayList;
    private LoadMoreItems onLoadMoreListener;

    public YTVideoAdapter setMinimumItemCount(int minimumItemCount) {
        this.minimumItemCount = minimumItemCount;
        return this;
    }

    public YTVideoAdapter setOnLoadMoreListener(LoadMoreItems mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
        return this;
    }

    public interface LoadMoreItems {
        void onLoadMore();
        void onProgressUpdateBar(int visibility);
    }

    public YTVideoAdapter(Context context, YTType ytType, ArrayList<YTVideoModel> youtubeVideoModelArrayList, OnItemClickListener<YTVideoModel> listener) {
        this.context = context;
        this.ytType = ytType;
//        this.developerKey = YTConfig.getApiKey();
        this.youtubeVideoModelArrayList = youtubeVideoModelArrayList;
        this.listener = listener;
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.yt_video_custom_layout2, parent, false);
        return new YoutubeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof YoutubeViewHolder) {
            YoutubeViewHolder holder = (YoutubeViewHolder) viewHolder;
            final YTVideoModel item = youtubeVideoModelArrayList.get(position);

            if (!TextUtils.isEmpty(item.getTitle())) {
                holder.tvTitle.setVisibility(View.VISIBLE);
                holder.tvTitle.setText(item.getTitle());
            } else {
                holder.tvTitle.setVisibility(View.GONE);
            }
            if (ytType == YTType.PLAYLIST && !TextUtils.isEmpty(item.getChannelTitle())) {
                holder.tvChannel.setVisibility(View.VISIBLE);
                holder.tvChannel.setText(item.getChannelTitle());
            } else {
                holder.tvChannel.setVisibility(View.GONE);
            }
            if (item.getStatistics()!=null && !TextUtils.isEmpty(item.getStatistics().getDuration())) {
                holder.tvDuration.setVisibility(View.VISIBLE);
                holder.llDuration.setVisibility(View.VISIBLE);
                holder.tvDuration.setText(item.getStatistics().getDuration());
            }  else {
                holder.tvDuration.setVisibility(View.GONE);
                holder.llDuration.setVisibility(View.GONE);
            }
            if (item.getStatistics()!=null && !TextUtils.isEmpty(item.getStatistics().getViewCount())) {
                holder.tvViews.setVisibility(View.VISIBLE);
                holder.tvViews.setText(getViewCount(item.getStatistics().getViewCount())+" views");
            }  else {
                holder.tvViews.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(item.getTotalResults())) {
                holder.tvCount.setText(item.getTotalResults());
                holder.tvCount.setVisibility(View.VISIBLE);
                holder.llCount.setVisibility(View.VISIBLE);
            } else {
                holder.tvCount.setVisibility(View.GONE);
                holder.llCount.setVisibility(View.GONE);
            }

            if (item.getImage() == null) {
                /*  initialize the thumbnail image view , we need to pass Developer Key */
//            holder.videoThumbnailImageView.initialize(developerKey, new YouTubeThumbnailView.OnInitializedListener() {
//                @Override
//                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
//                    //when initialization is sucess, set the video id to thumbnail to load
//                    youTubeThumbnailLoader.setVideo(item.getVideoId());
//
//                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
//                        @Override
//                        public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
//                            //when thumbnail loaded successfully release the thumbnail loader as we are showing thumbnail in adapter
//                            youTubeThumbnailLoader.release();
//                        }
//
//                        @Override
//                        public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
//                            //print or show error when thumbnail load failed
//                            Log.e(TAG, "Youtube Thumbnail Error");
//                        }
//                    });
//                }
//
//                @Override
//                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
//                    //print or show error when initialization failed
//                    Log.e(TAG, "Youtube Initialization Failure");
//
//                }
//            });
                holder.ivThumbnail.setVisibility(View.VISIBLE);
//                holder.videoThumbnailImageView.setVisibility(View.VISIBLE);
            } else {
                holder.ivThumbnail.setVisibility(View.VISIBLE);
//                holder.videoThumbnailImageView.setVisibility(View.GONE);
//            Picasso.get().load(item.getImage()).fit().centerInside().into(holder.ivThumbnail);
                Glide
                        .with(context)
                        .load(item.getImage())
                        .apply(new RequestOptions().placeholder(R.drawable.ic_yt_placeholder))
                        .into(holder.ivThumbnail);

            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, ytType);
                }
            });

            if (onLoadMoreListener != null && getItemCount() >= minimumItemCount
                    && !isLoading && position == youtubeVideoModelArrayList.size() - 1) {
                updateList(true);
                onLoadMoreListener.onLoadMore();
                isLoading = true;
            }
        }
    }

    private String getViewCount(String views) {
        try {
            return NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(views));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return views;
        }
    }

    /**
     * Call this method when no data available on server or no when no longer to use.
     */
    public void stop() {
        isLoading = true;
        updateList(false);
    }

    private void updateList(boolean isAdd) {
        onLoadMoreListener.onProgressUpdateBar(isAdd ? View.VISIBLE : View.GONE);
    }
    /**
     * Call this method when data fetch successfully from server and hide progressBar.
     */
    public void finish() {
        updateList(false);
        isLoading = false;
    }

    public void setYtType(YTType ytType) {
        this.ytType = ytType;
    }

    @Override
    public int getItemCount() {
        return youtubeVideoModelArrayList != null ? youtubeVideoModelArrayList.size() : 0;
    }

    class YoutubeViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivThumbnail;
        private final View llDuration,llCount;
        private TextView tvTitle, tvDuration,tvCount,tvViews,tvChannel;

        YoutubeViewHolder(View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.imageView);
//            videoThumbnailImageView = itemView.findViewById(R.id.video_thumbnail_image_view);
            tvTitle = itemView.findViewById(R.id.tv_video_title_label);
            tvChannel = itemView.findViewById(R.id.tv_title_channel);
            tvDuration = itemView.findViewById(R.id.tv_video_duration);
            llDuration = itemView.findViewById(R.id.ll_video_duration);
            tvCount = itemView.findViewById(R.id.tv_total_video_count);
            llCount = itemView.findViewById(R.id.ll_video_count);
            tvViews = itemView.findViewById(R.id.tv_video_views_count);
        }
    }

}
