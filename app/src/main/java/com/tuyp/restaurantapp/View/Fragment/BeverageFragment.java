package com.tuyp.restaurantapp.View.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tuyp.restaurantapp.Model.BeverageModel;
import com.tuyp.restaurantapp.Model.Order;

import com.tuyp.restaurantapp.R;
import com.tuyp.restaurantapp.View.Adapter.BeverageAdapter;
import com.tuyp.restaurantapp.View.ListOrderActivity;


import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BeverageFragment extends Fragment {

    RecyclerView recyclerView;
    BeverageAdapter beverageAdapter;
    CardView popOrder;
    List<BeverageModel> beverageModels = new ArrayList<>();
    List<Order> orders = new ArrayList<>();
    TextView qtyNumberText, priceText;
    SharedPreferences sharedPreferences1, storage;

    public BeverageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sharedPreferences1 = getActivity().getSharedPreferences("order", Context.MODE_PRIVATE);
        // Inflate the layout for this fragment
        Log.d("dataaaaa", "masuk sini createview");
        String json = sharedPreferences1.getString("data-order", null);
        Log.d("dataaaaaaaa", "json oncreate = " + json);
        return inflater.inflate(R.layout.fragment_beverage, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        popOrder = getActivity().findViewById(R.id.popOrder);
        qtyNumberText = getActivity().findViewById(R.id.qtyNumberText);
        priceText = getActivity().findViewById(R.id.priceText);
        recyclerView = getActivity().findViewById(R.id.recBeverage);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        final Gson gson = new Gson();
        storage = getActivity().getSharedPreferences("storage", Context.MODE_PRIVATE);
        if (storage.getString("data-beverage", null) == null) {
            Log.d("dataaaaa", "beverage null");
            beverageModels.add(new BeverageModel("Green Addict", "Hot/Cold", "greenaddict", 20000));
            beverageModels.add(new BeverageModel("Honey Dew", "Hot/Cold", "honeydew", 20000));
            beverageModels.add(new BeverageModel("Orange Peach Tea", "Hot/Cold", "orangepeachtea", 20000));
            SharedPreferences.Editor storageEdit = storage.edit();
            String json = gson.toJson(beverageModels);
            storageEdit.putString("data-beverage", json);
            Log.d("dataaaaaa", "json = " + json);
            storageEdit.commit();
            beverageAdapter = new BeverageAdapter(getContext(), beverageModels, this);
            recyclerView.setAdapter(beverageAdapter);
            beverageAdapter.notifyDataSetChanged();
            Log.d("dataaaaaa", "size = " + beverageModels.size());
        } else {

            String json = storage.getString("data-beverage", null);
            Log.d("dataaaaaa", "json = " + json);
            Type type = new TypeToken<List<BeverageModel>>() {
            }.getType();

            beverageModels = (List<BeverageModel>) gson.fromJson(json, type);
            beverageAdapter = new BeverageAdapter(getContext(), beverageModels, this);
            recyclerView.setAdapter(beverageAdapter);
            beverageAdapter.notifyDataSetChanged();
        }

        if (sharedPreferences1.getString("data-order",null) != null){
            String json = sharedPreferences1.getString("data-order",null);
            Type type = new TypeToken<List<Order>>() {}.getType();
            orders = gson.fromJson(json,type);
            if (orders != null){
                setVisPop();
            }
        }

        popOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent checkout = new Intent(getActivity(), ListOrderActivity.class);
                Bundle bundle = new Bundle();
                Log.d("dataaaaaaaa","size intent = "+orders.size());
                String json = sharedPreferences1.getString("data-order",null);
                Type type = new TypeToken<List<Order>>() {}.getType();
                orders = gson.fromJson(json,type);
                bundle.putSerializable("bundle", (Serializable) orders);
                checkout.putExtra("checkout-data",bundle);
                startActivity(checkout);




            }
        });


    }

    public void setVisPop() {
        int Qty = sharedPreferences1.getInt("qty_pop_total", 0);
        int Price = sharedPreferences1.getInt("price_pop_total", 0);
        if (Qty != 0){
            popOrder.setVisibility(View.VISIBLE);
        }else {
            popOrder.setVisibility(View.GONE);
        }
        qtyNumberText.setText(Qty + "  items");
        priceText.setText("" + Price);
    }

    @Override
    public void onResume() {
        super.onResume();
        setVisPop();
        Gson gson = new Gson();
        String json = storage.getString("data-beverage",null);
        Type type = new TypeToken<List<BeverageModel>>() {}.getType();
        beverageModels = (List<BeverageModel>) gson.fromJson(json,type);
        beverageAdapter = new BeverageAdapter(getContext(),beverageModels,this);
        recyclerView.setAdapter(beverageAdapter);
        beverageAdapter.notifyDataSetChanged();
    }
}
