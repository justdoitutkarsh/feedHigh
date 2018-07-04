package com.softup.utkarsh.feedhigh;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.database.FirebaseDatabase;

import static com.softup.utkarsh.feedhigh.App.CHANNEL_ID;


public class ExampleService extends Service {
    private FirebaseDatabase database;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String input = intent.getStringExtra("inputExtra");

        Intent notificationIntent = new Intent(this, GroupDiscussion.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        //Define sound URI
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        android.app.Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("feedHigh")
                .setContentText("New Subscription Message")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .setSound(soundUri)
                .setOngoing(false)
                .setAutoCancel(true)
                .build();


        startForeground(1, notification);

        //do heavy work on a background thread
        //stopSelf();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}