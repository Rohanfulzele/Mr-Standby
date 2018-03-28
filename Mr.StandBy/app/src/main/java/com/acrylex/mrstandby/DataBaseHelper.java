package com.acrylex.mrstandby;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by sony on 28-02-2017.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Schedule.db";
    public static final String TABLE_NAME = "Schedule_table";
    public static final String COL1 = "ID";
    public static final String COL2 = "SCHEDULE_NAME";
    public static final String COL3 = "FROM_TIME";
    public static final String COL4 = "TO_TIME";
    public static final String COL5 = "BRIGHTNESS";
    public static final String COL6 = "SILENT";
    public static final String COL7 = "ONCE";
    public static final String COL8 = "WIFI";
    public static final String COL9 = "VIBRATE";
    public static final String COL10 = "BLUETOOTH";
    public static final String COL11 = "VOLUME";
    public static final String COL12 = "TOGGLE";

Context mContext;
    public DataBaseHelper(Context context) {
        super(context,DATABASE_NAME,null,1);
        mContext=context;

    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY,SCHEDULE_NAME TEXT,FROM_TIME TEXT,TO_TIME TEXT,BRIGHTNESS INTEGER,SILENT INTEGER,ONCE INTEGER,WIFI INTEGER,VIBRATE INTEGER,BLUETOOTH INTEGER,VOLUME INTEGER,TOGGLE INTEGER)");
        ContentValues contentValues=new ContentValues();
        for(int i=1;i<=5;i++){

            contentValues.put(COL1,i);
            contentValues.put(COL2,"Schedule "+i);
            contentValues.put(COL3,"");
            contentValues.put(COL4,"");
            contentValues.put(COL5,0);
            contentValues.put(COL6,0);
            contentValues.put(COL7,0);
            contentValues.put(COL8,0);
            contentValues.put(COL9,0);
            contentValues.put(COL10,0);
            contentValues.put(COL11,0);
            contentValues.put(COL12,0);
            db.insert(TABLE_NAME,null,contentValues);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(int id,String schedule_name,String from,String to,int brightness,int silent,int once,int wifi,int vibrate,int bluetooth,int volume,int toggle){

        //Toast.makeText(mContext,id+"\n"+schedule_name+"\n"+from+"\n"+to+"\n"+wifi+"\n"+vibrate+"\n"+silent,Toast.LENGTH_SHORT).show();
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL1,id);
        contentValues.put(COL2,schedule_name);
        contentValues.put(COL3,from);
        contentValues.put(COL4,to);
        contentValues.put(COL5, brightness);
        contentValues.put(COL6, silent);
        contentValues.put(COL7, once);
        contentValues.put(COL8,wifi);
        contentValues.put(COL9,vibrate);
        contentValues.put(COL10, bluetooth);
        contentValues.put(COL11, volume);
        contentValues.put(COL12, toggle);
        int result=db.update(TABLE_NAME,contentValues,COL1+"="+id,null);
        if(result==1)
            return true;
        else {
            Log.e("Inserted", "false");
            return false;
        }
    }

    public Cursor getAllData(){
        SQLiteDatabase  db = this.getWritableDatabase();
        //Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        Cursor res=db.query(TABLE_NAME,null,null,null,null,null,null);
        return res;
    }

    public Cursor getToggles(int id){
        SQLiteDatabase  db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME+" where "+COL1+"="+id,null);
        //Cursor res=db.query(TABLE_NAME,null,null,null,null,null,null);
        return res;
    }
}
