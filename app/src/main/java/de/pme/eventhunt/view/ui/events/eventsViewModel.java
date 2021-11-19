package de.pme.eventhunt.view.ui.events;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class eventsViewModel extends AndroidViewModel {
    //private final EventRepository eventRepository;

    public eventsViewModel(@NonNull Application application) {
        super(application);
        //this.eventRepository = EventRepository.getRepository(application);
    }
}
