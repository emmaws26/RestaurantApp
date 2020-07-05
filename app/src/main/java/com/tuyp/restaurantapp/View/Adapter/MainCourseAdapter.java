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
import com.tuyp.restaurantapp.Model.MainCourseModel;
import com.tuyp.restaurantapp.Model.Order;
import com.tuyp.restaurantapp.R;
import com.tuyp.restaurantapp.View.Fragment.MainCourseFragment;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainCourseAdapter extends RecyclerView.Adapter<MainCourseAdapter.MainCourseViewHolder> {

    List<MainCourseModel> mainCourseModels;
    List<Order> orders = new ArrayList<>();
    Context context;
    MainCourseFragment mainCourseFragment;
    LayoutInflater layoutInflater;

    public MainCourseAdapter(Context context, List<MainCourseModel> mainCourseModels,MainCourseFragment mainCourseFragment) {
        this.mainCourseModels = mainCourseModels;
        this.context = context;
        this.mainCourseFragment = mainCourseFragment;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public MainCourseAdapter.MainCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.sashimi_item,parent,false);
        MainCourseAdapter.MainCourseViewHolder holder = new MainCourseAdapter.MainCourseViewHolder(view);
        return holder;
    }

    public void onBindViewHolder(@NonNull final MainCourseAdapter.MainCourseViewHolder holder, int position) {
        Locale localeID = new Locale("in","ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
        final Gson gson = new Gson();
        final SharedPreferences sharedPreferences = context.getSharedPreferences("order",Context.MODE_PRIVATE);
        SharedPreferences storage = context.getSharedPreferences("storage",Context.MODE_PRIVATE);
        String storage1 = storage.getString("data-maincourse",null);
//        Log.d("dataaaaa","isi storage = "+storage1);
        if (sharedPreferences.getString("data-order",null)!= null){
            Log.d("dataaaaaa","masuk tidak null atas");
            String json = sharedPreferences.getString("data-order",null);
            Type type = new TypeToken<List<Order>>() {}.getType();
            orders = (List<Order>) gson.fromJson(json,type);
//            Log.d("dataaaaaa","order = "+orders.get(0).getName());
            for (int i = 0; i<orders.size();i++){
                if (mainCourseModels.get(position).getName().equals(orders.get(i).getName())){
                    holder.qtyText.setText(""+orders.get(i).getQty());
                }
            }

//            Log.d("dataaaaaaaaaa","size dalam share = "+orders.size());
        }else {

        }
        int resource = context.getResources().getIdentifier(mainCourseModels.get(position).getImage(),"drawable",context.getPackageName());
        holder.imgFood.setImageResource(resource);
        holder.foodName.setText(mainCourseModels.get(position).getName());
        holder.descFood.setText(mainCourseModels.get(position).getDescName());
        holder.foodPrice.setText(format.format(mainCourseModels.get(position).getPrice()).toString());
        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty =0;
                int pricepop = 0;
                int jumlah = Integer.parseInt(holder.qtyText.getText().toString());
                int hitung = jumlah+1;

                int price = mainCourseModels.get(holder.getAdapterPosition()).getPrice() * hitung;
                holder.qtyText.setText(String.valueOf(hitung));


                if (orders.size() > 0){
                    for (int i=0;i<orders.size();i++){

                        if (orders.get(i).getName().equals(mainCourseModels.get(holder.getAdapterPosition()).getName())){


                            orders.set(i,new Order(mainCourseModels.get(holder.getAdapterPosition()).getName(),hitung,price,mainCourseModels.get(holder.getAdapterPosition()).getImage()));
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
                            mainCourseFragment.setVisPop();
                            break;
                        }else {
                            for (int j = 0;j<orders.size();j++){
                                if (mainCourseModels.get(holder.getAdapterPosition()).getName().equals(orders.get(j).getName())){
//                                   Log.d("dataaaaaaaaaa","ada yg sama");
                                    orders.set(j,new Order(mainCourseModels.get(holder.getAdapterPosition()).getName(),hitung,price,mainCourseModels.get(holder.getAdapterPosition()).getImage()));
                                    String json = gson.toJson(orders);
//                                   Log.d("dataaaaa","json dlm contain = "+json);
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
                                    mainCourseFragment.setVisPop();
                                    return;
                                }
                            }

//                           Log.d("dataaaa","keluar for");
                            String json = gson.toJson(orders);
                            orders.add(new Order(mainCourseModels.get(holder.getAdapterPosition()).getName(),hitung,price,mainCourseModels.get(holder.getAdapterPosition()).getImage()));
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
                            mainCourseFragment.setVisPop();
//                           String json = gson.toJson(orders);
//                           Log.d("dataaaaa","posisi = "+i);
//                           Log.d("dataaaaa","json tidak cointain = "+json);
                            break;
                        }
                    }
                    String getData = sharedPreferences.getString("data-order",null);
                    Log.d("isi get","isi get = "+getData);

//                    Log.d("dataaaaaaaaaa","size = "+orders.size());



                }else {
                    orders.add(new Order(mainCourseModels.get(holder.getAdapterPosition()).getName(),hitung,price,mainCourseModels.get(holder.getAdapterPosition()).getImage()));
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
                    mainCourseFragment.setVisPop();
                }
//                for (int k = 0;k<orders.size();k++){
//
//                    qty += orders.get(k).getQty();
//                    pricepop += orders.get(k).getPrice();
//                    int totalQty = 0;
//                    totalQty = totalQty + qty;
//                    Log.d("dataaaaaaa","total = "+qty);
//                }
                SharedPreferences.Editor editorPref = sharedPreferences.edit();
//                editorPref.putInt("qty_pop_total",qty);
//                editorPref.putInt("price_pop_total",pricepop);
//                editorPref.commit();
                mainCourseFragment.setVisPop();
                String json = gson.toJson(orders);
                Log.d("dataaaaa","json d luar smua  = "+json);
                if (sharedPreferences.getString("data-order",null)!= null){
                    Log.d("dataaa","masuk share tidak null");
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
                    int price = mainCourseModels.get(holder.getAdapterPosition()).getPrice() * hitung;
                    Log.d("dataaaaa","json d luar smua  = "+json);
                    if (orders.size() > 0){
                        for (int i=0;i<orders.size();i++){

                            if (orders.get(i).getName().equals(mainCourseModels.get(holder.getAdapterPosition()).getName())){
                                Log.d("dataaaa","masuk contain");

                                orders.set(i,new Order(mainCourseModels.get(holder.getAdapterPosition()).getName(),hitung,price,mainCourseModels.get(holder.getAdapterPosition()).getImage()));
                                json = gson.toJson(orders);
                                Log.d("dataaaaa","json dlm contain = "+json);

                                break;
                            }else {
                                for (int j = 0;j<orders.size();j++){
                                    if (mainCourseModels.get(holder.getAdapterPosition()).getName().equals(orders.get(j).getName())){
                                        Log.d("dataaaaaaaaaa","ada yg sama");
                                        orders.set(j,new Order(mainCourseModels.get(holder.getAdapterPosition()).getName(),hitung,price,mainCourseModels.get(holder.getAdapterPosition()).getImage()));
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
                                        mainCourseFragment.setVisPop();
                                        return;
                                    }
                                }

                                Log.d("dataaaa","keluar for");
                                orders.add(new Order(mainCourseModels.get(holder.getAdapterPosition()).getName(),hitung,price,mainCourseModels.get(holder.getAdapterPosition()).getImage()));
                                json = gson.toJson(orders);
                                Log.d("dataaaaa","posisi = "+i);
                                Log.d("dataaaaa","json tidak cointain = "+json);
                                break;
                            }
                        }

                        Log.d("dataaaaaaaaaa","size = "+orders.size());



                    }else {
                        orders.add(new Order(mainCourseModels.get(holder.getAdapterPosition()).getName(),hitung,price,mainCourseModels.get(holder.getAdapterPosition()).getImage()));
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
                    mainCourseFragment.setVisPop();
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

    public int getItemCount() {
        return mainCourseModels.size();
    }

    public class MainCourseViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFood;
        TextView foodName,descFood,foodPrice,qtyText;
        Button btnMinus,btnPlus;
        public MainCourseViewHolder(@NonNull View itemView) {
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
