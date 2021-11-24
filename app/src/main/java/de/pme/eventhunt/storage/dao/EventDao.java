package de.pme.eventhunt.storage.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;


import java.util.Date;
import java.util.List;

import de.pme.eventhunt.model.Event;

@Dao
public interface EventDao {

    @Insert
    long insert(Event Event);

    @Update
    void update(Event Event);

    @Delete
    void delete(Event Event);

    @Query("DELETE FROM Event")
    void deleteAll();

    @Query("SELECT count(*) FROM Event")
    int count();

    @Query("SELECT * FROM Event")
    List<Event> getEvents();

    @Query("SELECT * FROM Event ORDER BY EventName DESC")
    List<Event> getEventSortByNameDesc();

    @Query("SELECT * FROM Event ORDER BY EventName ASC")
    List<Event> getEventsSortByNameAsc();

    @Query("SELECT * FROM Event ORDER BY EventDate DESC")
    List<Event> getEventSortByDateDesc();
    @Query("SELECT * FROM Event ORDER BY EventDate ASC")
    List<Event> getEventSortByDateAsc();

    @Query("SELECT * FROM Event ORDER BY eventId DESC LIMIT 1")
    Event getLastEntry();

    @Query("SELECT * FROM event WHERE eventId LIKE :search")
    Event getEventForId(long search);

    @Query("SELECT * FROM Event WHERE EventName LIKE :name")
    Event getEventWithName(String name);

    @Query("SELECT * FROM Event WHERE EventLocation LIKE :Location")
    Event getEventWithLocation(String Location);

    @Query("SELECT * FROM Event WHERE Type LIKE :Type")
    Event getEventWithType(String Type);

    @Query("SELECT * FROM Event WHERE EventDate LIKE :Date")
    Event getEventWithDate(Date Date);


}
