package de.pme.eventhunt.model.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import de.pme.eventhunt.model.documents.Event;
import de.pme.eventhunt.model.documents.EventUser;
import de.pme.eventhunt.model.documents.Notification;
import de.pme.eventhunt.model.documents.User;

public class
NotificationRepository {

    FirebaseFirestore db;
    FirebaseAuth auth;
    EventUserRepository eventUserRepository;

    String notificationText = "";

    public NotificationRepository()
    {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        eventUserRepository = new EventUserRepository();
    }
    List<Notification> notifications;

    public void addNotification(Notification notification)
    {
        db.collection("notification").document().set(notification).addOnFailureListener(e -> {
            Log.e("addNotification: ", e.toString());
        });
        // to add
        // whether the data is complete gets checked in the fragments
    }

    public void addNotificationsForEvent(int notificationType, Event event)
    {
        if(notificationType == 0)
        {
            notificationText = "\"" + event.getTitle() + "\" " + "has been updated";
        }
        else if(notificationType == 1)
        {
            notificationText = "\"" + event.getTitle() + "\" " + "has been cancelled";
        }
        else
        {
            notificationText = "\"" + event.getTitle() + "\" " + "notification";
        }

        db.collection("eventUser").whereEqualTo("eventId", event.getEventId()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<EventUser> eventUsers = task.getResult().toObjects(EventUser.class);

                        eventUsers.forEach(eventUser -> {
                            Notification notification = new Notification();
                            notification.setNotificationDescription(notificationText);
                            notification.setEventId(eventUser.getEventId());
                            notification.setUserId(eventUser.getUserId());

                            String createdAt = LocalDateTime.now().toString();
                            createdAt = createdAt.substring(0, createdAt.indexOf("."));
                            notification.setCreatedAt(createdAt);

                            notification.setEventImage(event.getImageSmallRef());

                            db.collection(Notification.collection).document(notification.getNotificationId()).set(notification);
                        });
                    }
                });
    }
    public void addNotificationsForSettings(int notificationType,String userId)
    {
        if(notificationType == 0)
        {
            notificationText = "you changed the Settings, your name is now shown on your profile !";
        }
        else if(notificationType == 1)
        {
            notificationText = "you changed the Settings, your name is now hidden on your profile !";
        }
        else if(notificationType == 2)
        {
            notificationText = "you changed the Settings, your email is now shown on your profile !";
        }
        else if(notificationType == 3)
        {
            notificationText = "you changed the Settings, your email is now hidden on your profile !";
        }
        else if(notificationType == 4)
        {
            notificationText = "you changed the Settings, your location is now shown on your profile !";
        }
        else if(notificationType == 5)
        {
            notificationText = "you changed the Settings, your location is now hidden on your profile !";
        }
        else if(notificationType == 6)
        {
            notificationText = "you changed the Settings, your age is now shown on your profile !";
        }
        else if(notificationType == 7)
        {
            notificationText = "you changed the Settings, your age is now hidden on your profile !";
        }

        db.collection("user").whereEqualTo("id", userId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<User> users = task.getResult().toObjects(User.class);
                        User user = users.get(0);
                            Notification notification = new Notification();
                            notification.setNotificationDescription(notificationText);
                            notification.setUserId(userId);

                            String createdAt = LocalDateTime.now().toString();
                            createdAt = createdAt.substring(0, createdAt.indexOf("."));
                            notification.setCreatedAt(createdAt);

                            notification.setEventImage(user.getImageSmallRef());

                            db.collection(Notification.collection).document(notification.getNotificationId()).set(notification);
                    }
                });
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
