package com.mezi.flicker.data.threads;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mezi.flicker.DownloadPics;
import com.mezi.flicker.MainActivity;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Anuja on 10/18/16.
 */
public class FlickrPicsDownloadThread implements Runnable {

    int threadNo;
    boolean status = false;
    Handler handler;
    String imageUrl;
    public static final String TAG = "LongThread";
    public static ArrayList<Bitmap> bitmaps = new ArrayList<>();
    private Bitmap bitmap;
    private FlickrPicsDownloadThread flickrPicsDownloadThread;
    private Thread currentThread;
    public final static int THREAD_FINISHED = 1;
    private DownloadPics downloadPics;
    private MainActivity mainActivity;


    public FlickrPicsDownloadThread(int threadNo, String imageUrl, Handler handler, MainActivity mainActivity) {
        this.threadNo = threadNo;
        this.handler = handler;
        this.imageUrl = imageUrl;
        this.mainActivity = mainActivity;
    }


    private void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        Log.i(TAG, "Starting Thread : " + threadNo);
        bitmap = getBitmap(imageUrl);
        setStatus(true);
        bitmaps.add(bitmap);
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivity.handleFinishedState(THREAD_FINISHED);


            }
        });

        //handler.setImage(bitmap);
        sendMessage(threadNo, "Thread Completed");

        Log.i(TAG, "Thread Completed " + threadNo);
    }


    public void sendMessage(int what, String msg) {
        Message message = handler.obtainMessage(what, msg);
        message.sendToTarget();
    }

    private Bitmap getBitmap(String url) {
        Bitmap bitmap = null;
        try {
            // Download Image from URL
            InputStream input = new java.net.URL(url).openStream();
            // Decode Bitmap
            bitmap = BitmapFactory.decodeStream(input);
            // Do extra processing with the bitmap
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}