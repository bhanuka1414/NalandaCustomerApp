package com.bp.nalandacustomerapp;

import com.bp.nalandacustomerapp.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.bp.nalandacustomerapp.HomeActivity;
import com.bp.nalandacustomerapp.services.DatabaseHelper;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by filipp on 5/23/2016.
 */
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private DatabaseHelper databaseHelper;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        databaseHelper = new DatabaseHelper(this);

        databaseHelper.saveMsg(remoteMessage.getData().get("message"), remoteMessage.getData().get("title"), remoteMessage.getData().get("subject"));

        showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("subject"), remoteMessage.getData().get("message"));
    }

    private void showNotification(String title, String subjrct, String message) {

        Intent i = new Intent(this, MyOrdersActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo12);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(subjrct)
                .setSmallIcon(R.drawable.ww)
                .setLargeIcon(largeIcon)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0, builder.build());
    }


}