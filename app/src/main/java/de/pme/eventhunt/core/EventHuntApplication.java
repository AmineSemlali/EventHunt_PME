package de.pme.eventhunt.core;


import android.app.Application;
import de.pme.eventhunt.storage.KeyValueStore;




public class EventHuntApplication extends Application {

    private KeyValueStore store;

    public KeyValueStore getStore()
    {
        if( this.store == null )
            this.store = new KeyValueStore(this);

        return this.store;
    }
}
