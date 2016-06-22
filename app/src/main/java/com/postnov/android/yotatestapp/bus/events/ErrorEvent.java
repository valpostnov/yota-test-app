package com.postnov.android.yotatestapp.bus.events;

/**
 * Created by platon on 21.06.2016.
 */
public class ErrorEvent
{
    private String mErrorMessage;

    public ErrorEvent(String errorMsg)
    {
        mErrorMessage = errorMsg;
    }

    public String getErrorMessage()
    {
        return mErrorMessage;
    }
}
