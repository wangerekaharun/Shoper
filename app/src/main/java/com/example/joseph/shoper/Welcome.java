package com.example.joseph.shoper;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;


public class Welcome extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(new AppInfo());
        addSlide(new Variety());
        addSlide(new Prices());
        setBarColor(Color.parseColor("#00BCD4"));
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startActivity(new Intent(currentFragment.getContext(),SignIn.class));
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(currentFragment.getContext(),SignIn.class));
    }

    @Override
    public void finish() {
//        startActivity(new Intent(this,SignIn.class));
        super.finish();
    }
}
