package com.example.josh.youtube2mp3;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Josh on 2017-12-16.
 */

class download extends AsyncTask<String, Integer, Void> {

    //type =1 is single download, type=2 is download all
    private int type;

    public void setType(int i){
        type = i;
    }

    protected Void doInBackground(String... urls) {
        URLTranslator URLT = new URLTranslator();

        switch(type){
            case 1:
                URLT.downloadOne(urls[0]);
                break;
            case 2:
                URLT.downloadAll(urls[0]);
                break;

        }

        return null;
    }

    protected void onProgressUpdate(Integer... progress) {


    }

    protected void onPostExecute(Integer result) {
        Log.d("#######################", "asdasdasdasd" );

    }
}