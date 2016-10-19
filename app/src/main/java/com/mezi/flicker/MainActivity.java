package com.mezi.flicker;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mezi.flicker.data.model.FlickrPics;
import com.mezi.flicker.ui.adapter.FlickrPicsAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements TaskDelegate {

    private RecyclerView flickrPicsView;
    private FlickrPicsAdapter picsAdapter;
    ThreadPoolExecutor executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        flickrPicsView = (RecyclerView) findViewById(R.id.flickrRecyclerView);
        picsAdapter = new FlickrPicsAdapter(this, new ArrayList<String>());
        setSupportActionBar(toolbar);
        int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

        executor = new ThreadPoolExecutor(
                NUMBER_OF_CORES * 2,
                NUMBER_OF_CORES * 2,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>()
        );


        String stringUrl = "https://api.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=d98f34e2210534e37332a2bb0ab18887&format=json&extras=url_n";
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadPics(this, this).execute(stringUrl);
        } else {
            Snackbar.make(flickrPicsView, "No Network Connection Available", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        flickrPicsView.setLayoutManager(new LinearLayoutManager(this));
        flickrPicsView.setAdapter(picsAdapter);
        picsAdapter.notifyDataSetChanged();

    }


    public ThreadPoolExecutor getExecutor() {
        return executor;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void taskCompletionResult(ArrayList<String> result, List<FlickrPics> flickrPics) {
        picsAdapter.setUrls(result);
        picsAdapter.setFlickrData(flickrPics);
        picsAdapter.notifyDataSetChanged();
    }
}
