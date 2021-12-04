package de.pme.eventhunt.view.ui.filter_event;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.time.LocalDateTime;

import de.pme.eventhunt.model.utilities.EventFilter;

public class FilterEventViewModel extends AndroidViewModel {

    public EventFilter eventFilter;


    public FilterEventViewModel(@NonNull Application application) {

        super(application);
        eventFilter = new EventFilter();
    }

    public void resetFilter()
    {
        eventFilter.filterTitle = "";
        eventFilter.filterFirstDate = "";
        eventFilter.filterLastDate = "";
        eventFilter.filterDistance = 100;
    }

    public Boolean isDefault()
    {
        return eventFilter.filterTitle.equals("") && eventFilter.filterCategory.equals("") && eventFilter.filterDistance == 0
                && eventFilter.filterFirstDate.equals("") && eventFilter.filterLastDate.equals("");
    }


}
