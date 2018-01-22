package com.example.josh.youtube2mp3;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //show the GUI
        setContentView(R.layout.activity_main);


    }
    //button to download only one song
    public void downloadOne(View v) {
        //select the text field
        EditText text = findViewById(R.id.URLBox);
        //get the text form the field
        String str = text.getText().toString();
        //new download object
        download dwnld = new download();
        //set download type to 1 (download only one song)
        dwnld.setType(1);
        //create an array of objects holding the url to download as well as the application context
        Object[] data = {str,getApplicationContext()};
        //run the async to download the mp3
        dwnld.execute(data);


    }
    //button to download the whole playlist
    public void downloadAll(View v){
        //select the text field
        EditText text = findViewById(R.id.URLBox);
        //get the text form the field
        String str = text.getText().toString();
        //new download object
        download dwnld = new download();
        //set the download type to 2 (entire playlist)
        dwnld.setType(2);
        //create an array of objects holding the url to download as well as the application context
        Object[] data = {str,getApplicationContext()};
        //run the async to download the mp3
        dwnld.execute(data);
    }



}
