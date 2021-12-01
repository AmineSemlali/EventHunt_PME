package de.pme.eventhunt.view.ui.notifications;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import de.pme.eventhunt.model.documents.Notification;
import de.pme.eventhunt.model.repositories.NotificationRepository;

public class NotificationsViewModel extends AndroidViewModel {

    private final NotificationRepository notificationRepository;

    public NotificationsViewModel(@NonNull Application application) {
        super(application);
        notificationRepository = new NotificationRepository();
    }
    public List<Notification>getNotifications() {
        return this.notificationRepository.GetNotificationsForUser();
    }
}
