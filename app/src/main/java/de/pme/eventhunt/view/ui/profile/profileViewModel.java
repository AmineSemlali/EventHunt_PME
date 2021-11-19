package de.pme.eventhunt.view.ui.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import de.pme.eventhunt.storage.repository.UserRepository;

public class profileViewModel extends AndroidViewModel {

    private final UserRepository userRepository;

    public profileViewModel(@NonNull Application application) {
        super(application);
        this.userRepository = UserRepository.getRepository(application);
    }
}
