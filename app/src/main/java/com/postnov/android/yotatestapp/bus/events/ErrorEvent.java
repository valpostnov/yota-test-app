package com.postnov.android.yotatestapp.bus.events;

/**
 * Created by platon on 21.06.2016.
 */
public class ErrorEvent
{
    private Throwable mError;

    public ErrorEvent(Throwable error)
    {
        mError = error;
    }

    public Throwable getError()
    {
        return mError;
    }
}
