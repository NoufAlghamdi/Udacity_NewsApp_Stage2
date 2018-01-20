package com.example.android.news.api;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.example.android.news.model.NewsItem;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<NewsItem>> {
    /**
     * Tag for log messages.
     */
    private static final String LOG_TAG = NewsLoader.class.getName();
    /**
     * Query URL.
     */
    private String url;

    public NewsLoader(Context context, String url) {
        super(context);
        Log.e(LOG_TAG, "NewsLoader()");
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.e(LOG_TAG, "onStartLoading()");
    }

    @Override
    public List<NewsItem> loadInBackground() {
        // Don't perform the request if the first URL is null.
        if (url == null) {
            return null;
        }
        Log.e(LOG_TAG, "loadInBackground()");
        return QueryUtils.fetchNewsStoriesData(url);
    }
}
