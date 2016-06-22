package com.postnov.android.yotatestapp;

/**
 * Created by platon on 22.06.2016.
 */
public class Utils
{
    public static String formatUrl(String url)
    {
        if (url != null && (url.contains("http://") || url.contains("https://")))
        {
            return url;
        }
        return "https://" + url;
    }

    public static String formatResponse(String message, int code)
    {
        return String.format("%s: %s", message, code);
    }
}
