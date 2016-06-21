package com.postnov.android.yotatestapp.bus;

import android.util.Log;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by platon on 21.06.2016.
 */
public class RxBus
{
    private String TAG = "RxBus";
    private Subject<Object, Object> mBus;

    public RxBus()
    {
        mBus = new SerializedSubject<>(PublishSubject.create());
    }

    public void post(Object o)
    {
        Log.v(TAG, "Sending: " + o);
        mBus.onNext(o);
    }

    public Observable<Object> toObservable()
    {
        return mBus;
    }
}
