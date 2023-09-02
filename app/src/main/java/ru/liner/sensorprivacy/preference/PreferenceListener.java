package ru.liner.sensorprivacy.preference;

import androidx.annotation.NonNull;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 03.09.2023, 0:24
 * @noinspection JavadocLinkAsPlainText
 */
public interface PreferenceListener<Value> {
    @NonNull
    String key();
    void onChanged(@NonNull Value newValue);

    @NonNull
    Value defaultValue();

    default Class<Value> defaultValueClass() {
        return null;
    }
}
