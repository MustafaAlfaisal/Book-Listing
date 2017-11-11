package com.example.musta.booklisting;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mustafa on 11/09/17.
 */

public class BooksAdapter extends ArrayAdapter<Books> {

    private Context context;
    private int resources;
    private ArrayList<Books> booksArrayList;

    public BooksAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Books> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resources = resource;
        this.booksArrayList = objects;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resources, null);
        }


        Books book = booksArrayList.get(position);

        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView publisher = (TextView) convertView.findViewById(R.id.publisher);
        TextView authors = (TextView) convertView.findViewById(R.id.author);

        title.setText(String.valueOf(book.getTitle()));
        publisher.setText(String.valueOf(book.getPublisher()));
        authors.setText(String.valueOf(book.getAuthors()));


        return convertView;
    }
}
