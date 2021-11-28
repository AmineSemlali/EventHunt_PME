package de.pme.eventhunt.model.repositories;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import de.pme.eventhunt.model.documents.Event;
import de.pme.eventhunt.model.documents.User;

public class EventRepository {
    FirebaseAuth auth;
    FirebaseFirestore db;

    public EventRepository(){
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }


    public void createEvent(Event event)
    {
        db.collection("event").document().set(event).addOnFailureListener(e -> {
            Log.e("createEvent: ", e.toString());
        });

    }

}
