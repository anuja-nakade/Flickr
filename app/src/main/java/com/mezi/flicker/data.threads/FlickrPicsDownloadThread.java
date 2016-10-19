package com.mezi.flicker.data.threads;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

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

    public FlickrPicsDownloadThread() {
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    private void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isThreadCompleted(int threadNo) {
        return status;
    }

    public FlickrPicsDownloadThread(int threadNo, String imageUrl, Handler handler) {
        this.threadNo = threadNo;
        this.handler = handler;
        this.imageUrl = imageUrl;
    }

    @Override
    public void run() {
        Log.i(TAG, "Starting Thread : " + threadNo);


        bitmap = getBitmap(imageUrl);
        setStatus(true);
        //if (threadNo < bitmaps.size() && bitmaps.get(threadNo) == null)
        bitmaps.add(bitmap);

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