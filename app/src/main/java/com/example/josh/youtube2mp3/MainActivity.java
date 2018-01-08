package com.example.josh.youtube2mp3;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void downloadOne(View v) {
        //select the text field
        EditText text = findViewById(R.id.URLBox);
        //get the text form the field
        String str = text.getText().toString();

        download dwnld = new download();
        dwnld.setType(1);

        dwnld.execute(str);



    }

    public void downloadAll(View v){
        //select the text field
        EditText text = findViewById(R.id.URLBox);
        //get the text form the field
        String str = text.getText().toString();

        download dwnld = new download();
        dwnld.setType(2);

        dwnld.execute(str);

    }



}
