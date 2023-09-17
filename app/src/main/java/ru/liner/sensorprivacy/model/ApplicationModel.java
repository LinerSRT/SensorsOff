package ru.liner.sensorprivacy.model;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 17.09.2023, 11:38
 * @noinspection JavadocLinkAsPlainText
 */
public class ApplicationModel {
    public String packageName;
    public String applicationName;
    public Drawable icon;
    public boolean isBlocked;

    public ApplicationModel(String packageName, String applicationName, Drawable icon, boolean isBlocked) {
        this.packageName = packageName;
        this.applicationName = applicationName;
        this.icon = icon;
        this.isBlocked = isBlocked;
    }

    @Override
    @NonNull
    public String toString() {
        return "ApplicationModel{" +
                "packageName='" + packageName + '\'' +
                ", applicationName='" + applicationName + '\'' +
                ", icon=" + icon +
                ", isBlocked=" + isBlocked +
                '}';
    }
}
