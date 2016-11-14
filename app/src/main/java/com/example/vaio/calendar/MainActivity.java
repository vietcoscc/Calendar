package com.example.vaio.calendar;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import static com.example.vaio.calendar.MainFragment.REQUEST_CODE;

public class MainActivity extends AppCompatActivity {
    private MainFragment mainFragment ;
    private StartFragment startFragment = new StartFragment();
    private FrameLayout frameLayout ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addFragment();
        initViews();
        showFragmentOnDelay();

    }

    private void initViews() {
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
    }

    public void addFragment() {
        mainFragment = new MainFragment(this);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frameLayout, mainFragment);
        fragmentTransaction.add(R.id.frameLayout, startFragment);
        fragmentTransaction.commit();
    }
    public void showFragmentOnDelay(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showFragment(mainFragment,startFragment);
            }
        },3000);
    }
    public void showFragment(Fragment show, Fragment hide) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.hide(hide);
        TranslateAnimation animation  = (TranslateAnimation) AnimationUtils.loadAnimation(this,R.anim.fragment_anim);
        frameLayout.startAnimation(animation);
        fragmentTransaction.show(show);
        fragmentTransaction.commit();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
            if(resultCode == RESULT_OK){
                mainFragment.initCalendar();
            }
        }

    }
}
