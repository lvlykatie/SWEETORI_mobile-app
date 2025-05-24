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
import com.example.sweetori.dto.response.ResCartDetailDTO;
import com.example.sweetori.dto.response.ResDeliveryDTO;
import com.example.sweetori.dto.response.ResDiscountDTO;
import com.example.sweetori.dto.response.ResUserDTO;
import com.example.sweetori.content.UserFetching;
import com.example.sweetori.dto.response.ResVoucherDTO;
import com.example.sweetori.dto.response.ShippingDTO;
import com.example.sweetori.content.DeliveryFetching;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddToBagActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AddToBagAdapter adapter;
    private List<ResDiscountDTO> discountList = new ArrayList<>();

    private double totalPrice, total, discountedPrice;
    private double totalQuantity;
    private int deliveryId;
    private TextView item, userName, Phone, shipping, discount, voucher_discount, total_Price;
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

        checkoutBtn = findViewById(R.id.checkoutBtn);
        checkoutBtn.setOnClickListener(v -> {
            Intent noti = new Intent(AddToBagActivity.this, PaymentActivity.class);
            noti.putExtra("item", item.getText().toString());
            noti.putExtra("discount", discount.getText().toString());
            noti.putExtra("voucher", voucher_discount.getText().toString());
            noti.putExtra("shipping", shipping.getText().toString());
            noti.putExtra("total_Price", total_Price.getText().toString());
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

        // Khởi tạo danh sách tỉnh thành và khoảng cách
        List<ShippingDTO> provinceList = new ArrayList<>();
        provinceList.add(new ShippingDTO("An Giang", 190));
        provinceList.add(new ShippingDTO("Bà Rịa - Vũng Tàu", 95));
        provinceList.add(new ShippingDTO("Bạc Liêu", 290));
        provinceList.add(new ShippingDTO("Bắc Giang", 1600));
        provinceList.add(new ShippingDTO("Bắc Kạn", 1800));
        provinceList.add(new ShippingDTO("Bắc Ninh", 1650));
        provinceList.add(new ShippingDTO("Bến Tre", 90));
        provinceList.add(new ShippingDTO("Bình Dương", 30));
        provinceList.add(new ShippingDTO("Bình Định", 630));
        provinceList.add(new ShippingDTO("Bình Phước", 120));
        provinceList.add(new ShippingDTO("Bình Thuận", 200));
        provinceList.add(new ShippingDTO("Cà Mau", 350));
        provinceList.add(new ShippingDTO("Cao Bằng", 1950));
        provinceList.add(new ShippingDTO("Cần Thơ", 170));
        provinceList.add(new ShippingDTO("Đà Nẵng", 960));
        provinceList.add(new ShippingDTO("Đắk Lắk", 350));
        provinceList.add(new ShippingDTO("Đắk Nông", 250));
        provinceList.add(new ShippingDTO("Điện Biên", 2000));
        provinceList.add(new ShippingDTO("Đồng Nai", 40));
        provinceList.add(new ShippingDTO("Đồng Tháp", 160));
        provinceList.add(new ShippingDTO("Gia Lai", 520));
        provinceList.add(new ShippingDTO("Hà Giang", 2000));
        provinceList.add(new ShippingDTO("Hà Nam", 1700));
        provinceList.add(new ShippingDTO("Hà Nội", 1730));
        provinceList.add(new ShippingDTO("Hà Tĩnh", 1170));
        provinceList.add(new ShippingDTO("Hải Dương", 1680));
        provinceList.add(new ShippingDTO("Hải Phòng", 1700));
        provinceList.add(new ShippingDTO("Hậu Giang", 200));
        provinceList.add(new ShippingDTO("Hòa Bình", 1650));
        provinceList.add(new ShippingDTO("Hưng Yên", 1680));
        provinceList.add(new ShippingDTO("Khánh Hòa", 440));
        provinceList.add(new ShippingDTO("Kiên Giang", 270));
        provinceList.add(new ShippingDTO("Kon Tum", 580));
        provinceList.add(new ShippingDTO("Lai Châu", 2100));
        provinceList.add(new ShippingDTO("Lâm Đồng", 310));
        provinceList.add(new ShippingDTO("Lạng Sơn", 1850));
        provinceList.add(new ShippingDTO("Lào Cai", 2000));
        provinceList.add(new ShippingDTO("Long An", 40));
        provinceList.add(new ShippingDTO("Nam Định", 1750));
        provinceList.add(new ShippingDTO("Nghệ An", 1300));
        provinceList.add(new ShippingDTO("Ninh Bình", 1730));
        provinceList.add(new ShippingDTO("Ninh Thuận", 350));
        provinceList.add(new ShippingDTO("Phú Thọ", 1800));
        provinceList.add(new ShippingDTO("Phú Yên", 550));
        provinceList.add(new ShippingDTO("Quảng Bình", 1070));
        provinceList.add(new ShippingDTO("Quảng Nam", 940));
        provinceList.add(new ShippingDTO("Quảng Ngãi", 820));
        provinceList.add(new ShippingDTO("Quảng Ninh", 1750));
        provinceList.add(new ShippingDTO("Quảng Trị", 1010));
        provinceList.add(new ShippingDTO("Sóc Trăng", 240));
        provinceList.add(new ShippingDTO("Sơn La", 1900));
        provinceList.add(new ShippingDTO("Tây Ninh", 95));
        provinceList.add(new ShippingDTO("Thái Bình", 1770));
        provinceList.add(new ShippingDTO("Thái Nguyên", 1750));
        provinceList.add(new ShippingDTO("Thanh Hóa", 1500));
        provinceList.add(new ShippingDTO("Thừa Thiên Huế", 870));
        provinceList.add(new ShippingDTO("Tiền Giang", 70));
        provinceList.add(new ShippingDTO("TP. Hồ Chí Minh", 0));
        provinceList.add(new ShippingDTO("Trà Vinh", 130));
        provinceList.add(new ShippingDTO("Tuyên Quang", 1850));
        provinceList.add(new ShippingDTO("Vĩnh Long", 130));
        provinceList.add(new ShippingDTO("Vĩnh Phúc", 1700));
        provinceList.add(new ShippingDTO("Yên Bái", 1900));

        ArrayAdapter<ShippingDTO> adapterSpinner = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                provinceList
        );
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ShippingDTO selected = (ShippingDTO) parent.getItemAtPosition(position);
                int distance = selected.distanceKm;
                if (distance < 70) {
                    deliveryId = 1;
                } else if (distance >= 70 && distance < 300) {
                    deliveryId = 2;
                } else if (distance >= 300 && distance < 500) {
                    deliveryId = 3;
                }else if (distance >= 500 && distance < 1000) {
                    deliveryId = 4;
                }else {
                    deliveryId = 5;
                }
                Log.d("PROVINCE_SELECT", "Tỉnh: " + selected.name + ", Cách TP.HCM: " + distance + " km");
                Log.d("PROVINCE_SELECT", "deliveryId: " + deliveryId);

                DeliveryFetching deliveryapiService = APIClient.getClientWithToken(accessTokenWithUserId.first).create(DeliveryFetching.class);
                deliveryapiService.getdelivery(deliveryId).enqueue(new Callback<APIResponse<ResDeliveryDTO>>() {
                    @Override
                    public void onResponse(Call<APIResponse<ResDeliveryDTO>> call, Response<APIResponse<ResDeliveryDTO>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ResDeliveryDTO delivery = response.body().getData();
                            Log.d("USER_API", "cost: " + delivery.getShippingCost());
                            shipping.setText(String.format("%, .0f VND",(delivery.getShippingCost())));
                            updateGrandTotal();
                        } else {
                            Log.e("USER_API", "Response unsuccessful or body is null");
                        }
                    }

                    @Override
                    public void onFailure(Call<APIResponse<ResDeliveryDTO>> call, Throwable t) {
                        Log.e("USER_API", "Network error: " + t.getMessage());
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
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
                    Log.d("USER_API", "Address: " + user.getBuyingAddress());
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

        // Lấy JSON string từ intent
        // Nhận danh sách sản phẩm đã chọn từ Intent
        String selectedItemsJson = getIntent().getStringExtra("selectedItems");
        Type listType = new TypeToken<List<ResCartDetailDTO>>() {}.getType();
        List<ResCartDetailDTO> selectedItems = new Gson().fromJson(selectedItemsJson, listType);

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
    }

    private void updateTotalPrice() {
        double totalPriceBeforeDiscount = 0;
        double totalPriceAfterDiscount = 0;

        for (ResCartDetailDTO detail : adapter.getCartDetails()) {
            double price = detail.getProduct().getSellingPrice();
            double discountPercent = detail.getProduct().getDiscount().getDiscountPercentage();
            int quantity = detail.getQuantity();

            totalPriceBeforeDiscount += price * quantity;
            totalPriceAfterDiscount += price * (discountPercent) * quantity;
        }

        totalPrice = totalPriceBeforeDiscount;

        discount.setText(String.format("- %,.0f VND", totalPriceAfterDiscount).replace(",", "."));
        item.setText(String.format("%,.0f VND", totalPrice).replace(",", "."));
        updateGrandTotal();

    }
    private void fetchAndCompareVoucherCode(String testCode) {
        Pair<String, Integer> accessTokenWithUserId = SharedPref.getAccessTokenWithUserId(AddToBagActivity.this);
        VoucherFetching api_ServiceVouch = APIClient.getClientWithToken(accessTokenWithUserId.first).create(VoucherFetching.class);
        api_ServiceVouch.getAllVouchers().enqueue(new Callback<APIResponse<ResVoucherDTO>>() {
            @Override
            public void onResponse(Call<APIResponse<ResVoucherDTO>> call, Response<APIResponse<ResVoucherDTO>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<ResVoucherDTO.VoucherData> voucherList = response.body().getData().getData();

                    boolean found = false;
                    for (ResVoucherDTO.VoucherData voucher : voucherList) {
                        if (voucher.getCode().equalsIgnoreCase(testCode)) {
                            Log.d("VoucherMatch", "✅ Mã hợp lệ: " + voucher.getCode());
                            voucher_discount.setText(String.format("%, .0f VND",(voucher.getDiscountAmount())));
                            updateGrandTotal();
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        Log.d("VoucherMatch", "❌ Không tìm thấy voucher với mã: " + testCode);
                    }

                } else {
                    Log.e("VoucherAPI", "⚠️ API trả về lỗi hoặc không có dữ liệu.");
                }
            }

            @Override
            public void onFailure(Call<APIResponse<ResVoucherDTO>> call, Throwable t) {
                Log.e("VoucherAPI", "❌ Lỗi kết nối API: " + t.getMessage());
            }
        });
    }

    private void fetchVoucherWithMaxDiscountByUserId() {
        Pair<String, Integer> accessTokenWithUserId = SharedPref.getAccessTokenWithUserId(AddToBagActivity.this);
        VoucherFetching voucherService = APIClient.getClientWithToken(accessTokenWithUserId.first).create(VoucherFetching.class);
        String filter = "users:" + accessTokenWithUserId.second;

        voucherService.getVoucherByUser(filter).enqueue(new Callback<APIResponse<ResVoucherDTO>>() {
            @Override
            public void onResponse(Call<APIResponse<ResVoucherDTO>> call, Response<APIResponse<ResVoucherDTO>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<ResVoucherDTO.VoucherData> voucherList = response.body().getData().getData();

                    if (!voucherList.isEmpty()) {
                        ResVoucherDTO.VoucherData maxDiscountVoucher = null;
                        double maxDiscount = Double.MIN_VALUE;

                        for (ResVoucherDTO.VoucherData voucher : voucherList) {
                            if (voucher.getDiscountAmount() > maxDiscount) {
                                maxDiscount = voucher.getDiscountAmount();
                                maxDiscountVoucher = voucher;
                            }
                        }

                        if (maxDiscountVoucher != null) {
                            Log.d("VoucherInfo", "🎟️ Max discount voucher for userId " + accessTokenWithUserId.second +
                                    ": " + maxDiscountVoucher.getCode() + " with discount " + maxDiscountVoucher.getDiscountAmount());
                            voucher_discount.setText(String.format("%,.0f VND", maxDiscountVoucher.getDiscountAmount()));
                        }
                    } else {
                        Log.d("VoucherInfo", "⚠️ No vouchers found for userId: " + accessTokenWithUserId.second);
                    }
                } else {
                    Log.e("VoucherAPI", "⚠️ API responded with an error or no data.");
                }
            }

            @Override
            public void onFailure(Call<APIResponse<ResVoucherDTO>> call, Throwable t) {
                Log.e("VoucherAPI", "❌ Failed to connect to voucher API: " + t.getMessage());
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

