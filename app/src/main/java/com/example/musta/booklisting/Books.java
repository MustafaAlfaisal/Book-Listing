package com.example.musta.booklisting;

import java.util.ArrayList;

/**
 * Created by mustafa on 11/09/17.
 */

public class Books {


    private String title;
    private String publisher;
    private ArrayList<String> authors;


    public Books(String title, String publisher, ArrayList<String> authors) {
        this.title = title;
        this.publisher = publisher;
        this.authors = authors;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }
}
