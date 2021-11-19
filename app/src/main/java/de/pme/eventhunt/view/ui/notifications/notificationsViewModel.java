package de.pme.eventhunt.view.ui.notifications;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import de.pme.eventhunt.model.Notification;
import de.pme.eventhunt.storage.repository.NotificationRepository;

public class notificationsViewModel extends AndroidViewModel {

    private final NotificationRepository notificationRepository;

    public notificationsViewModel(@NonNull Application application) {
        super(application);
        this.notificationRepository = NotificationRepository.getRepository(application);
    }
}
