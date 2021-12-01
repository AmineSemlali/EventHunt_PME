package de.pme.eventhunt.model.documents;

public class Notification

{

    public static String collection = "notification";
    private String EventId;
//    private String NotificationType;
    private String NotificationDescription;
    private String CreatedAt;
    private String EventImage;



    public String getEventId() {
        return EventId;
    }

    public void setEventId(String eventId) {
        EventId = eventId;
    }

//    public String getNotificationType() {
//        return NotificationType;
//    }
//
//    public void setNotificationType(String notificationType) {
//        NotificationType = notificationType;
//    }
    public String getNotificationDescription() {
        return NotificationDescription ;
    }

    public void setNotificationDescription(String notificationDescription) {
        NotificationDescription = notificationDescription;
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
        this.CreatedAt = eventImage;
    }


}
