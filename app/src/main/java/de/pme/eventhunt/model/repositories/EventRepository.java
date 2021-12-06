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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import de.pme.eventhunt.model.documents.Event;
import de.pme.eventhunt.model.documents.EventUser;
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
        db.collection(Event.collection).document(event.getEventId()).set(event)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                EventUser eventUser = new EventUser();
                eventUser.setEventId(event.getEventId());
                eventUser.setUserId(auth.getCurrentUser().getUid());
                eventUser.setJoinedAt(LocalDateTime.now().toString());
                db.collection(EventUser.collection).document().set(eventUser);
            }
        })
                .addOnFailureListener(e -> {
            Log.e("createEvent: ", e.toString());
        });

    }

}
