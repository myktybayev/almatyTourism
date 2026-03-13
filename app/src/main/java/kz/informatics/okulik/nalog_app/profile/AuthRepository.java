package kz.informatics.okulik.nalog_app.profile;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Auth via SharedPreferences: users stored as JSON array, password as SHA-256 hash.
 */
public class AuthRepository {
    private static final String PREFS_NAME = "auth_users";
    private static final String KEY_USERS_JSON = "users_json";
    private static final String KEY_SESSION_EMAIL = "session_email";
    private static final String KEY_AVATAR_PREFIX = "avatar_";

    private final SharedPreferences prefs;

    public AuthRepository(Context context) {
        this.prefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     * @return true if registered, false if email already exists
     */
    public boolean register(String email, String password, String fullName, String phone) {
        String hash = hashPassword(password);
        if (hash == null) return false;
        JSONArray arr = getUsersArray();
        for (int i = 0; i < arr.length(); i++) {
            try {
                JSONObject o = arr.getJSONObject(i);
                if (email.equalsIgnoreCase(o.optString("email", ""))) return false;
            } catch (Exception ignored) { }
        }
        try {
            JSONObject user = new JSONObject();
            user.put("id", System.currentTimeMillis());
            user.put("email", email);
            user.put("passwordHash", hash);
            user.put("fullName", fullName != null ? fullName : "");
            user.put("phone", phone != null ? phone : "");
            arr.put(user);
            prefs.edit().putString(KEY_USERS_JSON, arr.toString()).apply();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @return User if credentials valid, null otherwise
     */
    public User login(String email, String password) {
        String hash = hashPassword(password);
        if (hash == null) return null;
        JSONArray arr = getUsersArray();
        for (int i = 0; i < arr.length(); i++) {
            try {
                JSONObject o = arr.getJSONObject(i);
                if (!email.equalsIgnoreCase(o.optString("email", ""))) continue;
                if (!hash.equals(o.optString("passwordHash", ""))) return null;
                return new User(
                        o.optString("email"),
                        o.optString("passwordHash"),
                        o.optString("fullName"),
                        o.optString("phone")
                );
            } catch (Exception ignored) { }
        }
        return null;
    }

    /** Save session after successful login. */
    public void saveSession(User user) {
        if (user != null && user.email != null) {
            prefs.edit().putString(KEY_SESSION_EMAIL, user.email).apply();
        }
    }

    /** Clear session (e.g. on logout). */
    public void clearSession() {
        prefs.edit().remove(KEY_SESSION_EMAIL).apply();
    }

    /** True if user is logged in (session exists). */
    public boolean isLoggedIn() {
        String email = prefs.getString(KEY_SESSION_EMAIL, null);
        return email != null && !email.isEmpty();
    }

    /** Current logged-in user, or null if not logged in / user not found. */
    public User getCurrentUser() {
        String email = prefs.getString(KEY_SESSION_EMAIL, null);
        if (email == null || email.isEmpty()) return null;
        JSONArray arr = getUsersArray();
        for (int i = 0; i < arr.length(); i++) {
            try {
                JSONObject o = arr.getJSONObject(i);
                if (!email.equalsIgnoreCase(o.optString("email", ""))) continue;
                return new User(
                        o.optString("email"),
                        o.optString("passwordHash"),
                        o.optString("fullName"),
                        o.optString("phone")
                );
            } catch (Exception ignored) { }
        }
        return null;
    }

    /** Update fullName and phone for the user with given email. Email cannot be changed. */
    public boolean updateUser(String email, String fullName, String phone) {
        if (email == null || email.isEmpty()) return false;
        JSONArray arr = getUsersArray();
        for (int i = 0; i < arr.length(); i++) {
            try {
                JSONObject o = arr.getJSONObject(i);
                if (!email.equalsIgnoreCase(o.optString("email", ""))) continue;
                o.put("fullName", fullName != null ? fullName : "");
                o.put("phone", phone != null ? phone : "");
                arr.put(i, o);
                prefs.edit().putString(KEY_USERS_JSON, arr.toString()).apply();
                return true;
            } catch (Exception ignored) { }
        }
        return false;
    }

    /** Path to saved avatar image file for the given user email, or null. */
    public String getAvatarPath(String email) {
        if (email == null || email.isEmpty()) return null;
        return prefs.getString(KEY_AVATAR_PREFIX + email, null);
    }

    /** Save the path to the avatar file for the given user. */
    public void setAvatarPath(String email, String path) {
        if (email == null || email.isEmpty()) return;
        prefs.edit().putString(KEY_AVATAR_PREFIX + email, path).apply();
    }

    private JSONArray getUsersArray() {
        String json = prefs.getString(KEY_USERS_JSON, "[]");
        try {
            return new JSONArray(json);
        } catch (Exception e) {
            return new JSONArray();
        }
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
