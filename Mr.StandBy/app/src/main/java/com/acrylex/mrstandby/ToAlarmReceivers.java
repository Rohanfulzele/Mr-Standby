package com.acrylex.mrstandby;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v4.content.WakefulBroadcastReceiver;

public class ToAlarmReceivers extends WakefulBroadcastReceiver {
    public static final String TABLE_NAME = "Schedule_table";
    DataBaseHelper mydb;
    @Override
    public void onReceive(Context context, Intent intent) {
        mydb=new DataBaseHelper(context);
        SQLiteDatabase db = mydb.getWritableDatabase();
        int toggle = 0;
        int position = intent.getIntExtra("position", 0);
        Cursor res = db.rawQuery("select TOGGLE from " + TABLE_NAME + " where ID=" + (position + 1), null);
        if (res != null) {
            res.moveToFirst();
            toggle = res.getInt(res.getColumnIndex("TOGGLE"));
        }
        res.close();
        if (toggle != 0) {

            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            wifiManager.setWifiEnabled(false);

            AudioManager vibrator = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

            vibrator.setRingerMode(AudioManager.RINGER_MODE_NORMAL);


            android.provider.Settings.System.putInt(context.getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS,
                    50);

            audioManager.setStreamVolume(AudioManager.STREAM_RING, 50, 0);
            

            BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
            bluetooth.disable();

        }
    }
}
