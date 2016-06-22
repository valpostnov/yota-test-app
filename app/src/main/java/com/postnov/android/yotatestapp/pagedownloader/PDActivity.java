package com.postnov.android.yotatestapp.pagedownloader;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.postnov.android.yotatestapp.R;
import com.postnov.android.yotatestapp.Utils;
import com.postnov.android.yotatestapp.pagedownloader.interfaces.PDPresenter;
import com.postnov.android.yotatestapp.pagedownloader.interfaces.PDView;

public class PDActivity extends AppCompatActivity implements PDView
{
    public static final String RESULT_STATE = "com.postnov.android.RESULT_STATE";
    private static final String PD_STATE = "com.postnov.android.PD_STATE";

    private EditText mAddressView;
    private TextView mResultView;
    private ProgressDialog mProgressDialog;
    private PDPresenter mPresenter;

    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_pd);
        mPresenter = new PDPresenterImpl(this);
        initViews();

        if (bundle != null)
        {
            mResultView.setText(bundle.getString(RESULT_STATE));
            if (bundle.getBoolean(PD_STATE)) mProgressDialog.show();
        }
    }

    @Override
    public void showSource(String s)
    {
        mResultView.setText(s);
    }

    @Override
    public void showProgressDialog()
    {
        mProgressDialog.show();
    }

    @Override
    public void hideProgressDialog()
    {
        mProgressDialog.dismiss();
    }

    @Override
    public void showError(String errorMessage)
    {
        mResultView.setText(errorMessage);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        mPresenter.bind(this);
    }

    public void onGoClick(View view)
    {
        go();
        hideKeyboard();
    }

    @Override
    public void onStop()
    {
        mPresenter.unbind();
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString(RESULT_STATE, String.valueOf(mResultView.getText()));
        outState.putBoolean(PD_STATE, mProgressDialog.isShowing());
    }

    private void initViews()
    {
        mResultView = (TextView) findViewById(R.id.txtSource);
        mAddressView = (EditText) findViewById(R.id.editTextAddress);
        mAddressView.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView tv, int actionId, KeyEvent event)
            {
                switch (actionId)
                {
                    case EditorInfo.IME_ACTION_GO:
                        go();
                        hideKeyboard();
                        return true;
                }
                return false;
            }
        });

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(getString(R.string.downloading));
        mProgressDialog.setCancelable(false);
    }

    private void go()
    {
        String address = Utils.formatUrl(String.valueOf(mAddressView.getText()));
        mPresenter.getPage(address);
    }

    private void hideKeyboard()
    {
        View view = getCurrentFocus();
        if (view != null)
        {
            view.clearFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

            if (imm != null)
            {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}
