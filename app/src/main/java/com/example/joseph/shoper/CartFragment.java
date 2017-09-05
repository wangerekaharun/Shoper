package com.example.joseph.shoper;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {
    ImageButton cartAdd;
    ImageButton cartRemove;
    TextView cartItemTf;
    LinearLayout cartStatus;
    private  int  cartItems=0;
    private static int totalCartItems=0;
    TextView totalCartItemView;
    Animation slideUpAnimation, slideDownAnimation;
    Button mOrderBtn;
    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cartStatus=getActivity().findViewById(R.id.cart_status);
        totalCartItemView=getActivity().findViewById(R.id.total_cart_item);
        mOrderBtn=getActivity().findViewById(R.id.order_btn);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cartAdd=view.findViewById(R.id.cart_add);
        cartRemove=view.findViewById(R.id.cart_remove);
        cartItemTf=view.findViewById(R.id.item_added);
        slideUpAnimation= AnimationUtils.loadAnimation(getContext(),R.anim.slide_up);
        slideDownAnimation=AnimationUtils.loadAnimation(getContext(),R.anim.slide_down);
//        cartStatus=view.findViewById(R.id.cart_status);
        cartAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
                cartItemTf.setText(String.valueOf(cartItems));
                totalCartItemView.setText(String.valueOf(totalCartItems));
                if(totalCartItems>=1){
                    cartStatus.setVisibility(View.VISIBLE);
                    if(totalCartItems==1){
                        cartStatus.startAnimation(slideUpAnimation);
                    }

                }
            }
        });
        cartRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItem();
                cartItemTf.setText(String.valueOf(cartItems));
                totalCartItemView.setText(String.valueOf(totalCartItems));
                if(totalCartItems==0){
                    cartStatus.setVisibility(View.INVISIBLE);
                    cartStatus.startAnimation(slideDownAnimation);
                }
            }
        });
        mOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getBaseContext().startActivity(new Intent(view.getContext(),Delivery.class));
            }
        });

    }
    public void removeItem(){

        if(cartItems>0){
            cartItems--;
            totalCartItems--;
        }
    }
    public void addItem(){
        cartItems++;
        totalCartItems++;
    }
}
