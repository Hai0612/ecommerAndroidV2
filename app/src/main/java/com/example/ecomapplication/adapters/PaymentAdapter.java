package com.example.ecomapplication.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecomapplication.R;
import com.example.ecomapplication.models.Payment;

import java.util.List;

public class PaymentAdapter  extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {

    private Context context;
    private List<Payment> payments;

    public PaymentAdapter(Context context, List<Payment> payments) {
        this.context = context;
        this.payments = payments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_item , parent, false)) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Glide.with(context).load(popularProductAdapterList.get(position).getImg_url()).into(holder.imageView);
        holder.number_account.setText(payments.get(position).getAccount_nb());
        holder.provider.setText(payments.get(position).getProvider());
//        holder.expired.setText(payments.get(position).getExpired());
//        holder.price.setText((Integer) products.get(position).getPrice());

    }

    @Override
    public int getItemCount() {
        return payments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView number_account, expired, provider;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.payment_image);
            number_account = itemView.findViewById(R.id.payment_account_nb);
            expired = itemView.findViewById(R.id.payment_expired);

            provider = itemView.findViewById(R.id.payment_provider);

        }
    }
}
