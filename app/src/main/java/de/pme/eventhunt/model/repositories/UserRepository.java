package de.pme.eventhunt.model.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

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
