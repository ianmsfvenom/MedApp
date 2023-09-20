package com.medapp.network;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClientDatabase {
    private static Retrofit retrofit;
    private static String BASE_URL = "http://192.168.1.6:3000/"; //Ipv4 local do servidor express (obs: abra a porta 3000 no firewall)

    public static Retrofit getRetrofitClient() {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
