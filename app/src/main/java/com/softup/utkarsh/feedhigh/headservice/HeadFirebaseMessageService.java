package com.softup.utkarsh.feedhigh.headservice;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

public class HeadFirebaseMessageService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = HeadFirebaseMessageService.class.getSimpleName();

    private LocalBroadcastManager broadcaster;

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        String body2 = remoteMessage.getNotification().getBody();
        String body3 = remoteMessage.getNotification().getBody();
        String body4 = remoteMessage.getNotification().getBody();
        String body5 = remoteMessage.getNotification().getBody();
        Log.i(TAG, "onMessageReceived: title : "+title);

        Intent intent = new Intent("MyData");
        intent.putExtra("title", title);
        intent.putExtra("body", body);
        intent.putExtra("body2", body2);
        intent.putExtra("body3", body3);
        intent.putExtra("body4", body4);
        intent.putExtra("body5", body5);

        //uncomment to fetch separate data like key or value of a note
     /*   for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Log.d(TAG, "key, " + key + " value " + value);
        }*/

        broadcaster.sendBroadcast(intent);
    }
}
