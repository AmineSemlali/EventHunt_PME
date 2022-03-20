package de.pme.eventhunt.model.documents;


import java.util.UUID;

///////////////////// Notification class contains attributes for managing notifications

public class Notification

{

    ////////////////////////attributes
    public static String collection = "notification";
    private String NotificationId;
    private String EventId;
    private String UserId;
    private String NotificationDescription;
    private int NotificationCategory; // 0 means changed, 1 means deleted
    private String CreatedAt;
    private String EventImage;

////////////////////////////constructors

    public Notification() { NotificationId = UUID.randomUUID().toString(); };

    //////////////////////// getters and setters

    public String getEventId() {
        return EventId;
    }

    public void setEventId(String eventId) {
        EventId = eventId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getNotificationId() {
        return NotificationId;
    }

    public void setNotificationId(String notificationId) {
        NotificationId = notificationId;
    }

    public String getNotificationDescription() {
        return NotificationDescription ;
    }

    public void setNotificationDescription(String notificationDescription) {
        NotificationDescription = notificationDescription;
    }
    public int getNotificationCategory() {
        return NotificationCategory;
    }

    public void setNotificationCategory(int notificationCategory) {
        NotificationCategory = notificationCategory;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        this.CreatedAt = createdAt;
    }

    public String getEventImage() {
        return EventImage;
    }

    public void setEventImage(String eventImage) {
        this.EventImage = eventImage;
    }


}
