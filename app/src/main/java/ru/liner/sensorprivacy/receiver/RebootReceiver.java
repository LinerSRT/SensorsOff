package ru.liner.sensorprivacy.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ru.liner.sensorprivacy.service.BlockingService;
import ru.liner.sensorprivacy.utils.Consumer;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 17.09.2023, 11:05
 */
public class RebootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Consumer.of(intent.getAction())
                .ifPresent(input -> {
                    if (input.equals(Intent.ACTION_BOOT_COMPLETED)) {
                        Intent service = new Intent(context, BlockingService.class);
                        context.startForegroundService(service);
                    }
                });
    }
}