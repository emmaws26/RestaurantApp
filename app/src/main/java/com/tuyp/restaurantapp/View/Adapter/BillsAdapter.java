package com.tuyp.restaurantapp.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tuyp.restaurantapp.Model.Order;
import com.tuyp.restaurantapp.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class BillsAdapter extends RecyclerView.Adapter<BillsAdapter.BillViewHolder> {

    Context context;
    LayoutInflater layoutInflater;
    List<Order> orders;

    public BillsAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.bill_layout,parent,false);
        BillViewHolder holder = new BillViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        Locale localeID = new Locale("in","ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
        holder.foodName.setText(orders.get(position).getName());
        holder.foodPrice.setText(format.format(orders.get(position).getPrice()).toString());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class BillViewHolder extends RecyclerView.ViewHolder {
        TextView foodName,foodPrice;
        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodName);
            foodPrice = itemView.findViewById(R.id.foodPrice);
        }
    }
}
