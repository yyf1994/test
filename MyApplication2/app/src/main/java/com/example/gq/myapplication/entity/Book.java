package com.example.gq.myapplication.entity;

/**
 * Created by gq on 2016/1/22.
 */
public class Book {

    private String bookname;

    public Book(String bookname) {
        this.bookname = bookname;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }
}
