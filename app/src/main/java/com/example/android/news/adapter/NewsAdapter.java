package com.example.android.news.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.news.R;
import com.example.android.news.model.NewsItem;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<NewsItem>{
    public NewsAdapter(@NonNull Context context, @NonNull List<NewsItem> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;

        // Check if an existing view is being reused, otherwise inflate the view
        if (itemView == null)
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_news, parent, false);
        //false here because we don't want to attach the list item view to the parent list view just yet.

        NewsItem currentNewsItem = getItem(position);

        TextView titleTextView = itemView.findViewById(R.id.textview_main_title);
        titleTextView.setText(currentNewsItem.getTitle());

        TextView sectionTextView = itemView.findViewById(R.id.textview_main_section);
        sectionTextView.setText(currentNewsItem.getSection());

        TextView authorTextView = itemView.findViewById(R.id.textview_main_author);
        authorTextView.setText(currentNewsItem.getAuthor());

        TextView dateTextView = itemView.findViewById(R.id.textview_main_date);
        String date = getDateOnly(currentNewsItem.getDate());
        dateTextView.setText(date);

        return itemView;
    }

    private String getDateOnly(String date) {
       String[] parts = date.split("T");
       return parts[0];
    }
}
