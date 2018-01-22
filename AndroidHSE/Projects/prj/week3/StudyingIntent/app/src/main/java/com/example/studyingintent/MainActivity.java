package com.example.studyingintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;

public class MainActivity extends Activity {

    static final int PICK_CONTACT = 1;

    final String gmStreetViewPrefix = "google.streetview:cbll=";
    final String gm = "com.google.android.apps.maps";
    final String googleSearchPrefix = "http://www.google.com/#q=";
    final String gmSearchPrefix = "geo:0,0?q=";


    String[] locations = {"46.414382,10.013988", "-77.5542751,166.1616476", "36.0650956,-112.1371072",
                          "38.787638,-9.390717", "48.8045405,2.1201304",
                          "46.9246319,-121.502053", "28.0027257,86.8526279"};

    Random mRandom = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mapButton = findViewById(R.id.mapButton);

        mapButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Uri uri = Uri.parse(gmStreetViewPrefix + locations[mRandom.nextInt(locations.length)]);
                startMapActivity(uri);
                return true;
            }
        });
    }

    private String getRequestString() {
        EditText userText = findViewById(R.id.editText);
        return userText.getText().toString();
    }

    public void startMapActivity(Uri gmmIntentUri) {
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);     // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        mapIntent.setPackage(gm);                // Make the Intent explicit by setting the Google Maps package

        startActivity(mapIntent);                                            // Attempt to start an activity that can handle the Intent
    }

    public void browserActivity(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(googleSearchPrefix  + getRequestString()));
        startActivity(browserIntent);
    }

    public void mapActivity(View view) {

        try {

            // Create a Uri from an intent encoded string. Use the result to create an Intent.
            Uri gmmIntentUri = Uri.parse(gmSearchPrefix + URLEncoder.encode(getRequestString(), "UTF-8"));
            startMapActivity(gmmIntentUri);

        } catch (UnsupportedEncodingException e) {}

    }

    public void contactActivity(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);

    }


    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (reqCode == PICK_CONTACT && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();

// ContactsContract - The contract between the contacts provider and applications. Contains definitions for the supported URIs and columns.
// Contacts - Constants for the contacts table, which contains a record per aggregate of raw contacts representing the same person.
            String[] projection = new String[]{ContactsContract.Contacts.DISPLAY_NAME};

/*

CONTEXT - Interface to global information about an application environment.
This is an abstract class whose implementation is provided by the Android system.
It allows access to application-specific resources and classes, as well as up-calls for application-level
operations such as launching activities, broadcasting and receiving intents, etc.

getApplicationContext() - Returns the context for the entire application (the process all the Activities are running inside of).
Use this instead of the current Activity context if you need a context tied to the lifecycle of the entire application, not just the current Activity.

CURSOR - This interface provides random read-write access to the result set returned by a database query.

CONTENT RESOLVER - is class provides applications access to the content model.

query (Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) - Query the given URI, returning a Cursor over the result set.
 */

            Cursor cursor = getApplicationContext().getContentResolver().query(contactUri, projection,
                    null, null, null);

            if (cursor != null && cursor.moveToFirst())             {
                int columnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                String contactName = cursor.getString(columnIndex);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Selected contact")
                       .setMessage(contactName)
                       .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {}
                }).show();
            }

            cursor.close();
        }
    }
}
