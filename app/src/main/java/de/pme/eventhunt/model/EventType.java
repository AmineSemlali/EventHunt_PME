package de.pme.eventhunt.model;

public enum EventType {
    Festival("Festival"),
    Workshop("Worksgop"),
    Concert("Concert"),
    Social_Event ("Social_Event");

    private String DisplayName;

    EventType(String DisplayName) {
        this.DisplayName= DisplayName;
    }

    public String getDisplayName() {
        return DisplayName;
    }

}
