package ru.tubi.project.utilites;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static ru.tubi.project.Config.ADMIN_PANEL_URL;

public class RetroClient {

    private static final String BASE_URL = ADMIN_PANEL_URL;
                                            //"http://h102582557.nichost.ru/api/";
    private static RetroClient myClient;
    private Retrofit retrofit;

    private RetroClient(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
    public static synchronized RetroClient getInstance(){

        if(myClient == null){
            myClient = new RetroClient();
        }

        return myClient;
    }
    public  Api getApi(){
        return  retrofit.create(Api.class);
    }
}
