package com.shaheryarbhatti.polaroidapp.services;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class PolaroidFirebaseMessagingService extends FirebaseMessagingService {
    private final String TAG = "PolaFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Map<String, String> data = remoteMessage.getData();
        for (Map.Entry<String, String> pair : data.entrySet())
            Log.d(TAG, "onMessageReceived: key:" + pair.getKey() + " value:" + pair.getValue());
        Log.d(TAG, "onMessageReceived: ");
        Log.d("FROM", remoteMessage.getFrom());
//        sendNotification(notification, data);
    }
}
