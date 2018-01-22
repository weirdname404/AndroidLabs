package com.evolution.phonebookapp;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

public class MainActivity extends ListActivity{

    private ListView lv;
    private ArrayList<String> conNames;
    private ArrayList<String> conNumbers;
    private Cursor crContacts;
    String number = "+79037194370";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getActionBar();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddContact.class));
            }
        });

        lv = findViewById(list);

        conNames = new ArrayList<String>();
        conNumbers = new ArrayList<String>();

        crContacts = ContactHelper.getContactCursor(getContentResolver(), "");
        crContacts.moveToFirst();

        while (!crContacts.isAfterLast()) {
            conNames.add(crContacts.getString(1));
            conNumbers.add(crContacts.getString(2));
            crContacts.moveToNext();
        }

        setListAdapter(new MyAdapter(this, android.R.layout.simple_list_item_1,
                R.id.tvNameMain, conNames));
    }

    @Override
    protected void onListItemClick (ListView l, View v, int position, long id) {
       Toast.makeText(this, "You have selected contact " + position, Toast.LENGTH_SHORT).show();
        number = conNumbers.get(position);
    }

    public void sendSms(View view) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("+79037194370", null, "SMS Message Body", null, null);
        Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_SHORT).show();
    }

    public void makeCall(View view) {

        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse( "tel: "+number)));
    }


    public void deleteNumb(View view, int postion){
        String conNum  = conNumbers.get(postion);
    }


    private class MyAdapter extends ArrayAdapter<String> {

        public MyAdapter(Context context, int resource, int textViewResourceId,
                         ArrayList<String> conNames) {
            super(context, resource, textViewResourceId, conNames);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View row = setList(position, parent);
            return row;
        }

        private View setList(int position, ViewGroup parent) {
            LayoutInflater inf = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row = inf.inflate(R.layout.liststyle, parent, false);

            TextView tvName = row.findViewById(R.id.tvNameMain);
            TextView tvNumber = row.findViewById(R.id.tvNumberMain);
            ImageView sms = row.findViewById(R.id.text);
            ImageView call = row.findViewById(R.id.call);


            tvName.setText("Имя: " + conNames.get(position));
            tvNumber.setText("Номер: " + conNumbers.get(position));

            return row;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.item1) {
            Intent intent = new Intent(MainActivity.this, AddContact.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.item2) {
            Intent intent = new Intent(MainActivity.this, DeleteContacts.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
