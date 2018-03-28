package com.acrylex.mrstandby;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by sony on 03-01-2017.
 */
public class Tab2 extends Fragment implements View.OnClickListener {

    ListView item1;
    SimpleCursorAdapter sca1;
    String fromLogs[] = {
            CallLog.Calls.NUMBER,
            CallLog.Calls.CACHED_NAME,
            CallLog.Calls.DATE,
            CallLog.Calls._ID,
    };
    int toLogs[] = {
            R.id.number,
            R.id.cached_name

    };
    public static final String TABLE_NAME = "Schedule_table";
    DataBaseHelper mydb;
    //ListView mListView;
    String schedule_name[], from[], to[];
    int wifi[], vibrate[], silent[], once[], bluetooth[], volume[], brightness[];

    private static final int REQUEST_CODE = 0;
    static View tempView;
    static int position;
    SwitchCompat switch1, switch2, switch3, switch4, switch5;

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v1 = inflater.inflate(R.layout.tab2, container, false);

        tempView=v1;
        v1.findViewById(R.id.one).setOnClickListener(this);
        v1.findViewById(R.id.two).setOnClickListener(this);
        v1.findViewById(R.id.three).setOnClickListener(this);
        v1.findViewById(R.id.four).setOnClickListener(this);
        v1.findViewById(R.id.five).setOnClickListener(this);
        switch1 = (SwitchCompat) v1.findViewById(R.id.name1);
        switch2 = (SwitchCompat) v1.findViewById(R.id.name2);
        switch3 = (SwitchCompat) v1.findViewById(R.id.name3);
        switch4 = (SwitchCompat) v1.findViewById(R.id.name4);
        switch5 = (SwitchCompat) v1.findViewById(R.id.name5);

        mydb = new DataBaseHelper(getContext());
        createList();
        setToggles();

       
        return v1;
    }


    public void createList() {
        int i = 0;
        schedule_name = new String[5];
        from = new String[5];
        to = new String[5];
        brightness = new int[5];
        wifi = new int[5];
        vibrate = new int[5];
        silent = new int[5];
        volume = new int[5];
        bluetooth = new int[5];
        once = new int[5];

        Cursor cr = mydb.getAllData();
        //int schedule_name[]
        if (cr != null) {
            while (cr.moveToNext()) {
                schedule_name[i] = cr.getString(1);
                from[i] = cr.getString(2);
                to[i] = cr.getString(3);
                brightness[i] = cr.getInt(4);
                wifi[i] = cr.getInt(7);
                vibrate[i] = cr.getInt(8);
                silent[i] = cr.getInt(5);
                once[i] = cr.getInt(6);
                bluetooth[i] = cr.getInt(9);
                volume[i] = cr.getInt(10);
                i++;
            }
        }
        cr.close();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
      
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getContext(), Activity2.class);
        int id = view.getId();
        //tempView = view;
        position = 0;
        if (id == R.id.one) {
            position = 0;
        } else if (id == R.id.two) {
            position = 1;
        } else if (id == R.id.three) {
            position = 2;
        } else if (id == R.id.four) {
            position = 3;
        } else if (id == R.id.five) {
            position = 4;
        }

        intent.putExtra("position", position);
        intent.putExtra("schedule", schedule_name[position]);
        intent.putExtra("from", from[position]);
        intent.putExtra("to", to[position]);
        intent.putExtra("brightness", brightness[position]);
        intent.putExtra("silent", silent[position]);
        //intent.putExtra("gps", gps[position]);
        intent.putExtra("wifi", wifi[position]);
        intent.putExtra("vibrate", vibrate[position]);
        intent.putExtra("bluetooth", bluetooth[position]);
        intent.putExtra("volume", volume[position]);
        intent.putExtra("once", once[position]);

        startActivityForResult(intent, REQUEST_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            createList();
            setToggles();
        }


    }

    void setToggles() {
        int toggle = 0;
        SQLiteDatabase db = mydb.getWritableDatabase();
        for (int i = 0; i < 5; i++) {
            Cursor res = db.rawQuery("select TOGGLE from " + TABLE_NAME + " where ID=" + (i + 1), null);
            if (res != null) {
                res.moveToNext();
                toggle = res.getInt(0);
            }
            res.close();
            String schedule = schedule_name[i];

            if (i == 0) {
                TextView name = (TextView) tempView.findViewById(R.id.line1);
                name.setText(schedule);
                if (toggle == 1) {
                    switch1.setChecked(true);
                } else {
                    switch1.setChecked(false);
                }
            } else if (i == 1) {
                TextView name = (TextView) tempView.findViewById(R.id.line2);
                name.setText(schedule);
                if (toggle == 1) {
                    switch2.setChecked(true);
                } else {
                    switch2.setChecked(false);
                }
            } else if (i == 2) {
                TextView name = (TextView) tempView.findViewById(R.id.line3);
                name.setText(schedule);
                if (toggle == 1) {
                    switch3.setChecked(true);
                } else {
                    switch3.setChecked(false);
                }
            } else if (i == 3) {
                TextView name = (TextView) tempView.findViewById(R.id.line4);
                name.setText(schedule);
                if (toggle == 1) {
                    switch4.setChecked(true);
                } else {
                    switch4.setChecked(false);
                }
            } else if (i == 4) {
                TextView name = (TextView) tempView.findViewById(R.id.line5);
                name.setText(schedule);
                if (toggle == 1) {
                    switch5.setChecked(true);
                } else {
                    switch5.setChecked(false);
                }
            }

        }
    }

    }


