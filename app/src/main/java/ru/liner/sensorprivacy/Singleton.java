package ru.liner.sensorprivacy;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 30.08.2023, 13:33
 * @noinspection JavadocLinkAsPlainText
 */
public abstract class Singleton<Object> {
    private Object instance;

    protected abstract Object create();

    public final Object get(){
        synchronized (this){
            return instance == null ? instance = create() : instance;
        }
    }
}
