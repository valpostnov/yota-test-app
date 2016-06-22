package com.postnov.android.yotatestapp.pagedownloader.interfaces;

/**
 * Created by platon on 21.06.2016.
 */
public interface PDView
{
    void showSource(String s);
    void showProgressDialog();
    void hideProgressDialog();
    void showError(String errorMessage);
}
