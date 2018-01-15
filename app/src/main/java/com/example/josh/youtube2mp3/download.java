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

    public void setType(int i){
        type = i;
    }

    protected Void doInBackground(Object... urls) {
        URLTranslator URLT = new URLTranslator();

        switch(type){
            case 1:
                try {
                    URLT.downloadOne((String)urls[0],(Context)urls[1]);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    URLT.downloadAll((String)urls[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }

        return null;
    }

    protected void onProgressUpdate(Integer... progress) {


    }


    protected void onPostExecute(Void Result) {
        Log.d("#######################", "asdasdasdasd" );

    }
}