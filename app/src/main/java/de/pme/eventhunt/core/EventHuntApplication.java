package de.pme.eventhunt.core;


import android.app.Application;
import android.util.Log;

import java.util.List;

import de.pme.eventhunt.storage.KeyValueStore;
import de.pme.eventhunt.model.User;
import de.pme.eventhunt.storage.repository.NotificationRepository;
import de.pme.eventhunt.storage.repository.UserRepository;


public class EventHuntApplication extends Application {

    private KeyValueStore store;

    @Override
    public void onCreate() {
        super.onCreate();

        testDatabase();
    }


    public KeyValueStore getStore()
    {
        if( this.store == null )
            this.store = new KeyValueStore(this);

        return this.store;
    }

    private void testDatabase() {

        // Create Repo instance - which in turn will init the Contact DB
        UserRepository userRepository = new UserRepository( this );

        // Query all contacts and log them
        List<User> allUsers = userRepository.getUsers();
        Log.i("LOG_TAG", allUsers.toString() );

    }
}


