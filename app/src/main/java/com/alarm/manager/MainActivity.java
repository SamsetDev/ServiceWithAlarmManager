package com.alarm.manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private AppCompatTextView textView;
    private AppCompatButton buttonStart,buttonLog,buttonStop;
    private InternalFileReadWrite fileReadWrite;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();

        fileReadWrite = new InternalFileReadWrite(context);

        textView = findViewById(R.id.log_text);
        buttonStart = findViewById(R.id.button_start);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), MyService.class);
                intent.putExtra("REQUEST_CODE", 1);
                ContextCompat.startForegroundService(MainActivity.this,intent);

            }
        });

        buttonLog = findViewById(R.id.button_log);
        buttonLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(fileReadWrite.readFile());
            }
        });


        buttonStop = findViewById(R.id.button_reset);
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), MyService.class);
                stopService(intent);

                fileReadWrite.clearFile();
                textView.setText("");
            }
        });
    }
}