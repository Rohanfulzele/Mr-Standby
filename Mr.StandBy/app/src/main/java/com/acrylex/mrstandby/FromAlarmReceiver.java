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
import android.widget.Toast;


public class FromAlarmReceiver extends WakefulBroadcastReceiver {

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
            toggle = res.getInt(0);
        }
        res.close();
        if (toggle != 0) {
            int wifi = intent.getIntExtra("wifi", 0);
            int vibrate = intent.getIntExtra("vibrate", 0);
            int silent = intent.getIntExtra("silent", 0);
            int brightness = intent.getIntExtra("brightness", 0);
            //int gps=intent.getIntExtra("gps",0);
            int bluetooth = intent.getIntExtra("bluetooth", 0);
            int volume = intent.getIntExtra("volume", 0);

            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

            if (wifi == 1) {
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                wifiManager.setWifiEnabled(true);
            }

            if (vibrate == 1) {
                AudioManager vibrator = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                vibrator.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            }

            if (silent == 1) {
                AudioManager silence = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                silence.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            }

            android.provider.Settings.System.putInt(context.getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS,
                    brightness);

            audioManager.setStreamVolume(AudioManager.STREAM_RING, volume, 0);

        /*if(gps==1){
            Intent intent1=new Intent("android.location.GPS_ENABLED_CHANGE");
            intent.putExtra("enabled", true);
            context.sendBroadcast(intent1);
        }*/
            if (bluetooth == 1) {
                BluetoothAdapter bluetoothMode = BluetoothAdapter.getDefaultAdapter();
                bluetoothMode.enable();
            }
        }
    }
}
