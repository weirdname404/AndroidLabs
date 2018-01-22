package com.example.aolsvt.myapplication;

import android.content.DialogInterface;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.AbstractList;

public class MainActivity  extends AppCompatActivity implements GestureOverlayView.OnGesturePerformedListener {

    private GestureLibrary mGestureLibrary;

    @Override
    public void onGesturePerformed(GestureOverlayView gestureOverlayView, Gesture gesture) {
        Log.d(getClass().getSimpleName(), String.format("Gesture performed: %s", gesture));
        AbstractList<Prediction> predictions = mGestureLibrary.recognize(gesture);
        if (!predictions.isEmpty()) {
            Prediction prediction = predictions.get(0);
            TextView textView = (TextView) findViewById(R.id.text_view);
            if (textView != null) {
                if (prediction.score > 1.0) {
                    textView.setText(prediction.name);
                } else {
                    textView.setText(R.string.unknown_gesture);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGestureLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if (!mGestureLibrary.load()) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.gesture_library_load_fail_dialog_title)
                    .setMessage(R.string.gesture_library_load_fail_dialog_message)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Log.d(getClass().getSimpleName(), "Terminating app");
                            finish();
                        }
                    }).show();
        }
        GestureOverlayView gesturePanel = (GestureOverlayView) findViewById(R.id.gesture_panel);
        if (gesturePanel != null) {
            gesturePanel.addOnGesturePerformedListener(this);
        }
    }
}