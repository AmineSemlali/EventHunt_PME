package de.pme.eventhunt.storage.repository;

import android.content.Context;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import de.pme.eventhunt.model.Notification;
import de.pme.eventhunt.storage.dao.NotificationDao;
import de.pme.eventhunt.storage.database.EventHuntDatabase;

public class NotificationRepository {
    public static final String LOG_TAG = "NotificationRepository";

    private NotificationDao notificationDao;

    public NotificationRepository( Context context ) {
        EventHuntDatabase db = EventHuntDatabase.getDatabase( context );
        this.notificationDao = db.notificationDao();
    }

    public List<Notification> getNotifications()
    {
        return this.query( () -> this.notificationDao.getNotifications() );
    }

    private List<Notification> query( Callable<List<Notification>> query )
    {
        try {
            return EventHuntDatabase.query( query );
        }
        catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public void insert(Notification notification) {
        notification.setCreated(LocalDate.now());
        EventHuntDatabase.execute( () -> notificationDao.insert( notification ) );
    }


}
