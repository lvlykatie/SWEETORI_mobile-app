package com.example.sweetori;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sweetori.adapter.ProductAdapter;
import com.example.sweetori.dto.response.ResProductDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import android.app.Activity;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

public class ProductActivity extends AppCompatActivity {
    private ImageView btnAccount, btnHome, btnCart, btnNoti, btnVoucher;
    private RecyclerView productItemRecyclerView;
    private ProductAdapter productAdapter;
    private List<ResProductDTO.ProductData> productList = new ArrayList<>();
    private EditText searchInput;
    private ImageView searchIcon;
    private ImageView filterIcon;

    private ActivityResultLauncher<Intent> filterLauncher;
    private List<ResProductDTO.ProductData> managerList;  // dữ liệu gốc

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product);

        // Ánh xạ view
        btnAccount            = findViewById(R.id.btnAccount);
        btnHome               = findViewById(R.id.btnHome);
        btnCart               = findViewById(R.id.btnCart);
        btnNoti               = findViewById(R.id.btnNoti);
        btnVoucher            = findViewById(R.id.btnVoucher1);
        productItemRecyclerView = findViewById(R.id.productItemRecyclerView);
        searchInput           = findViewById(R.id.searchInput);
        searchIcon            = findViewById(R.id.searchIcon);

        // Setup RecyclerView
        productItemRecyclerView.setHasFixedSize(true);
        productItemRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        productAdapter = new ProductAdapter(productList, this);
        productItemRecyclerView.setAdapter(productAdapter);

        // Load cached data vào list gốc và adapter
        List<ResProductDTO.ProductData> cached = ResProductDTO.ProductDataManager
                .getInstance().getProductList();
        if (cached == null) {
            managerList = new ArrayList<>();
            ResProductDTO.ProductDataManager.getInstance().setProductList(managerList);
            Log.e("PRODUCT_ACTIVITY", "No cached data; initialized empty managerList");
        } else {
            managerList = cached;
            Log.d("PRODUCT_ACTIVITY", "Loaded cached data. Items: " + managerList.size());
        }

        // Hiển thị lần đầu:
        productList.clear();
        productList.addAll(managerList);
        productAdapter.notifyDataSetChanged();


        // Footer navigation (giữ nguyên)
        btnAccount.setOnClickListener(v -> startActivity(new Intent(this, AccountActivity.class)));
        btnHome   .setOnClickListener(v -> startActivity(new Intent(this, HomepageActivity.class)));
        btnCart   .setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
        btnNoti   .setOnClickListener(v -> startActivity(new Intent(this, NotiActivity.class)));
        btnVoucher.setOnClickListener(v -> startActivity(new Intent(this, VoucherActivity.class)));

        // --- CHỈNH 2: nhận query từ Homepage ---
        String incomingQuery = getIntent().getStringExtra("searchQuery");
        if (incomingQuery != null) {
            // hiển thị trong EditText
            searchInput.setText(incomingQuery);
            // thực thi lọc ngay
            applyFilter(incomingQuery.trim().toLowerCase());
        }

        // --- CHỈNH 3: xử lý search nội bộ ---
        searchIcon.setOnClickListener(v -> {
            String q = searchInput.getText().toString().trim().toLowerCase();
            applyFilter(q);
        });

        // Đăng ký launcher để nhận kết quả từ TabFilterActivity
        filterLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::onFilterResult
        );

        // Ánh xạ icon filter
        filterIcon = findViewById(R.id.filter);
        filterIcon.setOnClickListener(v -> {
            Intent intent = new Intent(this, TabFilterActivity.class);
            filterLauncher.launch(intent);        // <-- dùng launcher
            overridePendingTransition(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
            );
        });


    }

    // Callback khi filter xong
    private void onFilterResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
            Intent data = result.getData();
            ArrayList<String> selCats   = data.getStringArrayListExtra("FILTER_CATEGORIES");
            ArrayList<String> selBrands = data.getStringArrayListExtra("FILTER_BRANDS");
            int selRate     = data.getIntExtra("FILTER_RATE", 0);
            String priceOrd = data.getStringExtra("FILTER_PRICE");
            applyAdvancedFilter(selCats, selBrands, selRate, priceOrd);
        }
    }
    /**
     * Lọc theo category, brand, rate, sau đó sắp xếp theo priceOrder.
     */
    private void applyAdvancedFilter(
            List<String> cats,
            List<String> brands,
            int minRate,
            String priceOrder
    ) {
        List<ResProductDTO.ProductData> filtered = new ArrayList<>();

        for (ResProductDTO.ProductData p : managerList) {
            String name = p.getProductName() != null
                    ? p.getProductName().toLowerCase()
                    : "";

            // 1) Check Category: nếu danh sách cats rỗng, bỏ qua luôn
            boolean okCat = cats == null || cats.isEmpty()
                    || cats.stream()
                    .map(String::toLowerCase)
                    .anyMatch(name::contains);

            // 2) Check Brand: tương tự
            boolean okBrand = brands == null || brands.isEmpty()
                    || brands.stream()
                    .map(String::toLowerCase)
                    .anyMatch(name::contains);

            // 3) Check Rate
            boolean okRate = p.getAvgRate() >= minRate;

            if (okCat && okBrand && okRate) {
                filtered.add(p);
            }
        }

        // 4) Sort theo priceOrder
        if ("LOW_HIGH".equals(priceOrder)) {
            Collections.sort(filtered,
                    Comparator.comparingDouble(ResProductDTO.ProductData::getSellingPrice)
            );
        } else if ("HIGH_LOW".equals(priceOrder)) {
            Collections.sort(filtered,
                    (a, b) -> Double.compare(b.getSellingPrice(), a.getSellingPrice())
            );
        }

        // 5) Cập nhật adapter
        productAdapter.updateProductList(filtered);
    }
    @Override
    public void finish() {
        super.finish();
        // override khi bấm back hoặc finish()
        overridePendingTransition(
                R.anim.slide_in_left,   // trở về trượt từ trái vào
                R.anim.slide_out_right  // đóng trượt sang phải
        );
    }

    // Hàm lọc dùng chung
    private void applyFilter(String q) {
        List<ResProductDTO.ProductData> tmp = ResProductDTO.ProductDataManager
                .getInstance().getProductList();
        List<ResProductDTO.ProductData> filtered = new ArrayList<>();
        if (q.isEmpty()) {
            filtered.addAll(tmp);
        } else {
            for (ResProductDTO.ProductData p : tmp) {
                String name = p.getProductName();
                if (name != null && name.toLowerCase().contains(q)) {
                    filtered.add(p);
                }
            }
        }
        productAdapter.updateProductList(filtered);
    }
}