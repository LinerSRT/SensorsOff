package ru.liner.sensorprivacy.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 14.09.2023, 9:29
 * @noinspection JavadocLinkAsPlainText
 */
public abstract class Broadcast extends BroadcastReceiver {
    @NonNull
    private final Context context;
    private final List<String> actionList;
    private boolean isRegistered;

    public Broadcast(@NonNull Context context, @NonNull String... actions) {
        this.context = context;
        this.actionList = Arrays.asList(actions);
        this.isRegistered = false;
    }

    public void register(){
        if(isRegistered)
            return;
        IntentFilter intentFilter = new IntentFilter();
        for(String action:actionList)
            intentFilter.addAction(action);
        context.registerReceiver(this, intentFilter);
        isRegistered = true;
    }

    public void unregister(){
        if(!isRegistered)
            return;
        context.unregisterReceiver(this);
        isRegistered = false;
    }

    @Override
    public final void onReceive(Context context, Intent intent) {
        handle(context, intent);
    }

    public abstract void handle(@NonNull Context context, @NonNull Intent intent);
}
