package com.ytplayer.network;

import java.util.ArrayList;
import java.util.List;

public class ParamBuilder {
    public static List<KeyValuePair> get(String part, String channelId, String maxResults, String key) {
        List<KeyValuePair> list = new ArrayList<>();
        list.add(new KeyValuePair("part",part));
        list.add(new KeyValuePair("channelId",channelId));
        list.add(new KeyValuePair("maxResults",maxResults));
        list.add(new KeyValuePair("key",key));
        return list;
    }

    public static List<KeyValuePair> getPlayListItems(String part, String playlistId, String maxResults, String key) {
        List<KeyValuePair> list = new ArrayList<>();
        list.add(new KeyValuePair("part",part));
        list.add(new KeyValuePair("playlistId",playlistId));
        list.add(new KeyValuePair("maxResults",maxResults));
        list.add(new KeyValuePair("key",key));
        return list;
    }

    public static List<KeyValuePair> getStatistics(String part, String id, String key) {
        List<KeyValuePair> list = new ArrayList<>();
        list.add(new KeyValuePair("part",part));
        list.add(new KeyValuePair("id",id));
        list.add(new KeyValuePair("key",key));
        return list;
    }
}
