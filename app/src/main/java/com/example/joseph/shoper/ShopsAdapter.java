package com.example.joseph.shoper;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by joseph on 8/31/17.
 * github:codechef_
 */

public class ShopsAdapter extends RecyclerView.Adapter<ShopsAdapter.MyViewHolder> {
    List<Shop> shopList;
    Context mContext;
    int lastPosition = -1;

    public ShopsAdapter(List<Shop> shopList, Context mContext) {
        this.shopList = shopList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.shops_list,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TextView name;
        ImageView background,shopImage;
        Shop shop=shopList.get(position);
        name=holder.mShopTitle;
        name.setText(shop.getShopName());
        name.setTypeface(Typeface.createFromAsset(mContext.getAssets(),"font.ttf"));
        background=holder.mShopBackground;
//        background.setImageResource(shop.getShopBackground());
        Picasso.with(mContext).load(shop.getShopBackground()).into(background);
        shopImage=holder.mShopImage;
//        shopImage.setImageResource(shop.getShopImage());
        Picasso.with(mContext).load(shop.getShopImage()).into(shopImage);
        if(position >lastPosition) {

            Animation animation = AnimationUtils.loadAnimation(mContext,
                    R.anim.up_from_bottom);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mShopTitle;
        ImageView mShopBackground;
        ImageView mShopImage;
        public MyViewHolder(View itemView) {
            super(itemView);
            mShopTitle=itemView.findViewById(R.id.shop_name);
            mShopBackground=itemView.findViewById(R.id.shop_background);
            mShopImage=itemView.findViewById(R.id.shop_image);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            mContext.startActivity(new Intent(view.getContext(),IndidualShop.class));
        }
    }
}
