package com.postnov.android.yotatestapp.pagedownloader;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.postnov.android.yotatestapp.Utils;
import com.postnov.android.yotatestapp.YotaTestApp;
import com.postnov.android.yotatestapp.bus.events.ErrorEvent;
import com.postnov.android.yotatestapp.bus.RxBus;
import com.postnov.android.yotatestapp.bus.events.SuccessEvent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.net.HttpURLConnection.HTTP_OK;

public class PDService extends IntentService
{
    public static final String TAG = "PDService";
    public static final String EXTRA_URL = "com.postnov.android.URL";

    private static final int READ_TIMEOUT = 10000;
    private static final int CONNECTION_TIMEOUT = 15000;
    private static final String REQUEST_METHOD = "GET";

    private RxBus mEventBus;

    public PDService()
    {
        super("PDService");
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        mEventBus = YotaTestApp.getEventBus();
        Log.d(TAG, "onCreate");
    }

    public static void startService(Context context, String url)
    {
        Intent intent = new Intent(context, PDService.class);
        intent.putExtra(EXTRA_URL, url);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        String url = intent.getStringExtra(EXTRA_URL);
        HttpURLConnection conn = null;

        try
        {
            conn = getConnection(url);
            switch (conn.getResponseCode())
            {
                case HTTP_OK:
                    String result = getStringFromIS(conn.getInputStream());
                    mEventBus.post(new SuccessEvent(result));
                    break;

                default:
                    String error = Utils.formatResponse(conn.getResponseMessage(), conn.getResponseCode());
                    mEventBus.post(new ErrorEvent(error));
            }
        }
        catch (IOException e)
        {
            mEventBus.post(new ErrorEvent(e.getMessage()));
        }
        finally
        {
            if (conn != null)
            {
                conn.disconnect();
            }
        }
    }

    private HttpURLConnection getConnection(String urlString) throws IOException
    {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(READ_TIMEOUT);
        connection.setConnectTimeout(CONNECTION_TIMEOUT);
        connection.setRequestMethod(REQUEST_METHOD);
        connection.setDoInput(true);

        connection.connect();
        return connection;
    }

    private String getStringFromIS(InputStream stream) throws IOException
    {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;

        while ((length = stream.read(buffer)) != -1)
        {
            result.write(buffer, 0, length);
        }

        return result.toString("UTF-8");
    }
}
