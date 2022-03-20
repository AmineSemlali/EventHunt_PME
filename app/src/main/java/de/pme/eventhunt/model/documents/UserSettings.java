package de.pme.eventhunt.model.documents;


/* UserSettings class for managing user settings */

public class UserSettings {

    ////////////////////////attributes
    public static String collection = "userSettings";
    private String UserId;
    private Boolean showLocation;
    private Boolean showName;
    private Boolean showEmail;
    private Boolean showAge;

    /////////////////////// getters and setters

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }


    public Boolean getShowLocation() {
        return showLocation;
    }

    public void setShowLocation(Boolean showLocation) {
        this.showLocation = showLocation;
    }


    public Boolean getShowName() {
        return showName;
    }

    public void setShowName(Boolean showName) {
        this.showName = showName;
    }


    public Boolean getShowEmail() {
        return showEmail;
    }

    public void setShowEmail(Boolean showEmail) {
        this.showEmail = showEmail;
    }


    public Boolean getShowAge() {
        return showAge;
    }

    public void setShowAge(Boolean showAge) {
        this.showAge = showAge;
    }

    //////////////////////////// constructors

    public UserSettings ( String userId)
    {
        this.showAge = true;
        this.showEmail = true;
        this.showLocation = true;
        this.showName = true;
        this.UserId = userId;

    };

    public UserSettings ()
    {
    };
}
