package com.example.stickynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddStickyNote extends AppCompatActivity {
    private EditText editText;
    private TextView tvDate;
    private TextView header;
    private DatabaseHelper databaseHelper;
    private MaterialButton btnSave;
    private MaterialButton btnCancel;
    private String currentDate;
    private int dbId;
    private String dbNote;
    private String dbDate;
    private boolean isUpdate = false;
    //prevents double click
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sticky_note);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        Utils.setStatusBar(this);
        databaseHelper = new DatabaseHelper(this);
        if (getIntent().hasExtra("note") && getIntent().hasExtra("date") && getIntent().hasExtra("id")){
            dbId = getIntent().getIntExtra("id",-1);
            dbNote = getIntent().getStringExtra("note");
            dbDate = getIntent().getStringExtra("date");
            isUpdate = true;
        }
        setUpGUI();


    }
    /**
     * Setting up ids for the UI components.
     */
    private void setUpGUI() {
        editText = findViewById(R.id.editText);
        tvDate = findViewById(R.id.currentDate);
        header = findViewById(R.id.header);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        editText.setFocusable(true);
        editText.requestFocus();
        if (isUpdate){
            editText.setText(dbNote);
            tvDate.setText(dbDate);
            btnSave.setText("Update");
            header.setText("Update Note");
        }
        else {
            getCurrentDate();
        }
        setUpListeners();
    }

    /**
     * Setting up the formatted date and time.
     */
    private void getCurrentDate() {
        Calendar now = Calendar.getInstance();
        Date currentTime = now.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = df.format(currentTime);
        String onlyTime = currentTime.getHours()+":"+ currentTime.getMinutes();
        currentDate = Utils.getFormattedDateTime(formattedDate,onlyTime);
        tvDate.setText("Dated: "+currentDate);
    }

    /**
     * Setting up listeners.
     */
    private void setUpListeners() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (isUpdate){
                    updateNote();
                }
                else {
                    if (editText.getText().toString().trim().equalsIgnoreCase("") || tvDate.getText().toString().equalsIgnoreCase("")) {
                        Utils.showToast("Please add note",AddStickyNote.this);
                    } else {
                        boolean isDataInserted = databaseHelper.insertNote(editText.getText().toString(), currentDate);
                        if (isDataInserted) {
                            Utils.showToast("Note successfully inserted.",AddStickyNote.this);
                        }
                    }
                }
                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    /**
     * Updating note.
     */
    private void updateNote() {
        if (editText.getText().toString().trim().equalsIgnoreCase("")) {
            Utils.showToast("Please add note",AddStickyNote.this);
            return;
        }
        boolean update = databaseHelper.updateNote(dbId+"",editText.getText().toString(),dbDate);
        if (update){
            Utils.showToast("Note successfully updated",this);
        }
        else {
            Utils.showToast("Cannot update note",this);
        }
    }



}