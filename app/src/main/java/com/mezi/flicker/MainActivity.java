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

import com.mezi.flicker.data.model.FlickrPics;
import com.mezi.flicker.data.threads.FlickrPicsDownloadThread;
import com.mezi.flicker.ui.adapter.FlickrPicsAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements TaskDelegate, Handler.Callback {


    private RecyclerView flickrPicsView;
    private FlickrPicsAdapter picsAdapter;
    ThreadPoolExecutor executor;
    public static MainActivity sInstance;
    private static int NUMBER_OF_CORES =
            Runtime.getRuntime().availableProcessors();

    final BlockingQueue<Runnable> mDecodeWorkQueue;

    static {
        // Creates a single static instance of PhotoManager
        sInstance = new MainActivity();
    }

    public MainActivity() {
        mDecodeWorkQueue = new LinkedBlockingQueue<Runnable>();

        final int KEEP_ALIVE_TIME = 1;
        final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;


        Log.d("No of cores ", NUMBER_OF_CORES + " ");
        executor = new ThreadPoolExecutor(
                1,       // Initial pool size
                1,       // Max pool size
                KEEP_ALIVE_TIME,
                KEEP_ALIVE_TIME_UNIT,
                mDecodeWorkQueue);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        flickrPicsView = (RecyclerView) findViewById(R.id.flickrRecyclerView);
        picsAdapter = new FlickrPicsAdapter(this, new ArrayList<String>());
        setSupportActionBar(toolbar);

        String stringUrl = "https://api.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=d98f34e2210534e37332a2bb0ab18887&format=json&extras=url_n";
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())

        {
            new DownloadPics(this, this).execute(stringUrl);
        } else

        {
            Snackbar.make(flickrPicsView, "No Network Connection Available", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        flickrPicsView.setLayoutManager(new LinearLayoutManager(this)

        );
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

        for (int i = 0; i < result.size(); i++) {
            getExecutor().execute(new FlickrPicsDownloadThread(i, result.get(i), new Handler(), this));
        }
        picsAdapter.setUrls(result);
        picsAdapter.setFlickrData(flickrPics);
        picsAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean handleMessage(Message message) {
        return false;
    }

    public void handleFinishedState(int threadFinished) {
        if (threadFinished == 1) {
            picsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        cancelAll();

        try {

            executor.shutdown();
            executor.awaitTermination(1000, TimeUnit.SECONDS);
            cancelAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void cancelAll() {
        /*
         * Creates an array of Runnables that's the same size as the
         * thread pool work queue
         */
        Runnable[] runnableArray = new Runnable[mDecodeWorkQueue.size()];

        // Populates the array with the Runnables in the queue
        mDecodeWorkQueue.toArray(runnableArray);
        // Stores the array length in order to iterate over the array
        int len = runnableArray.length;
        /*
         * Iterates over the array of Runnables and interrupts each one's Thread.
         */
        synchronized (sInstance) {
            // Iterates over the array of tasks
            for (int runnableIndex = 0; runnableIndex < len; runnableIndex++) {
                // Gets the current thread
                Thread thread = new Thread(runnableArray[runnableIndex]);
                // if the Thread exists, post an interrupt to it
                if (null != thread) {
                    thread.interrupt();
                }
            }
        }
    }

}
