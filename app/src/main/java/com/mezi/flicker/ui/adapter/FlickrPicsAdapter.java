package com.mezi.flicker.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mezi.flicker.R;
import com.mezi.flicker.data.model.FlickrPics;
import com.mezi.flicker.data.threads.FlickrPicsDownloadThread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Anuja on 10/18/16.
 */
public class FlickrPicsAdapter extends RecyclerView.Adapter<FlickrPicsAdapter.ItemHolder> {

    static int threadCompleted = 4;
    int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private Context context;
    private int flag = 1;
    private static ArrayList<String> urls;
    private double imageHeight;
    private double imageWidth;
    private ArrayList<Bitmap> bitmaps = new ArrayList<>();
    final ThreadPoolExecutor executor = new ThreadPoolExecutor(
            NUMBER_OF_CORES,
            NUMBER_OF_CORES,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>()
    );
    private List<FlickrPics> flickrData;


    public FlickrPicsAdapter() {

    }

    public FlickrPicsAdapter(Context context, ArrayList<String> urls) {
        this.context = context;
        this.urls = urls;
    }

    public void setUrls(ArrayList<String> urls) {
        this.urls = urls;
    }

    @Override
    public FlickrPicsAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flikr_pics_view, parent, false);

        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(FlickrPicsAdapter.ItemHolder holder, int position) {

        if (position >= FlickrPicsDownloadThread.bitmaps.size() || FlickrPicsDownloadThread.bitmaps.get(position) == null) {
            holder.imgView.setImageBitmap(null);
        } else {
            holder.imgView.setImageBitmap(FlickrPicsDownloadThread.bitmaps.get(position));
        }

        if (flickrData.get(0).getPhotos().getPhoto().get(position).getTitle() != null)
            holder.imgTitle.setText(flickrData.get(0).getPhotos().getPhoto().get(position).getTitle());
        else {
            holder.imgTitle.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

    public void setFlickrData(List<FlickrPics> flickrData) {
        this.flickrData = flickrData;
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {
        ImageView imgView;
        TextView imgTitle;
        FrameLayout contentLayout, progressBar;

        public ItemHolder(View itemView) {
            super(itemView);
            imgView = (ImageView) itemView.findViewById(R.id.flickrImageView);
            imgTitle = (TextView) itemView.findViewById(R.id.title);
            contentLayout = (FrameLayout) itemView.findViewById(R.id.contentLayout);
            progressBar = (FrameLayout) itemView.findViewById(R.id.progress_bar_parent);
        }
    }
}
