package com.postnov.android.yotatestapp.pagedownloader;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.postnov.android.yotatestapp.YotaTestApp;
import com.postnov.android.yotatestapp.bus.events.ErrorEvent;
import com.postnov.android.yotatestapp.bus.RxBus;
import com.postnov.android.yotatestapp.bus.events.SuccessEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by postnov on 23.02.2016.
 */
public class PDService extends IntentService
{
    public static final String TAG = "PDService";
    public static final String EXTRA_URL = "com.postnov.android.URL";

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

        try (InputStream stream = getIS(url))
        {
            String result = getStringFromIS(stream);
            mEventBus.post(new SuccessEvent(result));
        }
        catch (IOException e)
        {
            Log.e(TAG, e.getMessage());
            mEventBus.post(new ErrorEvent(e));
        }
    }

    private InputStream getIS(String urlString) throws IOException
    {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);

        conn.connect();
        return conn.getInputStream();
    }

    private String getStringFromIS(InputStream stream) throws IOException
    {
        Reader reader = new InputStreamReader(stream, "UTF-8");
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(reader);
        String read;

        while ((read = br.readLine()) != null)
        {
            sb.append(read);
        }

        br.close();
        return sb.toString();
    }
}
