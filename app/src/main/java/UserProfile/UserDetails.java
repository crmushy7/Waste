package UserProfile;

import android.content.Context;
import android.content.SharedPreferences;

public class UserDetails {
    private static final String SHARED_PREF_NAME = "User_data";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_PHONE_NUMBER = "phone_number";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_FCM_TOKEN = "FCM_Token";

    private static String fullName;
    private static String password;
    private static String phoneNumber;
    private static String email;
    private static String FCM_Token;

    public static void init(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        fullName = sharedPreferences.getString(KEY_FULL_NAME, null);
        phoneNumber = sharedPreferences.getString(KEY_PHONE_NUMBER, null);
        email = sharedPreferences.getString(KEY_EMAIL, null);
        password = sharedPreferences.getString(KEY_PASSWORD, null);
        FCM_Token = sharedPreferences.getString(KEY_FCM_TOKEN, null);
    }

    public static String getFullName() {
        return fullName;
    }
    public static String getPassword() {
        return password;
    }

    public static String getPhoneNumber() {
        return phoneNumber;
    }

    public static String getEmail() {
        return email;
    }
    public static String getFCM_Token() {
        return FCM_Token;
    }
}
