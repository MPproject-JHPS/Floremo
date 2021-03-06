package org.androidtown.Floremo.TodayFlower;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

import org.androidtown.Floremo.MainActivity;
import org.androidtown.Floremo.TodayFlower.TodayFlowerActivity;
import org.androidtown.Floremo.R;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = "FirebaseMsgService";
    private String msg, title;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.e(TAG, "onMessageReceived");
        title = remoteMessage.getNotification().getTitle();
        msg = remoteMessage.getNotification().getBody();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        SharedPreferences sharedPreferences = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        boolean switchOn = sharedPreferences.getBoolean("Key", false);
        if (switchOn) {
            Intent intent = new Intent(this, TodayFlowerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //알림 눌렀을때 행위
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setVibrate(new long[]{1, 1000})
                    .setContentIntent(contentIntent);
            notificationManager.notify(0, mBuilder.build());
        } else {

        }
    }
}

