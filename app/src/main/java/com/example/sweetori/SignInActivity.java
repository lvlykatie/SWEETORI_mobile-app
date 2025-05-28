package com.example.sweetori;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
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
import com.example.sweetori.content.UserFetching;
import com.example.sweetori.dto.request.ReqLoginDTO;
import com.example.sweetori.dto.response.ResLoginDTO;
import com.example.sweetori.dto.response.ResUserDTO;
import com.google.android.gms.ads.MobileAds;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        // 1. Check xem token có tồn tại không
        Pair<String, Integer> accessTokenWithUserId = SharedPref.getAccessTokenWithUserId(this);
        String accessToken = accessTokenWithUserId.first;
        int userId = accessTokenWithUserId.second;

        if (accessToken != null && !accessToken.isEmpty() && userId != -1) {
            // Có token, lấy user luôn
            fetchUser(userId, accessToken, new Callback<APIResponse<ResUserDTO>>() {
                @Override
                public void onResponse(Call<APIResponse<ResUserDTO>> call, Response<APIResponse<ResUserDTO>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ResUserDTO user = response.body().getData();
                        Intent homepage = new Intent(SignInActivity.this, HomepageActivity.class);
                        homepage.putExtra("user", user);
                        homepage.putExtra("accessToken", accessToken);
                        homepage.putExtra("userId", userId);
                        startActivity(homepage);
                        finish();
                    } else {
                        // Token không hợp lệ hoặc fetch user lỗi -> hiện màn login
                        setupLoginUI();
                    }
                }

                @Override
                public void onFailure(Call<APIResponse<ResUserDTO>> call, Throwable t) {
                    // Kết nối lỗi, vẫn cho hiển thị login để user đăng nhập lại
                    setupLoginUI();
                }
            });
        } else {
            // Không có token -> hiển thị màn login
            setupLoginUI();
        }

        // Khởi tạo quảng cáo (nếu có)
        MobileAds.initialize(this, initializationStatus -> {});
    }

    private void setupLoginUI() {
        // Ánh xạ component và set onClickListener
        btnSignIn = findViewById(R.id.btnSignIn);
        btnForgot = findViewById(R.id.btnForgot);
        btnRegisterNow = findViewById(R.id.btnRegisterNow);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);

        btnSignIn.setOnClickListener(v -> {
            ReqLoginDTO reqLoginDTO = new ReqLoginDTO();
            reqLoginDTO.setUsername(editTextUsername.getText().toString());
            reqLoginDTO.setPassword(editTextPassword.getText().toString());

            AuthFetching authFetching = APIClient.getClient().create(AuthFetching.class);

            authFetching.login(reqLoginDTO).enqueue(new Callback<APIResponse<ResLoginDTO>>() {
                @Override
                public void onResponse(Call<APIResponse<ResLoginDTO>> call, Response<APIResponse<ResLoginDTO>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(SignInActivity.this, "Sign in successfully!", Toast.LENGTH_SHORT).show();
                        ResLoginDTO resLoginDTO = response.body().getData();

                        // Lưu token và userId
                        SharedPref.saveTokens(SignInActivity.this,
                                resLoginDTO.getAccess_token(),
                                resLoginDTO.getRefresh_token());// nhớ lưu userId
                        Pair<String, Integer> accessTokenWithUserId = SharedPref.getAccessTokenWithUserId(SignInActivity.this);
                        // Sau khi có token, lấy user rồi chuyển sang homepage
                        fetchUser(accessTokenWithUserId.second, resLoginDTO.getAccess_token(), new Callback<APIResponse<ResUserDTO>>() {
                            @Override
                            public void onResponse(Call<APIResponse<ResUserDTO>> call, Response<APIResponse<ResUserDTO>> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    ResUserDTO user = response.body().getData();
                                    SharedPref.saveUser(SignInActivity.this, user);
                                    Intent homepage = new Intent(SignInActivity.this, HomepageActivity.class);
                                    homepage.putExtra("user", user);
                                    startActivity(homepage);
                                    finish();
                                } else {
                                    Toast.makeText(SignInActivity.this, "Failed to fetch user info", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<APIResponse<ResUserDTO>> call, Throwable t) {
                                Toast.makeText(SignInActivity.this, "Fetch user failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(SignInActivity.this, "Username or password is wrong!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<APIResponse<ResLoginDTO>> call, Throwable t) {
                    Toast.makeText(SignInActivity.this, "Login failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnForgot.setOnClickListener(v -> {
            Intent forgetEmail = new Intent(SignInActivity.this, ForgetEmailActivity.class);
            startActivity(forgetEmail);
        });

        btnRegisterNow.setOnClickListener(v -> {
            Intent register = new Intent(SignInActivity.this, RegisterActivity.class);
            startActivity(register);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void fetchUser(int userId, String accessToken, Callback<APIResponse<ResUserDTO>> callback) {
        UserFetching apiService = APIClient.getClientWithToken(accessToken).create(UserFetching.class);
        apiService.getUser(userId).enqueue(callback);
    }
}