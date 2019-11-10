package com.androidfinal_aptech_api.main.com.androidfinal_aptech_api.adapter;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

public class DateContainer implements Serializable {

    public Date date;

    public DateContainer(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return date.toString() ;
    }
}
