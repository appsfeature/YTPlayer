package com.ytplayer.util;

import android.app.Activity;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SizeUtil {
    private static final String IMG_HEIGHT_SIZE_TAG_1 = "height=\"";
    private static final String IMG_WIDTH_SIZE_TAG_1 = "width=\"";
    private static final String IMG_HEIGHT_SIZE_TAG_2 = "height:";
    private static final String IMG_WIDTH_SIZE_TAG_2 = "width:";

    public static int[] getScreenWidthAndHeight(Activity context) throws Exception {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth = displaymetrics.widthPixels;
        int screenHeight = displaymetrics.heightPixels;
        return new int[]{screenWidth, screenHeight};
    }

    public static int dpToPx(int dp) throws Exception {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) throws Exception {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }


    public static String formatDate(String date) {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yy hh:mm:a", Locale.getDefault());

        try {
            Date d = input.parse(date);
            return output.format(d);

        } catch (ParseException e) {
            e.printStackTrace();
            return " ";
        }
    }

    public static int[] getWidthAndHeight(String source) throws Exception {
        String height = null, width = null;
        if (source.contains(IMG_HEIGHT_SIZE_TAG_1)) {
            height = source.substring(source.indexOf(IMG_HEIGHT_SIZE_TAG_1));
            height = height.substring(IMG_HEIGHT_SIZE_TAG_1.length());
            height = height.substring(0, height.indexOf("\""));

            width = source.substring(source.indexOf(IMG_WIDTH_SIZE_TAG_1));
            width = width.substring(IMG_WIDTH_SIZE_TAG_1.length());
            width = width.substring(0, width.indexOf("\""));
        } else if (source.contains("height:")) {
            height = source.substring(source.indexOf(IMG_HEIGHT_SIZE_TAG_2));
            height = height.substring(IMG_HEIGHT_SIZE_TAG_2.length());
            height = height.substring(0, height.indexOf("px"));

            width = source.substring(source.indexOf(IMG_WIDTH_SIZE_TAG_2));
            width = width.substring(IMG_WIDTH_SIZE_TAG_2.length());
            width = width.substring(0, width.indexOf("px"));
        }

        int imgWidth = SizeUtil.dpToPx(Integer.parseInt(width));
        int imgHeight = SizeUtil.dpToPx(Integer.parseInt(height));

        return new int[]{imgWidth, imgHeight};
    }

}
