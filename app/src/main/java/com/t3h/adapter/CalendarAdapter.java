package com.t3h.adapter;

import android.content.Context;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.vaio.calendar.MainActivity;
import com.example.vaio.calendar.R;
import com.t3h.item.Date;
import com.t3h.item.EventCalendar;
import com.t3h.item.Today;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by vaio on 11/13/2016.
 */

public class CalendarAdapter extends ArrayAdapter implements View.OnTouchListener {
    private Calendar calendar = new GregorianCalendar();  // 1/MM/yyy
    private Context context;
    private int currentDay = 1;
    private int dayOfWeek;
    private Date date; // dd/MM/yyyy;
    private ArrayList<EventCalendar> arrEventCalendar;

    public CalendarAdapter(Context context, Date date, ArrayList<EventCalendar> arrEventCalendar) {

        super(context, android.R.layout.simple_list_item_1);

        this.context = context;
        calendar.set(date.getYear(), date.getMonth(), 1);
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        currentDay = 1;
        if (dayOfWeek == 1) {
            currentDay = -1;
        }
        this.date = date;
        this.arrEventCalendar = arrEventCalendar;
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public int getCount() {
        int month = calendar.get(Calendar.MONTH);
        if (month == 1) {
            if (calendar.get(Calendar.YEAR) % 400 == 0 || (calendar.get(Calendar.YEAR) % 4 == 0 && calendar.get(Calendar.YEAR) % 100 != 0)) {
                return calendar.get(Calendar.DAY_OF_WEEK) + 29 - 1;
            } else {
                return calendar.get(Calendar.DAY_OF_WEEK) + 28 - 1;
            }
        }

        if (month == 0 || month == 2 || month == 4 || month == 6 || month == 7 || month == 9 || month == 11) {
            return calendar.get(Calendar.DAY_OF_WEEK) + 31 - 1;
        }
        return calendar.get(Calendar.DAY_OF_WEEK) + 30 - 1;
    }
    public int getDayOfWeek(){
        return dayOfWeek;
    }
    public boolean hasEventDate() {
        for (int i = 0; i < arrEventCalendar.size(); i++) {
            int day = arrEventCalendar.get(i).getDay();
            int month = arrEventCalendar.get(i).getMonth();
            int year = arrEventCalendar.get(i).getYear();
            if (currentDay == day && date.getMonth() +1 == month && date.getYear() == year) {
                return true;
            }
        }
        return false;
    }

    @NonNull
    @Override
    public View getView(int position, View v, ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        v = layoutInflater.inflate(R.layout.item_grid_view, parent, false);

        TextView textView = (TextView) v.findViewById(R.id.tvDate);
        ImageView imageView = (ImageView) v.findViewById(R.id.ivEvent);
        if (!hasEventDate()) {
            imageView.setVisibility(View.INVISIBLE);
        } else {
            imageView.setVisibility(View.VISIBLE);
        }
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        if ((position - dayOfWeek + 2) == Today.dayOfMonth() && month == Today.month() && year == Today.year()) {
            v.setBackgroundColor(Color.YELLOW);
        }
        if (position >= (dayOfWeek - 1 )&& position < getCount()) {
            textView.setText(currentDay + "");
            currentDay++;
        } else {
            textView.setText("0");
            textView.setVisibility(View.INVISIBLE);
        }
        v.setOnTouchListener(this);
        return v;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                v.setBackgroundColor(Color.RED);
//                break;
//            case MotionEvent.ACTION_UP:
//                v.setBackgroundColor(Color.WHITE);
//                break;
//        }
        return false;
    }
}
