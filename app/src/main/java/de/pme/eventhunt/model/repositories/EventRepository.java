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
import com.google.firebase.storage.FirebaseStorage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import de.pme.eventhunt.model.documents.Event;
import de.pme.eventhunt.model.documents.EventUser;
import de.pme.eventhunt.model.documents.Notification;
import de.pme.eventhunt.model.documents.User;

// class for managing event documents in firebase firestore

public class EventRepository {

    ////////////////////////attributes

    FirebaseAuth auth;
    FirebaseFirestore db;
    FirebaseStorage storage;
    NotificationRepository notificationRepository;
    List<Event> events;

    //////////////////////////// constructors
    public EventRepository(){
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        notificationRepository = new NotificationRepository();
    }

/* this method saves an event document in firebase fireStore,
also saves an EventUser document that contains both the event and user ids */

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


    public void deleteEvent(Event event)
    {
        notificationRepository.addNotificationsForEvent(1, event);

        db.collection(EventUser.collection).whereEqualTo("eventId", event.getEventId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                task.getResult().forEach(queryDocumentSnapshot -> {
                    db.document(queryDocumentSnapshot.getReference().toString()).delete();
                });
            }
        });

        storage.getReference(event.getImageSmallRef()).delete();
        storage.getReference(event.getImageLargeRef()).delete();
        db.collection(Event.collection).document(event.getEventId()).delete();
    }

}
