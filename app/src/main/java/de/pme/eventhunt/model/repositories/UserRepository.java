package de.pme.eventhunt.model.repositories;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import de.pme.eventhunt.model.documents.User;

public class UserRepository {

    FirebaseFirestore db;

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



}
