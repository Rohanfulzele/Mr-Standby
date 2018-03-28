package com.acrylex.mrstandby;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;


public class CustomPhoneStateListener extends PhoneStateListener {
    private int prev_state;
    static int flag=1,missed=0,received=0,outgoing=0;
    static String incoming_number;
    private static final String TAG = "CustomPhoneState";
    Context context;
    DataBaseHelper1 mydb;
    static String type,duration;
    long startTime=0,endTime=0;

    public CustomPhoneStateListener(Context context){
        this.context=context;
    }
    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        mydb = new DataBaseHelper1(context);

        if( incomingNumber != null && incomingNumber.length() > 0 )
            incoming_number = incomingNumber;


        switch(state) {
            case TelephonyManager.CALL_STATE_RINGING:
                Log.d(TAG, "CALL_RINGING");
                prev_state = state;
                if(flag==1) {
                    boolean isInserted = mydb.insertData(incoming_number);
                    if (isInserted) {
                        Toast.makeText(context, "data inserted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "data not inserted", Toast.LENGTH_LONG).show();
                    }
                    flag=2;
                    missed=1;
                    received=1;
                }
                break;

            case TelephonyManager.CALL_STATE_OFFHOOK:
                startTime=System.currentTimeMillis();
                Log.d(TAG, "CALL_OFFHOOK");
                prev_state = state;
                outgoing=1;
                break;

            case TelephonyManager.CALL_STATE_IDLE:
               
                    flag=1;
                    Log.d(TAG, "CALL_STATE_IDLE==>" + incoming_number);

                    if ((prev_state == TelephonyManager.CALL_STATE_OFFHOOK))
                    {
                        endTime=System.currentTimeMillis();
                        long callTime=(endTime-startTime)/1000;
                        String duration=String.valueOf(callTime);
                        prev_state = state;
                        if(received==1)
                        {
                            //Answered Call which is ended
                            boolean isUpdated = mydb.updateData(incoming_number, duration, "1");
                            if (isUpdated) {
                                Toast.makeText(context, "data updated", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, "data not updated", Toast.LENGTH_LONG).show();
                            }
                            received=0;
                            outgoing=0;
                        }
                        if(outgoing==1){
                           
                            boolean isUpdated = mydb.updateData(incoming_number, duration, "1");
                            if (isUpdated) {
                                Toast.makeText(context, "data updated", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, "data not updated", Toast.LENGTH_LONG).show();
                            }
                            received=0;
                            outgoing=0;
                        }

                    }

                    if ((prev_state == TelephonyManager.CALL_STATE_RINGING) && missed==1) {
                        prev_state = state;
                        boolean isUpdated = mydb.updateData(incoming_number,"0","2");
                        if (isUpdated) {
                            Toast.makeText(context, "data updated", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "data not updated", Toast.LENGTH_LONG).show();
                        }
                        missed=0;
                        //Rejected or Missed call
                    }
                break;
        }
    }
}
