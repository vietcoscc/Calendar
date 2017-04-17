package com.example.vaio.calendar;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.t3h.database.MyDatabase;
import com.t3h.item.EventCalendar;
import com.t3h.service.MyService;

public class NoteEventActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener{
    private static final String KEY = "xx";
    private static final int FLAG = 1;
    private TextView tvTitle;
    private EditText edtNoteName;
    private EditText edtNoteContent;
    private TimePicker timePicker;
    private Spinner spinner;
    private RadioButton radioNormal;
    private RadioButton radioImportain;
    private RadioButton radioLoop;
    private RadioButton radioUnLoop;
    private Button btnSave;
    private Button btnCancel;
    private MyDatabase database;
    private boolean hasEvent;
    private EventCalendar eventCalendar;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_event);
        database = new MyDatabase(this);
        initViews();
    }

    private void initViews() {
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        edtNoteName = (EditText) findViewById(R.id.edtNoteName);
        edtNoteContent = (EditText) findViewById(R.id.edtNoteContent);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        spinner = (Spinner) findViewById(R.id.spinner);
        radioNormal = (RadioButton) findViewById(R.id.radioNormal);
        radioImportain = (RadioButton) findViewById(R.id.radioImportain);
        radioUnLoop = (RadioButton) findViewById(R.id.radioUnLoop);
        radioLoop = (RadioButton) findViewById(R.id.radioLoop);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        timePicker.setIs24HourView(false);
        //
        String s[] = {"Chỉ ngày hiện tại", "Mọi ngày"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, s);
        spinner.setAdapter(arrayAdapter);
        //
        Intent intent = getIntent();
        int date = intent.getExtras().getInt(MainFragment.DATE);
        int month = intent.getExtras().getInt(MainFragment.MONTH);
        int year = intent.getExtras().getInt(MainFragment.YEAR);
        hasEvent = intent.getExtras().getBoolean(MainFragment.HAS_EVENT);
        if (hasEvent) {
            EventCalendar eventCalendar = (EventCalendar) intent.getSerializableExtra(MainFragment.EVENT_CALENDAR);
            edtNoteName.setText(eventCalendar.getNoteName());
            edtNoteContent.setText(eventCalendar.getNoteContent());
        }
        tvTitle.setText("Ngày " + date + " Tháng " + month + " Năm " + year);
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        edtNoteContent.setOnFocusChangeListener(this);
        edtNoteName.setOnFocusChangeListener(this);
        radioNormal.setOnClickListener(this);
        radioImportain.setOnClickListener(this);
        radioUnLoop.setOnClickListener(this);
        radioLoop.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        Intent intent = getIntent();
        int dateIntent = intent.getExtras().getInt(MainFragment.DATE);
        int monthIntent = intent.getExtras().getInt(MainFragment.MONTH);
        int yearIntent = intent.getExtras().getInt(MainFragment.YEAR);
        edtNoteName.clearFocus();
        edtNoteContent.clearFocus();
        switch (v.getId()) {
            case R.id.btnSave:
                String date = dateIntent + "/" + monthIntent + "/" + yearIntent;
                String noteName = edtNoteName.getText().toString();
                String noteContent = edtNoteContent.getText().toString();
                String time = timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute();
                int mode = spinner.getSelectedItemPosition();
                int warninglevels;
                if (radioNormal.isChecked()) {
                    warninglevels = 0;
                } else {
                    warninglevels = 1;
                }
                int looping;
                if (radioUnLoop.isChecked()) {
                    looping = 0;
                } else {
                    looping = 1;
                }
                eventCalendar = new EventCalendar(date, noteName, noteContent, time, mode, warninglevels, looping);


                if (!edtNoteName.getText().toString().isEmpty() && !edtNoteContent.getText().toString().isEmpty()
                        && (radioLoop.isChecked() || radioUnLoop.isChecked()) && (radioNormal.isChecked() || radioImportain.isChecked())) {
                    if (hasEvent) {
                        long row = database.updateEvent(eventCalendar);
                        if (row > 0) {
                            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        long id = database.insertEvent(eventCalendar);
                        if (id != -1) {
                            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, 1);
                    }
                    setResult(RESULT_OK);
                    Intent intent2 = new Intent(this, MyService.class);
                    startService(intent2);


                    intent.putExtra(KEY, "Hello from the other side");
                    finish();
                } else {
                    Toast.makeText(this, "Không được để trống các trường", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnCancel:

                showCancelDialog();
                break;
            case R.id.radioNormal:
                edtNoteName.clearFocus();
                edtNoteName.clearFocus();
                break;
            case R.id.radioImportain:
                edtNoteName.clearFocus();
                edtNoteName.clearFocus();
                break;
            case R.id.radioLoop:
                edtNoteName.clearFocus();
                edtNoteName.clearFocus();
                break;
            case R.id.radioUnLoop:
                edtNoteName.clearFocus();
                edtNoteName.clearFocus();
                break;
        }

    }

    public void showCancelDialog() {
        edtNoteName.clearFocus();
        edtNoteName.clearFocus();
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        builder.setTitle("Bạn có muốn hủy bỏ không ? ");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        builder.create().show();
    }

    public void hideSoftKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            hideSoftKeyboard(v);
        }
    }

    @Override
    public void onBackPressed() {
        showCancelDialog();
    }
}
