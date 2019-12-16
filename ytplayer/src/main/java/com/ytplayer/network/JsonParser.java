package com.ytplayer.network;

import com.ytplayer.YTUtility;
import com.ytplayer.adapter.YTVideoModel;
import com.ytplayer.adapter.YTVideoStatistics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class JsonParser {

    public static YTVideoModel parsePlayList(String response) {
        YTVideoModel jsonModel = new YTVideoModel();
        try {
            jsonModel.setList(new ArrayList<YTVideoModel>());
            JSONObject mainObject = new JSONObject(response);
            if (!mainObject.optString("items").equals("")) {
                jsonModel.setNextPageToken(mainObject.optString("nextPageToken"));
                JSONObject pageInfo = mainObject.getJSONObject("pageInfo");
                jsonModel.setTotalResults(pageInfo.optString("totalResults"));
                JSONArray itemsArr = mainObject.getJSONArray("items");
                for (int i = 0; i < itemsArr.length(); i++) {
                    YTVideoModel model = new YTVideoModel();
                    JSONObject item = itemsArr.getJSONObject(i);
                    model.setVideoId(item.optString("id"));
                    JSONObject snippet = item.getJSONObject("snippet");
                    JSONObject contentDetails = item.getJSONObject("contentDetails");
                    model.setTotalResults(contentDetails.optString("itemCount"));
                    JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                    JSONObject medium = thumbnails.getJSONObject("medium");
                    model.setImage(medium.optString("url"));
                    model.setPublishedAt(snippet.optString("publishedAt"));
                    model.setTitle(snippet.optString("title"));
                    model.setChannelId(snippet.optString("channelId"));
                    model.setChannelTitle(snippet.optString("channelTitle"));
                    model.setDescription(snippet.optString("description"));
                    jsonModel.getList().add(model);
                }
            } else if (!mainObject.getString("error").equals("")) {
                JSONObject errorObject = mainObject.getJSONObject("error");
                jsonModel.setError(errorObject.getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            jsonModel.setError(e.getMessage());
        }
        return jsonModel;
    }

    public static YTVideoModel parseSearchList(String response) {
        YTVideoModel jsonModel = new YTVideoModel();
        try {
            jsonModel.setList(new ArrayList<YTVideoModel>());
            JSONObject mainObject = new JSONObject(response);
            if (!mainObject.optString("nextPageToken").equals("")) {
                jsonModel.setNextPageToken(mainObject.getString("nextPageToken"));
                JSONObject pageInfo = mainObject.getJSONObject("pageInfo");
                jsonModel.setTotalResults(pageInfo.optString("totalResults"));
                JSONArray itemsArr = mainObject.getJSONArray("items");
                for (int i = 0; i < itemsArr.length(); i++) {
                    YTVideoModel model = new YTVideoModel();
                    JSONObject item = itemsArr.getJSONObject(i);
                    JSONObject id = item.getJSONObject("id");
                    model.setVideoId(id.optString("videoId"));
                    JSONObject snippet = item.getJSONObject("snippet");
                    JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                    JSONObject medium = thumbnails.getJSONObject("medium");
                    model.setPublishedAt(snippet.optString("publishedAt"));
                    model.setTitle(snippet.optString("title"));
                    model.setChannelId(snippet.optString("channelId"));
                    model.setChannelTitle(snippet.optString("channelTitle"));
                    model.setDescription(snippet.optString("description"));
                    model.setImage(medium.optString("url"));
                    jsonModel.getList().add(model);
                }
            } else if (!mainObject.getString("error").equals("")) {
                JSONObject errorObject = mainObject.getJSONObject("error");
                jsonModel.setError(errorObject.getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            jsonModel.setError(e.getMessage());
        }
        return jsonModel;
    }

    public static YTVideoModel parsePlayListItems(String response) {
        YTVideoModel jsonModel = new YTVideoModel();
        try {
            jsonModel.setList(new ArrayList<YTVideoModel>());
            jsonModel.setVideoIds(new ArrayList<String>());
            JSONObject mainObject = new JSONObject(response);
            if (!mainObject.optString("items").equals("")) {
                String pageToken = mainObject.optString("nextPageToken");
                jsonModel.setNextPageToken(pageToken !=null ? pageToken : "");
                JSONObject pageInfo = mainObject.optJSONObject("pageInfo");
                jsonModel.setTotalResults(pageInfo != null ? pageInfo.optString("totalResults") : "");
                JSONArray itemsArr = mainObject.getJSONArray("items");
                for (int i = 0; i < itemsArr.length(); i++) {
                    YTVideoModel model = new YTVideoModel();
                    JSONObject item = itemsArr.getJSONObject(i);
                    JSONObject snippet = item.getJSONObject("snippet");
                    model.setPublishedAt(snippet.optString("publishedAt"));
                    model.setTitle(snippet.optString("title"));
                    model.setChannelId(snippet.optString("channelId"));
                    model.setChannelTitle(snippet.optString("channelTitle"));
                    model.setDescription(snippet.optString("description"));
                    JSONObject resourceId = snippet.getJSONObject("resourceId");
                    JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                    JSONObject medium = thumbnails.getJSONObject("medium");
                    model.setImage(medium.optString("url"));
                    model.setVideoId(resourceId.optString("videoId"));
                    jsonModel.getVideoIds().add(model.getVideoId());
                    jsonModel.getList().add(model);
                }
            } else if (!mainObject.getString("error").equals("")) {
                JSONObject errorObject = mainObject.getJSONObject("error");
                jsonModel.setError(errorObject.getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            jsonModel.setError(e.getMessage());
        }
        return jsonModel;
    }

    public static YTVideoStatistics parseStatistics(String response) {
        YTVideoStatistics jsonModel = new YTVideoStatistics();
        try {
            jsonModel.setList(new ArrayList<YTVideoStatistics>());
            JSONObject mainObject = new JSONObject(response);
            if (!mainObject.optString("items").equals("")) {
                JSONArray itemsArr = mainObject.getJSONArray("items");
                for (int i = 0; i < itemsArr.length(); i++) {
                    YTVideoStatistics model = new YTVideoStatistics();
                    JSONObject item = itemsArr.getJSONObject(i);
                    model.setVideoId(item.optString("id"));
                    JSONObject snippet = item.getJSONObject("snippet");
                    model.setPublishedAt(snippet.optString("publishedAt"));
                    model.setChannelId(snippet.optString("channelId"));
                    model.setChannelTitle(snippet.optString("channelTitle"));
                    model.setTitle(snippet.optString("title"));
                    model.setDescription(snippet.optString("description"));
                    JSONObject statistics = item.getJSONObject("statistics");
                    model.setViewCount(statistics.optString("viewCount"));
                    model.setLikeCount(statistics.optString("likeCount"));
                    model.setDislikeCount(statistics.optString("dislikeCount"));
                    model.setFavoriteCount(statistics.optString("favoriteCount"));
                    model.setCommentCount(statistics.optString("commentCount"));
                    JSONObject contentDetails = item.getJSONObject("contentDetails");
                    model.setDuration(YTUtility.getValidDuration(contentDetails.optString("duration")));
                    jsonModel.getList().add(model);
                }
            } else if (!mainObject.getString("error").equals("")) {
                JSONObject errorObject = mainObject.getJSONObject("error");
                jsonModel.setError(errorObject.getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            jsonModel.setError(e.getMessage());
        }
        return jsonModel;
    }
}
