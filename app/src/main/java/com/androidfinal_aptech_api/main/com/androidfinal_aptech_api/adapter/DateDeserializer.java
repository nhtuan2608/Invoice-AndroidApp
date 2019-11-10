package com.androidfinal_aptech_api.main.com.androidfinal_aptech_api.adapter;

import android.annotation.SuppressLint;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

@SuppressLint("SimpleDateFormat")
public class DateDeserializer implements JsonDeserializer<DateContainer>{

public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    SimpleDateFormat formatter;

    public DateDeserializer() {
            formatter = new SimpleDateFormat(DATE_FORMAT);
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    }


    @Override
    public DateContainer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return new DateContainer(formatter.parse(json.getAsString()));
        }
        catch (ParseException e) {
            System.out.println(e.getMessage());
            throw new JsonParseException(e);
        }
    }
}