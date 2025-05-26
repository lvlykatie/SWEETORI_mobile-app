package com.example.sweetori;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

public class VNPayActivity extends AppCompatActivity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        setContentView(webView);

        // Lấy URL VNPay được truyền từ PaymentActivity
        String paymentUrl = getIntent().getStringExtra("url");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            // Khi WebView chuyển trang
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("myapp-vnpay://callback")) {
                    // Xử lý kết quả callback
                    Uri uri = Uri.parse(url);
                    String responseCode = uri.getQueryParameter("vnp_ResponseCode");
                    if ("00".equals(responseCode)) {
                        Toast.makeText(VNPayActivity.this, "Thanh toán VNPay thành công!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(VNPayActivity.this, "Thanh toán VNPay thất bại: " + responseCode, Toast.LENGTH_LONG).show();
                    }
                    // Quay về PaymentActivity (hoặc bất cứ Activity nào bạn muốn)
                    Intent i = new Intent(VNPayActivity.this, PaymentActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                    return true;
                }
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(paymentUrl);
    }
}
