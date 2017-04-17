package com.example.vaio.calendar;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import com.t3h.database.MyDatabase;
import com.t3h.service.MyService;

import static com.example.vaio.calendar.MainFragment.REQUEST_CODE;

public class MainActivity extends AppCompatActivity {
    private static final int DIALOG = 1;
    private MainFragment mainFragment;
    private StartFragment startFragment = new StartFragment();
    private FrameLayout frameLayout;
    private TranslateAnimation animation;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, 1);
        }
        addFragment();
        initViews();
        showFragmentOnDelay();
    }


    private void initViews() {
        animation = (TranslateAnimation) AnimationUtils.loadAnimation(this, R.anim.fragment_anim);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
    }

    public void addFragment() {
        mainFragment = new MainFragment(this);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frameLayout, mainFragment);
        fragmentTransaction.add(R.id.frameLayout, startFragment);
        fragmentTransaction.hide(mainFragment);
        fragmentTransaction.commit();
    }

    public void showFragmentOnDelay() {
        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showFragment(mainFragment, startFragment);
            }
        }, 2000);
    }

    public void showFragment(Fragment show, Fragment hide) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.hide(hide);

        frameLayout.startAnimation(animation);
        fragmentTransaction.show(show);
        fragmentTransaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
//                mainFragment = new MainFragment(this);
                mainFragment.initCalendar();
            }
        }

    }

    public void showExitDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        builder.setTitle("Bạn có muốn thoát không ? ");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }
}
