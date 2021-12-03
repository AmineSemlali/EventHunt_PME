package de.pme.eventhunt.model.utilities;

import java.time.LocalDateTime;

import de.pme.eventhunt.view.ui.utilities.DateAndTime;

public class EventFilter {
    LocalDateTime firstDate;
    LocalDateTime lastDate;
    String titleText;
    String category;

    public EventFilter() {
        this.firstDate = LocalDateTime.parse("1900-01-01T12:00:00");
        this.lastDate = LocalDateTime.parse("2100-01-01T12:00:00");
        this.titleText = "";
        this.category = "";
    }


}
