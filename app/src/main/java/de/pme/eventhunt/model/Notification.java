package de.pme.eventhunt.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.LocalDate;

import de.pme.eventhunt.model.utilities.LocalDateConverter;

@Entity
public class Notification {

    @Ignore
    public static final String LOG_TAG = "Notification";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "notificationId")
    private long notificationId;

    @NonNull
    @ColumnInfo(name = "eventId")
    private long eventId;

    @NonNull
    @ColumnInfo(name = "notificationType")
    private int notificationType;

    @NonNull
    @ColumnInfo(name = "created")
    @TypeConverters(LocalDateConverter.class)
    private LocalDate created;



    public long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(long notificationId) {
        this.notificationId = notificationId;
    }


    @NonNull
    public long getEventId() {
        return eventId;
    }

    public void setEventId(@NonNull long eventId) {
        this.eventId = eventId;
    }

    @NonNull
    public int getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(@NonNull int notificationType) {
        this.notificationType = notificationType;
    }

    @NonNull
    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(@NonNull LocalDate created) {
        this.created = created;
    }

}
