package com.example.vaio.calendar;

import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.t3h.database.MyDatabase;
import com.t3h.item.EventCalendar;

public class NoteEventActivity extends AppCompatActivity implements View.OnClickListener {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_event);
        database = new MyDatabase(this);
        initViews();
    }

    private void initViews() {
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
        timePicker.setIs24HourView(true);
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
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        Intent intent = getIntent();
        int dateIntent = intent.getExtras().getInt(MainFragment.DATE);
        int monthIntent = intent.getExtras().getInt(MainFragment.MONTH);
        int yearIntent = intent.getExtras().getInt(MainFragment.YEAR);

        switch (v.getId()) {
            case R.id.btnSave:
                String date = dateIntent + "/" + monthIntent + "/" + yearIntent;
                String noteName = edtNoteName.getText().toString();
                String noteContent = edtNoteContent.getText().toString();
                String time =  timePicker.getHour()+ ":" +timePicker.getMinute() ;
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
                        if(row>0){
                            Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(this,"Fail",Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        long id = database.insertEvent(eventCalendar);
                        if(id!=-1){
                            Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(this,"Fail",Toast.LENGTH_SHORT).show();
                        }
                    }
                    setResult(RESULT_OK);
                    finish();
                }else {
                    Toast.makeText(this,"Không được để trống các trường",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnCancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }

}
