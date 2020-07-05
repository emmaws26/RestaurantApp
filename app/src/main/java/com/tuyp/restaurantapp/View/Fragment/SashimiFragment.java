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
import com.tuyp.restaurantapp.Model.Order;
import com.tuyp.restaurantapp.Model.SashimiModel;
import com.tuyp.restaurantapp.R;
import com.tuyp.restaurantapp.View.Adapter.SashimiAdapter;
import com.tuyp.restaurantapp.View.ListOrderActivity;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SashimiFragment extends Fragment {

    RecyclerView recyclerView;
    List<SashimiModel> sashimiModels = new ArrayList<>();
    List<Order> orders = new ArrayList<>();
    SashimiAdapter sashimiAdapter;
    CardView popOrder;
    TextView qtyNumberText,priceText;
    SharedPreferences sharedPreferences1,storage;
    public SashimiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPreferences1 = getActivity().getSharedPreferences("order", Context.MODE_PRIVATE);
        // Inflate the layout for this fragment
        Log.d("dataaaaa","masuk sini createview");
        String json = sharedPreferences1.getString("data-order",null);
        Log.d("dataaaaaaaa","json oncreate = "+json);
        return inflater.inflate(R.layout.fragment_sashimi, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("dataaaaa","masuk sini createview ac");

        popOrder = getActivity().findViewById(R.id.popOrder);
        qtyNumberText = getActivity().findViewById(R.id.qtyNumberText);
        priceText = getActivity().findViewById(R.id.priceText);
        recyclerView = getActivity().findViewById(R.id.recSashimi);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        final Gson gson = new Gson();

        storage = getActivity().getSharedPreferences("storage",Context.MODE_PRIVATE);
            if (storage.getString("data-sashimi" , null) == null){
                Log.d("dataaaaa","sashimi null");
                sashimiModels.add(new SashimiModel("Yummy","Tuna","maguro",42000));
                sashimiModels.add(new SashimiModel("Mekajiki","Swordfish","sashimi2",55000));
                sashimiModels.add(new SashimiModel("Toro","Tuna Belly","sashimi3",70000));
                sashimiModels.add(new SashimiModel("Salmon Ikura ","Salom roe on sushi rice","sashimi7",68000));
                sashimiModels.add(new SashimiModel("Yufu","Sashimi Mariawase","sashimi5",100000));
                SharedPreferences.Editor storageEdit = storage.edit();
                String json = gson.toJson(sashimiModels);
                storageEdit.putString("data-sashimi",json);
                Log.d("dataaaaaa","json = "+json);
                storageEdit.apply();
                sashimiAdapter = new SashimiAdapter(getContext(),sashimiModels,this);
                recyclerView.setAdapter(sashimiAdapter);
                sashimiAdapter.notifyDataSetChanged();
                Log.d("dataaaaaa","size = "+sashimiModels.size());
            } else {

            String json = storage.getString("data-sashimi",null);
            Log.d("dataaaaaa","json = "+json);
            Type type = new TypeToken<List<SashimiModel>>() {}.getType();

            sashimiModels = (List<SashimiModel>) gson.fromJson(json,type);
            sashimiAdapter = new SashimiAdapter(getContext(),sashimiModels,this);
            recyclerView.setAdapter(sashimiAdapter);
            sashimiAdapter.notifyDataSetChanged();
        }

        if (sharedPreferences1.getString("data-order",null) != null){
            String json = sharedPreferences1.getString("data-order",null);
            Type type = new TypeToken<List<Order>>() {}.getType();
            orders = gson.fromJson(json,type);
            if (orders != null){
                setVisPop(true);
            }else {
                setVisPop(false);
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

    public void setVisPop(Boolean visible){
        int Qty = sharedPreferences1.getInt("qty_pop_total",0);
        int Price= sharedPreferences1.getInt("price_pop_total",0);
        Log.d("dataaaaaa","qty = "+Qty);
        if (Qty != 0){
            popOrder.setVisibility(View.VISIBLE);
        }else {
            popOrder.setVisibility(View.GONE);
        }

        qtyNumberText.setText(Qty+"  items");
        priceText.setText(""+Price);
    }

    @Override
    public void onResume() {
        super.onResume();
        setVisPop(true);
        Gson gson = new Gson();
        String json = storage.getString("data-sashimi",null);
        Type type = new TypeToken<List<SashimiModel>>() {}.getType();
        sashimiModels = (List<SashimiModel>) gson.fromJson(json,type);
        sashimiAdapter = new SashimiAdapter(getContext(),sashimiModels,this);
        recyclerView.setAdapter(sashimiAdapter);
        sashimiAdapter.notifyDataSetChanged();
    }
}
