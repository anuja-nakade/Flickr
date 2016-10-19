package com.mezi.flicker;

import com.mezi.flicker.data.model.FlickrPics;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anuja on 10/18/16.
 */
public interface TaskDelegate {
    public void taskCompletionResult(ArrayList<String> result , List<FlickrPics> flickrPics);
}
