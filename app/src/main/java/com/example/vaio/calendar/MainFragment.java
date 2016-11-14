package com.example.vaio.calendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.t3h.adapter.CalendarAdapter;
import com.t3h.database.MyDatabase;
import com.t3h.item.Date;
import com.t3h.item.EventCalendar;
import com.t3h.item.Today;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by vaio on 11/12/2016.
 */

public class MainFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    public static final String DATE = "date";
    public static final String MONTH = "month";
    public static final String YEAR = "year";
    public static final int REQUEST_CODE = 1;
    public static final String EVENT_CALENDAR = "event_calendar";
    public static final String HAS_EVENT = "has_event";
    private int currentDisplayDate = Today.dayOfMonth();
    private int currentDisplayMonth = Today.month();
    private int currentDisplayYear = Today.year();


    private GridView gridView;
    private TextView tvTitile;
    private ImageView ivPrevious;
    private ImageView ivNext;
    private TextView tvDate;
    private TextView tvMonth;
    private TextView tvYear;
    private View v;  // The View In Fragment
    private ArrayList<EventCalendar> arrEnventCalendar;
    private ImageButton btnScheduleList;
    private MyDatabase database;
    private Dialog dialog;
    private Context context;

    public MainFragment(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        this.v = v;
        initData();
        initCalendar();
        return v;
    }

    private void initData() {
        database = new MyDatabase(getActivity().getBaseContext());
        arrEnventCalendar = database.getEvent();
//        Toast.makeText(getActivity().getBaseContext(), arrEnventCalendar.get(0).toString(), Toast.LENGTH_SHORT).show();
        btnScheduleList = (ImageButton) v.findViewById(R.id.btnScheduleList);
        btnScheduleList.setOnClickListener(this);
        ivPrevious = (ImageView) v.findViewById(R.id.btnPrevious);
        ivNext = (ImageView) v.findViewById(R.id.btnNext);

        tvDate = (TextView) v.findViewById(R.id.tvDate);
        tvMonth = (TextView) v.findViewById(R.id.tvMonth);
        tvYear = (TextView) v.findViewById(R.id.tvYear);

        tvDate.setText("Ngày\n" + (currentDisplayDate));
        tvMonth.setText("Tháng\n" + (currentDisplayMonth + 1));
        tvYear.setText("Năm\n" + currentDisplayYear);

        ivPrevious.setOnClickListener(this);
        ivNext.setOnClickListener(this);

//
//        Calendar calendar = Calendar.getInstance();
//        currentDisplayDate = calendar.get(Calendar.DAY_OF_MONTH);
//        currentDisplayYear = calendar.get(java.util.Calendar.YEAR);
//        currentDisplayMonth = calendar.get(java.util.Calendar.MONTH);
    }

    public void initCalendar() {
        arrEnventCalendar = database.getEvent();
        tvTitile = (TextView) v.findViewById(R.id.tvTitle);
        tvTitile.setText("Tháng " + (currentDisplayMonth + 1) + " Năm " + currentDisplayYear);
        gridView = (GridView) v.findViewById(R.id.gridView);
        CalendarAdapter calendarAdapter = new CalendarAdapter(getActivity().getBaseContext(), new Date(currentDisplayDate, currentDisplayMonth, currentDisplayYear), arrEnventCalendar);
        gridView.setAdapter(calendarAdapter);
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView tv = (TextView) view.findViewById(R.id.tvDate);
        int date = Integer.parseInt(tv.getText().toString());
        int month = currentDisplayMonth + 1;
        int year = currentDisplayYear;

        if (tv.getText() != "0") {
            Toast.makeText(getActivity().getBaseContext(), date + "/" + month + "/" + year, Toast.LENGTH_SHORT).show();
        } else {
            return;
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.ivEvent);
        Intent intent = new Intent(MainFragment.this.getActivity(), NoteEventActivity.class);
        if (imageView.getVisibility() == View.VISIBLE) {
            for (int i = 0; i < arrEnventCalendar.size(); i++) {
                if (arrEnventCalendar.get(i).getDay() == date && arrEnventCalendar.get(i).getMonth() == month && arrEnventCalendar.get(i).getYear() == year) {
                    intent.putExtra(EVENT_CALENDAR, arrEnventCalendar.get(i));
                    intent.putExtra(HAS_EVENT, true);
                    break;
                }
            }
        } else {
            intent.putExtra(HAS_EVENT, false);
        }
        intent.putExtra(DATE, date);
        intent.putExtra(MONTH, month);
        intent.putExtra(YEAR, year);
        getActivity().startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onClick(View v) {
        TranslateAnimation animationNext = (TranslateAnimation) AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.fragment_anim);
        TranslateAnimation animationPrevious = (TranslateAnimation) AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.fragment_anim_2);
        switch (v.getId()) {
            case R.id.btnNext:
                if (currentDisplayMonth == 11) {
                    currentDisplayMonth = 0;
                    currentDisplayYear++;
                } else {
                    currentDisplayMonth++;
                }
                initCalendar();
                gridView.startAnimation(animationNext);
                break;
            case R.id.btnPrevious:
                if (currentDisplayMonth == 0) {
                    currentDisplayYear--;
                    currentDisplayMonth = 11;
                } else {
                    currentDisplayMonth--;
                }
                initCalendar();
                gridView.startAnimation(animationPrevious);
                break;
            case R.id.btnScheduleList:
                showDialog();
                break;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog, null);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.layoutDialog);
        builder.setView(view);
        for (int i = 0; i < arrEnventCalendar.size(); i++) {
            TextView textView = new TextView(context);
            textView.setText("["+arrEnventCalendar.get(i).getNoteName()+"]" + "\n" + arrEnventCalendar.get(i).getNoteContent() + "\n"
                    + arrEnventCalendar.get(i).getTime() + "\n" + arrEnventCalendar.get(i).getDate() + "\n" +
                    "-----------------------------------------------------");
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(20, 10, 0, 10);
            textView.setLayoutParams(params);
            layout.addView(textView);
        }
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
