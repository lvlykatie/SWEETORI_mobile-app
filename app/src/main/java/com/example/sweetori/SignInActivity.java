package com.example.sweetori;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sweetori.content.AuthFetching;
import com.example.sweetori.dto.request.ReqLoginDTO;
import com.example.sweetori.dto.response.ResLoginDTO;
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

public class SignInActivity extends AppCompatActivity {
    Button btnSignIn;
    TextView btnForgot;
    TextView btnRegisterNow;
    EditText editTextUsername;
    EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        EdgeToEdge.enable(this);

        //Ánh xạ component
        btnSignIn = findViewById(R.id.btnSignIn);
        btnForgot = findViewById(R.id.btnForgot);
        btnRegisterNow = findViewById(R.id.btnRegisterNow);

        //Intent
        btnSignIn.setOnClickListener(v -> {
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
            AuthFetching authFetching = retrofit.create(AuthFetching.class);

// Tạo dữ liệu đăng nhập
            ReqLoginDTO reqLoginDTO = new ReqLoginDTO();
            // Lấy giá trị từ EditText
            editTextUsername = findViewById(R.id.editTextUsername);
            editTextPassword = findViewById(R.id.editTextPassword);
            reqLoginDTO.setPassword(editTextPassword.getText().toString()); // Thay bằng giá trị thực tế
            reqLoginDTO.setUsername(editTextUsername.getText().toString()); // Thay bằng giá trị thực tế

            authFetching.login(reqLoginDTO).enqueue(new Callback<APIResponse<ResLoginDTO>>() {
                @Override
                public void onResponse(Call<APIResponse<ResLoginDTO>> call, Response<APIResponse<ResLoginDTO>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // Xử lý khi đăng nhập thành công
                        APIResponse<ResLoginDTO> apiResponse = response.body();
                        ResLoginDTO resLoginDTO = apiResponse.getData();
                        Log.d("LOGIN", resLoginDTO.getAccess_token());

                        Log.d("LOGIN", "Access Token: " + resLoginDTO.getAccess_token());
                        // Chuyển đến ShoppingActivity
                        Intent homepage = new Intent(SignInActivity.this, HomepageActivity.class);
                        startActivity(homepage);
                    } else {
                        // Xử lý khi đăng nhập thất bại
                        // Hiển thị thông báo lỗi hoặc xử lý logic khác
                    }
                }

                @Override
                public void onFailure(Call<APIResponse<ResLoginDTO>> call, Throwable t) {

                }
            });
        });
        btnForgot.setOnClickListener(v -> {
            // Chuyển đến ShoppingActivity
            Intent forgetEmail = new Intent(SignInActivity.this, ForgetEmailActivity.class);
            startActivity(forgetEmail);
        });
        btnRegisterNow.setOnClickListener(v -> {
            // Chuyển đến ShoppingActivity
            Intent register = new Intent(SignInActivity.this, RegisterActivity.class);
            startActivity(register);
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
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