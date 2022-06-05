package com.example.ecomapplication.ui.cart;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecomapplication.Helper.NotificationApi;
import com.example.ecomapplication.MainActivity;
import com.example.ecomapplication.R;
import com.example.ecomapplication.activities.CheckoutActitvity;
import com.example.ecomapplication.adapters.MyCartAdapter;
import com.example.ecomapplication.models.FCMNotification;
import com.example.ecomapplication.models.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartFragment extends Fragment {

    Button buttonMinus, buttonPlus, button_buy_now, test_noti;
    int overAllTotalAmount;
    TextView overAllAmount;
    TextView gioHangTrong;
    Toolbar toolbar;
    RecyclerView recyclerView;
    List<Product> cartModelList;
    MyCartAdapter cartAdapter;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_cart, container, false);

        firestore = FirebaseFirestore.getInstance();

        toolbar = root.findViewById(R.id.my_cart_toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        get data from my cart adapter
        LocalBroadcastManager.getInstance(getContext())
                .registerReceiver(mMessageReceiver, new IntentFilter("MyTotalAmount"));
        auth = FirebaseAuth.getInstance();
        buttonMinus = root.findViewById(R.id.button_minus);
        buttonPlus = root.findViewById(R.id.button_plus);
        overAllAmount = root.findViewById(R.id.total_amount);
        gioHangTrong = root.findViewById(R.id.gioHangTrong);
        recyclerView = root.findViewById(R.id.cart_rec);
        button_buy_now = root.findViewById(R.id.button_buy_now);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartModelList = new ArrayList<>();
        cartAdapter = new MyCartAdapter(getContext(), cartModelList);
        recyclerView.setAdapter(cartAdapter);
        test_noti = root.findViewById(R.id.testnoti);

        firestore.collection("Cart").document(auth.getUid())
                .collection("Products").get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot doc :task.getResult().getDocuments()) {
    //                        Log.v("Test", auth.getCurrentUser().getUid());
                            Product myCartModel = doc.toObject(Product.class);
                            myCartModel.setDocumentId(doc.getId());
                            cartModelList.add(myCartModel);
                            cartAdapter.notifyDataSetChanged();
                            gioHangTrong.setVisibility(View.GONE);
                            button_buy_now.setEnabled(true);
                        }
                    }
                });
        if (cartModelList.size() == 0) {
            gioHangTrong.setVisibility(View.VISIBLE);
            button_buy_now.setEnabled(false);
        }

        button_buy_now.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), CheckoutActitvity.class);
            intent.putExtra("totalOrder",overAllTotalAmount);
            startActivity(intent);
            Toast.makeText(getContext(), " " + cartModelList.size(), Toast.LENGTH_SHORT).show();
        });

        test_noti.setOnClickListener(view -> {
            FCMNotification.Notification noti = FCMNotification.createNotification(
                    "Thông báo đơn hàng",
                    "Khách hàng X254QH71 đã đặt hàng của bạn. Hãy xác nhận."
            );

            FCMNotification.Data data = FCMNotification.createData(
                    "Thông báo đơn hàng",
                    "Khách hàng X254QH71 đã đặt hàng của bạn. Hãy xác nhận."
            );

            List<String> registrationIds = new ArrayList<>();
            registrationIds.add("cVzXNXUxQwWyBnzurmnMlP:APA91bGifIKqvsmLnSleqs3ca6aouU9gmx09TvE7euAVQOJ_ysUCmVuH7rDXxw7TQU58I1k46wGIfixev2jHWSGg0dym_s8vvKWhl6FA6G13zAegYcB5uYwTsULqQ7Uf_Ol2PnZTCh3M");

            FCMNotification notification = new FCMNotification(noti, data, registrationIds);

            new PushNotification(getContext()).execute(notification);
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setActionBarTitle("My Cart");
        }
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            overAllTotalAmount = intent.getIntExtra("totalAmount", 0);
            overAllAmount.setText(NumberFormat.getNumberInstance(new Locale("vi", "VN")).format(overAllTotalAmount) + "VND");
            if(overAllTotalAmount == 0){
                button_buy_now.setEnabled(false);
            }
        }
    };

    public void refreshActivtiy() {
//        recreate();
    }

    private static class PushNotification extends AsyncTask<Object, Void, String> {
        protected ProgressDialog progressDialog;
        protected Context context;

        public PushNotification(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(context, 1);
            this.progressDialog.setMessage("Creating Order...");
            this.progressDialog.show();
        }

        @Override
        protected String doInBackground(Object... objects) {
            String response = null;

            try {
                Log.v("Test", "Sending notification...");
                response = NotificationApi.pushNotification((FCMNotification) objects[0]);
            } catch (Exception e) {
                Log.v("Test", "POST Error: " + e.getMessage());
            }

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }


    }
}