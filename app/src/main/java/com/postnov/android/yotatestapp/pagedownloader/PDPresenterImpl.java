package com.postnov.android.yotatestapp.pagedownloader;

import android.content.Context;

import com.postnov.android.yotatestapp.YotaTestApp;
import com.postnov.android.yotatestapp.bus.events.ErrorEvent;
import com.postnov.android.yotatestapp.bus.RxBus;
import com.postnov.android.yotatestapp.bus.events.SuccessEvent;
import com.postnov.android.yotatestapp.pagedownloader.interfaces.PDPresenter;
import com.postnov.android.yotatestapp.pagedownloader.interfaces.PDView;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by platon on 21.06.2016.
 */
public class PDPresenterImpl implements PDPresenter
{
    private PDView mView;
    private RxBus mEventBus;
    private Subscription mRxBusSubscription;
    private Context mContext;

    public PDPresenterImpl(Context context)
    {
        mEventBus = YotaTestApp.getEventBus();
        mContext = context;
    }

    @Override
    public void getPage(String url)
    {
        mView.showProgressDialog();
        PDService.startService(mContext, url);
        unsubscribe();
        mRxBusSubscription = mEventBus
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<Object>()
                {
                    @Override
                    public void call(Object event)
                    {
                        if (event instanceof SuccessEvent)
                        {
                            mView.hideProgressDialog();
                            mView.showSource(((SuccessEvent) event).getResult());
                        }
                        else if (event instanceof ErrorEvent)
                        {
                            mView.hideProgressDialog();
                            mView.showError(((ErrorEvent) event).getError());
                        }
                    }
                })
                .subscribe();
    }

    @Override
    public void bind(PDView view)
    {
        mView = view;
    }

    @Override
    public void unbind()
    {
        unsubscribe();
        mView = null;
    }

    private void unsubscribe()
    {
        if (mRxBusSubscription != null && !mRxBusSubscription.isUnsubscribed())
        {
            mRxBusSubscription.unsubscribe();
        }
    }
}
