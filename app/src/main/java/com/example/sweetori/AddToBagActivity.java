package com.example.sweetori;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sweetori.adapter.AddToBagAdapter;
import com.example.sweetori.content.VoucherFetching;
import com.example.sweetori.dto.response.PaginationWrapper;
import com.example.sweetori.dto.response.ResCartDetailDTO;
import com.example.sweetori.dto.response.ResDeliveryDTO;
import com.example.sweetori.dto.response.ResDiscountDTO;
import com.example.sweetori.dto.response.ResUserDTO;
import com.example.sweetori.content.UserFetching;
import com.example.sweetori.dto.response.ResUserVoucherDTO;
import com.example.sweetori.dto.response.ResVoucherDTO;
import com.example.sweetori.dto.response.ShippingDTO;
import com.example.sweetori.content.DeliveryFetching;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddToBagActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AddToBagAdapter adapter;
    private List<ResDiscountDTO> discountList = new ArrayList<>();

    private List<ResDeliveryDTO> deliveryList = new ArrayList<>();
    private int selectedVoucherCode ;
    private double totalPrice, total, discountedPrice;
    private Integer voucherId;
    private int deliveryId;
    private String deliveryName;

    private TextView item, userName, Phone, shipping, discount, voucher_discount, total_Price, itemCount;
    private EditText Editaddress, couponEdit;
    private Button checkoutBtn, applyBtn;
    Spinner spinner;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_to_bag);

        recyclerView = findViewById(R.id.recyclerViewCart);
        item = findViewById(R.id.item);
        itemCount = findViewById(R.id.itemCount);
        Phone = findViewById(R.id.Phone);
        userName = findViewById(R.id.userName);
        Editaddress = findViewById(R.id.Editaddress);
        spinner = findViewById(R.id.spinnerOptions);
        shipping = findViewById(R.id.shipping);
        discount = findViewById(R.id.discount);
        voucher_discount = findViewById(R.id.voucher_discount);
        applyBtn = findViewById(R.id.applyBtn);
        couponEdit = findViewById(R.id.couponEdit);
        total_Price = findViewById(R.id.totalPrice);
        Pair<String, Integer> accessTokenWithUserId = SharedPref.getAccessTokenWithUserId(AddToBagActivity.this);

        int selectedDeliveryId = deliveryId;
        // Lấy JSON string từ intent
        // Nhận danh sách sản phẩm đã chọn từ Intent
        ArrayList<ResCartDetailDTO> selectedItems = (ArrayList<ResCartDetailDTO>) getIntent().getSerializableExtra("selectedItems");
        Type listType = new TypeToken<List<ResCartDetailDTO>>() {}.getType();

        // Lấy danh sách productId để gọi API giảm giá
        List<Integer> productIds = new ArrayList<>();
        for (ResCartDetailDTO item : selectedItems) {
            productIds.add(item.getProduct().getProductId());
        }

        // Thiết lập RecyclerView
        recyclerView = findViewById(R.id.recyclerViewCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AddToBagAdapter(selectedItems, () -> {
            updateTotalPrice();
        });
        recyclerView.setAdapter(adapter);
        updateGrandTotal();
        updateTotalPrice();
        fetchVoucherWithMaxDiscountByUserId();


        checkoutBtn = findViewById(R.id.checkoutBtn);
        checkoutBtn.setOnClickListener(v -> {
            Intent noti = new Intent(AddToBagActivity.this, PaymentActivity.class);
            noti.putExtra("item", item.getText().toString());
            noti.putExtra("discount", discount.getText().toString());
            noti.putExtra("voucher", voucher_discount.getText().toString());
            noti.putExtra("shipping", shipping.getText().toString());
            noti.putExtra("total_Price", total_Price.getText().toString());
            noti.putExtra("itemCount", String.valueOf(adapter.getItemCount()));
            noti.putExtra("voucherId", voucherId);
            noti.putExtra("productListJson", new Gson().toJson(selectedItems));
            noti.putExtra("selectedDeliveryId", deliveryId);
            Log.d("CHECK_VOUCHER_ID", "voucherId: " + voucherId);
            startActivity(noti);
        });


        applyBtn.setOnClickListener(v -> {
            String testCode = couponEdit.getText().toString().trim();
            if (!testCode.isEmpty()) {
                fetchAndCompareVoucherCode(testCode);
            } else {
                Toast.makeText(this, "Vui lòng nhập mã voucher", Toast.LENGTH_SHORT).show();
            }
        });

        double itemTotal = safeParse(item.getText().toString());
        double shippingCost = safeParse(shipping.getText().toString());
        double discountproduct = safeParse(discount.getText().toString());
        double voucher = safeParse(voucher_discount.getText().toString());

        double grandTotal = itemTotal + shippingCost - discountproduct - voucher;
        total_Price.setText(String.format("%.0f VND", grandTotal));

        List<ShippingDTO> provinceList = new ArrayList<>();

        DeliveryFetching deliveryapiService = APIClient.getClientWithToken(accessTokenWithUserId.first).create(DeliveryFetching.class);
        deliveryapiService.getdelivery().enqueue(new Callback<APIResponse<PaginationWrapper<ResDeliveryDTO>>>() {
            @Override
            public void onResponse(Call<APIResponse<PaginationWrapper<ResDeliveryDTO>>> call, Response<APIResponse<PaginationWrapper<ResDeliveryDTO>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    deliveryList = response.body().getData().getData();
                    for (ResDeliveryDTO delivery : deliveryList) {
                        provinceList.add(new ShippingDTO(delivery.getName(), delivery.getShippingCost(), delivery.getDeliveryId()));
                    }

                    // Tạo adapter SAU khi đã có dữ liệu
                    ArrayAdapter<ShippingDTO> adapterSpinner = new ArrayAdapter<>(
                            AddToBagActivity.this,
                            android.R.layout.simple_spinner_item,
                            provinceList
                    );
                    adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapterSpinner);

                    // Set listener sau khi gán adapter
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            ShippingDTO selected = (ShippingDTO) parent.getItemAtPosition(position);
                            double distance = selected.deliveryCost;
                            shipping.setText(String.format("%,.0f VND", distance).replace(",", "."));
                            deliveryId = selected.deliveryId;
                            deliveryName = selected.name;
                            Log.d("PROVINCE_SELECT", "Name: " + selected.name + ", Cost: " + distance + " km");
                            Log.d("PROVINCE_SELECT", "deliveryId: " + deliveryId);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });

                    spinner.setSelection(0);
                    updateGrandTotal();
                } else {
                    Log.e("USER_API", "Response unsuccessful or body is null");
                }
            }

            @Override
            public void onFailure(Call<APIResponse<PaginationWrapper<ResDeliveryDTO>>> call, Throwable t) {
                Log.e("USER_API", "Network error: " + t.getMessage());
            }
        });



        int userId = accessTokenWithUserId.second;
        String filter = "users:" + accessTokenWithUserId.second;

        UserFetching apiService = APIClient.getClientWithToken(accessTokenWithUserId.first).create(UserFetching.class);
        apiService.getUser(userId).enqueue(new Callback<APIResponse<ResUserDTO>>() {
            @Override
            public void onResponse(Call<APIResponse<ResUserDTO>> call, Response<APIResponse<ResUserDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResUserDTO user = response.body().getData();
                    Log.d("USER_API", "First: " + user.getFirstName());
                    Log.d("USER_API", "Last: " + user.getLastName());
                    Log.d("USER_API", "Phone: " + user.getPhoneNumber());
                    Log.d("USER_API",user.getBuyingAddress());
                    userName.setText("Name: " + user.getFirstName() + " " + user.getLastName());
                    Phone.setText("Phone: " + user.getPhoneNumber());
                    Editaddress.setText("Address: " + user.getBuyingAddress());
                } else {
                    Log.e("USER_API", "Response unsuccessful or body is null");
                }
            }

            @Override
            public void onFailure(Call<APIResponse<ResUserDTO>> call, Throwable t) {
                Log.e("USER_API", "Network error: " + t.getMessage());
            }
        });


    }

    private void updateTotalPrice() {
        double totalPriceBeforeDiscount = 0;
        double totalPriceAfterDiscount = 0;

        for (ResCartDetailDTO detail : adapter.getCartDetails()) {
            double price = detail.getProduct().getSellingPrice();
            int quantity = detail.getQuantity();
            totalPriceBeforeDiscount += price * quantity;

            ResDiscountDTO discountDTO = detail.getProduct().getDiscount();
            if (discountDTO != null && isWithinDiscountPeriod(discountDTO.getStartDate(), discountDTO.getEndDate())) {
                double discountPercent = discountDTO.getDiscountPercentage();
                double discountedPrice = price * (1 - discountPercent);
                totalPriceAfterDiscount += discountedPrice * quantity;
            } else {
                totalPriceAfterDiscount += price * quantity;
            }
        }

        double discountAmount = totalPriceBeforeDiscount - totalPriceAfterDiscount;
        totalPrice = totalPriceAfterDiscount;

        discount.setText(String.format("- %,.0f VND", discountAmount).replace(",", "."));
        item.setText(String.format("%,.0f VND", totalPriceBeforeDiscount).replace(",", "."));
        itemCount.setText("Item ("+ String.valueOf(adapter.getItemCount()) + ")");
        updateGrandTotal();
    }

    private void fetchAndCompareVoucherCode(String testCode) {
        Pair<String, Integer> accessTokenWithUserId = SharedPref.getAccessTokenWithUserId(AddToBagActivity.this);
        VoucherFetching apiService = APIClient.getClientWithToken(accessTokenWithUserId.first).create(VoucherFetching.class);
        String filter = "user:" + accessTokenWithUserId.second;

        apiService.getVouchersByUser(filter).enqueue(new Callback<APIResponse<ResUserVoucherDTO>>() {
            @Override
            public void onResponse(Call<APIResponse<ResUserVoucherDTO>> call, Response<APIResponse<ResUserVoucherDTO>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<ResUserVoucherDTO.UserVoucherData> allVouchers = response.body().getData().getData();

                    boolean found = false;

                    for (ResUserVoucherDTO.UserVoucherData voucher : allVouchers) {
                        if (!voucher.isUsed() && voucher.getVoucher().getCode().equalsIgnoreCase(testCode)) {
                            Log.d("VoucherMatch", "✅ Mã hợp lệ và chưa dùng: " + voucher.getVoucher().getCode());

                            // Gán ID voucher
                            voucherId = voucher.getVoucher().getVoucherId();
                            Log.d("CHECK_VOUCHER_ID", "voucherId: " + voucherId);

                            // Hiển thị giảm giá
                            voucher_discount.setText("- " + String.format("%,.0f VND", voucher.getVoucher().getDiscountAmount()));

                            // Cập nhật tổng tiền
                            updateGrandTotal();

                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        Log.d("VoucherMatch", "❌ Không tìm thấy voucher hợp lệ hoặc đã dùng với mã: " + testCode);
                        Toast.makeText(AddToBagActivity.this, "Mã voucher không hợp lệ hoặc đã được sử dụng.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Log.e("VoucherAPI", "⚠️ Không thể lấy dữ liệu voucher từ API.");
                    Toast.makeText(AddToBagActivity.this, "Lỗi khi lấy danh sách voucher.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse<ResUserVoucherDTO>> call, Throwable t) {
                Log.e("VoucherAPI", "❌ Lỗi kết nối API: " + t.getMessage());
                Toast.makeText(AddToBagActivity.this, "Không thể kết nối đến máy chủ.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchVoucherWithMaxDiscountByUserId() {
        Pair<String, Integer> accessTokenWithUserId = SharedPref.getAccessTokenWithUserId(AddToBagActivity.this);
        VoucherFetching voucherService = APIClient.getClientWithToken(accessTokenWithUserId.first).create(VoucherFetching.class);
        String filter = "users:" + accessTokenWithUserId.second;

        voucherService.getVouchersByUser(filter).enqueue(new Callback<APIResponse<ResUserVoucherDTO>>() {
            @Override
            public void onResponse(Call<APIResponse<ResUserVoucherDTO>> call, Response<APIResponse<ResUserVoucherDTO>> response) {
                if (response.isSuccessful()) {
                    APIResponse<ResUserVoucherDTO> apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getData() != null) {
                        List<ResUserVoucherDTO.UserVoucherData> allVouchers = apiResponse.getData().getData();
                        if (allVouchers != null) {
                            List<ResUserVoucherDTO.UserVoucherData> unusedVouchers = new ArrayList<>();
                            for (ResUserVoucherDTO.UserVoucherData voucher : allVouchers) {
                                if (!voucher.isUsed()) {
                                    unusedVouchers.add(voucher);
                                }
                            }

                            if (unusedVouchers.isEmpty()) {
                                Toast.makeText(AddToBagActivity.this, "Không có voucher nào chưa dùng.", Toast.LENGTH_SHORT).show();
                            }
                            return;
                        }
                    }
                    Toast.makeText(AddToBagActivity.this, "Không có dữ liệu voucher.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddToBagActivity.this, "Không thể tải danh sách voucher.", Toast.LENGTH_SHORT).show();
                    Log.e("VoucherAPI", "Phản hồi không thành công: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<APIResponse<ResUserVoucherDTO>> call, Throwable t) {
                Toast.makeText(AddToBagActivity.this, "Lỗi kết nối đến máy chủ.", Toast.LENGTH_SHORT).show();
                Log.e("VoucherAPI", "Lỗi: " + t.getMessage());
            }
        });

    }


    private double safeParse(String s) {
        if (s == null || s.isEmpty()) return 0;
        // Loại bỏ "VND", dấu cách, dấu - và dấu chấm/thập phân để chuẩn hóa
        s = s.replace("VND", "")
                .replace("vnd", "")
                .replace("-", "")
                .replace(".", "")
                .replace(",", "")
                .trim();
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private boolean isWithinDiscountPeriod(String startDateStr, String endDateStr) {
        try {
            // Chuyển từ String ISO 8601 sang OffsetDateTime rồi lấy LocalDate
            LocalDate startDate = OffsetDateTime.parse(startDateStr).toLocalDate();
            LocalDate endDate = OffsetDateTime.parse(endDateStr).toLocalDate();
            LocalDate currentDate = LocalDate.now();

            // Kiểm tra: startDate <= currentDate <= endDate
            return (currentDate.isEqual(startDate) || currentDate.isAfter(startDate)) &&
                    (currentDate.isEqual(endDate) || currentDate.isBefore(endDate));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void updateGrandTotal() {
        double itemTotal = safeParse(item.getText().toString());
        double shippingCost = safeParse(shipping.getText().toString());
        double discountAmount = safeParse(discount.getText().toString());
        double voucherAmount = safeParse(voucher_discount.getText().toString());

        double grandTotal = itemTotal + shippingCost - discountAmount - voucherAmount;
        if (grandTotal < 0) grandTotal = 0;

        total_Price.setText(String.format("%,.0f VND", grandTotal));
    }

}
