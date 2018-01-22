package ru.xidv.andrst.greatfiledownloader;

import android.support.v7.app.AppCompatActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloaderService.BCAST_ACT_DL_COMPLETE);
        registerReceiver(new ServReceiver(), filter);

        Button downloadButton = (Button) findViewById(R.id.downloadButton);
        downloadButton.setOnClickListener(this);

        mEditText = (EditText) findViewById(R.id.editText);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, DownloaderService.class);
        intent.putExtra(DownloaderService.DOWNLOAD_URL, mEditText.getText().toString());
        intent.setAction(DownloaderService.SERV_ACT_DOWNLOAD);
        startService(intent);
    }

    public class ServReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            assert action != null;
            if (action.equals(DownloaderService.BCAST_ACT_DL_COMPLETE)) {
                String url = intent.getStringExtra(DownloaderService.DOWNLOAD_URL);

                Toast.makeText(MainActivity.this, url,
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
