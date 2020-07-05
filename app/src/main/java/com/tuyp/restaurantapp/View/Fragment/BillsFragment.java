package com.tuyp.restaurantapp.View.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tuyp.restaurantapp.Config.NotificationManager;
import com.tuyp.restaurantapp.MainActivity;
import com.tuyp.restaurantapp.Model.Order;
import com.tuyp.restaurantapp.R;
import com.tuyp.restaurantapp.View.Adapter.BillsAdapter;
import com.tuyp.restaurantapp.View.ListOrderActivity;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class BillsFragment extends Fragment {

    RecyclerView recBill;
    List<Order> orders = new ArrayList<>();
    SharedPreferences sharedPreferences;
    Gson gson = new Gson();
    BillsAdapter billsAdapter;
    TextView foodPriceTotal;
    Button btnProcess;
    int price = 0;
    public BillsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bills, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Locale localeID = new Locale("in","ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
        sharedPreferences = getActivity().getSharedPreferences("order", Context.MODE_PRIVATE);
        recBill = getActivity().findViewById(R.id.recBill);
        foodPriceTotal = getActivity().findViewById(R.id.foodPriceTotal);
        btnProcess = getActivity().findViewById(R.id.btnProses);
        recBill.setLayoutManager(new LinearLayoutManager(getContext()));
        if (sharedPreferences.getString("data-order",null) != null){
            String json = sharedPreferences.getString("data-order",null);
            Type type = new TypeToken<List<Order>>() {}.getType();
            orders = gson.fromJson(json,type);
            billsAdapter = new BillsAdapter(getContext(),orders);
            recBill.setAdapter(billsAdapter);
            billsAdapter.notifyDataSetChanged();
            for (int i = 0;i<orders.size();i++){
                price += orders.get(i).getPrice();
            }
            foodPriceTotal.setText(format.format(price));
            if(orders.size() == 0) {
                btnProcess.setVisibility(View.GONE);
            }else {
                btnProcess.setVisibility(View.VISIBLE);
            }

        }else{
            btnProcess.setVisibility(View.GONE);
        }

        btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationManager notificationManager = new NotificationManager(getActivity());
                notificationManager.setNotif();
                sharedPreferences.edit().clear().commit();
                Intent done = new Intent(getActivity(), MainActivity.class);
                startActivity(done);
                orders.removeAll(orders);
                Log.d("dataaaaaa","order = "+orders);
                getActivity().finish();
            }
        });
    }
}
