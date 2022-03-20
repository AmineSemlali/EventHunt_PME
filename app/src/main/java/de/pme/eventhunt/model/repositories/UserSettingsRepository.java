package de.pme.eventhunt.model.repositories;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import de.pme.eventhunt.model.documents.User;
import de.pme.eventhunt.model.documents.UserSettings;
// class for managing user settings documents in firebase firestore
public class UserSettingsRepository {
    ////////////////////////attributes
    FirebaseFirestore db;
    UserSettings userSettings;

    //////////////////////////// constructors
    public UserSettingsRepository() {
        db = FirebaseFirestore.getInstance();
    }

    /* this function saves a user settings document in firebase fireStore when a user is created, the user settings are here standard (all active)*/

    public void createUserSettings (User user,UserSettings userSettings)
    {
        db.collection("userSettings").document(user.getId()).set(userSettings).addOnFailureListener(e -> {
            Log.e("createUserSettings: ", e.toString());
        });
    }
    /* this method updates a user settings document in firebase fireStore whit help of a UserSettings object (argument) */

    public void updateUserSettings( UserSettings userSettings)
    {
        db.collection("userSettings").document(userSettings.getUserId()).update("showLocation", userSettings.getShowLocation());
        db.collection("userSettings").document(userSettings.getUserId()).update("showName", userSettings.getShowName());
        db.collection("userSettings").document(userSettings.getUserId()).update("showEmail", userSettings.getShowEmail());
        db.collection("userSettings").document(userSettings.getUserId()).update("showAge", userSettings.getShowAge());

    }
}
