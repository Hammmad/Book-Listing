package com.example.shekhchilli.booklisting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by shekh chilli on 10/17/2016.
 */
public class ListAdapter extends ArrayAdapter<BookInfo> {
    public ListAdapter(Context context, ArrayList<BookInfo> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        BookInfo currentbookInfo = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.booktitle_textview);
        title.setText(currentbookInfo.getTitle());

        TextView author = (TextView) convertView.findViewById(R.id.bookauthor_textview);
        author.setText(currentbookInfo.getAuthor());

        TextView publisher = (TextView) convertView.findViewById(R.id.publisher_textview);
        publisher.setText(currentbookInfo.getPublisher());

        TextView releaseDate = (TextView) convertView.findViewById(R.id.releasedate_textview);
        releaseDate.setText(currentbookInfo.getReleaseDate());

        ImageView image = (ImageView) convertView.findViewById(R.id.bookcover_imageview);
        image.setImageBitmap(currentbookInfo.getImage());


        return convertView;


    }
}
