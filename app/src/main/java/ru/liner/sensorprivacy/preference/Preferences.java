package ru.liner.sensorprivacy.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 29.08.2023, 14:23
 *
 * @noinspection JavadocLinkAsPlainText
 */
public class Preferences implements IPreference {
    private final SharedPreferences preferences;
    private final List<PreferenceListener<Object>> listenerList = new ArrayList<>();

    public Preferences(@NonNull Context context, @NonNull String preferenceName) {
        this.preferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        this.preferences.registerOnSharedPreferenceChangeListener((sharedPreferences, s) -> {
            if (s == null)
                return;
            for(PreferenceListener<Object> listener : listenerList)
                if(listener.key().equals(s)){
                    if (listener.defaultValueClass() != null) {
                        listener.onChanged(get(s, listener.defaultValueClass()));
                    } else {
                        listener.onChanged(get(s, listener.defaultValue()));
                    }
                }

        });
    }

    public Preferences(@NonNull Context context) {
        this(context, context.getPackageName());
    }


    @Override
    public <Value> void put(@NonNull String key, @NonNull Value value) {
        SharedPreferences.Editor editor = preferences.edit();
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof byte[]) {
            editor.putString(key, Base64.encodeToString((byte[]) value, Base64.DEFAULT));
        } else {
            editor.putString(key, new Gson().toJson(value));
        }
        editor.apply();
    }

    /**
     * @noinspection unchecked
     */
    @Override
    public <Value> Value get(@NonNull String key, @NonNull Value defaultValue) {
        if (defaultValue instanceof String) {
            return (Value) preferences.getString(key, (String) defaultValue);
        } else if (defaultValue instanceof Integer) {
            return (Value) Integer.valueOf(preferences.getInt(key, (Integer) defaultValue));
        } else if (defaultValue instanceof Boolean) {
            return (Value) Boolean.valueOf(preferences.getBoolean(key, (Boolean) defaultValue));
        } else if (defaultValue instanceof Float) {
            return (Value) Float.valueOf(preferences.getFloat(key, (Float) defaultValue));
        } else if (defaultValue instanceof Long) {
            return (Value) Long.valueOf(preferences.getLong(key, (Long) defaultValue));
        } else if (defaultValue instanceof byte[]) {
            return (Value) Base64.decode(preferences.getString(key, ""), Base64.DEFAULT);
        }
        return defaultValue;
    }


    @Override
    public <Value> List<Value> getList(@NonNull String key, @NonNull Class<Value> valueClass) {
        List<Value> valueList = new Gson().fromJson(preferences.getString(key, ""), new Token<>(valueClass));
        return valueList == null ? new ArrayList<>() : valueList;
    }

    @Override
    public <Value> Value get(@NonNull String key, @NonNull Class<Value> valueClass) {
        return new Gson().fromJson(get(key, ""), valueClass);
    }

    @Override
    public void clear(@NonNull String key) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }

    @Override
    public void clear() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    public boolean has(@NonNull String key) {
        return preferences.contains(key);
    }


    /** @noinspection unchecked*/
    @Override
    public <Value> void register(@NonNull PreferenceListener<Value> listener) {
        listenerList.add((PreferenceListener<Object>) listener);
    }

    /** @noinspection unchecked*/
    @Override
    public <Value> void unregister(@NonNull PreferenceListener<Value> listener) {
        listenerList.add((PreferenceListener<Object>) listener);
    }
}
