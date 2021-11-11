package de.pme.eventhunt.storage.repository;



import android.content.Context;
import android.os.Debug;
import android.util.Log;

import de.pme.eventhunt.model.User;
import de.pme.eventhunt.model.utilities.PasswordHashing;
import de.pme.eventhunt.storage.dao.UserDao;
import de.pme.eventhunt.storage.database.EventHuntDatabase;
import de.pme.eventhunt.storage.repository.ObjectNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class UserRepository {
    public static final String LOG_TAG = "UserRepository";

    private UserDao userDao;

    public UserRepository( Context context ) {
        EventHuntDatabase db = EventHuntDatabase.getDatabase( context );
        this.userDao = db.userDao();
    }

    public List<User> getUsers()
    {
        return this.query( () -> this.userDao.getUsers() );
    }

    private List<User> query( Callable<List<User>> query )
    {
        try {
            return EventHuntDatabase.query( query );
        }
        catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public void insert(User user) {
        user.setCreated(LocalDate.now());
        user.setUpdated(user.getCreated());
        user.setVersion(1);
        EventHuntDatabase.execute( () -> userDao.insert( user ) );
    }

    /**
     * Find user with name if the user is found return its id.
     * if the user is not found return -1
     *
     * @param name the name of the user
     * @return the id of the searched user
     */
    public long getUserIdFromName(String name) {
        User user = userDao.getUserWithName(name);

        if(user != null)
        {
            return user.getUserId();
        }
        else{
            Log.w(LOG_TAG, "User with that name not found on database");
            return -1;
        }
    }

    /**
     * Find user with email if the user is found and the given password is right return its id.
     * if the user is not found return -1
     * if the user is found but the password is wrong return -2
     *
     * @param email    the email
     * @param password the password
     * @return the id of the searched user
     */
    public long logIN(String email, String password) {

        User user = userDao.getUserWithEMail(email);

        if(user != null)
        {
            if(PasswordHashing.verifyUserPassword(password, userDao.getSecuredPasswordWithEMail(email), user.getSalt()))
            {
                return user.getUserId();
            }
            else
            {
                Log.w(LOG_TAG, "password wrong, please try again");
                return -2;
            }
        }
        else{
            Log.w(LOG_TAG, "User with that email not found on database");
            return -1;
        }
    }
}
