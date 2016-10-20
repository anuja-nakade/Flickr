package com.mezi.flicker;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mezi.flicker.data.model.FlickrPhotoInfo;
import com.mezi.flicker.data.model.FlickrPics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Anuja on 10/17/16.
 */
public class DownloadPics extends AsyncTask<String, Void, String> {


    ArrayList<String> urlList = new ArrayList<>();
    Context context;
    private TaskDelegate delegate;


    public DownloadPics(MainActivity mainActivity, TaskDelegate taskDelegate) {
        this.context = mainActivity;
        this.delegate = taskDelegate;
    }


    @Override
    protected String doInBackground(String... urls) {

        try {
            return downloadUrl(urls[0]);
        } catch (IOException e) {
            return "Unable to retrieve web page. URL may be invalid.";
        }
    }


    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        Gson gson = gsonBuilder.create();
        List<FlickrPics> posts = new ArrayList<FlickrPics>();
        posts = Arrays.asList(gson.fromJson(result, FlickrPics.class));
        ArrayList<FlickrPhotoInfo> photoInfo = new ArrayList<>();
        photoInfo = posts.get(0).getPhotos().getPhoto();

        for (int i = 0; i < photoInfo.size(); i++) {
            if (null != photoInfo.get(i).getUrl()) {
                urlList.add(photoInfo.get(i).getUrl());
                Log.d("url is ", photoInfo.get(i).getUrl() + " ");

            } else
                urlList.add("");

        }
        delegate.taskCompletionResult(urlList, posts);

    }


    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        try {
            URL url = new URL(myurl);
            String firstLine = "";
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("Tag", "The response is: " + response);
            //is = conn.getInputStream();
            if (response == 200) {

                is = conn.getInputStream();


                try {
                    //Read the server response and attempt to parse it as JSON
                    Reader readers = new InputStreamReader(is);
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(is));
                    firstLine = reader.readLine();
                    firstLine = firstLine.substring(firstLine.indexOf("{"), firstLine.length() - 1);
                    is.close();

                } catch (Exception ex) {
                    Log.e("TAG", "Failed to parse JSON due to: " + ex);
                }
            } else {
                Log.e("TAG", "Server responded with status code: " + conn.getResponseMessage());
            }
            return firstLine;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

}
