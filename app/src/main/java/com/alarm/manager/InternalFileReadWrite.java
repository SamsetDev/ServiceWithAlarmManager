package com.alarm.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Copyright (C) ServiceAlarmmanager - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 * Created by samset on 20/10/18 at 1:55 PM for ServiceAlarmmanager .
 */


public class InternalFileReadWrite {
    private Context context;
    private String FILE_NAME = "logfile.txt";
    private StringBuffer stringBuffer;

    InternalFileReadWrite(Context context) {
        this.context = context;

    }

    void clearFile() {
        context.deleteFile(FILE_NAME);
        // StringBuffer clear
        stringBuffer.setLength(0);
    }



    @SuppressLint("NewApi")
   public void writeFile() {

        stringBuffer = new StringBuffer();

        long currentTime = System.currentTimeMillis();
        SimpleDateFormat dataFormat = new SimpleDateFormat("hh:mm:ss a", Locale.US);
        String cTime = dataFormat.format(currentTime);
        Log.d("TAG", "write current time "+cTime);

        stringBuffer.append(" Log Time ::  "+cTime);
        stringBuffer.append(System.getProperty("line.separator"));

        // try-with-resources
        try (FileOutputStream fileOutputstream = context.openFileOutput(FILE_NAME, Context.MODE_APPEND)) {

            fileOutputstream.write(stringBuffer.toString().getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
   public String readFile() {

        stringBuffer = new StringBuffer();

        // try-with-resources
        try (FileInputStream fileInputStream = context.openFileInput(FILE_NAME);
             BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream, "UTF-8"))
        ) {

            String lineBuffer;

            while ((lineBuffer = reader.readLine()) != null) {
                stringBuffer.append(lineBuffer);
                stringBuffer.append(System.getProperty("line.separator"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("TAG", "read current file "+stringBuffer.toString());
        return stringBuffer.toString();
    }

}
