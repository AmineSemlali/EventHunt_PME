package de.pme.eventhunt.model.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import de.pme.eventhunt.model.documents.Event;
import de.pme.eventhunt.model.documents.EventUser;
import de.pme.eventhunt.model.documents.Notification;
import de.pme.eventhunt.model.documents.User;

public class NotificationRepository {

    FirebaseFirestore db;
    FirebaseAuth auth;
    EventUserRepository eventUserRepository;

    public NotificationRepository()
    {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        eventUserRepository = new EventUserRepository();
    }


    public void addNotification(Notification notification)
    {
        db.collection("notification").document().set(notification).addOnFailureListener(e -> {
            Log.e("addNotification: ", e.toString());
        });
        // to add
        // whether the data is complete gets checked in the fragments
    }

    public void deleteNotification(String notificationID)
    {
        db.collection("notification").document(notificationID).delete().addOnFailureListener(e -> {
            Log.e("deleteNotification: ", e.toString());
        });
        // to add
        // whether the data is complete gets checked in the fragments
    }

    public List<Notification> getAllNotifications()
    {
        // to add
        // returns List<Notification>
        List<Notification> notifications = new ArrayList<Notification>();
        notifications = db.collection("notification").get().getResult().toObjects(Notification.class);
        return notifications;

    }


    public List<Notification>GetNotificationsForUser()
    {
        // Get all events of currently logged in user
        Task<QuerySnapshot> events = db.collection("eventUser").whereEqualTo("UserId", auth.getUid()).get();
        List<EventUser> eventUsers = events.getResult().toObjects(EventUser.class);

        // Go through each event and query the corresponding notifications
        // and only take the notifications created after the user joined
        List<Notification> notifications = new ArrayList<Notification>();
        eventUsers.forEach(eventUser -> {
            Task<QuerySnapshot> notificationsForEvent = db.collection(Notification.collection)
                    .whereEqualTo("EventId", eventUser.getEventId())
                    .whereGreaterThanOrEqualTo("CreatedAt", eventUser.getJoinedAt()).get();


            List<Notification> eventNotifications = notificationsForEvent.getResult().toObjects(Notification.class);
            notifications.addAll(eventNotifications);
        });

        return notifications;
    }
}
