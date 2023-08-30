package ru.liner.sensorprivacy.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;

/** @noinspection unused*/
public class Consumer<T> {
    /**
     * Operating value
     */
    @Nullable
    private final T value;

    /**
     * Default constructor
     * @param value that be processed
     */
    public Consumer(@Nullable T value) {
        this.value = value;
    }

    /**
     * Default constructor
     */
    public Consumer() {
        this.value = null;
    }

    /**
     * Static constructor
     * @param value that be processed
     * @return instance of consumer
     * @param <T> any type of objects
     */
    public static <T> Consumer<T> of(@Nullable T value) {
        return new Consumer<>(value);
    }

    /**
     * Static constructor
     * @return empty consumer
     * @param <T>  any type of objects
     */

    public static <T> Consumer<T> empty() {
        return new Consumer<>();
    }

    /**
     * Convert operating value to other type
     * @param function that convert value
     * @return empty consumer if  original operated value was null. Return consumer with converted value
     * @param <S> next operating object type
     */
    public <S> Consumer<S> next(@NonNull FunctionB<? super T, ? extends S> function) {
        try {
            return new Consumer<>(value == null ? null : function.apply(value));
        } catch (IOException e) {
            e.printStackTrace();
            return Consumer.empty();
        }
    }

    /**
     * Interact with value if its not null
     * @param consumer operating function
     */
    public void ifPresent(@NonNull Function<? super T> consumer){
        if(value != null) {
            try {
                consumer.apply(value);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Check if values is not null
     * @return true if value not null
     */
    public boolean isPresent(){
        return value != null;
    }

    /**
     * Return specified value if operating value is null
     * @param other new value
     * @return value
     */
    public T orElse(T other){
        return value == null ? other : value;
    }

    /**
     * Return operating value. This may return null, use {@link Consumer#orElse(Object)}
     * @return value
     */
    @Nullable
    public T get() {
        return value;
    }

    /**
     * Return value as boolean
     * @return value
     */
    public boolean asBoolean() {
        if (value == null)
            return false;
        if (value instanceof Boolean)
            return (Boolean) value;
        return false;
    }
    /**
     * Return value as int
     * @return value
     */
    public int asInt() {
        if (value == null)
            return 0;
        if (value instanceof Integer)
            return (Integer) value;
        return 0;
    }
    /**
     * Return value as long
     * @return value
     */
    public long asLong() {
        if (value == null)
            return 0;
        if (value instanceof Long)
            return (Long) value;
        return 0;
    }

    /**
     * Return value as double
     * @return value
     */
    public double asDouble() {
        if (value == null)
            return 0;
        if (value instanceof Double)
            return (Double) value;
        return 0;
    }

    /**
     * Return value as float
     * @return value
     */
    public float asFloat() {
        if (value == null)
            return 0;
        if (value instanceof Float)
            return (Float) value;
        return 0;
    }


    /**
     * Self written function to provide backward compatibility of {@link java.util.function.Function}
     * @param <T> any type
     */
    public interface Function<T>{
        void apply(@NonNull T input) throws IOException;
    }

    /**
     * Self written function to provide backward compatibility of {@link java.util.function.Function}
     * @param <T> any type
     */
    public interface FunctionB<T, R>{
        @NonNull
        R apply(@NonNull T input) throws IOException;
    }
}