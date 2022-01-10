package de.pme.eventhunt.model.documents;

import de.pme.eventhunt.model.utilities.EventLocation;
import de.pme.eventhunt.model.utilities.UserLocation;

public class User {

    public static String collection = "user";

    private String Id;
    private String Email;
    private String FirstName;
    private String LastName;
    private String DateOfBirth;
    private UserLocation Location;
    private String ImageSmallRef;
public User () {};

    public User(String id, String email, String firstname,String lastname, String dateofbirth, UserLocation userlocation, String imagesmallref) {
        this.Id = id;
        this.Email = email;
        this.FirstName = firstname;
        this.LastName = lastname;
        this.DateOfBirth = dateofbirth;
        this.Location = userlocation;
        this.ImageSmallRef = imagesmallref;
    };


    public UserLocation getLocation() {
        return Location;
    }

    public void setLocation(UserLocation location) {
        Location = location;
    }

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getImageSmallRef() {
        return ImageSmallRef;
    }

    public void setImageSmallRef(String imageSmallRef) {
        this.ImageSmallRef = imageSmallRef;
    }

}
