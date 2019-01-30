package com.app.court.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.app.court.entities.SignUpEntity;
import com.app.court.entities.UserProfileEntity;
import com.app.court.retrofit.GsonFactory;


public class BasePreferenceHelper extends PreferenceHelper {

    private Context context;

    protected static final String KEY_LOGIN_STATUS = "islogin";
    protected static final String KEY_ISDUEPAYMENT = "isPayment";
    private static final String FILENAME = "preferences";
    protected static final String Firebase_TOKEN = "Firebasetoken";
    protected static final String NotificationCount = "NotificationCount";
    protected static final String PAYMENT_FILTER = "PAYMENT_FILTER";

    protected static final String KEY_SIGNUP_USER = "KEY_SIGNUP_USER";
    protected static final String KEY_USER_PROFILE = "KEY_USER_PROFILE";

    public BasePreferenceHelper(Context c) {
        this.context = c;
    }

    public SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(FILENAME, Activity.MODE_PRIVATE);
    }

    public void setLoginStatus( boolean isLogin ) {
        putBooleanPreference( context, FILENAME, KEY_LOGIN_STATUS, isLogin );
    }

    public void setDuePayment( boolean isDuePayment ) {
        putBooleanPreference( context, FILENAME, KEY_ISDUEPAYMENT, isDuePayment );
    }

    public boolean isDuePayment() {
        return getBooleanPreference(context, FILENAME, KEY_ISDUEPAYMENT);
    }

    public SignUpEntity getSignUpUser() {
        return GsonFactory.getConfiguredGson().fromJson(
                getStringPreference(context, FILENAME, KEY_SIGNUP_USER), SignUpEntity.class);
    }

    public void putSignupUser(SignUpEntity signupuser) {
        putStringPreference(context, FILENAME, KEY_SIGNUP_USER, GsonFactory
                .getConfiguredGson().toJson(signupuser));
    }

    public UserProfileEntity getUserProfile() {
        return GsonFactory.getConfiguredGson().fromJson(
                getStringPreference(context, FILENAME, KEY_USER_PROFILE), UserProfileEntity.class);
    }

    public void putUserProfile(UserProfileEntity profileEntity) {
        putStringPreference(context, FILENAME, KEY_USER_PROFILE, GsonFactory
                .getConfiguredGson().toJson(profileEntity));
    }

    public boolean isLogin() {
        return getBooleanPreference(context, FILENAME, KEY_LOGIN_STATUS);
    }


    public String getFirebase_TOKEN() {
        return getStringPreference(context, FILENAME, Firebase_TOKEN);
    }

    public void setFirebase_TOKEN(String _token) {
        putStringPreference(context, FILENAME, Firebase_TOKEN, _token);
    }
    public int getNotificationCount() {
        return getIntegerPreference(context, FILENAME, NotificationCount);
    }

    public void setNotificationCount(int count) {
        putIntegerPreference(context, FILENAME, NotificationCount, count);
    }

    public void setPaymentFilter(String PaymentFilter) {
        putStringPreference(context, FILENAME, PAYMENT_FILTER, PaymentFilter);
    }

    public String getPaymentFilter() {
        return getStringPreference(context, FILENAME, PAYMENT_FILTER);
    }

}
