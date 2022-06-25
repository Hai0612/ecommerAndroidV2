package com.example.ecomapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.ecomapplication.BuildConfig;
import com.example.ecomapplication.MainActivity;
import com.example.ecomapplication.R;
import com.example.ecomapplication.models.OrderPlace;
import com.example.ecomapplication.models.Payment;
import com.google.firebase.database.annotations.NotNull;
import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.approve.Approval;
import com.paypal.checkout.approve.OnApprove;
import com.paypal.checkout.cancel.OnCancel;
import com.paypal.checkout.config.CheckoutConfig;
import com.paypal.checkout.config.Environment;
import com.paypal.checkout.config.SettingsConfig;
import com.paypal.checkout.createorder.CreateOrder;
import com.paypal.checkout.createorder.CreateOrderActions;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.OrderIntent;
import com.paypal.checkout.createorder.UserAction;
import com.paypal.checkout.error.ErrorInfo;
import com.paypal.checkout.error.OnError;
import com.paypal.checkout.order.Amount;
import com.paypal.checkout.order.AppContext;
import com.paypal.checkout.order.CaptureOrderResult;
import com.paypal.checkout.order.OnCaptureComplete;
import com.paypal.checkout.order.Order;
import com.paypal.checkout.order.PurchaseUnit;
import com.paypal.checkout.paymentbutton.PayPalButton;

import java.util.ArrayList;
import java.util.Date;

public class Paypal extends AppCompatActivity {
    PayPalButton payPalButton ;
    private static  final String YOUR_CLIENT_ID = "Af5lN3Np_V4Fh7PHES8kwfm6s07CzqyT8HgRLasT5GHMZB7ZN7g7__r0EPzCg9UZo2IkBJ0YjLC7RemE";
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal);
        payPalButton = findViewById(R.id.payPalButton);
        Intent intent = getIntent();
        String amount = intent.getExtras().getString("amount");
        String auth = intent.getExtras().getString("auth");
        String orderAddress = intent.getExtras().getString("orderAddress");
        int total = Integer.valueOf(amount) / 20000;
        Log.v("TAGGG", String.valueOf(total));
        CheckoutConfig config = new CheckoutConfig(
                getApplication(),
                YOUR_CLIENT_ID,
                Environment.SANDBOX,
                String.format("%s://paypalpay", "com.example.ecomapplication"),
                CurrencyCode.USD,
                UserAction.PAY_NOW,
                new SettingsConfig(
                        true,true
                )
        );
        PayPalCheckout.setConfig(config);
        String finalAmount = String.valueOf(total);
        payPalButton.setup(
                new CreateOrder() {
                    @Override
                    public void create(@NotNull CreateOrderActions createOrderActions) {
                        Log.v("taggg", "fdsfdsf");
                        ArrayList<PurchaseUnit> purchaseUnits = new ArrayList<>();
                        purchaseUnits.add(
                                new PurchaseUnit.Builder()
                                        .amount(
                                                new Amount.Builder()
                                                        .currencyCode(CurrencyCode.USD)
                                                        .value("0.5")
                                                        .build()
                                        )
                                        .build()
                        );
                        Order order = new Order(
                                OrderIntent.CAPTURE,
                                new AppContext.Builder()
                                        .userAction(UserAction.PAY_NOW)
                                        .build(),
                                purchaseUnits
                        );
                        createOrderActions.create(order, (CreateOrderActions.OnOrderCreated) null);
                    }
                },
                new OnApprove() {
                    @Override
                    public void onApprove(@NotNull Approval approval) {
                        approval.getOrderActions().capture(new OnCaptureComplete() {
                            @Override
                            public void onCaptureComplete(@NotNull CaptureOrderResult result) {
                                progressDialog = new ProgressDialog(getApplication());
                                progressDialog.setTitle("Đang thanh toán");
                                progressDialog.setMessage("Vui lòng chờ...");
                                progressDialog.setCanceledOnTouchOutside(true);
                                OrderWithPaypal orderWithPaypal = new OrderWithPaypal( orderAddress, Integer.valueOf(total), auth, new Date() , new Date());
                                Log.v("aaaaa", auth);
                                Log.v("aaaaa", orderAddress);
//                                orderWithPaypal.order();
                                showSuccessDialog();
                            }
                        });
                    }
                },
                new OnCancel() {
                    @Override
                    public void onCancel() {
                        Log.d("OnCancel", "Buyer cancelled the PayPal experience.");
                    }
                },
                new OnError() {
                    @Override
                    public void onError(@NotNull ErrorInfo errorInfo) {
                        Log.d("OnError", String.format("Error: %s", errorInfo));
                    }
                }
        );

    }
    public void showSuccessDialog(){
        new AlertDialog.Builder(this)
                .setTitle("Thanh toán thành công")
                .setMessage("Xem đơn hàng ngay bây giờ.")
                .setPositiveButton("Ok", (dialog, which) -> {
                    Log.v("taggggg", "fdsfdsf");
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    dialog.dismiss();
                })
                .show();
    }
    public void showFailDialog(){
        new AlertDialog.Builder(this)
                .setTitle("Thanh toán không thành công")
                .setMessage("Bạn có muốn thử lại không ?")
                .setPositiveButton("Có", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setNegativeButton("Không", (dialog, which) -> {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    dialog.dismiss();
                })
                .show();
    }
}