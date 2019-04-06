package com.ytplayer.adapter;

import com.ytplayer.util.YTType;

public interface OnItemClickListener<T> {
    void onItemClick(T item, YTType ytType);
}
