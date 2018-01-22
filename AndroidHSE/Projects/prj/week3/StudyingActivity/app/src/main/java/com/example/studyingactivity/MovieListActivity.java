package com.example.studyingactivity;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.content.Intent;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import java.util.ArrayList;

public class MovieListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        // Determining user age
        String ageStr = getIntent().getStringExtra("age");
        String[] ages = ageStr.split("–");
        int age = Integer.parseInt(ages[0]);
        boolean isUnderage = (age < 18);

        String[] allMovieTitles = getResources().getStringArray(R.array.funny_movies);              // Declare array of available movie titles from strings.xml
        ArrayList<String> moviesToShow = new ArrayList<>();                                         // Declaring of ArrayList class

        for (String movieTitle: allMovieTitles) {
            if (!(isUnderage && movieTitle.contains("XXX"))) {
                moviesToShow.add(movieTitle);
            }
        }

        ListView listView = (ListView) findViewById(R.id.movies_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, moviesToShow); // Setting adapter
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("movie", ((TextView)view).getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
