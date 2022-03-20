package de.pme.eventhunt.model.documents;


///////////// Class for managing m:n relation between User and Event

public class EventUser {

    ////////////////////////attributes
    public static String collection = "eventUser";

    private String UserId;
    private String EventId;
    private String JoinedAt;

    /////////////////////// getters and setters

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getEventId() {
        return EventId;
    }

    public void setEventId(String eventId) {
        EventId = eventId;
    }

    public String getJoinedAt() {
        return JoinedAt;
    }

    public void setJoinedAt(String joinedAt) {
        JoinedAt = joinedAt;
    }

}
