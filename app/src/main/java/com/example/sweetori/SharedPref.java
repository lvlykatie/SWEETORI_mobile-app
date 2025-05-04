package com.example.sweetori;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPref {
    private static final String PREF_NAME = "SweetoriPrefs";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private static final String KEY_OTP = "otp";
    private static final String KEY_OTP_EXPIRY = "otp_expiry";

    public static void saveTokens(Context context, String accessToken, String refreshToken) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_ACCESS_TOKEN, accessToken);
        editor.putString(KEY_REFRESH_TOKEN, refreshToken);
        editor.apply();
        Log.d("TOKEN", "Tokens saved: AccessToken=" + accessToken + ", RefreshToken=" + refreshToken);
    }

    public static String getAccessToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_ACCESS_TOKEN, null);
    }

    public static void clearTokens(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

    // Lưu OTP và thời gian hết hạn
    public static void saveOTP(Context context, String otp, String exp) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_OTP, otp);
        editor.putString(KEY_OTP_EXPIRY, exp);
        editor.apply();
        Log.d("OTP", "OTP saved: " + otp + ", Expiry=" + exp);
    }

    // Lấy OTP
    public static String getOTP(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_OTP, null);
    }

    // Lấy thời gian hết hạn của OTP
    public static String getOTPExpiry(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_OTP_EXPIRY, null);
    }

    // Xóa OTP và thời gian hết hạn
    public static void clearOTP(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_OTP);
        editor.remove(KEY_OTP_EXPIRY);
        editor.apply();
        Log.d("OTP", "OTP and expiry cleared.");
    }
}
