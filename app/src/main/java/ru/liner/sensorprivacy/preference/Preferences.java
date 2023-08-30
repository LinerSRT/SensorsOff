package ru.liner.sensorprivacy.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 29.08.2023, 14:23
 */
public class Preferences implements IPreference {
    private final SharedPreferences preferences;
    @Nullable
    private static Preferences instance;

    public Preferences(@NonNull Context context, @NonNull String preferenceName) {
        this.preferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
    }

    public Preferences(@NonNull Context context){
        this(context, context.getPackageName());
    }

    public static void create(@NonNull Context context){
        if (instance == null)
            instance = new Preferences(context);
    }
    @NonNull
    public static Preferences get(){
        return Objects.requireNonNull(instance);
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
        } else if (defaultValue instanceof List) {
            List<Value> valueList = new Gson().fromJson(preferences.getString(key, ""), new Token<>(defaultValue.getClass()));
            return valueList == null ? (Value) new ArrayList<>() : (Value) valueList;
        }
        return defaultValue;
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
}
