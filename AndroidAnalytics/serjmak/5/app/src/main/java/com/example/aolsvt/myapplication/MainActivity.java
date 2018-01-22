package com.example.aolsvt.myapplication;


import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView lv = (ListView) findViewById(R.id.lv);
        final TextView tv = (TextView) findViewById(R.id.textView);

        String path = Environment.getExternalStorageDirectory().getAbsolutePath();

        Log.d("Files", "Path: " + path);

        File f = new File(path);

        path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        File f1 = new File(path);
        File file[] = f.listFiles();
        File file1[] = f1.listFiles();

        Log.d("Files", "Size: " + file1.length);
        Log.d("Files", "Size: " + file.length);

        if(file1.length == 0) {
            String [] fileNames = new String[file.length];
            for (int i = 0; i < file.length; i++) {
                fileNames[i] = file[i].getPath();
            }

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                    (this, android.R.layout.simple_list_item_1, fileNames);

            lv.setAdapter(arrayAdapter);
            tv.setText("Image count - 0");
        } else {
            String [] fileNames1 = new String[file1.length];
            for (int i = 0; i < file1.length; i++) {
                fileNames1[i] = file1[i].getPath();
            }

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                    (this, android.R.layout.simple_list_item_1, fileNames1);

            lv.setAdapter(arrayAdapter);
            tv.setText("Image count - " + file1.length);
        }

    }
}