package com.postnov.android.yotatestapp;

import android.app.Application;

import com.postnov.android.yotatestapp.bus.RxBus;

/**
 * Created by platon on 21.06.2016.
 */
public class YotaTestApp extends Application
{
    private static RxBus mRxBus;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mRxBus = new RxBus();
    }

    public static RxBus getEventBus()
    {
        return mRxBus;
    }
}
