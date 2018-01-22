package com.example.josh.youtube2mp3;

//probably should have just imported everything. There is nothing that this class does not use
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
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

public class URLTranslator{
    //url for test api call
    //https://www.googleapis.com/youtube/v3/playlistItems?playlistId=PLZIM5Rh7FjGALmP8_tJX0JeFeWvPQH0y_&maxResults=25&part=snippet%2CcontentDetails&key=AIzaSyA__jWOZux1quyKhcs2S4S8U9i1TXsBsrc

    //the api key
    final String key = "AIzaSyA__jWOZux1quyKhcs2S4S8U9i1TXsBsrc";
    //the application context. needed for getting storage locations
    private Context context;

    //download one song
    public  void downloadOne(String url, Context context) throws IOException, JSONException {
        //used for parsing variables out of a URL
        Uri uri = Uri.parse(url);
        //set the context
        this.context = context;
        //a default file name, changes later
        String title = "save";

        //if the video is in a playlist, the url will contain list= and we must remove the video from the playlist to convert it
        if(url.contains("list=")) {
            //get the index of the video in the playlist from the given URL
            int index = Integer.parseInt(uri.getQueryParameter("index"));
            //get the playlist id from the given url
            String playlistId = uri.getQueryParameter("list");
            //call the translateSingle method to get the song title and direct link
            ArrayList[] ids = translatePlaylist(playlistId, index);
            //the video id
            ArrayList videoIds = ids[0];
            //the video title
            ArrayList titles = ids[1];
            //download the call the download method and give it the video id as well as the title
            download(videoIds.get(0).toString(), title);

        //if the video is not in a playlist, the url appears as follows if it is not auto-shortened
        }else if(url.contains("https://www.youtube.com/watch?v=")){
            //get the video title
            title = translateSingle(uri.getQueryParameter("v"));
            //call the download method giving it the video id found after v=
            download(uri.getQueryParameter("v"), title);

            //else the video must be in the shortened single video youtu.be format
        }else{
            //get the video title
            title = translateSingle((url.substring(url.lastIndexOf("/")+1)));
            //if the URL is a youtu.be link, the video id is after the final /, pass this to the download method
            download((url.substring(url.lastIndexOf("/")+1)), title);
        }
    }

    public  void downloadAll(String url, Context context) throws IOException, JSONException {
        //used to parse into out of the URL
        Uri uri = Uri.parse(url);
        //the app context
        this.context = context;
        //get the playlist id from the URL
        String playlistID = uri.getQueryParameter("list");
        //turn the playlist id into titles and video id's
        ArrayList[] ids = translatePlaylist(playlistID, -1);
        //holds the video id's
        ArrayList videoIds = ids[0];
        //holds the video titles
        ArrayList titles = ids[1];

        for(int i = 0;i<videoIds.size();i++){
            //download each song in the playlist
            download(videoIds.get(i).toString(),titles.get(i).toString());
        }

    }

    //used for extracting video id's and titles from playlist url's
    private ArrayList[] translatePlaylist(String playlistID, int index) throws IOException, JSONException {
        //the url for the playlist api call
        URLConnection rawData = new URL("https://www.googleapis.com/youtube/v3/playlistItems?playlistId="+playlistID+"&maxResults=50&part=snippet%2CcontentDetails&key="+key).openConnection();
        //create a reader for the info returned by the api call
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(rawData.getInputStream()));
        //holds the data returned by teh api call
        String jsonString = "";
        //each line in the api call response
        String line;
        //read each single line of the api response into a complete string so it can be parsed into a JSON object
        while((line = bufferedReader.readLine())!= null){
            jsonString += line;
        }
        //make the json object from the string retrieved above
        JSONObject json = new JSONObject(jsonString);
        //get the JSON array named items - it contains the data for each video
        JSONArray videos = json.getJSONArray("items");
        //the following array lists hold the video info
        ArrayList videoIds = new ArrayList();
        ArrayList titles = new ArrayList();

        //if we are translating an entire playlist
        if (index == -1){
            //for every video in the jsonARRAY
            for(int i=0; i<videos.length();i++){
                //get the JSON object for the video
                JSONObject x = (JSONObject) videos.get(i);
                //get the video id
                videoIds.add(x.getJSONObject("contentDetails").get("videoId"));
                //get the title
                titles.add(x.getJSONObject("snippet").get("title"));
            }
            //else we are saving one video from the playlist
        }else{
            //get the JSON object for the video to download. remove one from the index as youtube indexes start at 1, not 0 like everything else.
            JSONObject x = (JSONObject)videos.get(index-1);
            //get the video id
            videoIds.add(x.getJSONObject("contentDetails").get("videoId"));
            //get the video title
            titles.add(x.getJSONObject("snippet").get("title"));
        }
        //return the video ids and titles
        return new ArrayList[]{videoIds, titles};

    }

    //used for extracting titles from non-playlist url's
    private String translateSingle(String videoId) throws IOException, JSONException {
        //url for single video api call
        URLConnection rawData = new URL("https://www.googleapis.com/youtube/v3/videos?id="+videoId+"&maxResults=50&part=snippet%2CcontentDetails&key="+key).openConnection();
        //create a reader for the data returned by the api
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(rawData.getInputStream()));
        //will store the entire json string as one line
        String jsonString = "";
        //each line of the JSON string as it is read in
        String line;
        //read in the json
        while((line = bufferedReader.readLine())!= null){
            jsonString += line;
        }
        //create the json object from the json string
        JSONObject json = new JSONObject(jsonString);
        //get the items array from the json object
        JSONArray videos = json.getJSONArray("items");
        //holds the video title
        String title = "";
        //get object 0 from the json array (there is only one object in the array but google sends and array anyway.
        JSONObject x = (JSONObject)videos.get(0);
        //get the title of the video
        title = x.getJSONObject("snippet").get("title").toString();
        //return the title of the video
        return title;
    }

    //downloads the videos using a youtube to mp3 api
    private void download(String videoID, String title) throws IOException, JSONException {
        //create the connection to the converter api
        URLConnection rawData = new URL("https://youtubetoany.com/@api/json/mp3/"+videoID).openConnection();
        //create a reader for the data returned by the api
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(rawData.getInputStream()));
        //the entire JSON string
        String jsonString = "";
        //each line returned by the API call
        String line;
        //read everything returned by the api into the complete JSON string
        while((line = bufferedReader.readLine())!= null){
            jsonString += line;
        }
        //the api does not return just JSON, rather it has some other crap at the end that must be removed via substring
        jsonString = jsonString.substring(0,jsonString.indexOf("}}}")+3);
        //create the json object off the json string
        JSONObject json = new JSONObject(jsonString);
        //create the link that can be used to download the mp3
        String downloadLink = "Http:"+ json.getJSONObject("vidInfo").getJSONObject("2").get("dloadUrl");

        //slow and no longer used
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
        //the connection used to download the mp3
        URL download = new URL(downloadLink);
        //used to get the downloaded mp3
        ReadableByteChannel rbc = Channels.newChannel(download.openStream());
        //create the file on the local device
        File mp3 = new File(String.valueOf(context.getExternalFilesDir(null)),title+".mp3");
        //save the download to the file and close the file
        FileOutputStream fos = new FileOutputStream(mp3);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();

    }

}
