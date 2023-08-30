package ru.liner.sensorprivacy.service;

import android.graphics.drawable.Icon;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;

import androidx.annotation.CallSuper;

import ru.liner.sensorprivacy.utils.Consumer;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 30.08.2023, 14:44
 */
public abstract class QSTile extends TileService {

    protected abstract boolean isEnabled();

    protected abstract void setIsEnabled(boolean isEnabled);

    protected abstract boolean shouldActivate();

    protected abstract Icon getActiveIcon();

    protected abstract Icon getInactiveIcon();

    protected abstract Icon getUnavailableIcon();


    @Override
    @CallSuper
    public void onStartListening() {
        super.onStartListening();
        refresh();
    }

    public void refresh() {
        final int state = !shouldActivate() ? Tile.STATE_UNAVAILABLE : isEnabled() ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE;
        switch (state) {
            case Tile.STATE_ACTIVE:
                getQsTile().setIcon(getActiveIcon());
                break;
            case Tile.STATE_INACTIVE:
                getQsTile().setIcon(getInactiveIcon());
                break;
            case Tile.STATE_UNAVAILABLE:
                getQsTile().setIcon(getUnavailableIcon());
                break;
        }
        getQsTile().setState(state);
        getQsTile().updateTile();
    }

    @Override
    @CallSuper
    public void onClick() {
        Log.d("TAGTAG", "Enabled: "+isEnabled());

        setIsEnabled(!isEnabled());
        refresh();
    }
}
