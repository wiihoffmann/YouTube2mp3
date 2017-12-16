package com.example.josh.youtube2mp3;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }

    public void download(View v){
        EditText text = findViewById(R.id.URLBox);
        String str = text.getText().toString();
        Log.d("#######################", str);

    }



}
