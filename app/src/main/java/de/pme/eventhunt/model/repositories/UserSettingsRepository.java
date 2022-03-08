package de.pme.eventhunt.model.repositories;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import de.pme.eventhunt.model.documents.User;
import de.pme.eventhunt.model.documents.UserSettings;

public class UserSettingsRepository {

    FirebaseFirestore db;
    UserSettings userSettings;

    public UserSettingsRepository() {
        db = FirebaseFirestore.getInstance();
    }


    public void createUserSettings (User user,UserSettings userSettings)
    {
        db.collection("userSettings").document(user.getId()).set(userSettings).addOnFailureListener(e -> {
            Log.e("createUserSettings: ", e.toString());
        });
    }


    public void updateUserSettings( UserSettings userSettings)
    {
        db.collection("userSettings").document(userSettings.getUserId()).update("showLocation", userSettings.getShowLocation());
        db.collection("userSettings").document(userSettings.getUserId()).update("showName", userSettings.getShowName());
        db.collection("userSettings").document(userSettings.getUserId()).update("showEmail", userSettings.getShowEmail());
        db.collection("userSettings").document(userSettings.getUserId()).update("showAge", userSettings.getShowAge());

    }
}
