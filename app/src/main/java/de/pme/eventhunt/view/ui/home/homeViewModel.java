package de.pme.eventhunt.view.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import de.pme.eventhunt.storage.repository.EventRepository;
import de.pme.eventhunt.storage.repository.NotificationRepository;

public class homeViewModel extends AndroidViewModel {

    //private final EventRepository eventRepository;

    public homeViewModel(@NonNull Application application) {
        super(application);
        //this.eventRepository = EventRepository.getRepository(application);
    }
}
