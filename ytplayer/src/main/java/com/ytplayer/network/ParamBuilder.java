package com.ytplayer.network;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class ParamBuilder {
    public static List<KeyValuePair> getMultiplePlayListDetails(String part, String ids, String maxResults, String key) {
        List<KeyValuePair> list = new ArrayList<>();
        list.add(new KeyValuePair("part",part));
        list.add(new KeyValuePair("id",ids));
        list.add(new KeyValuePair("maxResults",maxResults));
        list.add(new KeyValuePair("key",key));
        return list;
    }

    public static List<KeyValuePair> getChannelItems(String part, String channelId, String maxResults, String key) {
        List<KeyValuePair> list = new ArrayList<>();
        list.add(new KeyValuePair("part",part));
        list.add(new KeyValuePair("channelId",channelId));
        list.add(new KeyValuePair("maxResults",maxResults));
        list.add(new KeyValuePair("key",key));
        return list;
    }

    public static List<KeyValuePair> getPlayListItems(String part, String playlistId, int maxResults, String key, String nextPageToken) {
        List<KeyValuePair> list = new ArrayList<>();
        list.add(new KeyValuePair("part",part));
        list.add(new KeyValuePair("playlistId",playlistId));
        list.add(new KeyValuePair("maxResults",maxResults + ""));
        list.add(new KeyValuePair("key",key));
        if(!TextUtils.isEmpty(nextPageToken)) {
            list.add(new KeyValuePair("pageToken", key));
        }
        return list;
    }

    public static List<KeyValuePair> getStatistics(String part, String id, String key) {
        List<KeyValuePair> list = new ArrayList<>();
        list.add(new KeyValuePair("part",part));
        list.add(new KeyValuePair("id",id));
        list.add(new KeyValuePair("key",key));
        return list;
    }

    public static List<KeyValuePair> getSearchParam(String part, String maxResults, String searchWord, String pageToken, String type, String key) {
        List<KeyValuePair> list = new ArrayList<>();
        list.add(new KeyValuePair("part",part));
        list.add(new KeyValuePair("maxResults",maxResults));
        list.add(new KeyValuePair("q",searchWord));
        list.add(new KeyValuePair("pageToken",pageToken));
        list.add(new KeyValuePair("type",type));
        list.add(new KeyValuePair("key",key));
        return list;
    }
}
