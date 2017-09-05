package com.example.joseph.shoper;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class Shops extends AppCompatActivity {
    Toolbar mToolBar;
    RecyclerView mShopsView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops);
        mToolBar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        mShopsView= (RecyclerView) findViewById(R.id.shops_view);
        mShopsView.setAdapter(new ShopsAdapter(createShops(),this));
        mShopsView.setLayoutManager(new LinearLayoutManager(this));
        mShopsView.hasFixedSize();
        mShopsView.setNestedScrollingEnabled(false);
//        mShopsView.setItemAnimator(new SlideInUpAnimator());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shops,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public List<Shop> createShops(){
        Shop shop=new Shop();
        List<Shop> shops=new ArrayList<>();
        shop.setShopName("Television");
        shop.setShopBackground(R.drawable.electronics);
        shop.setShopImage(R.drawable.tv);
        shops.add(shop);
        shop=new Shop();
        shop.setShopName("Computing");
        shop.setShopBackground(R.drawable.laptop);
        shop.setShopImage(R.drawable.lap);
        shops.add(shop);
        shop=new Shop();
        shop.setShopImage(R.drawable.iphone);
        shop.setShopName("Smart phones");
        shop.setShopBackground(R.drawable.smartphone);
        shops.add(shop);
        shop=new Shop();
        shop.setShopName("Clothing");
        shop.setShopBackground(R.drawable.clothing);
        shop.setShopImage(R.drawable.cloth);
        shops.add(shop);
        shop=new Shop();
        shop.setShopName("Computing");
        shop.setShopBackground(R.drawable.laptop);
        shop.setShopImage(R.drawable.lap);
        shops.add(shop);
        shop=new Shop();
        shop.setShopImage(R.drawable.iphone);
        shop.setShopName("Smart phones");
        shop.setShopBackground(R.drawable.smartphone);
        shops.add(shop);
        shop=new Shop();
        shop.setShopName("Clothing");
        shop.setShopBackground(R.drawable.clothing);
        shop.setShopImage(R.drawable.cloth);
        shops.add(shop);

        return shops;

    }
}
