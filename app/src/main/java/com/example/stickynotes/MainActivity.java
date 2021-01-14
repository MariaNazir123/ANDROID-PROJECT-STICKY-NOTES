package com.example.stickynotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvStickyNotes;
    private FloatingActionButton addStickyNote;
    private DatabaseHelper databaseHelper;
    private TextView tvNoNotes;
    private ArrayList<String> colors = new ArrayList<>();
    //prevents double click
    private long mLastClickTime = 0;
    //after deletion of sticky note recyclerview state will be same i.e. will not scroll to top.
    public Parcelable recyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        databaseHelper = new DatabaseHelper(this);
        setUpColors();
        setUpGUI();
        Utils.setStatusBar(this);
        getStickyNotes();
    }

    /**
      Setting up colors in the list and then will shuffle it and add random colors to the sticky notes in the adapter.
     */
    private void setUpColors() {
        colors.add("yellow");
        colors.add("light_blue");
        colors.add("light_green");
        colors.add("light_orange");
        colors.add("pink");
        colors.add("light_purple");
        colors.add("light_brown");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCodes.REQUEST_CODE_REFRESH:
                    getStickyNotes();
                    break;
            }
        }
    }

    /**
     * Setting up ids for the UI components.
     */
    private void setUpGUI() {
        rvStickyNotes = findViewById(R.id.rvStickyNotes);
        addStickyNote = findViewById(R.id.addStickyNote);
        tvNoNotes = findViewById(R.id.tvNoNotes);
        setUpClickListeners();
        addScrollListener();
    }

    /**
     * Adding scroll listener and saving state to prevent the scroll to top on stick note delete.
     */
    private void addScrollListener() {
        rvStickyNotes.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                recyclerViewState = rvStickyNotes.getLayoutManager().onSaveInstanceState();
            }
        });
    }

    /**
     * Setting up listeners.
     */
    private void setUpClickListeners() {
        addStickyNote.setOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            intentToAddStickyNote();
        });
    }

    /**
     * Intent to add sticky note class.
     */
    private void intentToAddStickyNote() {
        Intent intent = new Intent(this,AddStickyNote.class);
        startActivityForResult(intent,RequestCodes.REQUEST_CODE_REFRESH);
    }

    /**
     * Getting list of sticky notes.
     */
    private void getStickyNotes() {
        databaseHelper = new DatabaseHelper(this);
        Cursor res = databaseHelper.getAllNotes();
        if (res.getCount() == 0){
            rvStickyNotes.setVisibility(View.GONE);
            tvNoNotes.setVisibility(View.VISIBLE);
            return;
        }
        rvStickyNotes.setVisibility(View.VISIBLE);
        tvNoNotes.setVisibility(View.GONE);
        ArrayList<StickyNotesModel> stickyNotesModels = new ArrayList<>();
        while (res.moveToNext()){
            StickyNotesModel stickyNotesModel = new StickyNotesModel();
            stickyNotesModel.id = res.getInt(0); // column name id
            stickyNotesModel.note = res.getString(1); // column name note
            stickyNotesModel.date = res.getString(2); // column name date
            stickyNotesModels.add(stickyNotesModel);
        }
        setAdapter(stickyNotesModels);

    }

    /**
     * Setting adapter for list of sticky notes.
     * @param stickyNotesModels
     */
    private void setAdapter(ArrayList<StickyNotesModel> stickyNotesModels) {
        if (stickyNotesModels.isEmpty()){
            rvStickyNotes.setVisibility(View.GONE);
            tvNoNotes.setVisibility(View.VISIBLE);
        }
        else {
            LinearLayoutManager llManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            rvStickyNotes.setLayoutManager(llManager);
            StickyNotesAdapter numericDialogAdapter = new StickyNotesAdapter(this, stickyNotesModels,colors);
            rvStickyNotes.setAdapter(numericDialogAdapter);
        }
        if (recyclerViewState != null){
            rvStickyNotes.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        }
    }

    /**
     * Deleting note.
     * @param id
     */
    void deleteNote(String id){
        int deletedRows = databaseHelper.deleteNote(id);
        if (deletedRows > 0){
            Utils.showToast("Note successfully deleted",this);
            getStickyNotes();
        }
        else {
            Utils.showToast("Can't delete note",this);
        }
    }
}