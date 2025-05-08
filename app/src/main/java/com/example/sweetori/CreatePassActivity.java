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

        // Validate input
        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please enter password completely", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Password mismatch", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy access token
        String accessToken = SharedPref.getAccessToken(this);
        if (accessToken == null) {
            Toast.makeText(this, "Please sign in again", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo request body
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("newPassword", newPassword);

        // Cấu hình Retrofit với interceptor
        // Trong phương thức handlePasswordChange()
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)  // Thêm timeout
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + accessToken)
                            .build();
                    return chain.proceed(request);
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://spring-shop.onrender.com/auth/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AuthFetching authFetching = retrofit.create(AuthFetching.class);

        // Gọi API
        authFetching.changePassword(requestBody).enqueue(new Callback<APIResponse<Boolean>>() {
            @Override
            public void onResponse(Call<APIResponse<Boolean>> call, Response<APIResponse<Boolean>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Boolean isSuccess = response.body().getData();
                    if (isSuccess != null && isSuccess) {
                        Toast.makeText(CreatePassActivity.this, "Password changed successfully!", Toast.LENGTH_SHORT).show();
                        redirectToLogin();
                    } else {
                        Toast.makeText(CreatePassActivity.this, "Password changed failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        // Xử lý lỗi HTTP (ví dụ: 401, 500)
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        Toast.makeText(CreatePassActivity.this, "Lỗi: " + response.code() + " - " + errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override  // <-- PHẢI CÓ PHƯƠNG THỨC NÀY
            public void onFailure(Call<APIResponse<Boolean>> call, Throwable t) {
                // Xử lý lỗi kết nối/timeout
                Toast.makeText(CreatePassActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
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