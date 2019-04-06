package com.ytplayer.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.ytplayer.R;
import com.ytplayer.adapter.pagination.GlideApp;
import com.ytplayer.adapter.pagination.GlideRequest;
import com.ytplayer.util.SizeUtil;
import com.ytplayer.util.YTType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suleiman on 19/10/16.
 */

public class YTPaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // View Types
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w200";
 

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private PaginationAdapterCallback mCallback;

    private String errorMsg;
    private static final String TAG = YTVideoAdapter.class.getSimpleName();
    private OnItemClickListener<YTVideoModel> listener;
    private Context context;
    private YTType ytType;
    private ArrayList<YTVideoModel> mList;
    private String developerKey;

    public YTPaginationAdapter(Context context, YTType ytType, String developerKey, ArrayList<YTVideoModel> youtubeVideoModelArrayList, OnItemClickListener<YTVideoModel> listener) {
        this.context = context;
        this.ytType = ytType;
        this.developerKey = developerKey;
        this.mList = youtubeVideoModelArrayList;
        this.mCallback = (PaginationAdapterCallback) context;
        this.listener = listener;
    }

    public void setYtType(YTType ytType) {
        this.ytType = ytType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.yt_video_custom_layout, parent, false);
                viewHolder = new YTViewHolder(viewItem);
                break;
            case LOADING:
                View viewLoading = inflater.inflate(R.layout.yt_item_progress, parent, false);
                viewHolder = new LoadingVH(viewLoading);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        final YTVideoModel item = mList.get(position); // Movie

        switch (getItemViewType(position)) {

            case ITEM:
                final YTViewHolder holder = (YTViewHolder) viewHolder;

                if (!TextUtils.isEmpty(item.getTitle())) {
                    holder.videoTitle.setVisibility(View.VISIBLE);
                    holder.videoTitle.setText(item.getTitle());
                } else {
                    holder.videoTitle.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(item.getDuration())) {
                    holder.videoDuration.setVisibility(View.VISIBLE);
                    holder.videoDuration.setText(item.getDuration());
                } else {
                    if (!TextUtils.isEmpty(item.getPublishedAt())) {
                        holder.videoDuration.setVisibility(View.VISIBLE);
                        holder.videoDuration.setText(SizeUtil.formatDate(item.getPublishedAt()));
                    }else {
                        holder.videoDuration.setVisibility(View.GONE);
                    }
                }

                if (item.getImage() == null) {
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
                    holder.imageView.setVisibility(View.GONE);
                    holder.videoThumbnailImageView.setVisibility(View.VISIBLE);
                } else {
                    holder.imageView.setVisibility(View.VISIBLE);
                    holder.videoThumbnailImageView.setVisibility(View.GONE);
//            Picasso.get().load(item.getImage()).fit().centerInside().into(holder.imageView);
                    Glide
                            .with(context)
                            .load(item.getImage())
                            .into(holder.imageView);

                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(item, ytType);
                    }
                });

                break;

            case LOADING:
                LoadingVH loadingVH = (LoadingVH) viewHolder;

                if (retryPageLoad) {
                    loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                    loadingVH.mProgressBar.setVisibility(View.GONE);

                    loadingVH.mErrorTxt.setText(
                            errorMsg != null ?
                                    errorMsg :
                                    context.getString(R.string.error_msg_unknown));

                } else {
                    loadingVH.mErrorLayout.setVisibility(View.GONE);
                    loadingVH.mProgressBar.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == mList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    /*
        Helpers - Pagination
   _________________________________________________________________________________________________
    */

    public void add(YTVideoModel r) {
        mList.add(r);
        notifyItemInserted(mList.size() - 1);
    }
//
//    public void addAll(List<YTVideoModel> moveYTVideoModels) {
//        for (YTVideoModel YTVideoModel : moveYTVideoModels) {
//            add(YTVideoModel);
//        }
//    }

    public void remove(YTVideoModel r) {
        int position = mList.indexOf(r);
        if (position > -1) {
            mList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new YTVideoModel());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = mList.size() - 1;
        YTVideoModel YTVideoModel = getItem(position);

        if (YTVideoModel != null) {
            mList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public YTVideoModel getItem(int position) {
        return mList.get(position);
    }

    /**
     * Displays Pagination retry footer view along with appropriate errorMsg
     *
     * @param show
     * @param errorMsg to display if page load fails
     */
    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(mList.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    class YTViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private YouTubeThumbnailView videoThumbnailImageView;
        private TextView videoTitle, videoDuration;

        YTViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            videoThumbnailImageView = itemView.findViewById(R.id.video_thumbnail_image_view);
            videoTitle = itemView.findViewById(R.id.video_title_label);
            videoDuration = itemView.findViewById(R.id.video_duration_label);
        }
    }

    protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ProgressBar mProgressBar;
        private ImageButton mRetryBtn;
        private TextView mErrorTxt;
        private LinearLayout mErrorLayout;

        public LoadingVH(View itemView) {
            super(itemView);

            mProgressBar = itemView.findViewById(R.id.loadmore_progress);
            mRetryBtn = itemView.findViewById(R.id.loadmore_retry);
            mErrorTxt = itemView.findViewById(R.id.loadmore_errortxt);
            mErrorLayout = itemView.findViewById(R.id.loadmore_errorlayout);

            mRetryBtn.setOnClickListener(this);
            mErrorLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view.getId() ==R.id.loadmore_retry || view.getId()==R.id.loadmore_errorlayout){
                showRetry(false, null);
                mCallback.retryPageLoad();
            }
        }
    }

}
