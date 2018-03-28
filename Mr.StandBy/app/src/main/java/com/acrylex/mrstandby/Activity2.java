package com.acrylex.mrstandby;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class Activity2 extends AppCompatActivity {

    private static final String TAG = "Activity2";
    //private CardArrayAdapter2 cardArrayAdapter2;
    private ListView listView;


    static int set=0;
    TextView from,to,fromTime,toTime;
    EditText schedule;
    int timeHourFrom,timeMinuteFrom,timeHourTo,timeMinuteTo,position,currentBrightness,currentVolume,brightnessProgress,volumeProgress;
    AlarmManager alarmManager;
    SwitchCompat wifi,silent,vibrate,bluetooth,gps;
    SeekBar volume,brightness;
    private PendingIntent pendingIntentFrom,pendingIntentTo;
    Intent myIntentFrom,myIntentTo;
    Calendar calendarFrom,calendarTo;
    SwitchCompat freq;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        freq=(SwitchCompat)findViewById(R.id.freq);
    
        schedule=(EditText)findViewById(R.id.schedule2);
        from=(TextView)findViewById(R.id.from);
        to=(TextView)findViewById(R.id.to);
        fromTime=(TextView)findViewById(R.id.fromText);
        toTime=(TextView)findViewById(R.id.toText);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        myIntentFrom = new Intent(Activity2.this, FromAlarmReceiver.class);
        myIntentTo = new Intent(Activity2.this, ToAlarmReceivers.class);

       //airplane=(SwitchCompat)findViewById(R.id.switch1);
        volume=(SeekBar)findViewById(R.id.seekBar1);
        silent=(SwitchCompat)findViewById(R.id.switch2);
       // gps=(SwitchCompat)findViewById(R.id.switch3);
        wifi=(SwitchCompat)findViewById(R.id.switch4);
        vibrate=(SwitchCompat)findViewById(R.id.switch5);
        bluetooth=(SwitchCompat)findViewById(R.id.switch6);
        brightness=(SeekBar)findViewById(R.id.seekBar2);


        Intent intent=getIntent();

        position=intent.getIntExtra("position",0);
        schedule.setText(intent.getStringExtra("schedule"));
        fromTime.setText(intent.getStringExtra("from"));
        toTime.setText(intent.getStringExtra("to"));
        if(intent.getIntExtra("once",0)==0){
            freq.setChecked(true);
        }
        Toast.makeText(getApplicationContext(),position+"\n"+intent.getStringExtra("schedule")+"\n"+intent.getStringExtra("from")+"\n"
        +intent.getStringExtra("to"),Toast.LENGTH_LONG).show();

        if(intent.getIntExtra("wifi",0)==1){
            wifi.setChecked(true);
        }
        if(intent.getIntExtra("vibrate",0)==1){
            vibrate.setChecked(true);
        }
        if(intent.getIntExtra("silent",0)==1){
            silent.setChecked(true);
        }

        currentBrightness=intent.getIntExtra("brightness",0);
        currentVolume=intent.getIntExtra("volume",0);

        if(intent.getIntExtra("bluetooth",0)==1){
            bluetooth.setChecked(true);
        }

        calendarFrom = Calendar.getInstance();
        calendarTo = Calendar.getInstance();

        setBrightness();
        setVolume();
    }


    private void setBrightness() {

        brightness.setMax(255);
        //float curBrightnessValue = 0;

        /*try {
            curBrightnessValue = android.provider.Settings.System.getInt(
                    getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }*/
        //int screen_brightness =currentBrightness;
        brightness.setProgress(currentBrightness);

        brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue,
                                          boolean fromUser) {
                brightnessProgress = progresValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do something here,
                // if you want to do anything at the start of
                // touching the seekbar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setVolume() {

        volume.setMax(255);
        //float curBrightnessValue = 0;

        /*try {
            curBrightnessValue = android.provider.Settings.System.getInt(
                    getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }*/
        //int system_volume =currentVolume;
        //volume.setProgress(currentVolume);

        volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue,
                                          boolean fromUser) {
                volumeProgress = progresValue;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do something here,
                // if you want to do anything at the start of
                // touching the seekbar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /*public void inputDuration(View v){

        final EditText hrText,minText;
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Duration");
        LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v1=inflater.inflate(R.layout.duration,null);
        builder.setView(R.layout.duration);
        builder.setPositiveButton("set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText hrText=(EditText)v1.findViewById(R.id.hr);
                EditText minText=(EditText)v1.findViewById(R.id.min);
                Log.i("hr",hrText.getText().toString());
                TextView durVal=(TextView)findViewById(R.id.duration);
                int hr=Integer.parseInt(hrText.getText().toString());
                int min=Integer.parseInt(minText.getText().toString());
                //int hr=5,min=5;
                durVal.setText(hr+":"+min);
                timeHourTo=timeHourFrom+hr;
                timeMinuteTo=timeMinuteFrom+min;
                calendarTo.set(Calendar.HOUR_OF_DAY, timeHourTo);
                calendarTo.set(Calendar.MINUTE, timeMinuteTo);
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }*/

    public void setTime(View v) {
        final int flag;
        if (v.getId() == R.id.fromLayout) {
            flag = 1;
        } else {
            flag = 0;
        }

        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override

            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                if (flag == 1) {
                    timeHourFrom=hourOfDay;
                    timeMinuteFrom=minute;
                    fromTime.setText(hourOfDay + " : " + minute);
                    calendarFrom.set(Calendar.HOUR_OF_DAY, timeHourFrom);
                    calendarFrom.set(Calendar.MINUTE, timeMinuteFrom);

                } else {
                    timeHourTo=hourOfDay;
                    timeMinuteTo=minute;
                    toTime.setText(hourOfDay + " : " + minute);
                    calendarTo.set(Calendar.HOUR_OF_DAY, timeHourTo);
                    calendarTo.set(Calendar.MINUTE, timeMinuteTo);
                }
            }
        }, hour, minute, true);

        timePickerDialog.show();

    }


    public void setCancel(){
        DataBaseHelper db=new DataBaseHelper(getApplicationContext());
        alarmManager.cancel(pendingIntentFrom);
        alarmManager.cancel(pendingIntentTo);
        //pendingIntentFrom.cancel();
        //pendingIntentTo.cancel();
        db.insertData(position+1,schedule.getText().toString(),"","",0,0,0,0,0,0,0,0);
        Intent intent=new Intent();
        intent.putExtra("schedule_name",schedule.getText().toString());
        setResult(0,intent);
        finish();//finishing activity

    }

    public void setButtonClick(){

        DataBaseHelper db=new DataBaseHelper(getApplicationContext());
        int wifi_no=0,silent_no=0,vibrate_no=0,airplane_no=0,gps_no=0,bluetooth_no=0;
        //RadioButton once=(RadioButton)findViewById(R.id.once);
        //RadioButton everyday=(RadioButton)findViewById(R.id.everyday);

        boolean flag4=wifi.isChecked();
        boolean flag5=vibrate.isChecked();
        boolean flag2=silent.isChecked();
       // boolean flag1=airplane.isChecked();
        //boolean flag3=gps.isChecked();
        boolean flag6=bluetooth.isChecked();

        if(flag4)wifi_no=1;
        if(flag5)vibrate_no=1;
        if(flag2)silent_no=1;
       // if(flag1)airplane_no=1;
        //if(flag3)gps_no=1;
        if(flag6)bluetooth_no=1;

        myIntentFrom.putExtra("brightness",brightnessProgress);
        myIntentFrom.putExtra("silent",silent_no);
        //myIntentFrom.putExtra("once",gps_no);
        myIntentFrom.putExtra("wifi",wifi_no);
        myIntentFrom.putExtra("vibrate",vibrate_no);
        myIntentFrom.putExtra("bluetooth",bluetooth_no);
        myIntentFrom.putExtra("volume",volumeProgress);
        myIntentFrom.putExtra("position",position);
        pendingIntentFrom = PendingIntent.getBroadcast(Activity2.this, position+1 , myIntentFrom, 0);
        pendingIntentTo = PendingIntent.getBroadcast(Activity2.this, -(position+1) , myIntentTo, 0);

        int isOnce=0;
       if(!freq.isChecked()) {

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendarFrom.getTimeInMillis(), pendingIntentFrom);

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendarTo.getTimeInMillis(), pendingIntentTo);

           isOnce=1;
        }
        else{

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendarFrom.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntentFrom);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendarTo.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntentTo);
            Log.i("set","enabled");
            Toast.makeText(getApplicationContext(),"set",Toast.LENGTH_SHORT).show();
        }
        db.insertData(position+1, schedule.getText().toString(), fromTime.getText().toString(), toTime.getText().toString(),brightnessProgress, silent_no,isOnce, wifi_no, vibrate_no,bluetooth_no,volumeProgress,1);

        Intent intent=new Intent();
        //intent.putExtra("schedule_name",schedule.getText().toString());
        setResult(0,intent);
        finish();//finishing activity

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setbutton, menu);
        MenuItem setItem = menu.findItem(R.id.set_button);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.set_button) {
            setButtonClick();
            return true;
        }
        else if(id == R.id.cancel_button){
            setCancel();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
