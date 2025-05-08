package com.example.sweetori;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sweetori.content.AuthFetching;
import com.example.sweetori.dto.response.ResLoginDTO;
import com.example.sweetori.dto.response.ResSendEmailDTO;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForgetEmailActivity extends AppCompatActivity {
    Button btnSendOTP;
    TextView btnSignInNow;
    EditText editTextOTPEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetpass_email);
        EdgeToEdge.enable(this);

        //Ánh xạ component
        btnSendOTP = findViewById(R.id.btnSendOTP);
        btnSignInNow = findViewById(R.id.btnSignInNow);
        editTextOTPEmail = findViewById(R.id.editTextOTPEmail);

        //Intent
        btnSendOTP.setOnClickListener(v -> {
            String email = editTextOTPEmail.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                return;
            }

            // Chuyển đến ShoppingActivity
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://spring-shop.onrender.com/auth/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            AuthFetching authAPI = retrofit.create(AuthFetching.class);

            authAPI.sendOTP(email).enqueue(new Callback<APIResponse<ResSendEmailDTO>>() {

                @Override
                public void onResponse(Call<APIResponse<ResSendEmailDTO>> call, Response<APIResponse<ResSendEmailDTO>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        APIResponse<ResSendEmailDTO> apiResponse = response.body();
                        ResSendEmailDTO resSendEmailDTO = apiResponse.getData();
                        if (resSendEmailDTO.getOtp() != null) {
                            Toast.makeText(ForgetEmailActivity.this, "OTP sent successfully", Toast.LENGTH_SHORT).show();
                            SharedPref.saveOTP(ForgetEmailActivity.this,
                                    resSendEmailDTO.getOtp(),
                                    resSendEmailDTO.getExp());

                            // Chuyển sang OTPActivity
                            Intent intent = new Intent(ForgetEmailActivity.this, OTPActivity.class);
                            intent.putExtra("email", email); // nếu cần truyền email sang OTPActivity
                            startActivity(intent);
                        } else {
                            Toast.makeText(ForgetEmailActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ForgetEmailActivity.this, "OTP sent failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<APIResponse<ResSendEmailDTO>> call, Throwable t) {
                    Toast.makeText(ForgetEmailActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }

            });
        });
        btnSignInNow.setOnClickListener(v -> {
            // Chuyển đến ShoppingActivity
            Intent signIn = new Intent(ForgetEmailActivity.this, SignInActivity.class);
            startActivity(signIn);
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.forgetpass_email), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Khởi tạo Mobile Ads SDK
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                // Callback khi khởi tạo xong (có thể để trống nếu chưa cần)
            }
        });
    }
}