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
import com.tuyp.restaurantapp.Model.SushiModel;
import com.tuyp.restaurantapp.Model.SushiModel;
import com.tuyp.restaurantapp.R;
import com.tuyp.restaurantapp.View.Adapter.SashimiAdapter;
import com.tuyp.restaurantapp.View.Adapter.SushiAdapter;
import com.tuyp.restaurantapp.View.ListOrderActivity;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SushiFragment extends Fragment {
    RecyclerView recyclerView;
    List<SushiModel> sushiModels = new ArrayList<>();
    List<Order> orders = new ArrayList<>();
    SushiAdapter sushiAdapter;
    CardView popOrder;
    TextView qtyNumberText,priceText;
    SharedPreferences sharedPreferences1,storage;

    public SushiFragment() {
        // Required empty public constructor
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPreferences1 = getActivity().getSharedPreferences("order", Context.MODE_PRIVATE);
        String json = sharedPreferences1.getString("data-order",null);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sushi, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        popOrder = getActivity().findViewById(R.id.popOrder);
        qtyNumberText = getActivity().findViewById(R.id.qtyNumberText);
        priceText = getActivity().findViewById(R.id.priceText);
        recyclerView = getActivity().findViewById(R.id.recSushi);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        final Gson gson = new Gson();

        storage = getActivity().getSharedPreferences("storage",Context.MODE_PRIVATE);
        if (storage.getString("data-sushi", null) == null){
            sushiModels.add(new SushiModel("Jo Unagi","Tuna","roll1",55000));
            sushiModels.add(new SushiModel("Fuji Roll","Salmon and Tamago","roll2",32000));
            sushiModels.add(new SushiModel("Toro","Tuna Belly","roll3",15000));
            sushiModels.add(new SushiModel("Ebi Mentai Sushi","Cooked prawn","roll5",28000));
            sushiModels.add(new SushiModel("Spicy Maguro Roll","Spicy Tuna Roll","roll6",55000));
            sushiModels.add(new SushiModel("Ikoro Sushi","Salmon Roe on Sushi Rice","roll7",55000));
            sushiModels.add(new SushiModel("Kirisima","Tuna","roll8",125000));
            SharedPreferences.Editor storageEdit = storage.edit();
            String json = gson.toJson(sushiModels);
            storageEdit.putString("data-sushi",json);
            storageEdit.apply();
            sushiAdapter = new SushiAdapter(getContext(),sushiModels,this);
            recyclerView.setAdapter(sushiAdapter);
            sushiAdapter.notifyDataSetChanged();
        } else {
            String json = storage.getString("data-sushi",null);
            Type type = new TypeToken<List<SushiModel>>() {}.getType();

            sushiModels = (List<SushiModel>) gson.fromJson(json,type);
            sushiAdapter = new SushiAdapter(getContext(),sushiModels,this);
            recyclerView.setAdapter(sushiAdapter);
            sushiAdapter.notifyDataSetChanged();
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
                String json = sharedPreferences1.getString("data-order",null);
                Type type = new TypeToken<List<Order>>() {}.getType();
                orders = gson.fromJson(json,type);
                bundle.putSerializable("bundle", (Serializable) orders);
                checkout.putExtra("checkout-data",bundle);
                startActivity(checkout);
            }
        });
    }

    public void setVisPop(){
        int Qty = sharedPreferences1.getInt("qty_pop_total",0);
        int Price= sharedPreferences1.getInt("price_pop_total",0);
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
        setVisPop();
        Gson gson = new Gson();
        String json = storage.getString("data-sushi",null);
        Type type = new TypeToken<List<SushiModel>>() {}.getType();
        sushiModels = (List<SushiModel>) gson.fromJson(json,type);
        sushiAdapter = new SushiAdapter(getContext(),sushiModels,this);
        recyclerView.setAdapter(sushiAdapter);
        sushiAdapter.notifyDataSetChanged();
    }
}
