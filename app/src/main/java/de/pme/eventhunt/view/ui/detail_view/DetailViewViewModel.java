package de.pme.eventhunt.view.ui.detail_view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class DetailViewViewModel extends AndroidViewModel {

    String eventId;

    public DetailViewViewModel(@NonNull Application application) {
        super(application);
    }
}
