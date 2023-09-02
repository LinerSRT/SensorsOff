package ru.liner.sensorprivacy.preference;

import androidx.annotation.NonNull;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 29.08.2023, 14:18
 * @noinspection JavadocLinkAsPlainText
 */
public interface IPreference {
    <Value> void put(@NonNull String key, @NonNull Value value);
    <Value> Value get(@NonNull String key, @NonNull Value defaultValue);
    <Value> Value get(@NonNull String key, @NonNull Class<Value> valueClass);
    void clear(@NonNull String key);
    void clear();
    boolean has(@NonNull String key);
    <Value> void register(@NonNull PreferenceListener<Value> listener);
    <Value> void unregister(@NonNull PreferenceListener<Value> listener);
}
