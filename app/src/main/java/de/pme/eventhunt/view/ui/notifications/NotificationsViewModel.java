package de.pme.eventhunt.view.ui.notifications;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import de.pme.eventhunt.model.documents.Event;
import de.pme.eventhunt.model.documents.EventUser;
import de.pme.eventhunt.model.documents.Notification;
import de.pme.eventhunt.model.repositories.NotificationRepository;
import de.pme.eventhunt.view.ui.home.EventListAdapter;

public class NotificationsViewModel extends AndroidViewModel {

    private final NotificationRepository notificationRepository;
    FirebaseFirestore db;
    FirebaseAuth auth;
    List<Notification> notifications;
    notificationAdapter adapter;
    List<Event> events;

    public NotificationsViewModel(@NonNull Application application) {
        super(application);
        notificationRepository = new NotificationRepository();
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public List<Notification>getNotifications() {
        db.collection("notification").whereEqualTo("userId", auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                notifications = task.getResult().toObjects(Notification.class);
                adapter.setNotifications(notifications);
            }
        });

        return notifications;
    }

    public void setAdapterList(notificationAdapter adapter)
    {
        this.adapter = adapter;
        getNotifications();
    }
}
