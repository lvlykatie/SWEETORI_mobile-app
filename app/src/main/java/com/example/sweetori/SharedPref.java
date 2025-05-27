package com.example.sweetori;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Pair;

import com.example.sweetori.dto.response.ResLoginDTO;
import com.example.sweetori.dto.response.ResUserDTO;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

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

        try {
            String[] parts = accessToken.split("\\.");
            if (parts.length == 3) {
                String payload = parts[1];
                byte[] decodedBytes = android.util.Base64.decode(payload, android.util.Base64.URL_SAFE);
                String payloadJson = new String(decodedBytes, StandardCharsets.UTF_8);
                JSONObject json = new JSONObject(payloadJson);
                if (json.has("userId")) {
                    int userId = json.getInt("userId");
                    editor.putInt("userId", userId);
                    Log.d("TOKEN", "Extracted userId: " + userId);
                }
            }
        } catch (Exception e) {
            Log.e("TOKEN", "Failed to decode JWT and extract userId", e);
        }

        editor.apply();
        Log.d("TOKEN", "Tokens saved: AccessToken=" + accessToken + ", RefreshToken=" + refreshToken);
    }

    public static Pair<String, Integer> getAccessTokenWithUserId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String accessToken = prefs.getString(KEY_ACCESS_TOKEN, null);
        int userId = prefs.getInt("userId", -1); // -1 if userId is not set
        return new Pair<>(accessToken, userId);
    }

    public static void clearTokens(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_ACCESS_TOKEN);
        editor.remove(KEY_REFRESH_TOKEN);
        editor.apply();
    }

    private static final String KEY_USER = "user";

    public static void saveUser(Context context, ResUserDTO user) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        editor.putString(KEY_USER, userJson);
        editor.apply();
        Log.d("USER", "User saved: " + userJson);
    }

    public static ResUserDTO getUser(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String userJson = prefs.getString(KEY_USER, null);
        if (userJson != null) {
            Gson gson = new Gson();
            return gson.fromJson(userJson, ResUserDTO.class);
        }
        return null;
    }
    public static void clearUser(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_USER);
        editor.apply();
        Log.d("USER", "User cleared.");
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
