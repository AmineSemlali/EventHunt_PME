package de.pme.eventhunt.core;


import android.app.Application;
import android.util.Log;

import java.util.List;

import de.pme.eventhunt.storage.KeyValueStore;


public class EventHuntApplication extends Application {

    private KeyValueStore store;

    @Override
    public void onCreate() {
        super.onCreate();

    }


    public KeyValueStore getStore()
    {
        if( this.store == null )
            this.store = new KeyValueStore(this);

        return this.store;
    }

}


