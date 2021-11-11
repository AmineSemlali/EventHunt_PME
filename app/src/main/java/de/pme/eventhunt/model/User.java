package de.pme.eventhunt.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

import de.pme.eventhunt.model.utilities.PasswordHashing;

@Entity
public class User {

    @Ignore
    public static final String LOG_TAG = "User";

    @Ignore
    public static final int SALT_LENGTH = 30;

    /* /////////////////////Attributes///////////////////////// */

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "userId")
    private long userId;

    @NonNull
    @ColumnInfo(name = "version")
    private long version;

    @ColumnInfo(name = "created")
    private LocalDate created;


    @NonNull
    @ColumnInfo(name = "updated")
    private LocalDate updated;

    @NonNull
    @ColumnInfo(name = "firstName")
    private String firstName;

    @NonNull
    @ColumnInfo(name = "lastName")
    private String lastName;

    @NonNull
    @ColumnInfo(name = "email")
    private String email;

    @NonNull
    @ColumnInfo(name = "profileImageUrl")
    private String profileImageUrl;


    //For managing the password security
    @NonNull
    @ColumnInfo(name = "salt")
    private String salt;

    @NonNull
    @ColumnInfo(name = "securePassword")
    private String securePassword;

    /* /////////////////////Getter/Setter///////////////////////// */

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @NonNull
    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(@NonNull LocalDate created) {
        this.created = created;
    }

    @NonNull
    public LocalDate getUpdated() {
        return updated;
    }

    public void setUpdated(@NonNull LocalDate updated) {
        this.updated = updated;
    }

    @NonNull
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NonNull String firstName) {
        this.firstName = firstName;
    }

    @NonNull
    public String getLastName() {
        return lastName;
    }

    public void setLastName(@NonNull String lastName) {
        this.lastName = lastName;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @NonNull
    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(@NonNull String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    @NonNull
    public String getSalt() {
        return salt;
    }

    public void setSalt(@NonNull String salt) {
        this.salt = salt;
    }

    @NonNull
    public String getSecurePassword() {
        return this.securePassword;
    }

    /**
     * Create a new Salt for the new password
     * and Sets a new secure password.
     *
     * @param notSecurePassword the not secure password coming from the frontend
     */
    public void setSecurePassword(@NonNull String notSecurePassword) {
        this.salt = PasswordHashing.getSalt(SALT_LENGTH);
        this.securePassword = PasswordHashing.generateSecurePassword(notSecurePassword, salt);
    }



    /**
     * To string string.
     *
     * @return the string
     */
    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
