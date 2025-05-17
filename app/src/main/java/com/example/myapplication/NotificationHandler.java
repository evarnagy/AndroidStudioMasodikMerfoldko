package com.example.myapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHandler {
    private static final String CHANNEL_ID = "Question_not_channel";
    private final int NOTIFICATION_ID = 0;
    private NotificationManager mManager;
    private Context mContext;

    public NotificationHandler(Context context)
    {
        this.mContext = context;
        this.mManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);

        createChannel();
    }

    private void createChannel()
    {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return;

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"Qest Notification",NotificationManager.IMPORTANCE_DEFAULT);

        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(Color.RED);
        channel.setDescription("Notofication from Quest Not");
        this.mManager.createNotificationChannel(channel);
    }

    public void send(String message)
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setContentTitle("Querst app")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher_background);

        this.mManager.notify(NOTIFICATION_ID,builder.build());
    }

}
