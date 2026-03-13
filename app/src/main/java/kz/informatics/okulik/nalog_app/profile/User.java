package kz.informatics.okulik.nalog_app.profile;

/**
 * User model for local auth (SharedPreferences).
 */
public class User {
    public long id;
    public String email;
    public String passwordHash;
    public String fullName;
    public String phone;

    public User(String email, String passwordHash, String fullName, String phone) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.phone = phone != null ? phone : "";
    }
}
