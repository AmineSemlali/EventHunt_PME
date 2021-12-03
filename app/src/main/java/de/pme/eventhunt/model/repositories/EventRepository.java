package de.pme.eventhunt.model.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import de.pme.eventhunt.model.documents.Event;
import de.pme.eventhunt.model.documents.Notification;
import de.pme.eventhunt.model.documents.User;

public class EventRepository {
    FirebaseAuth auth;
    FirebaseFirestore db;

    List<Event> events;


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
