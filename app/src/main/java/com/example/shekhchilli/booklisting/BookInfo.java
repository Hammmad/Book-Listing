package com.example.shekhchilli.booklisting;

import android.graphics.Bitmap;

/**
 * Created by shekh chilli on 10/17/2016.
 */
public class BookInfo {

    private String mTiltle;
    private String mAuthor;
    private String mPublisher;
    private String mReleaseDate;
    private Bitmap mImage;

    public BookInfo(Bitmap image, String Author, String Publisher, String ReleaseDate, String Title) {
        this.mImage = image;
        this.mAuthor = Author;
        this.mPublisher = Publisher;
        this.mReleaseDate = ReleaseDate;
        this.mTiltle = Title;
    }


    public String getAuthor() {
        return mAuthor;
    }

    public Bitmap getImage() {
        return mImage;
    }

    public String getPublisher() {
        return mPublisher;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getTitle() {
        return mTiltle;
    }
}
