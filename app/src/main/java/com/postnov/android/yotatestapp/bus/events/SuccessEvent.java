package com.postnov.android.yotatestapp.bus.events;

/**
 * Created by platon on 21.06.2016.
 */
public class SuccessEvent
{
    private String mResult;

    public SuccessEvent(String result)
    {
        mResult = result;
    }

    public String getResult()
    {
        return mResult;
    }
}
