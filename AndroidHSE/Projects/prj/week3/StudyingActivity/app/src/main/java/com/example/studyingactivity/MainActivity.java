package com.example.studyingactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {

    // Tag for logging
    final String LOG_TAG = "StudyingActivity";
    final String ON_CREATE = "onCreate";
    final String ON_START = "onStart";
    final String ON_RESUME = "onResume";
    final String ON_PAUSE = "onPause";
    final String ON_STOP = "onStop";
    final String ON_RESTART = "onRestart";
    final String ON_DESTROY = "onDestroy";


    int[] counters = {0, 0, 0, 0, 0, 0, 0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        final Spinner spinner = findViewById(R.id.spinner);
        final EditText filmName = findViewById(R.id.filmName);

        // Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.ages, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.ages, R.layout.custom_spinner_item);

        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        Log.d(LOG_TAG, ON_CREATE);
        counters[0]++;
        updateTextView();

        //spinner value changed
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String[] ages = spinner.getSelectedItem().toString().split("–");
                int age = Integer.parseInt(ages[0]);
                boolean isUnderage = (age < 18);
                boolean isAdultFilm = (filmName.getText().toString().contains("XXX"));

                if (isUnderage && isAdultFilm) {
                    filmName.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    // show movies button
    public void showMovies(View view) {
        Intent intent = new Intent(getApplicationContext(), MovieListActivity.class);
        Spinner spinner = findViewById(R.id.spinner);
        intent.putExtra("age", spinner.getSelectedItem().toString());
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                TextView textView = findViewById(R.id.filmName);
                textView.setText(data.getStringExtra("movie"));
            }
        }
    }

    // Close button
    public void closeEvent(View view) {
        finishAndRemoveTask ();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("counters", counters);
        Log.d(LOG_TAG, "onSaveInstanceState");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, ON_START);
        counters[1]++;
        updateTextView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, ON_RESUME);
        counters[2]++;
        updateTextView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, ON_PAUSE);
        counters[3]++;
        updateTextView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, ON_STOP);
        counters[4]++;
        updateTextView();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        counters = savedInstanceState.getIntArray("counters");
        Log.d(LOG_TAG, "onRestoreInstanceState");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, ON_RESTART);
        counters[5]++;
        updateTextView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, ON_DESTROY);
        counters[6]++;
        updateTextView();
    }

    private void updateTextView() {
        TextView actionView = findViewById(R.id.actionView);
        actionView.setText("onCreate() " + Integer.toString(counters[0]) + '\n' +
                           "onStart() " + Integer.toString(counters[1]) + '\n' +
                           "onResume() " + Integer.toString(counters[2]) + '\n' +
                           "onPause() " + Integer.toString(counters[3]) + '\n' +
                           "onStop() " + Integer.toString(counters[4]) + '\n' +
                           "onRestart() " + Integer.toString(counters[5]) + '\n' +
                           "onDestroy() " + Integer.toString(counters[6]) + '\n');

    }

}
