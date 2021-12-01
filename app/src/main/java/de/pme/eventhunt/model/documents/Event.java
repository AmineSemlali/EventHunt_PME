package de.pme.eventhunt.model.documents;


import android.util.Log;

public class Event {

    public static String collection = "event";

    private String CreatorId;
    private String Title;
    private String Description;
    private String Category;
    private String Location;
    private String StartTime;
    private String EndTime;

    private String ImageSmallRef;
    private String ImageLargeRef;


    public Event(String creatorId, String title, String description,
                 String category, String location, String startTime,
                 String endTime, String imageSmallRef, String imageLargeRef)
    {
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

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
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
