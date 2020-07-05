package com.tuyp.restaurantapp.Config;

import android.app.Notification;
import android.app.NotificationChannel;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.tuyp.restaurantapp.R;

public class NotificationManager {
    Notification notification;
    String id = "my_channel_02";
    String name_chanel = "chanel_2";
    android.app.NotificationManager notificationManager;
    NotificationCompat.Builder mBuilder;
    String description = "desc chanel 2";
    Context context;
    int importance = android.app.NotificationManager.IMPORTANCE_HIGH;

    public NotificationManager(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
        notificationManager = (android.app.NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setNotif(){

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(id,name_chanel,importance);
            notificationChannel.setDescription(description);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.enableVibration(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }



        mBuilder.setContentText("Terimakasih")
                .setChannelId(id)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(R.drawable.restaurant_logo)
                .setContentText("Pesanan kamu akan segera kami proses")
                .setContentTitle("Terimakasih !")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        notification = mBuilder.build();

        notificationManager.notify(1,notification);





    }
}
