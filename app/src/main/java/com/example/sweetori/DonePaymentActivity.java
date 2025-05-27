package com.example.sweetori;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class DonePaymentActivity  extends AppCompatActivity {
    private TextView item, dateTextView, shipping, discount, voucher_discount, total;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.done_payments);
        EdgeToEdge.enable(this);
    }
}
