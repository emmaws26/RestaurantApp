package com.tuyp.restaurantapp.View.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tuyp.restaurantapp.Model.MainCourseModel;
import com.tuyp.restaurantapp.Model.Order;


import com.tuyp.restaurantapp.Model.SashimiModel;

import com.tuyp.restaurantapp.R;
import com.tuyp.restaurantapp.View.Adapter.MainCourseAdapter;
import com.tuyp.restaurantapp.View.Adapter.SashimiAdapter;
import com.tuyp.restaurantapp.View.ListOrderActivity;



import java.io.Serializable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainCourseFragment extends Fragment {

    RecyclerView recyclerView;
    List<MainCourseModel> mainCourseModels = new ArrayList<>();
    MainCourseAdapter mainCourseAdapter;
    List<Order> orders = new ArrayList<>();
    CardView popOrder;
    TextView qtyNumberText, priceText;
    SharedPreferences sharedPreferences1, storage;

    public MainCourseFragment() {
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
        return inflater.inflate(R.layout.fragment_main_course, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        popOrder = getActivity().findViewById(R.id.popOrder);
        qtyNumberText = getActivity().findViewById(R.id.qtyNumberText);
        priceText = getActivity().findViewById(R.id.priceText);
        recyclerView = getActivity().findViewById(R.id.recMainCourse);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        final Gson gson = new Gson();
        storage = getActivity().getSharedPreferences("storage", Context.MODE_PRIVATE);
        if (storage.getString("data-maincourse", null) == null) {
            Log.d("dataaaaa", "sashimi null");
            mainCourseModels.add(new MainCourseModel("Yakitori Ju", "Tuna", "yakitorodon", 15000));
            mainCourseModels.add(new MainCourseModel("Nabeyaki Udon", "Salmon and Tamago", "nabeyakiudon", 15000));
            mainCourseModels.add(new MainCourseModel("Soyu Ramen", "Soysouce based soup", "shoyuramen", 15000));
            mainCourseModels.add(new MainCourseModel("Unagi Hitsuma", "Grilled eef bowl", "unagihitsumabushi", 15000));
            mainCourseModels.add(new MainCourseModel("Nagitoro Don", "Minced Tuna and Onion", "negitorodon", 15000));
            SharedPreferences.Editor storageEdit = storage.edit();
            String json = gson.toJson(mainCourseModels);
            storageEdit.putString("data-maincourse", json);
            Log.d("dataaaaaa", "json = " + json);
            storageEdit.apply();
            mainCourseAdapter = new MainCourseAdapter(getContext(), mainCourseModels, this);
            recyclerView.setAdapter(mainCourseAdapter);
            mainCourseAdapter.notifyDataSetChanged();
            Log.d("dataaaaaa", "size = " + mainCourseModels.size());
        } else {

            String json = storage.getString("data-maincourse", null);
            Log.d("dataaaaaa", "json = " + json);
            Type type = new TypeToken<List<MainCourseModel>>() {
            }.getType();

            mainCourseModels = (List<MainCourseModel>) gson.fromJson(json, type);
            mainCourseAdapter = new MainCourseAdapter(getContext(), mainCourseModels, this);
            recyclerView.setAdapter(mainCourseAdapter);
            mainCourseAdapter.notifyDataSetChanged();
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


//        mainCourseAdapter = new MainCourseAdapter(getContext(), mainCourseModels);
//        recyclerView.setAdapter(mainCourseAdapter);
//        mainCourseAdapter.notifyDataSetChanged();
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
        String json = storage.getString("data-maincourse",null);
        Type type = new TypeToken<List<MainCourseModel>>() {}.getType();
        mainCourseModels = (List<MainCourseModel>) gson.fromJson(json,type);
        mainCourseAdapter = new MainCourseAdapter(getContext(),mainCourseModels,this);
        recyclerView.setAdapter(mainCourseAdapter);
        mainCourseAdapter.notifyDataSetChanged();
    }
}
