package com.example.josh.youtube2mp3;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by Josh on 2017-12-16.
 */

class download extends AsyncTask<Object, Integer, Void> {

    //type =1 is single download, type=2 is download all
    private int type;

    //used to set the download type (1= download one song) (2= download entire playlist)
    public void setType(int i){
        type = i;
    }

    protected Void doInBackground(Object... urls) {
        //create a url translator for downloading and retrieving videos
        URLTranslator URLT = new URLTranslator();

        switch(type){
            //single song download
            case 1:
                try {
                    //translate the urls and download the song
                    URLT.downloadOne((String)urls[0],(Context)urls[1]);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            //whole playlist download
            case 2:
                try {
                    //translate the urls and download the whole playlist
                    URLT.downloadAll((String)urls[0],(Context)urls[1]);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }

        return null;
    }
    //what to do when the async task finishes
    protected void onPostExecute(Void Result) {
        //log that the task finished successfully
        Log.d("FINISH", "ASYNC FINISHED" );

    }
}