package com.acrylex.mrstandby;
 import android.content.ContentResolver;
        import android.content.Context;
        import android.content.Intent;
        import android.database.Cursor;
 import android.database.sqlite.SQLiteDatabase;
 import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.provider.ContactsContract;
        import android.provider.MediaStore;
        import android.support.v4.app.Fragment;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ImageView;
        import android.widget.ListView;
        import android.widget.SearchView;
        import android.widget.SimpleCursorAdapter;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.List;

//Our class extending fragment
public class Tab1 extends Fragment {

    ArrayList<SelectUser> selectUsers;
    List<SelectUser> temp;
    // Contact List
    ListView listView;
    // Cursor to load contacts list
    Cursor phones, email;

    // Pop up
    ContentResolver resolver;
    SearchView search;
    SelectUserAdapter adapter;
    SimpleCursorAdapter sca1;
    ListView item1;
    String fromContacts[] = {
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone._ID,
    };

    int toContacts[] = {
            R.id.name,
            //R.id.phone
    };

    public static final String TABLE_NAME = "Priority_table";
    public static final String COL1 = "NUMBER";

    public static final String TABLE_NAME1 = "Contacts_table";
    public static final String COL2 = "PRIORITY";

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        View v1 = inflater.inflate(R.layout.tab1, container, false);
        item1 = (ListView) v1.findViewById(R.id.listView1);
        return v1;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       
        selectUsers = new ArrayList<SelectUser>();
    
        LoadContact loadContact = new LoadContact();
        loadContact.execute();

      
    }

    // Load data on background
    class LoadContact extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.e("flag", "started");
            // Get Contact list from Phone
            String number=null;
            DataBaseHelper1 priority=new DataBaseHelper1(getContext());
            ContactsDatabase contacts=new ContactsDatabase(getContext());
            SQLiteDatabase db=priority.getWritableDatabase();
            SQLiteDatabase db1=contacts.getWritableDatabase();
            Cursor val = db.rawQuery("select "+COL1+" from " + TABLE_NAME, null);
            if(val!=null){
                while(val.moveToNext()){
                    Log.e("infi1", "started");
                    number=val.getString(val.getColumnIndex(COL1));
                    long myPriority = 0;
                    Cursor temp = db.rawQuery("select CALL_DURATION*TIMES_CONTACTED*100000000000000*60/("+System.currentTimeMillis()+"-LAST_TIME_CONTACTED) from "  + TABLE_NAME + " where " + COL1 + "=? ", new String[]{number});
                    Log.e("countTemp", "" + temp.getCount());
                    if (temp != null) {
                        Log.e("infi2", "started");
                        if (temp.moveToNext()) {
                            Log.e("infi3", "started");
                            myPriority = temp.getLong(0);
                            Log.i("current", String.valueOf(myPriority));
                            contacts.updateData(number,myPriority);
                        }
                    }
                    temp.close();
                }
                val.close();
            }
            else{
                Toast.makeText(getContext(),"NULL",Toast.LENGTH_LONG).show();
            }


          /* if (phones != null) {
                Log.e("count", "" + phones.getCount());
                if (phones.getCount() == 0) {
                    //Toast.makeText(MainActivity.this, "No contacts in your contact list.", Toast.LENGTH_LONG).show();
                }*/

            Cursor sort = db1.rawQuery("SELECT * FROM "  + TABLE_NAME1+" ORDER BY PRIORITY DESC" , null);
            Log.e("countSort", "" + sort.getCount());
            String conNum=null;
            Log.e("in", "in");
            if(sort!=null){

                Log.e("flag1", "reached");
                while(sort.moveToNext()){
                    conNum=null;
                    Log.e("flag2", "reached");
                    conNum=sort.getString(0);

               resolver = getActivity().getApplicationContext().getContentResolver();

               phones = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.NUMBER+"='"+conNum+"'", null, null);
                    Log.e("countPhones", "" + phones.getCount());
                Log.e("flag", "reached");
                    Bitmap bit_thumb = null;
                    String name=null;
                    String image_thumb=null;
                    if(phones.getCount()!=0) {
                        Log.e("insidePhones", "in");

                        phones.moveToFirst();

                        
                         name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                        Log.e("conName", name);

                        
                        image_thumb = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));

                        phones.close();
                    }
                    else {
                        try {
                            if (image_thumb != null) {
                                bit_thumb = MediaStore.Images.Media.getBitmap(resolver, Uri.parse(image_thumb));
                            } else {
                                Log.e("No Image Thumb", "--------------");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e("No Image Thumb found", "--------------");
                        }
                    }


                    SelectUser selectUser = new SelectUser();

                    if (bit_thumb != null) {

                        selectUser.setThumb(bit_thumb);
                    } else {
                        bit_thumb = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.icon_user);
                        // selectUser.setThumb(bit_thumb);
                    }
                    selectUser.setName(name);
                    selectUser.setPhone(conNum);
                    //selectUser.setEmail(id);
                    //selectUser.setCheckedBox(false);
                    selectUsers.add(selectUser);

                }
                sort.close();

                }
            else{
                Toast.makeText(getContext(),"Not Sort",Toast.LENGTH_LONG).show();
            }
            //phones.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new SelectUserAdapter(selectUsers, getActivity().getApplicationContext());
            item1.setAdapter(adapter);

            // Select item on listclick
            item1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    TextView no=(TextView)view.findViewById(R.id.no);
                    String num=no.getText().toString();
                    String number="tel:"+num;
                    Uri uri=Uri.parse(number);
                    Intent intent=new Intent(Intent.ACTION_DIAL);
                    intent.setData(uri);
                    startActivity(intent);
                }


            });

            item1.setFastScrollEnabled(true);
        }

    }


}

