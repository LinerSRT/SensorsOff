package ru.liner.sensorprivacy.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;

import ru.liner.sensorprivacy.preference.Preferences;
import ru.liner.sensorprivacy.utils.Consumer;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 17.09.2023, 10:58
 * @noinspection JavadocLinkAsPlainText
 */
public class WindowService extends AccessibilityService {
    private Preferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = new Preferences(this);
        //TODO Check shizuku status here, now its not
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo serviceInfo = new AccessibilityServiceInfo();
        serviceInfo.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        serviceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        serviceInfo.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;
        setServiceInfo(serviceInfo);
        preferences.put("service_window_running", true);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED)
            Consumer.of(accessibilityEvent.getPackageName()).ifPresent(input -> {
                Intent intent = new Intent(BlockingService.WINDOW_CHANGE_ACTION);
                intent.putExtra(BlockingService.EXTRA_PACKAGE_NAME, input.toString());
                intent.putExtra(BlockingService.EXTRA_CLASS_NAME, accessibilityEvent.getClassName());
                sendBroadcast(intent);
            });
    }

    @Override
    public void onInterrupt() {
        preferences.put("service_window_running", false);
    }
}