package com.tuyp.restaurantapp.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tuyp.restaurantapp.Config.NotificationManager;
import com.tuyp.restaurantapp.MainActivity;
import com.tuyp.restaurantapp.Model.Order;
import com.tuyp.restaurantapp.R;
import com.tuyp.restaurantapp.View.Adapter.ListOrderAdapter;
import com.tuyp.restaurantapp.View.Fragment.BillsFragment;

import java.util.ArrayList;
import java.util.List;

public class ListOrderActivity extends AppCompatActivity {
    TextView textTitle;
    RecyclerView recOrder;
    RelativeLayout toolbar;
    ListOrderAdapter listOrderAdapter;
    String title;
    Button btnOrder;
    ImageView btnBack;
    List<Order> orders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_order);
        toolbar = findViewById(R.id.toolbar);
        textTitle = findViewById(R.id.textTitle);
        recOrder = findViewById(R.id.recOrder);
        btnOrder = findViewById(R.id.btnOrder);
        btnBack = findViewById(R.id.iv_btn_back);
        Log.d("dataaaaaa","masuk oncreate");
        title = "Choose the food you love";
        SpannableStringBuilder ssb = new SpannableStringBuilder(title);

        ForegroundColorSpan markColor = new ForegroundColorSpan(getResources().getColor(R.color.colorTint));
        ssb.setSpan(markColor,11,15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textTitle.setText(ssb);
        if (getIntent() != null){
           Bundle bundle = getIntent().getBundleExtra("checkout-data");
            orders = (List<Order>) bundle.getSerializable("bundle");
            recOrder.setLayoutManager(new LinearLayoutManager(this));
            listOrderAdapter = new ListOrderAdapter(ListOrderActivity.this,orders);
            recOrder.setAdapter(listOrderAdapter);
            listOrderAdapter.notifyDataSetChanged();
        }

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Log.d("dataaaaaaa","clicked");

                Bundle bundle = new Bundle();
                bundle.putString("bill-intent","bill-intent");
                Intent order = new Intent(ListOrderActivity.this, MainActivity.class);
                order.putExtra("bill-intent",bundle);
                startActivity(order);
                finish();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("bill-intent","bill-intent");
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.d("dataaaa","masuk resume");
//        finish();
//        startActivity(getIntent());
//    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        Log.d("dataa","masuk restart");
//        finish();
    }
}
