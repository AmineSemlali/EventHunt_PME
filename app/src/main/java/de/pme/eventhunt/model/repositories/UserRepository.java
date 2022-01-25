package de.pme.eventhunt.model.repositories;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import de.pme.eventhunt.model.documents.Event;
import de.pme.eventhunt.model.documents.EventUser;
import de.pme.eventhunt.model.documents.User;

public class UserRepository {

    FirebaseFirestore db;
    List<User> users;
    public UserRepository() {
        db = FirebaseFirestore.getInstance();
    }

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

}
