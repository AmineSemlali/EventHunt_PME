package de.pme.eventhunt.model.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// class for checking the validity of the password and email when creating or updating an account
public class EmailAndPasswordValidator {

    // Minimum eight characters, at least one letter and one number:
    private static final String PASSWORD_PATTERN =
            "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
    private static final Pattern patternPassword = Pattern.compile(PASSWORD_PATTERN);


    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";
    public static final Pattern patternEmail = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);



    public static boolean isPasswordValid(final String password) {
        Matcher matcher = patternPassword.matcher(password);
        return matcher.matches();
    }
    public static boolean isEmailValid(final String email) {
        Matcher matcher = patternEmail.matcher(email);
        return matcher.matches();
    }
    public EmailAndPasswordValidator() {
    }
}
