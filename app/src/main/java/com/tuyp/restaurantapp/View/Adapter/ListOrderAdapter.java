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
import com.tuyp.restaurantapp.Model.Order;
import com.tuyp.restaurantapp.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ListOrderAdapter extends RecyclerView.Adapter<ListOrderAdapter.ListViewHolder> {

    Context context;
    LayoutInflater layoutInflater;
    List<Order> orders;

    public ListOrderAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.orders = orders;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_order,parent,false);
        ListViewHolder holder = new ListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, final int position) {
        int qty =0;
        int price = 0;
        Locale localeID = new Locale("in","ID");
        final NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
        final Gson gson = new Gson();
        final SharedPreferences sharedPreferences = context.getSharedPreferences("order",Context.MODE_PRIVATE);
        int resource = context.getResources().getIdentifier(orders.get(position).getImage(),"drawable",context.getPackageName());
        holder.foodName.setText(orders.get(position).getName().toString());
        holder.imgFood.setImageResource(resource);
        for (int k = 0;k<orders.size();k++){
            qty += orders.get(k).getQty();
            price += orders.get(k).getPrice();
        }
        holder.foodPrice.setText(format.format(orders.get(position).getPrice()).toString());
        if (orders.get(position).getQty() == 0){
            orders.remove(position);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String json = gson.toJson(orders);
            editor.putString("data-order",json);
            for (int k = 0;k<orders.size();k++){
                qty += orders.get(k).getQty();
                price += orders.get(k).getPrice();
            }
            holder.foodPrice.setText(format.format(price));
            editor.putInt("qty_pop_total",qty);
            editor.putInt("price_pop_total",price);
            editor.commit();
//            notifyDataSetChanged();
        }else {
            holder.qtyText.setText(""+orders.get(position).getQty());
        }

        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty =0;
                int pricepop = 0;
                int jumlah = Integer.parseInt(holder.qtyText.getText().toString());
                int hitung = jumlah+1;

                int price = (orders.get(holder.getAdapterPosition()).getPrice() * hitung)/jumlah;
                holder.qtyText.setText(String.valueOf(hitung));
                Log.d("dataaaaaa","price = "+price);

                if (orders.size() > 0){
                    Log.d("dataaaaaa","masuk > 0");
                    for (int i=0;i<orders.size();i++){

                        if (orders.get(i).getName().equals(orders.get(holder.getAdapterPosition()).getName())){


                            orders.set(i,new Order(orders.get(holder.getAdapterPosition()).getName(),hitung,price,orders.get(holder.getAdapterPosition()).getImage()));
                            String json = gson.toJson(orders);
                            SharedPreferences.Editor editorPref = sharedPreferences.edit();
                            for (int k = 0;k<orders.size();k++){
                                qty += orders.get(k).getQty();
                                pricepop += orders.get(k).getPrice();


                            }
                            holder.foodPrice.setText(format.format(price));
                            editorPref.putInt("qty_pop_total",qty);
                            editorPref.putInt("price_pop_total",pricepop);
                            editorPref.putString("data-order",json);
                            editorPref.apply();

                            break;
                        }else {
                            for (int j = 0;j<orders.size();j++){
                                if (orders.get(holder.getAdapterPosition()).getName().equals(orders.get(j).getName())){
                                    orders.set(j,new Order(orders.get(holder.getAdapterPosition()).getName(),hitung,price,orders.get(holder.getAdapterPosition()).getImage()));
                                    String json = gson.toJson(orders);
                                    SharedPreferences.Editor editorPref = sharedPreferences.edit();
                                    editorPref.putString("data-order",json);
                                    editorPref.apply();
                                    for (int k = 0;k<orders.size();k++){
                                        qty += orders.get(k).getQty();
                                        pricepop += orders.get(k).getPrice();


                                    }
                                    editorPref.putInt("qty_pop_total",qty);
                                    editorPref.putInt("price_pop_total",pricepop);
                                    editorPref.commit();
                                    holder.foodPrice.setText(format.format(price));

                                    return;
                                }
                            }


                            String json = gson.toJson(orders);
                            orders.add(new Order(orders.get(holder.getAdapterPosition()).getName(),hitung,price,orders.get(holder.getAdapterPosition()).getImage()));
                            SharedPreferences.Editor editorPref = sharedPreferences.edit();
                            holder.foodPrice.setText(format.format(price));
                            for (int k = 0;k<orders.size();k++){
                                qty += orders.get(k).getQty();
                                pricepop += orders.get(k).getPrice();


                            }
                            editorPref.putInt("qty_pop_total",qty);
                            editorPref.putInt("price_pop_total",pricepop);
                            editorPref.putString("data-order",json);
                            editorPref.apply();

                            break;
                        }
                    }
                    String getData = sharedPreferences.getString("data-order",null);
//                    Log.d("isi get","isi get = "+getData);

//                    Log.d("dataaaaaaaaaa","size = "+orders.size());



                }else {
                    Log.d("dataaaaa","masuk size = 0");
                    orders.add(new Order(orders.get(holder.getAdapterPosition()).getName(),hitung,price,orders.get(holder.getAdapterPosition()).getImage()));
                    String json = gson.toJson(orders);
                    holder.foodPrice.setText(format.format(price));
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
//                    sashimiFragment.setVisPop();
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
                    holder.foodPrice.setText(format.format(price));
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
                if (jumlah > 0){
//                    Log.d("dataaaa","jumlah = "+jumlah);

                    int hitung = jumlah-1;
                    int price = (orders.get(holder.getAdapterPosition()).getPrice() * hitung)/jumlah;
                    if (hitung == 0){
                        Log.d("dataaaaa","data jumlah nol");
                        orders.remove(holder.getAdapterPosition());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        String json = gson.toJson(orders);
                        editor.putString("data-order",json);
                        for (int k = 0;k<orders.size();k++){
                            qty += orders.get(k).getQty();
                            pricepop += orders.get(k).getPrice();
                        }
                        holder.foodPrice.setText(format.format(price));
                        editor.putInt("qty_pop_total",qty);
                        editor.putInt("price_pop_total",pricepop);
                        editor.commit();
                        notifyDataSetChanged();
                    }else {
                        Log.d("dataaaaaa","price = "+price+"nilai hitung = "+hitung);
                        holder.qtyText.setText(String.valueOf(hitung));


                        if (orders.size() > 0){
                            Log.d("dataaaaaa","masuk > 0");
                            for (int i=0;i<orders.size();i++){

                                if (orders.get(i).getName().equals(orders.get(holder.getAdapterPosition()).getName())){

                                    Log.d("dataaaa","masuk cek awal min = "+price);
                                    orders.set(i,new Order(orders.get(holder.getAdapterPosition()).getName(),hitung,price,orders.get(holder.getAdapterPosition()).getImage()));
                                    String json = gson.toJson(orders);
                                    SharedPreferences.Editor editorPref = sharedPreferences.edit();
                                    for (int k = 0;k<orders.size();k++){
                                        qty += orders.get(k).getQty();
                                        pricepop += orders.get(k).getPrice();
                                    }
                                    holder.foodPrice.setText(format.format(price));
                                    editorPref.putInt("qty_pop_total",qty);
                                    editorPref.putInt("price_pop_total",pricepop);
                                    editorPref.putString("data-order",json);
                                    editorPref.apply();

                                    break;
                                }else {
                                    for (int j = 0;j<orders.size();j++){
                                        if (orders.get(holder.getAdapterPosition()).getName().equals(orders.get(j).getName())){
                                            orders.set(j,new Order(orders.get(holder.getAdapterPosition()).getName(),hitung,price,orders.get(holder.getAdapterPosition()).getImage()));
                                            String json = gson.toJson(orders);
                                            SharedPreferences.Editor editorPref = sharedPreferences.edit();
                                            editorPref.putString("data-order",json);
                                            editorPref.apply();
                                            for (int k = 0;k<orders.size();k++){
                                                qty += orders.get(k).getQty();
                                                pricepop += orders.get(k).getPrice();


                                            }
                                            editorPref.putInt("qty_pop_total",qty);
                                            editorPref.putInt("price_pop_total",pricepop);
                                            editorPref.commit();
                                            holder.foodPrice.setText(format.format(price));

                                            return;
                                        }
                                    }


                                    String json = gson.toJson(orders);
                                    orders.add(new Order(orders.get(holder.getAdapterPosition()).getName(),hitung,price,orders.get(holder.getAdapterPosition()).getImage()));
                                    SharedPreferences.Editor editorPref = sharedPreferences.edit();
                                    holder.foodPrice.setText(format.format(price));
                                    for (int k = 0;k<orders.size();k++){
                                        qty += orders.get(k).getQty();
                                        pricepop += orders.get(k).getPrice();


                                    }
                                    editorPref.putInt("qty_pop_total",qty);
                                    editorPref.putInt("price_pop_total",pricepop);
                                    editorPref.putString("data-order",json);
                                    editorPref.apply();

                                    break;
                                }
                            }
                            String getData = sharedPreferences.getString("data-order",null);
//                    Log.d("isi get","isi get = "+getData);

//                    Log.d("dataaaaaaaaaa","size = "+orders.size());



                        }else {
                            Log.d("dataaaaa","masuk size = 0");
                            orders.add(new Order(orders.get(holder.getAdapterPosition()).getName(),hitung,price,orders.get(holder.getAdapterPosition()).getImage()));
                            String json = gson.toJson(orders);
                            holder.foodPrice.setText(format.format(price));
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
//                    sashimiFragment.setVisPop();
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
                            holder.foodPrice.setText(format.format(price));
                        }else {
                            Log.d("dataaa","masuk share  null");
//                    SharedPreferences.Editor editorPref = sharedPreferences.edit();
                            editorPref.putString("data-order",json);
                            editorPref.apply();
                        }
                    }



                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView foodName,descFood,foodPrice,qtyText;
        Button btnPlus,btnMinus;
        ImageView imgFood;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodName);
            descFood = itemView.findViewById(R.id.descFood);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            imgFood = itemView.findViewById(R.id.imgFood);
            qtyText = itemView.findViewById(R.id.qtyText);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
        }
    }
}
