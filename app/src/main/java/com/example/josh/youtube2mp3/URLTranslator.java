package com.example.josh.youtube2mp3;



import android.util.JsonReader;

import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class URLTranslator{

    //https://www.googleapis.com/youtube/v3/playlistItems?playlistId="+playlistID+"&maxResults=25&part=snippet%2CcontentDetails&key="+AIzaSyA__jWOZux1quyKhcs2S4S8U9i1TXsBsrc

    final String key = "AIzaSyA__jWOZux1quyKhcs2S4S8U9i1TXsBsrc";
    final String playlistID = "PLZIM5Rh7FjGALmP8_tJX0JeFeWvPQH0y_";
    public  void downloadOne(String url) throws IOException, JSONException {
        translate(url);

    }

    public  void downloadAll(String url){

    }

    private  void translate(String url) throws IOException, JSONException {
        URLConnection rawData = new URL("https://www.googleapis.com/youtube/v3/playlistItems?playlistId="+playlistID+"&maxResults=25&part=snippet%2CcontentDetails&key="+key).openConnection();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(rawData.getInputStream()));
        String jsonString = "";
        String line;
        while((line = bufferedReader.readLine())!= null){
            jsonString += line;
        }

        JSONObject json = new JSONObject(jsonString);




        System.out.println(json.toString());

    }



}











