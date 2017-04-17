package com.t3h.service;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.vaio.calendar.R;
import com.t3h.database.MyDatabase;
import com.t3h.item.EventCalendar;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by vaio on 11/15/2016.
 */

public class MyService extends Service {
    public static final int ID = 1;
    private boolean isRunning = true;
    private MyDatabase database;
    private ArrayList<EventCalendar> arrEventCalendar;
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        database = new MyDatabase(this);
        arrEventCalendar = database.getEvent();
        Thread thread = new Thread(runnable);
        thread.start();
        return START_STICKY;
    }

    Handler handler = new Handler(new Handler.Callback() {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.arg1 == 1) {
                EventCalendar eventCalendar = (EventCalendar) msg.obj;
                AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext(), android.R.style.Theme_DeviceDefault_Light_Dialog);
                builder.setTitle(eventCalendar.getNoteName());
                builder.setCancelable(false);
                builder.setView(R.layout.dialog_alert);
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mediaPlayer.stop();
                        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        manager.cancel(ID);
                    }
                });
                Dialog dialog = builder.create();
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
                dialog.show();
                mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.nhacchuong);
                mediaPlayer.start();
            }
            return false;
        }
    });
    private Runnable runnable = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void run() {
            while (isRunning) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR);
                if ((calendar.get(Calendar.AM_PM)) == Calendar.PM) {
                    hour += 12;
                }
                int minute = calendar.get(Calendar.MINUTE);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH) + 1;
                int year = calendar.get(Calendar.YEAR);


                for (int i = arrEventCalendar.size() - 1; i >= 0; i--) {
                    EventCalendar event = arrEventCalendar.get(i);

                    int eventHour = event.getHour();
                    int eventMinute = event.getMinute();
                    int eventDay = event.getDay();
                    int eventMonth = event.getMonth();
                    int eventYear = event.getYear();
//                    if(eventHour>12){
//                        eventHour -=12;
//                    }
                    Log.e("1", hour + "_" + minute + "_" + day + "_" + month + "_" + year);
                    Log.e("2", event.getHour() + "_" + event.getMinute() + "_" + event.getDay() + "_" + event.getMonth() + "_" + event.getYear());
                    if (hour == eventHour && minute == eventMinute && day == eventDay &&
                            month == eventMonth && year == eventYear) {
                        Log.e("adfasfas", "asdfasfsfsf");
                        buildNotificaiton();
                        Message message = new Message();
                        message.arg1 = 1;
                        message.obj = event;
                        handler.sendMessage(message);
//                        isOpen = true;
                        database.deleteEvent(arrEventCalendar.get(i));
                        if (arrEventCalendar.size() > 0) {
                            arrEventCalendar.remove(i);
                        }

                    }
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void buildNotificaiton() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Alarm");
        builder.setContentText("Wake up");
        builder.setSmallIcon(android.R.drawable.ic_lock_idle_alarm);
        Notification notification = builder.build();
        manager.notify(ID, notification);
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
    }
}
