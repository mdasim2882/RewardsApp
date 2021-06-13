package com.creator.rewardsapp.Service;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.creator.rewardsapp.MainActivity;
import com.creator.rewardsapp.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyNotificationService extends FirebaseMessagingService {
    public static final String SET_Settings = "com.example.growfast_preferences";
    SharedPreferences sharedPreferences;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        sharedPreferences = getSharedPreferences(SET_Settings, Context.MODE_PRIVATE);

        Map<String, String> data = remoteMessage.getData();
//        String questionTitle = data.get("startDeclaration").toString();
//        Toast.makeText(this,questionTitle,Toast.LENGTH_LONG);
//        Log.d("check", "onMessageReceived: "+questionTitle);
        showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
    }


    private void showNotification(String title, String message) {
        PendingIntent gotoActivity = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        Notification notify = new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setAutoCancel(true)
                .setContentIntent(gotoActivity)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notify);


    }


}