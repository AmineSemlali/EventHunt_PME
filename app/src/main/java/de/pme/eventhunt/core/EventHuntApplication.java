package de.pme.eventhunt.core;


import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;

import de.pme.eventhunt.model.documents.Event;
import de.pme.eventhunt.storage.KeyValueStore;


public class EventHuntApplication extends Application {

    private KeyValueStore store;
    private boolean autoLogin = true;

    @Override
    public void onCreate() {
        super.onCreate();

        if(autoLogin)
        {
            loginAdmin();
        }


    }

    private void loginAdmin() {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.signInWithEmailAndPassword("skdk2@web.de", "joker123");

    }


    public KeyValueStore getStore()
    {
        if( this.store == null )
            this.store = new KeyValueStore(this);

        return this.store;
    }

}


