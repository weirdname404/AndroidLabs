package ru.xidv.andrst.greatfiledownloader;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;

/**
 * Created by Александр on 28.11.2017.
 */

public class DownloaderService extends Service {
    HandlerThread thread;

    public static String BCAST_ACT_DL_COMPLETE = "BCAST_ACT_DL_COMPLETE";
    public static String SERV_ACT_DOWNLOAD = "SERV_ACT_DOWNLOAD";
    public static String DOWNLOAD_URL = "DOWNLOAD_URL";

    public DownloaderService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        thread = new HandlerThread("ServiceThread");
        thread.start();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        String action = intent.getAction();
        assert action != null;
        if (action.equals(SERV_ACT_DOWNLOAD)) {

            // wrap the runnable into a Handler job
            Runnable runner = new Runnable() {
                @Override
                public void run() {
                    // obtain an extra parameter named "url" passed to the service
                    String url = intent.getStringExtra(DOWNLOAD_URL);

                    // performs "downloading"; use a special DownloadHelper class
                    // handling this operation
                    DownloadHelper.download(url);

                    // use a dedicated method to show a "Download Complete" notification
                    showDlNotification(url);

                    // use a dedicated method to send a broadcast message back to the
                    // application to inform it that the service are done
                    sendBroadcastMsg(url);
                }

                void showDlNotification(String url) {
                    Notification n = new Notification.Builder(DownloaderService.this)
                            .setContentTitle("Download complete")
                            .setContentText(url)
                            .setSmallIcon(android.R.drawable.stat_sys_download_done)
                            .setAutoCancel(true)
                            .build();

                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                    assert notificationManager != null;
                    notificationManager.notify(0, n);
                }

                void sendBroadcastMsg(String url) {
                    Intent intent = new Intent();
                    intent.setAction(BCAST_ACT_DL_COMPLETE);
                    intent.putExtra(DOWNLOAD_URL, url);
                    sendBroadcast(intent);
                }
            };

            Handler handler = new Handler(thread.getLooper());
            handler.post(runner);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
