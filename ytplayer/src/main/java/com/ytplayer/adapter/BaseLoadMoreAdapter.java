package com.ytplayer.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;

import com.ytplayer.R;

import java.util.List;

public abstract class BaseLoadMoreAdapter extends Adapter<ViewHolder> {
    private final int layoutResource;
    private LoadMoreItems onLoadMoreListener;
    private boolean isLoading;
    private List mMoreList;

    private int minimumItemCount = 10;

    public void setMinimumItemCount(int minimumItemCount) {
        this.minimumItemCount = minimumItemCount;
    }


    public BaseLoadMoreAdapter(List list) {
        this.layoutResource = R.layout.item_loading;
        this.mMoreList = list;
    }

    public BaseLoadMoreAdapter(List list, int layoutResource) {
        this.mMoreList = list;
        this.layoutResource = layoutResource;
    }


    protected abstract ViewHolder onAbstractCreateViewHolder(ViewGroup parent, int viewType);

    protected abstract void onAbstractBindViewHolder(ViewHolder holder, int position);

    public interface LoadMoreItems {
        void onLoadMore();
    }

    public void setOnLoadMoreListener(LoadMoreItems mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    @Override
    public int getItemViewType(int position) {
        return mMoreList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(layoutResource, parent, false);
            return new LoadingViewHolder(view);
        } else {
            return this.onAbstractCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        } else {
            this.onAbstractBindViewHolder(holder, position);
        }

        if (onLoadMoreListener != null && getItemCount() >= minimumItemCount
                && !isLoading && position == mMoreList.size() - 1) {
            holder.itemView.post(new Runnable() {
                @Override
                public void run() {
                    updateList(true);
                    onLoadMoreListener.onLoadMore();
                    isLoading = true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mMoreList == null ? 0 : mMoreList.size();
    }

    public void stop() {
        if (isLoading) {
            updateList(false);
        }
    }

    public void finish() {
        updateList(false);
        isLoading = false;
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        LoadingViewHolder(View view) {
            super(view);
            progressBar = view.findViewById(R.id.progressBar1);
        }
    }

    private void updateList(boolean isAdd) {
        if (isAdd) {
            mMoreList.add(null);
            notifyItemInserted(mMoreList.size() - 1);
        } else {
            if (mMoreList.size() > 1) {
                mMoreList.remove(mMoreList.size() - 1);
                notifyItemRemoved(mMoreList.size());
            }
        }
    }
}

//        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (linearLayoutManager != null) {
//                    totalItemCount = linearLayoutManager.getItemCount();
//                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
//                    if (!isLoading && totalItemCount >= minimumItemCount && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
//                        if (onLoadMoreListener != null && mMoreList!=null) {
//                            mMoreList.add(null);
//                            notifyItemInserted(mMoreList.size() - 1);
//                            onLoadMoreListener.onLoadMore();
//                        }
//                        isLoading = true;
//                    }
//                }
//            }
//        });