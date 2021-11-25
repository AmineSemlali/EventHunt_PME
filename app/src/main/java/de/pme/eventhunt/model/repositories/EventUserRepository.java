package de.pme.eventhunt.model.repositories;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.List;

import de.pme.eventhunt.model.documents.Event;
import de.pme.eventhunt.model.documents.EventUser;
import de.pme.eventhunt.model.documents.User;



// Repository for managing m:n relation between User and Event
public class EventUserRepository {
    FirebaseFirestore db;
    FirebaseAuth auth;

    public EventUserRepository(){
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public void AddUserToEvent (String eventId)
    {
        EventUser eventUser = new EventUser();

        eventUser.setEventId(eventId);
        eventUser.setUserId(auth.getCurrentUser().getUid());
        eventUser.setJoinedAt(LocalDate.now().toString());

        db.collection(EventUser.collection).document().set(eventUser).addOnFailureListener(e -> {
            Log.e("AddUserToEvent: ", e.toString());
        });
    }

    public List<Event> GetEventsOfUser (String userId)
    {
        // Get all events where the userId fits
        Task<QuerySnapshot> eventQuery = db.collection(EventUser.collection).whereEqualTo("EventId", userId).get();

        //Convert to event class
        List<Event> events = eventQuery.getResult().toObjects(Event.class);

        return events;
    }

    public List<User> GetUsersOfEvent (String eventId)
    {
        // Get all events where the userId fits
        Task<QuerySnapshot> userQuery = db.collection(EventUser.collection).whereEqualTo("UserId", eventId).get();

        //Convert to event class
        List<User> users = userQuery.getResult().toObjects(User.class);

        return users;
    }
}
