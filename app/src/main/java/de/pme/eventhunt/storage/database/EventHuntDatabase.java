package de.pme.eventhunt.storage.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.pme.eventhunt.model.Event;
import de.pme.eventhunt.model.Notification;
import de.pme.eventhunt.model.User;
import de.pme.eventhunt.storage.dao.EventDao;
import de.pme.eventhunt.storage.dao.NotificationDao;
import de.pme.eventhunt.storage.dao.UserDao;

@Database( entities = {User.class, Event.class, Notification.class}, version =1)
public abstract class EventHuntDatabase extends RoomDatabase {

    private static final String LOG_TAG_DB = "EventHuntDatabase" ;

    public abstract EventDao eventDao();
    public abstract UserDao userDao();
    public abstract NotificationDao notificationDao();

    /*
      Executor service to perform database operations asynchronous and independent from UI thread
   */
    private static final int NUMBER_OF_THREADS = 4;
    private static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool( NUMBER_OF_THREADS );

/*
    Helper methods to ease external usage of ExecutorService
    e.g. perform async database operations */

    public static <T> T query(Callable<T> task)
            throws ExecutionException, InterruptedException
    {
        return databaseWriteExecutor.invokeAny( Collections.singletonList( task ) );
    }

    public static void execute( Runnable runnable )
    {
        databaseWriteExecutor.execute( runnable );
    }
    /*
        Singleton Instance
     */
    private static volatile EventHuntDatabase INSTANCE;

    //Singleton 'getInstance' method to create database instance thereby opening and, if not
    //already done, init the database. Note the 'createCallback'.
    public static EventHuntDatabase getDatabase(final Context context)
    {
        Log.i( LOG_TAG_DB, "getDatabase() Called");
        if (INSTANCE == null)
        {
            synchronized (EventHuntDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            EventHuntDatabase.class, "eventhunt_database")
                            .addCallback(createCallback)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /*
        Create DB Callback
        Used to add some initial data
     */
    private static RoomDatabase.Callback createCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            Log.i( LOG_TAG_DB, "onCreate() called" );

            execute(() -> {
                UserDao dao = INSTANCE.userDao();

                Faker faker = Faker.instance();
                for (int i = 0; i < 10; i++)
                {

                    User user = new User(faker.name().firstName(), faker.name().lastName(),faker.internet().emailAddress(),faker.internet().password(),faker.internet().url());
                    user.setCreated( LocalDate.now() );
                    user.setVersion( 1 );
                    dao.insert(user);
                }
                Log.i(LOG_TAG_DB, "Inserted 10 values to DB");
            });
        }
    };
}
