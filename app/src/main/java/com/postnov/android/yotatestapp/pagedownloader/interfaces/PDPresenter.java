package com.postnov.android.yotatestapp.pagedownloader.interfaces;

/**
 * Created by platon on 21.06.2016.
 */
public interface PDPresenter
{
    void getPage(String url);
    void bind(PDView view);
    void unbind();
}
