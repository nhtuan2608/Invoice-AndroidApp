package com.androidfinal_aptech_api.main.com.androidfinal_aptech_api.services;

import com.androidfinal_aptech_api.main.com.androidfinal_aptech_api.adapter.DateContainer;
import com.androidfinal_aptech_api.main.com.androidfinal_aptech_api.adapter.DateDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static Retrofit retrofit = null;
    public static Retrofit getClient() {
        Gson gsonBuilder = new GsonBuilder().registerTypeAdapter(DateContainer.class, new DateDeserializer()).create();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        retrofit = new Retrofit.Builder().baseUrl("http://192.168.100.7:9596/api/")
                .addConverterFactory(GsonConverterFactory.create()).client(client).build();
//                .addConverterFactory(GsonConverterFactory.create(gsonBuilder)).client(client).build();
        return retrofit;

    }

}