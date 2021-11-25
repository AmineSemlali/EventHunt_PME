package de.pme.eventhunt.core;


import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;

import de.pme.eventhunt.model.documents.Event;
import de.pme.eventhunt.storage.KeyValueStore;


public class EventHuntApplication extends Application {

    private KeyValueStore store;

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Event event = new Event();
        event.setTitle("meetMe");
        event.setCategory("Meetup");
        event.setCreatorId("HQGG6jvwoNRhriubjUloGiN6O2t1");
        event.setDescription("this is an event");
        event.setStartTime(LocalDate.now().toString());
        event.setEndTime(LocalDate.now().toString());
        event.setStartTime(LocalDate.now().toString());

        db.collection(Event.collection).document().set(event).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Create Event failure: ", e.toString());
            }
        });


    }


    public KeyValueStore getStore()
    {
        if( this.store == null )
            this.store = new KeyValueStore(this);

        return this.store;
    }

}


