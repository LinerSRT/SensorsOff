package ru.liner.sensorprivacy.service;

import static androidx.core.app.NotificationCompat.CATEGORY_STATUS;
import static androidx.core.app.NotificationCompat.PRIORITY_MIN;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.CallSuper;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.concurrent.TimeUnit;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 27.08.2023, 14:06
 *
 * @noinspection JavadocLinkAsPlainText
 */

public abstract class ImmortalService extends Service {
    private AlarmManager alarmManager;

    @Override
    @CallSuper
    public void onCreate() {
        super.onCreate();
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, getChannelID())
                .setSmallIcon(getNotificationIcon())
                .setOngoing(true)
                .setPriority(PRIORITY_MIN)
                .setCategory(CATEGORY_STATUS)
                .setAutoCancel(true);
        NotificationChannel notificationChannel = new NotificationChannel(getChannelID(), getChannelID(), NotificationManager.IMPORTANCE_MIN);
        notificationBuilder.setChannelId(getChannelID());
        notificationChannel.setDescription(getChannelDescription());
        notificationChannel.enableVibration(false);
        notificationManager.createNotificationChannel(notificationChannel);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(9, notificationBuilder.build(), ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE);
        } else {
            startForeground(9, notificationBuilder.build());
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    @CallSuper
    public void onDestroy() {
        super.onDestroy();
        alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5),
                PendingIntent.getBroadcast(
                        this,
                        0,
                        new Intent(this, getRestartReceiverClass()),
                        PendingIntent.FLAG_IMMUTABLE
                )
        );
    }

    @NonNull
    public abstract String getChannelID();

    @NonNull
    public abstract String getChannelDescription();

    @DrawableRes
    public abstract int getNotificationIcon();

    @NonNull
    public abstract Class<? extends BroadcastReceiver> getRestartReceiverClass();
}
