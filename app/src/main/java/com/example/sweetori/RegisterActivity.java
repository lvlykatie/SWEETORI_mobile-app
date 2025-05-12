package com.example.sweetori;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sweetori.content.AuthFetching;
import com.example.sweetori.APIResponse;
import com.example.sweetori.dto.request.ReqRegisterDTO;
import com.example.sweetori.dto.response.ResRegisterDTO;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    private EditText etUsername, etEmail, etPassword;
    private CheckBox checkBoxPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.signup);

        // Ánh xạ component
        etUsername = findViewById(R.id.editTextUserName);
        etEmail = findViewById(R.id.editTextUserEmail);
        etPassword = findViewById(R.id.editTextUserPassword);
        checkBoxPolicy = findViewById(R.id.checkBoxPolicy);
        Button btnSignUp = findViewById(R.id.btnSignUp);
        TextView btnSignInNow = findViewById(R.id.btnSignInNow);

        // Xử lý click Sign Up
        btnSignUp.setOnClickListener(v -> {
            if (!checkBoxPolicy.isChecked()) {
                Toast.makeText(this, "Please accept our policies!", Toast.LENGTH_SHORT).show();
                return;
            }

            AuthFetching authFetching = APIClient.getClient().create(AuthFetching.class);

            ReqRegisterDTO reqRegisterDTO = new ReqRegisterDTO();
            reqRegisterDTO.setUsername(etUsername.getText().toString().trim());
            reqRegisterDTO.setEmail(etEmail.getText().toString().trim());
            reqRegisterDTO.setPassword(etPassword.getText().toString().trim());

            authFetching.register(reqRegisterDTO).enqueue(new Callback<APIResponse<ResRegisterDTO>>() {
                @Override
                public void onResponse(Call<APIResponse<ResRegisterDTO>> call, Response<APIResponse<ResRegisterDTO>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(RegisterActivity.this, "Sign up successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, SignInActivity.class));
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Sign up failed!", Toast.LENGTH_SHORT).show();
                        Log.e("REGISTER_ERROR", "Code: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<APIResponse<ResRegisterDTO>> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("REGISTER_ERROR", "Failure: ", t);
                }
            });
        });


        // Chuyển sang màn hình đăng nhập
        btnSignInNow.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, SignInActivity.class));
            finish();
        });

        // Xử lý padding cho hệ thống
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signup), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo Mobile Ads
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
    }
}