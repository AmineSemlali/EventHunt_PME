package de.pme.eventhunt.storage.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.pme.eventhunt.model.Notification;

@Dao
public interface NotificationDao {

    @Insert
    long insert(Notification notification);

    @Update
    void update(Notification notification);

    @Delete
    void delete(Notification notification);

    @Query("DELETE FROM Notification")
    void deleteAll();

    @Query("SELECT count(*) FROM Notification")
    int count();

    @Query("SELECT * FROM Notification")
    List<Notification> getNotifications();

    @Query("SELECT * FROM Notification ORDER BY created ASC")
    List<Notification> getNotificationsSortByDate();

    @Query("SELECT * FROM Notification ORDER BY notificationType ASC")
    List<Notification> getNotificationsSortByType();

    @Query("SELECT * FROM Notification WHERE eventId LIKE :search")
    Notification getNotificationOfEvent(long search);




}