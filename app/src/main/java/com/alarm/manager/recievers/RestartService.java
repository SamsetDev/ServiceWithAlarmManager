package com.alarm.manager.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alarm.manager.MyService;

import androidx.core.content.ContextCompat;

/**
 * Copyright (C) ServiceAlarmmanager - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 * Created by samset on 20/10/18 at 4:53 PM for ServiceAlarmmanager .
 */


public class RestartService extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intents) {

        Intent intent = new Intent(context, MyService.class);
        intent.putExtra("REQUEST_CODE", 1);
        ContextCompat.startForegroundService(context,intent);
        Log.e("TAG","service restart");

    }
}
