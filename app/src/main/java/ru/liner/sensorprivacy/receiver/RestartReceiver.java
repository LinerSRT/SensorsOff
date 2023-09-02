package ru.liner.sensorprivacy.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ru.liner.sensorprivacy.service.SensorService;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 02.09.2023, 23:11
 * @noinspection JavadocLinkAsPlainText
 */
public class RestartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startForegroundService(new Intent(context, SensorService.class));
    }
}
