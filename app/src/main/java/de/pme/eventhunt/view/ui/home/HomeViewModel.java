package de.pme.eventhunt.view.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import de.pme.eventhunt.model.documents.Event;
import de.pme.eventhunt.model.repositories.EventRepository;

public class HomeViewModel extends AndroidViewModel {

    private final EventRepository eventRepository;

    FirebaseFirestore db;
    List<Event> events;
    EventListAdapter adapter;

    public HomeViewModel(@NonNull Application application) {

        super(application);
        eventRepository = new EventRepository();
        db = FirebaseFirestore.getInstance();
    }

    public List<Event> getAllEvents()
    {
        db.collection("event").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                events = task.getResult().toObjects(Event.class);
                adapter.setEventList(events);
            }
        });

        return events;
    }

    public void setAdapterList(EventListAdapter adapter)
    {
        this.adapter = adapter;
        getAllEvents();
    }
}
