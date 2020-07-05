package com.tuyp.restaurantapp.View.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tuyp.restaurantapp.Model.Order;
import com.tuyp.restaurantapp.Model.BeverageModel;
import com.tuyp.restaurantapp.R;
import com.tuyp.restaurantapp.View.Fragment.BeverageFragment;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BeverageAdapter extends RecyclerView.Adapter<BeverageAdapter.BeverageViewHolder> {

    List<BeverageModel> beverageModels;
    List<Order> orders = new ArrayList<>();
    Context context;
    LayoutInflater layoutInflater;
    BeverageFragment beverageFragment;

    public BeverageAdapter( Context context,List<BeverageModel> beverageModels,BeverageFragment beverageFragment) {
        this.beverageModels = beverageModels;
        this.context = context;
        this.beverageFragment = beverageFragment;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public BeverageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.beverage_item,parent,false);
        BeverageViewHolder holder = new BeverageViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final BeverageViewHolder holder, int position) {
        Locale localeID = new Locale("in","ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
        final Gson gson = new Gson();
        final SharedPreferences sharedPreferences = context.getSharedPreferences("order",Context.MODE_PRIVATE);
        SharedPreferences storage = context.getSharedPreferences("storage",Context.MODE_PRIVATE);
        String storage1 = storage.getString("data-beverage",null);
//        Log.d("dataaaaa","isi storage = "+storage1);
        if (sharedPreferences.getString("data-order",null)!= null){
//            Log.d("dataaaaaa","masuk tidak null atas");
            String json = sharedPreferences.getString("data-order",null);
            Type type = new TypeToken<List<Order>>() {}.getType();
            orders = (List<Order>) gson.fromJson(json,type);
//            Log.d("dataaaaaa","order = "+orders.get(0).getName());
            for (int i = 0; i<orders.size();i++){
                if (beverageModels.get(position).getName().equals(orders.get(i).getName())){
                    holder.qtyText.setText(""+orders.get(i).getQty());
                }
            }

//            Log.d("dataaaaaaaaaa","size dalam share = "+orders.size());
        }else {

        }
        int resource = context.getResources().getIdentifier(beverageModels.get(position).getImage(),"drawable",context.getPackageName());

        holder.imgFood.setImageResource(resource);

        holder.foodName.setText(beverageModels.get(position).getName());
        holder.descFood.setText(beverageModels.get(position).getDescName());
        holder.foodPrice.setText(format.format(beverageModels.get(position).getPrice()).toString());
        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty =0;
                int pricepop = 0;
                int jumlah = Integer.parseInt(holder.qtyText.getText().toString());
                int hitung = jumlah+1;

                int price = beverageModels.get(holder.getAdapterPosition()).getPrice() * hitung;
                holder.qtyText.setText(String.valueOf(hitung));


                if (orders.size() > 0){
                    Log.d("dataaaaaa","masuk > 0");
                    for (int i=0;i<orders.size();i++){

                        if (orders.get(i).getName().equals(beverageModels.get(holder.getAdapterPosition()).getName())){


                            orders.set(i,new Order(beverageModels.get(holder.getAdapterPosition()).getName(),hitung,price,beverageModels.get(holder.getAdapterPosition()).getImage()));
                            String json = gson.toJson(orders);
                            SharedPreferences.Editor editorPref = sharedPreferences.edit();
                            for (int k = 0;k<orders.size();k++){
                                qty += orders.get(k).getQty();
                                pricepop += orders.get(k).getPrice();

//                                       Log.d("dataaaaaaa","total = "+qty);
                            }
                            editorPref.putInt("qty_pop_total",qty);
                            editorPref.putInt("price_pop_total",pricepop);
                            editorPref.putString("data-order",json);
                            editorPref.apply();
                            beverageFragment.setVisPop();
                            break;
                        }else {
                            for (int j = 0;j<orders.size();j++){
                                if (beverageModels.get(holder.getAdapterPosition()).getName().equals(orders.get(j).getName())){
                                    orders.set(j,new Order(beverageModels.get(holder.getAdapterPosition()).getName(),hitung,price,beverageModels.get(holder.getAdapterPosition()).getImage()));
                                    String json = gson.toJson(orders);
                                    SharedPreferences.Editor editorPref = sharedPreferences.edit();
                                    editorPref.putString("data-order",json);
                                    editorPref.apply();
                                    for (int k = 0;k<orders.size();k++){
                                        qty += orders.get(k).getQty();
                                        pricepop += orders.get(k).getPrice();

//                                       Log.d("dataaaaaaa","total = "+qty);
                                    }
                                    editorPref.putInt("qty_pop_total",qty);
                                    editorPref.putInt("price_pop_total",pricepop);
                                    editorPref.commit();
                                    beverageFragment.setVisPop();
                                    return;
                                }
                            }

//                           Log.d("dataaaa","keluar for");
                            String json = gson.toJson(orders);
                            orders.add(new Order(beverageModels.get(holder.getAdapterPosition()).getName(),hitung,price,beverageModels.get(holder.getAdapterPosition()).getImage()));
                            SharedPreferences.Editor editorPref = sharedPreferences.edit();
//                           editorPref.putString("data-order",json);
//                           editorPref.apply();
                            for (int k = 0;k<orders.size();k++){
                                qty += orders.get(k).getQty();
                                pricepop += orders.get(k).getPrice();

//                                       Log.d("dataaaaaaa","total = "+qty);
                            }
                            editorPref.putInt("qty_pop_total",qty);
                            editorPref.putInt("price_pop_total",pricepop);
                            editorPref.putString("data-order",json);
                            editorPref.apply();
                            beverageFragment.setVisPop();
//                           String json = gson.toJson(orders);
//                           Log.d("dataaaaa","posisi = "+i);
//                           Log.d("dataaaaa","json tidak cointain = "+json);
                            break;
                        }
                    }
                    String getData = sharedPreferences.getString("data-order",null);
//                    Log.d("isi get","isi get = "+getData);

//                    Log.d("dataaaaaaaaaa","size = "+orders.size());



                }else {
                    Log.d("dataaaaa","masuk size = 0");
                    orders.add(new Order(beverageModels.get(holder.getAdapterPosition()).getName(),hitung,price,beverageModels.get(holder.getAdapterPosition()).getImage()));
                    String json = gson.toJson(orders);
                    SharedPreferences.Editor editorPref = sharedPreferences.edit();
                    editorPref.putString("data-order",json);
                    editorPref.apply();
                    String getData = sharedPreferences.getString("data-order",null);
                    Log.d("isi get","isi get = "+getData);
                    for (int k = 0;k<orders.size();k++){
                        qty += orders.get(k).getQty();
                        pricepop += orders.get(k).getPrice();
                        int totalQty = 0;
                    }

                    editorPref.putInt("qty_pop_total",qty);
                    editorPref.putInt("price_pop_total",pricepop);
                    editorPref.commit();
                    Log.d("data order","isi = "+orders.size());
                    beverageFragment.setVisPop();
                }
                SharedPreferences.Editor editorPref = sharedPreferences.edit();

                String json = gson.toJson(orders);
                Log.d("dataaaaa","json d luar smua  = "+json);
                if (sharedPreferences.getString("data-order",null)!= null){
                    Log.d("dataaa","masuk share tidak null bawah");
                    String jsonnew = gson.toJson(orders);
                    Log.d("dataaaaaa","json new = "+jsonnew);
                    SharedPreferences.Editor editornew = sharedPreferences.edit();
                    editornew.putString("data-order",jsonnew);
                    editornew.apply();
                }else {
                    Log.d("dataaa","masuk share  null");
//                    SharedPreferences.Editor editorPref = sharedPreferences.edit();
                    editorPref.putString("data-order",json);
                    editorPref.apply();
                }








            }
        });
        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty =0;
                int pricepop = 0;
                int jumlah = Integer.parseInt(holder.qtyText.getText().toString());
                String json = gson.toJson(orders);
                if (jumlah > 0){
                    int hitung = jumlah - 1;
                    holder.qtyText.setText(String.valueOf(hitung));
                    int price = beverageModels.get(holder.getAdapterPosition()).getPrice() * hitung;
                    Log.d("dataaaaa","json d luar smua  = "+json);
                    if (orders.size() > 0){
                        for (int i=0;i<orders.size();i++){
                            if (orders.get(i).getName().equals(beverageModels.get(holder.getAdapterPosition()).getName())){
                                orders.set(i,new Order(beverageModels.get(holder.getAdapterPosition()).getName(),hitung,price,beverageModels.get(holder.getAdapterPosition()).getImage()));
                                break;
                            }else {
                                for (int j = 0;j<orders.size();j++){
                                    if (beverageModels.get(holder.getAdapterPosition()).getName().equals(orders.get(j).getName())){
                                        Log.d("dataaaaaaaaaa","ada yg sama");
                                        orders.set(j,new Order(beverageModels.get(holder.getAdapterPosition()).getName(),hitung,price,beverageModels.get(holder.getAdapterPosition()).getImage()));
                                        json = gson.toJson(orders);
                                        Log.d("dataaaaa","json dlm contain = "+json);
                                        SharedPreferences.Editor editorPref = sharedPreferences.edit();
                                        editorPref.putString("data-order",json);
                                        editorPref.apply();
                                        for (int k = 0;k<orders.size();k++){

                                            qty += orders.get(k).getQty();
                                            pricepop += orders.get(k).getPrice();
                                            int totalQty = 0;
                                            totalQty = totalQty + qty;
                                            Log.d("dataaaaaaa","total = "+qty);
                                        }
                                        editorPref.putInt("qty_pop_total",qty);
                                        editorPref.putInt("price_pop_total",pricepop);
                                        editorPref.commit();
                                        beverageFragment.setVisPop();
                                        return;
                                    }
                                }

                                Log.d("dataaaa","keluar for");
                                orders.add(new Order(beverageModels.get(holder.getAdapterPosition()).getName(),hitung,price,beverageModels.get(holder.getAdapterPosition()).getImage()));
                                json = gson.toJson(orders);
                                Log.d("dataaaaa","posisi = "+i);
                                Log.d("dataaaaa","json tidak cointain = "+json);
                                break;
                            }
                        }

                        Log.d("dataaaaaaaaaa","size = "+orders.size());



                    }else {
                        orders.add(new Order(beverageModels.get(holder.getAdapterPosition()).getName(),hitung,price,beverageModels.get(holder.getAdapterPosition()).getImage()));
                        json = gson.toJson(orders);
                        Log.d("dataaaaa","json > 0 = "+json);
                    }
                    for (int k = 0;k<orders.size();k++){

                        qty += orders.get(k).getQty();
                        int totalQty = 0;
                        totalQty = totalQty + qty;
                        pricepop += orders.get(k).getPrice();
                        Log.d("dataaaaaaa","total = "+qty);
                    }
                    SharedPreferences.Editor editorPref = sharedPreferences.edit();
                    editorPref.putInt("qty_pop_total",qty);
                    editorPref.putInt("price_pop_total",pricepop);
                    editorPref.commit();
                    beverageFragment.setVisPop();
                    json = gson.toJson(orders);
                    Log.d("dataaaaa","json d luar smua  = "+json);
                    if (sharedPreferences.getString("data-order",null)!= null){
                        Log.d("dataaa","masuk share tidak null");
                        String jsonnew = gson.toJson(orders);
                        SharedPreferences.Editor editornew = sharedPreferences.edit();
                        editornew.putString("data-order",jsonnew);
                        editornew.apply();
                    }else {
                        Log.d("dataaa","masuk share  null");

                        editorPref.putString("data-order",json);
                        editorPref.apply();
                    }
                }

            }
        });
    }

    void setData(){
        BeverageViewHolder holder;

    }

    @Override
    public int getItemCount() {
        return beverageModels.size();
    }

    public class BeverageViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFood;
        TextView foodName,descFood,foodPrice,qtyText;
        Button btnMinus,btnPlus;
        public BeverageViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.imgFood);
            foodName = itemView.findViewById(R.id.foodName);
            descFood = itemView.findViewById(R.id.descFood);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            qtyText = itemView.findViewById(R.id.qtyText);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnPlus = itemView.findViewById(R.id.btnPlus);
        }
    }
}
