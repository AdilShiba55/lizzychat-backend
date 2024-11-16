package chat.flirtbackend.util;

public class UtPattern {
    public static final String EMAIL_PATTERN = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
    public static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
    public static final String EMAIL_PATTERN_MSG = "invalid email format";
    public static final String PASSWORD_PATTERN_MSG = "password must be at least 8 characters long and contain both letters and numbers";
}
