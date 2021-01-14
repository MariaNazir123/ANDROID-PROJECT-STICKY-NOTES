package com.example.stickynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ShowNote extends AppCompatActivity {
    private String dbNote;
    private String dbDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_note);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        Utils.setStatusBar(this);
        if (getIntent().hasExtra("note") && getIntent().hasExtra("date")){
            dbNote = getIntent().getStringExtra("note");
            dbDate = getIntent().getStringExtra("date");
        }
        loadGUI();

    }

    /**
     * Setting up ids of UI components.
     */
    private void loadGUI() {
        TextView tvDate = findViewById(R.id.currentDate);
        TextView tvNote = findViewById(R.id.tvNote);
        tvDate.setText("Dated: "+dbDate);
        tvNote.setText("Note:\n"+dbNote);
    }
}