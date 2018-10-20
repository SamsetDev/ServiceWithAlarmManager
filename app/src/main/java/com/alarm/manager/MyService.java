package com.alarm.manager;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.alarm.manager.recievers.RestartService;

import androidx.core.app.NotificationCompat;

/**
 * Copyright (C) ServiceAlarmmanager - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 * Created by samset on 20/10/18 at 1:51 PM for ServiceAlarmmanager .
 */


public class MyService extends Service {

    private Context context;
    InternalFileReadWrite fileReadWrite;




    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent intent = new Intent(getApplicationContext(), MyService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 1005, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, SystemClock.elapsedRealtime() + 5000, pendingIntent);
        Log.e("TAG", " service is call onTaskRemoved  ");
        super.onTaskRemoved(rootIntent);

    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("TAG", " service is created ");
        context = getApplicationContext();
         fileReadWrite = new InternalFileReadWrite(context);
    }

    @SuppressLint("NewApi")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e("TAG", " service onStartCommand is called ");

        fileReadWrite.writeFile();

        int requestCode = intent.getIntExtra("REQUEST_CODE", 0);

        String channelId = "default";
        String title = context.getString(R.string.app_name);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Foreground Notification Context
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);

        Notification notification = notificationBuilder
                .setContentTitle(title)
                .setSmallIcon(android.R.drawable.btn_star)
                .setContentText("Alarm Counter")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .build();

        // Notification　Channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, title, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Silent Notification");
            channel.setSound(null, null);
            channel.enableLights(false);
            channel.setLightColor(Color.BLUE);
            channel.enableVibration(false);
            notificationManager.createNotificationChannel(channel);

        }
        startForeground(1, notification);

        // set Alarm
        setNextAlarmService(context);

        //return START_NOT_STICKY;
        return START_STICKY;
        //return START_REDELIVER_INTENT;
    }


    @SuppressLint("NewApi")
    private void setNextAlarmService(Context context) {

        // 15 min
        long repeatPeriod = 15 * 60 * 1000;

        Intent intent = new Intent(context, MyService.class);
        long startMillis = System.currentTimeMillis() + repeatPeriod;

        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);


        if (Build.VERSION.SDK_INT >= 23) {
            Log.e("TAG", "alarm set version O ");
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, startMillis, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= 19) {
            Log.e("TAG", "alarm set version setExact method ");
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, startMillis, pendingIntent);
        } else {
            Log.e("TAG", "alarm set version set method ");
            alarmManager.set(AlarmManager.RTC_WAKEUP, startMillis, pendingIntent);
        }

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        RestartService restartService = new RestartService();
        IntentFilter intentFilter = new IntentFilter("in.alarm.RestartSensor");
        context.registerReceiver(restartService,intentFilter);

        Log.e("TAG", " service is destroy");
    }

    public void stopServiceWithAlarm(){
        stopAlarmService();
        stopSelf();
    }

    private void stopAlarmService() {
        Intent indent = new Intent(context, MyService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, indent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}