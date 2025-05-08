package com.example.sweetori;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.example.sweetori.dto.response.ResSendEmailDTO;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OTPActivity extends AppCompatActivity {
    Button btnConfirm;
    TextView btnResend;
    EditText editTextOTPCode;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetpass_otp);
        EdgeToEdge.enable(this);

        //Ánh xạ component
        btnConfirm = findViewById(R.id.btnConfirm);
        btnResend = findViewById(R.id.btnResend);
        editTextOTPCode = findViewById(R.id.editTextOTPCode);

        //Intent
        email = getIntent().getStringExtra("email"); // Assuming email is passed from previous screen

        btnConfirm.setOnClickListener(v -> {
            String otp = editTextOTPCode.getText().toString().trim();
            if (otp.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập mã OTP", Toast.LENGTH_SHORT).show();
                return;
            }


            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Set up OkHttpClient with logging
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            // Retrofit setup
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://spring-shop.onrender.com/auth/") // Adjust base URL if necessary
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            // tạo API interface
            AuthFetching authAPI = retrofit.create(AuthFetching.class);

            authAPI.verifyOTP(email, otp).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            // Trước khi xử lý phản hồi, kiểm tra thời hạn OTP
                            String expiryStr = SharedPref.getOTPExpiry(OTPActivity.this);

                            if (expiryStr != null) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    Instant expiry = Instant.parse(expiryStr);
                                    Instant now = Instant.now();

                                    if (now.isAfter(expiry)) {
                                        Toast.makeText(OTPActivity.this, "OTP đã hết hạn. Vui lòng yêu cầu lại.", Toast.LENGTH_SHORT).show();
                                        return;
                                    } else {
                                        long secondsLeft = Duration.between(now, expiry).getSeconds();
                                        Log.d("OTP", "OTP còn hiệu lực trong " + secondsLeft + " giây");
                                    }
                                }
                            }

                            // Nếu OTP còn hiệu lực, xử lý phản hồi từ server
                            String body = response.body().string();
                            if (body.trim().equalsIgnoreCase("ok")) {
                                Toast.makeText(OTPActivity.this, "OTP verified successfully, new password has just been sent", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(OTPActivity.this, SignInActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(OTPActivity.this, "OTP verified failed", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(OTPActivity.this, "Lỗi xử lý phản hồi", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(OTPActivity.this, "OTP verified failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(OTPActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        });

        btnResend.setOnClickListener(v -> {
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
                            Toast.makeText(OTPActivity.this, "OTP đã được gửi", Toast.LENGTH_SHORT).show();
                            SharedPref.saveOTP(OTPActivity.this,
                                    resSendEmailDTO.getOtp(),
                                    resSendEmailDTO.getExp());
                        } else {
                            Toast.makeText(OTPActivity.this, "Lỗi ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(OTPActivity.this, "Gửi OTP thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<APIResponse<ResSendEmailDTO>> call, Throwable t) {
                    Toast.makeText(OTPActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }

            });
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.forgetpass_otp), (v, insets) -> {
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