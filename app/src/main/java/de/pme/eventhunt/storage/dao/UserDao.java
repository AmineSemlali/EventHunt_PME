package de.pme.eventhunt.storage.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;



import java.util.List;

import de.pme.eventhunt.model.User;

@Dao
public interface UserDao {

    @Insert
    long insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("DELETE FROM User")
    void deleteAll();

    @Query("SELECT count(*) FROM User")
    int count();

    @Query("SELECT * FROM User")
    List<User> getUsers();

    @Query("SELECT * FROM User ORDER BY firstName ASC")
    List<User> getUsersSortByFirstname();

    @Query("SELECT * FROM User ORDER BY lastName ASC")
    List<User> getUsersSortByLastname();

    @Query("SELECT * FROM User ORDER BY userId DESC LIMIT 1")
    User getLastEntry();

    @Query("SELECT * FROM User WHERE userId LIKE :search")
    User getUserForId(long search);

    @Query("SELECT * FROM User WHERE firstName LIKE :name")
    User getUserWithName(String name);

    @Query("SELECT * FROM User WHERE email LIKE :email")
    User getUserWithEMail(String email);

    @Query("SELECT securePassword FROM User WHERE email LIKE :email")
    String getSecuredPasswordWithEMail(String email);



}
