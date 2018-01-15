package com.example.josh.youtube2mp3;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
public class URLTranslator{

    //https://www.googleapis.com/youtube/v3/playlistItems?playlistId=PLZIM5Rh7FjGALmP8_tJX0JeFeWvPQH0y_&maxResults=25&part=snippet%2CcontentDetails&key=AIzaSyA__jWOZux1quyKhcs2S4S8U9i1TXsBsrc

    final String key = "AIzaSyA__jWOZux1quyKhcs2S4S8U9i1TXsBsrc";
    private Context context;

    public  void downloadOne(String url, Context context) throws IOException, JSONException {
        Uri uri = Uri.parse(url);
        this.context = context;
        String title = "save";

        System.out.println(url);


        if(url.contains("list=")) {
            int index = Integer.parseInt(uri.getQueryParameter("index"));
            String playlistId = uri.getQueryParameter("list");
            ArrayList[] ids = translate(playlistId, index);
            ArrayList videoIds = ids[0];
            ArrayList titles = ids[1];
            download(videoIds.get(0).toString(), title);

        }else if(url.contains("https://www.youtube.com/watch?v=")){
            download(uri.getQueryParameter("v"), title);
        }else{
            download((url.substring(url.lastIndexOf("/")+1)), title);
        }
    }

    public  void downloadAll(String url, Context context) throws IOException, JSONException {
        Uri uri = Uri.parse(url);
        this.context = context;
        String playlistID = uri.getQueryParameter("list");

        ArrayList[] ids = translate(playlistID, -1);
        ArrayList videoIds = ids[0];
        ArrayList titles = ids[1];

        for(int i = 0;i<videoIds.size();i++){

            download(videoIds.get(i).toString(),titles.get(i).toString());
        }

    }

    private ArrayList[] translate(String playlistID, int index) throws IOException, JSONException {
        URLConnection rawData = new URL("https://www.googleapis.com/youtube/v3/playlistItems?playlistId="+playlistID+"&maxResults=50&part=snippet%2CcontentDetails&key="+key).openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(rawData.getInputStream()));
        String jsonString = "";
        String line;
        while((line = bufferedReader.readLine())!= null){
            jsonString += line;
        }

        JSONObject json = new JSONObject(jsonString);

        JSONArray videos = json.getJSONArray("items");

        ArrayList videoIds = new ArrayList();
        ArrayList titles = new ArrayList();

        if (index == -1){
            for(int i=0; i<videos.length();i++){
                JSONObject x = (JSONObject) videos.get(i);
                videoIds.add(x.getJSONObject("contentDetails").get("videoId"));
                titles.add(x.getJSONObject("snippet").get("title"));
            }
        }else{
            JSONObject x = (JSONObject)videos.get(index-1);
            videoIds.add(x.getJSONObject("contentDetails").get("videoId"));
        }
        return new ArrayList[]{videoIds, titles};

    }

    private void download(String videoID, String title) throws IOException, JSONException {
        URLConnection rawData = new URL("https://youtubetoany.com/@api/json/mp3/"+videoID).openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(rawData.getInputStream()));
        String jsonString = "";
        String line;
        while((line = bufferedReader.readLine())!= null){
            jsonString += line;
        }
        jsonString = jsonString.substring(0,jsonString.indexOf("}}}")+3);
        JSONObject json = new JSONObject(jsonString);


        String downloadLink = "Http:"+ json.getJSONObject("vidInfo").getJSONObject("2").get("dloadUrl");

        /*
        URL download = new URL(downloadLink);
        InputStream in = download.openStream();
        File mp3 = new File(String.valueOf(context.getExternalFilesDir(null)  ), "save.mp3");
        FileOutputStream fos = new FileOutputStream(mp3);


        int i;
        byte[] b = new byte[4096];
        while ((i = in.read(b)) != -1) {
            fos.write(b, 0, i);
        }
*/

        URL download = new URL(downloadLink);
        ReadableByteChannel rbc = Channels.newChannel(download.openStream());
        File mp3 = new File(String.valueOf(context.getExternalFilesDir(null)),title+".mp3");
        FileOutputStream fos = new FileOutputStream(mp3);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();

    }


    public boolean isSDCardPresent() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }
}
