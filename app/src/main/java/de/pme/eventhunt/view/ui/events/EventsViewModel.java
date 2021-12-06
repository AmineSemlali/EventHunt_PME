package de.pme.eventhunt.view.ui.events;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import de.pme.eventhunt.model.documents.Event;
import de.pme.eventhunt.model.documents.EventUser;
import de.pme.eventhunt.view.ui.filter_event.FilterEventViewModel;
import de.pme.eventhunt.view.ui.home.EventListAdapter;

public class EventsViewModel extends AndroidViewModel {

    FirebaseFirestore db;
    FirebaseAuth auth;
    MyEventListAdapter adapter;

    boolean isJoinedList = true;


    List<Event> events = new ArrayList<Event>();

    public EventsViewModel(@NonNull Application application) {
        super(application);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public void setAdapterListJoined(MyEventListAdapter adapter) {
        this.adapter = adapter;
        getJoinedEvents();
    }

    public void setAdapterListOwn(MyEventListAdapter adapter) {
        this.adapter = adapter;
        getOwnEvents();
    }

    public void getJoinedEvents()
    {
        events = new ArrayList<>();
        db.collection("eventUser").whereEqualTo("userId", auth.getCurrentUser().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<EventUser> eventUsers = task.getResult().toObjects(EventUser.class);
                        if(eventUsers.isEmpty())    adapter.setEventList(events);


                        eventUsers.forEach(eventUser -> {
                            db.collection("event").whereEqualTo("eventId", eventUser.getEventId()).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            List<Event> eventQuery = task.getResult().toObjects(Event.class);
                                            Event event = eventQuery.get(0);

                                            events.add(event);
                                            adapter.setEventList(events);
                                        }
                                    });
                        });

                    }
                });

        isJoinedList = true;
    }

    public void getOwnEvents()
    {
        db.collection("event").whereEqualTo("creatorId", auth.getCurrentUser().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        events = task.getResult().toObjects(Event.class);
                        adapter.setEventList(events);
                    }
                });
        isJoinedList = false;
    }
}
