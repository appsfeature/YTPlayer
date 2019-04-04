package com.ytplayer.network;

import com.ytplayer.adapter.YTVideoModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonParser {

    public static YTVideoModel parsePlayList(String response) {
        YTVideoModel jsonModel = new YTVideoModel();
        try {
            jsonModel.setList(new ArrayList<YTVideoModel>());
            JSONObject mainObject = new JSONObject(response);
            if (!mainObject.optString("nextPageToken").equals("")) {
                jsonModel.setNextPageToken(mainObject.getString("nextPageToken"));
                JSONArray itemsArr = mainObject.getJSONArray("items");
                for (int i = 0; i < itemsArr.length(); i++) {
                    YTVideoModel model = new YTVideoModel();
                    JSONObject item = itemsArr.getJSONObject(i);
                    model.setVideoId(item.optString("id"));
                    JSONObject snippet = item.getJSONObject("snippet");
                    JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                    JSONObject medium = thumbnails.getJSONObject("medium");
                    model.setPublishedAt(snippet.optString("publishedAt"));
                    model.setTitle(snippet.optString("title"));
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
            JSONObject mainObject = new JSONObject(response);
            if (!mainObject.optString("items").equals("")) {
                JSONArray itemsArr = mainObject.getJSONArray("items");
                for (int i = 0; i < itemsArr.length(); i++) {
                    YTVideoModel model = new YTVideoModel();
                    JSONObject item = itemsArr.getJSONObject(i);
                    JSONObject snippet = item.getJSONObject("snippet");
                    model.setPublishedAt(snippet.optString("publishedAt"));
                    model.setTitle(snippet.optString("title"));
                    model.setDescription(snippet.optString("description"));
                    JSONObject resourceId = snippet.getJSONObject("resourceId");
                    model.setVideoId(resourceId.optString("videoId"));
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
