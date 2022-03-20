package de.pme.eventhunt.model.repositories;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

import de.pme.eventhunt.model.documents.Event;
import de.pme.eventhunt.model.documents.EventUser;
import de.pme.eventhunt.model.documents.User;
import de.pme.eventhunt.model.utilities.UserLocation;

// class for managing user documents in firebase firestore
public class UserRepository {

    ////////////////////////attributes
    FirebaseFirestore db;
    List<User> users;


    //////////////////////////// constructors
    public UserRepository() {
        db = FirebaseFirestore.getInstance();
    }


    /* this function saves an user document in firebase fireStore when a user is created */

    public void createUser(User user)
    {
            db.collection("user").document(user.getId()).set(user).addOnFailureListener(e -> {
                Log.e("createUser: ", e.toString());
            });
    }

    public void deleteUser(String id)
    {
        db.collection(User.collection).document(id).delete().addOnFailureListener(e -> {
            Log.e("createUser: ", e.toString());
        });
    }

   public List<User> findUserById(String id) {
       Task<QuerySnapshot> userQuery;
       userQuery = db.collection("user").whereEqualTo("id", id).get();

       userQuery.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
           @Override
           public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
               users =  userQuery.getResult().toObjects(User.class);
           }
       });

       return users;
   }
    /* this method updates a user document in firebase fireStore, it updates the email first name and last name*/

    public void updateUser(User user, String email, String firstName, String lastName)
    {
        db.collection("user").document(user.getId()).update("email", email);
        db.collection("user").document(user.getId()).update("firstName", firstName);
        db.collection("user").document(user.getId()).update("lastName", lastName);

    }
    /* this method updates a user document in firebase fireStore, it updates the date of birth */
    public void updateDateOfBirth(User user, String dobString)
{
    db.collection("user").document(user.getId()).update("dateOfBirth", dobString);
}

    /* this method updates a user document in firebase fireStore, it updates the latitude and longitude */
    public void updateLocation(User user, UserLocation userLocation)
    {
        db.collection("user").document(user.getId()).update("location.latitude", userLocation.getLatitude());
        db.collection("user").document(user.getId()).update("location.longitude", userLocation.getLongitude());
    }
    /* this method updates a user document in firebase fireStore, it updates the url of the image  */
    public void updateUserImage(User user, String url)
    {
        db.collection("user").document(user.getId()).update("imageSmallRef", url);
    }
}
