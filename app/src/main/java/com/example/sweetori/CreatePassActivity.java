package com.example.sweetori;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sweetori.content.AuthFetching;
import com.example.sweetori.SharedPref;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreatePassActivity extends AppCompatActivity {
    Button btnDone;
    EditText edtNewPass, edtConfirmPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.createpass);

        // Ánh xạ component
        btnDone = findViewById(R.id.btnDone);
        edtNewPass = findViewById(R.id.editTextNewPassword);
        edtConfirmPass = findViewById(R.id.editTextConfirmNewPassword);

        btnDone.setOnClickListener(v -> handlePasswordChange());
    }

    private void handlePasswordChange() {
        String newPassword = edtNewPass.getText().toString().trim();
        String confirmPassword = edtConfirmPass.getText().toString().trim();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please enter password completely", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Password mismatch", Toast.LENGTH_SHORT).show();
            return;
        }

        String accessToken = SharedPref.getAccessToken(this);
        if (accessToken == null) {
            Toast.makeText(this, "Please sign in again", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("newPassword", newPassword);

        // Sử dụng ApiClient thay vì tạo lại Retrofit
        AuthFetching authFetching = APIClient.getClientWithToken(accessToken).create(AuthFetching.class);

        authFetching.changePassword(requestBody).enqueue(new Callback<APIResponse<Boolean>>() {
            @Override
            public void onResponse(Call<APIResponse<Boolean>> call, Response<APIResponse<Boolean>> response) {
                if (response.isSuccessful() && response.body() != null && Boolean.TRUE.equals(response.body().getData())) {
                    Toast.makeText(CreatePassActivity.this, "Password changed successfully!", Toast.LENGTH_SHORT).show();
                    redirectToLogin();
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        Toast.makeText(CreatePassActivity.this, "Lỗi: " + response.code() + " - " + errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<APIResponse<Boolean>> call, Throwable t) {
                Toast.makeText(CreatePassActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void redirectToLogin() {
        SharedPref.clearTokens(CreatePassActivity.this);
        Intent intent = new Intent(this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}