package com.acrylex.mrstandby;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by sony on 03-02-2017.
 */
public class DataBaseHelper1 extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Priority.db";
    public static final String TABLE_NAME = "Priority_table";
    public static final String COL1 = "NUMBER";
    public static final String COL2 = "CALL_DURATION";
    public static final String COL3 = "MISSED_CALL_COUNT";
    public static final String COL4 = "TIMES_CONTACTED";
    public static final String COL5 = "LAST_TIME_CONTACTED";
    Context mContext;


    public DataBaseHelper1(Context context) {
        super(context, DATABASE_NAME, null, 1);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(NUMBER TEXT PRIMARY KEY,CALL_DURATION INTEGER,MISSED_CALL_COUNT INTEGER,TIMES_CONTACTED INTEGER,LAST_TIME_CONTACTED INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }
    public boolean insertData(String number) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, number);
        contentValues.put(COL2, 0);
        contentValues.put(COL3, 0);
        contentValues.put(COL4, 0);
        contentValues.put(COL5, 0);
        long result=-1;
        try {
            result=db.insert(TABLE_NAME, null, contentValues);
        }
        catch (Exception e) {

        }
        if(result ==-1) {
            long maxPriority = 0;
            Cursor val = db.rawQuery("select max(CALL_DURATION*TIMES_CONTACTED*1000*60/("+System.currentTimeMillis()+"-LAST_TIME_CONTACTED)) from " + TABLE_NAME, null);
            //try {
                if (val != null) {
                    if (val.moveToFirst()) {
                        maxPriority = val.getLong(0);
                    }
                    long myPriority = 0;
                    Cursor temp = db.rawQuery("select CALL_DURATION*TIMES_CONTACTED*1000*60/("+System.currentTimeMillis()+"-LAST_TIME_CONTACTED) from "  + TABLE_NAME + " where " + COL1 + "=? ", new String[]{number});
                    if (temp != null) {
                        if (temp.moveToFirst()) {
                            myPriority = temp.getLong(0);
                        }
                    }
                    int missedCallCount = 0;
                    Cursor missed = db.rawQuery("select * from " + TABLE_NAME + " where " + COL1 + "=? ", new String[]{number});
                    if (missed != null) {
                        if (missed.moveToFirst()) {
                            missedCallCount = missed.getInt(2);
                            Log.i("missedCallCount",String.valueOf(missedCallCount));
                        }
                    }
                    Toast.makeText(mContext, maxPriority + "\n" + myPriority, Toast.LENGTH_SHORT).show();
                    Vibrator v=(Vibrator)mContext.getSystemService(Context.VIBRATOR_SERVICE);
                    //AudioManager ringer=(AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
                    //Uri notification= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    //MediaPlayer myRingtone=MediaPlayer.create(mContext, notification);
                    if (myPriority > maxPriority * 0.8) {
                        if (missedCallCount > 0) {
                           // ringer.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                            //myRingtone.start();
                            v.vibrate(3000);
                            Toast.makeText(mContext, "High", Toast.LENGTH_LONG).show();
                        }
                    } else if (myPriority > maxPriority * 0.5) {
                        if (missedCallCount >= 2) {
                            //ringer.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                            //myRingtone.start();
                            v.vibrate(3000);
                            Toast.makeText(mContext, "Middle", Toast.LENGTH_LONG).show();

                        }
                    } else {
                        if (missedCallCount >= 4) {
                      //      ringer.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        //    myRingtone.start();
                            v.vibrate(3000);
                            Toast.makeText(mContext, "Low", Toast.LENGTH_LONG).show();
                        }
                    }
                    val.close();
                    temp.close();
                    missed.close();
                }
            //}
            //catch (Exception e){

            //}
        }
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean updateData(String number,String duration,String callCount){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        ContentValues contentValues1 = new ContentValues();
        contentValues1.put(COL1, number);
        contentValues1.put(COL2, 0);
        contentValues1.put(COL3, 0);
        contentValues1.put(COL4, 0);
        contentValues1.put(COL5, 0);
        long result1=-1;
        try {
            result1=db.insert(TABLE_NAME, null, contentValues1);
        }
        catch (Exception e) {

        }
        Cursor temp=db.rawQuery("select * from " + TABLE_NAME + " where " + COL1 + "=? ", new String[]{number});
        temp.moveToNext();
        int callDuration=Integer.parseInt(temp.getString(temp.getColumnIndex(COL2)));
        int missedCallCount=Integer.parseInt(temp.getString(temp.getColumnIndex(COL3)));
        int timesContacted=Integer.parseInt(temp.getString(temp.getColumnIndex(COL4)));
        timesContacted++;;
        long lastTimeContacted=System.currentTimeMillis();
        if(duration!=null)
            callDuration=callDuration+Integer.parseInt(duration);
        int type=Integer.parseInt(callCount);
        if(type==2){
            missedCallCount++;
        }
        contentValues.put(COL1,number);
        contentValues.put(COL2,callDuration);
        contentValues.put(COL3, missedCallCount);
        contentValues.put(COL4, timesContacted);
        contentValues.put(COL5, lastTimeContacted);
        Toast.makeText(mContext,"Number: "+number+"\nCallDuration: "+callDuration+"\nMissed Call: "+missedCallCount+"\nTimes-Contacted : "+timesContacted+"\nLast-Time-Contacted : "+lastTimeContacted,Toast.LENGTH_LONG).show();
        long result = db.update(TABLE_NAME, contentValues, COL1 + "=? ",new String[]{number});
        if(result == -1)
            return false;
        else
            return true;
    }
    public Cursor getAllData(String number){
        SQLiteDatabase  db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME+" where "+COL1+"=? ",new String[]{number});
        return res;
    }
}

