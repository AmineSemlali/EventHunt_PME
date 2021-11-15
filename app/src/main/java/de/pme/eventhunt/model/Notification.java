package de.pme.eventhunt.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Notification {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "notificationId")
    private long notificationId;

    public long getNotificationId() {
        return notificationId;
    }


    public void setNotificationId(long notificationId) {
        this.notificationId = notificationId;
    }
}
