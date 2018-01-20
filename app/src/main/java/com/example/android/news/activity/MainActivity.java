package com.example.android.news.activity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.news.R;
import com.example.android.news.adapter.NewsAdapter;
import com.example.android.news.api.NewsLoader;
import com.example.android.news.model.NewsItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsItem>> {

    /**
     * URL for news items data from the Guardian API.
     */
    private static final String GUARDIAN_REQUEST_URL = "http://content.guardianapis.com/search?q=app&order-by=newest&show-references=author&show-tags=contributor&api-key=test";
    /**
     * Constant value for the news loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int NEWS_LOADER_ID = 1;
    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = MainActivity.class.getName();
    /**
     * Adapter for the list of news stories.
     */
    private NewsAdapter newsAdapter;
    /**
     * TextView that is displayed when the list is empty.
     */
    private TextView emptyStateTextView;
    /**
     * Reference to the ProgressBar in activity_main that is displayed during the loading
     * process of the news stories.
     */
    private ProgressBar loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e(LOG_TAG, "onCreate()");

        // Find a reference to the {@link ListView} in the layout.
        ListView newsListView = findViewById(R.id.list);

        // Reference to the TextView in main_activity layout, that is used to display text in the
        //empty state of the list.
        emptyStateTextView = findViewById(R.id.empty_view);
        //Set to the newsListView.
        newsListView.setEmptyView(emptyStateTextView);

        //Reference to the ProgressBar in activity_main layout.
        loadingIndicator = findViewById(R.id.loading_indicator);

        // Create a new adapter that takes an empty list of NewsItem as input.
        newsAdapter = new NewsAdapter(MainActivity.this, new ArrayList<NewsItem>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface.
        newsListView.setAdapter(newsAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected news item.
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Find the current news item that was clicked on.
                NewsItem currentNewsItem = (NewsItem) parent.getItemAtPosition(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor).
                Uri uri = Uri.parse(currentNewsItem.getWebUrl());

                // Create a new intent to view the news item URI.
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                // Send the intent to launch a new activity.
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            // Get a reference to the LoaderManager, in order to interact with loaders.
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            getLoaderManager().initLoader(NEWS_LOADER_ID, null, this);

        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible.
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message.
            emptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<NewsItem>> onCreateLoader(int id, Bundle args) {
        Log.e(LOG_TAG, "onCreateLoader()");

        // Create a new loader for the given URL.
        NewsLoader newsLoader = new NewsLoader(this, GUARDIAN_REQUEST_URL);
        return newsLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, List<NewsItem> data) {
        // Hide loading indicator because the data has been loaded.
        loadingIndicator.setVisibility(View.GONE);

        // Clear the adapter of previous news stories data.
        newsAdapter.clear();

        // If there is a valid list of {@link NewsItem}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            newsAdapter.addAll(data);
            Log.e(LOG_TAG, "onLoadFinished()");
        } else {
            // Set empty state text to display "No news stories found."
            emptyStateTextView.setText(R.string.no_news_stories_found);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsItem>> loader) {
        // Loader reset, so we can clear out our existing data.
        newsAdapter.clear();
        Log.e(LOG_TAG, "onLoaderReset()");
    }
}
