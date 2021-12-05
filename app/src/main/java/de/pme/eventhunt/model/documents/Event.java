package de.pme.eventhunt.model.documents;


import de.pme.eventhunt.model.utilities.EventLocation;

public class Event {

    public static String collection = "event";

    public String getEventId() {
        return EventId;
    }

    public void setEventId(String eventId) {
        EventId = eventId;
    }

    private String EventId;
    private String CreatorId;
    private String Title;
    private String Description;
    private String Category;
    private EventLocation Location;
    private String StartTime;
    private String EndTime;

    private String ImageSmallRef;
    private String ImageLargeRef;


    public Event(String eventId, String creatorId, String title, String description,
                 String category, EventLocation location, String startTime,
                 String endTime, String imageSmallRef, String imageLargeRef)
    {
        this.EventId = eventId;
        this.CreatorId = creatorId;
        this.Title = title;
        this.Description = description;
        this.Category = category;
        this.Location = location;
        this.StartTime = startTime;
        this.EndTime = endTime;
        this.ImageSmallRef = imageSmallRef;
        this.ImageLargeRef = imageLargeRef;
    }

    public Event() {};




    public String getCreatorId() {
        return CreatorId;
    }

    public void setCreatorId(String creatorId) {
        CreatorId = creatorId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public EventLocation getLocation() {
        return Location;
    }

    public void setLocation(EventLocation location) {
        Location = location;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getImageSmallRef() {
        return ImageSmallRef;
    }

    public void setImageSmallRef(String imageSmallRef) {
        this.ImageSmallRef = imageSmallRef;
    }

    public String getImageLargeRef() {
        return ImageLargeRef;
    }

    public void setImageLargeRef(String imageLargeRef) {
        this.ImageLargeRef = imageLargeRef;
    }
}
