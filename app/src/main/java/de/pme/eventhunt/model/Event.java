package de.pme.eventhunt.model;

import android.view.WindowInsets;

import androidx.annotation.NonNull;
import androidx.annotation.Size;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Objects;

@Entity
public class Event {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "eventId")
    private long eventId;

    private static int nextId=1;

    @NonNull
    @Size(min=3, max= 50)
    private String EventName;

    @Size( max=200 )
    private String Description ;

    public Event(String Name, String Description){
        this.EventName= Name;
        this.Description= Description;
        this.eventId=nextId;
        nextId++;
    }
    @NonNull
    private Date EventDate;

    @NonNull
    private String ContactEmail;
    private String EventLocation;
    private  EventType Type;

    public Event (String EventName, String Description, String ContactEmail, EventType Type,String EventLocation )
    {
        this.EventName= EventName;
        this.Description= Description;
        this.EventDate=EventDate;

        this.ContactEmail=ContactEmail;
        this.Type=Type;
    }

    @Override
    public String toString(){
        return EventName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return eventId == event.eventId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }

    public Event () {}

    public long getEventId() { return eventId; }
    public void setEventId(long eventId) { this.eventId = eventId; }

    public static int getNextId() { return nextId; }
    public static void setNextId(int nextId) { Event.nextId = nextId; }

    public String getEventName() {return EventName;}
    public void setEventName (String EventName) {this.EventName= EventName;}

    public String getDescription() {return Description;}
    public void setDescription(String description) {this.Description= description;}

    public Date getEventDate() {return EventDate;}
    public void setEventDate(Date EventDate) {this.EventDate= EventDate;}

    public String getContactEmail() {return ContactEmail;}
    public void setContactEmail(String contactEmail) {this.ContactEmail= contactEmail;}

    public String getEventLocation() { return EventLocation; }
    public void setEventLocation(String eventLocation) { this.EventLocation = eventLocation; }

    public EventType getType() { return Type;}
    public void setType(EventType type) { Type = type; }




}
