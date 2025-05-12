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

import org.json.JSONObject;

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
                Toast.makeText(this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                return;
            }

            // Sử dụng lại Retrofit từ ApiClient
            AuthFetching authAPI = APIClient.getClient().create(AuthFetching.class);

            authAPI.verifyOTP(email, otp).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        // Kiểm tra thời hạn OTP
                        String expiryStr = SharedPref.getOTPExpiry(OTPActivity.this);
                        if (expiryStr != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            Instant expiry = Instant.parse(expiryStr);
                            Instant now = Instant.now();

                            if (now.isAfter(expiry)) {
                                Toast.makeText(OTPActivity.this, "OTP has expired!", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                long secondsLeft = Duration.between(now, expiry).getSeconds();
                                Log.d("OTP", "OTP is valid for " + secondsLeft + " seconds");
                            }
                        }

                        if (response.isSuccessful() && response.body() != null) {
                            String body = response.body().string().trim();
                            if (body.equalsIgnoreCase("ok")) {
                                Toast.makeText(OTPActivity.this, "New password has been sent to your email", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(OTPActivity.this, SignInActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(OTPActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (response.errorBody() != null) {
                                String errorJson = response.errorBody().string();
                                JSONObject jsonObject = new JSONObject(errorJson);
                                String message = jsonObject.optString("message", "Invalid OTP");
                                Toast.makeText(OTPActivity.this, message, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(OTPActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(OTPActivity.this, "Response processing error", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
                return;
            }

            AuthFetching authAPI = APIClient.getClient().create(AuthFetching.class);

            authAPI.sendOTP(email).enqueue(new Callback<APIResponse<ResSendEmailDTO>>() {
                @Override
                public void onResponse(Call<APIResponse<ResSendEmailDTO>> call, Response<APIResponse<ResSendEmailDTO>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        APIResponse<ResSendEmailDTO> apiResponse = response.body();
                        ResSendEmailDTO resSendEmailDTO = apiResponse.getData();
                        if (resSendEmailDTO.getOtp() != null) {
                            Toast.makeText(OTPActivity.this, "OTP has been sent", Toast.LENGTH_SHORT).show();
                            SharedPref.saveOTP(OTPActivity.this,
                                    resSendEmailDTO.getOtp(),
                                    resSendEmailDTO.getExp());
                        } else {
                            Toast.makeText(OTPActivity.this, "Lỗi khi nhận OTP", Toast.LENGTH_SHORT).show();
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