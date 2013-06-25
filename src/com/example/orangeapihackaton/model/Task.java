package com.example.orangeapihackaton.model;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Tomek
 * Date: 6/11/13
 * Time: 11:27 AM
 * To change this template use File | Settings | File Templates.
 */
//object representing to do task submitted by user
public class Task {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDateOfRecord() {
        return dateOfRecord;
    }

    public void setDateOfRecord(Date dateOfRecord) {
        this.dateOfRecord = dateOfRecord;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private Date dateOfRecord;
    private Integer id;

    @Override
    public String toString() {
        return "id: " + id + ", text: " + text + ", date : " + dateOfRecord;
    }
}
