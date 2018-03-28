package com.acrylex.mrstandby;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Sachin on 16-Mar-17.
 */
public class ContactsDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Contacts.db";
    public static final String TABLE_NAME = "Contacts_table";
    public static final String COL1 = "NUMBER";
    public static final String COL2 = "PRIORITY";

    public ContactsDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TABLE_NAME + "(NUMBER TEXT PRIMARY KEY,PRIORITY INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public boolean updateData(String number, long mypriority) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, number);
        contentValues.put(COL2, mypriority);
        Log.e("number", number);
        //long result = db.update(TABLE_NAME, contentValues, COL1 + "=? ", new String[]{number});
        try {
            long result = db.insert(TABLE_NAME, null, contentValues);
            Log.e("rows", String.valueOf(result));
            if(result==-1){
                long result1 = db.update(TABLE_NAME, contentValues, COL1 + "=? ", new String[]{number});
                Log.e("rowsUpdated", String.valueOf(result1));
                return true;
            }
        }
        catch (Exception e){
            long result1 = db.update(TABLE_NAME, contentValues, COL1 + "=? ", new String[]{number});
            Log.e("rowsUpdated", String.valueOf(result1));
            return true;
        }
        return false;
    }
}