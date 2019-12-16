package com.ytplayer.network;


import com.ytplayer.YTUtility;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Abhijit on 13-Dec-16.
 */

public class ApiCall {
    public static final String IO_EXCEPTION = "IOException";

    //GET network request
    public static String GET(String url, String params){
        return GET(url+params);
    }
    public static String GET(String url, List<KeyValuePair> params){
        return GET(url,params,false);
    }
    public static String GET(String url, List<KeyValuePair> params, boolean hasEncoding){
        String paramString = format2(params,hasEncoding ? "utf-8":null);
        String finalUrl = url + "?" + paramString;
        return GET(finalUrl);
    }

    public static String GET(String url){
        YTUtility.log(url);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if(response.body()!=null){
//                YTUtility.log(response.body().string());
                return response.body().string();
            }else {
                return IO_EXCEPTION;
            }
        }catch (IOException e){
            return IO_EXCEPTION+" "+e.toString();
        }
    }




    public static String format2(List<KeyValuePair> list, String encoding) {
        StringBuilder result = new StringBuilder();

        for (KeyValuePair parameter:list){
            String encodedValue;
            String encodedKey;
            if(encoding!=null){
                encodedKey = encode(parameter.getKey(), encoding);
                encodedValue = parameter.getValue() != null?encode(parameter.getValue(), encoding):"";
            }else{
                encodedKey = parameter.getKey();
                encodedValue = parameter.getValue();
            }
            if(result.length() > 0) {
                result.append("&");
            }

            result.append(encodedKey);
            result.append("=");
            result.append(encodedValue);
        }
        return result.toString();
    }

    private static String decode(String content, String encoding) {
        try {
            return URLDecoder.decode(content, encoding != null?encoding:"ISO-8859-1");
        } catch (UnsupportedEncodingException var3) {
            throw new IllegalArgumentException(var3);
        }
    }

    private static String encode(String content, String encoding) {
        try {
            return URLEncoder.encode(content, encoding != null?encoding:"ISO-8859-1");
        } catch (UnsupportedEncodingException var3) {
            throw new IllegalArgumentException(var3);
        }
    }
}