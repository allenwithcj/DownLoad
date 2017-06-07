package com.example.lwl.download;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.lwl.download.service.DownloadService;

public class MainActivity extends AppCompatActivity {
    public static final String URL ="https://github.com/allenwithcj/DownLoad/blob/master/raw/apk/app-debug.apk";
    private Button down_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        down_btn = (Button)findViewById(R.id.down_btn);
        down_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DownloadService.class);
                intent.putExtra("url",URL);
                startService(intent);
            }
        });
    }
}
