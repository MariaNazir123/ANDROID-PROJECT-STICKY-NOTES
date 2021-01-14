package com.example.stickynotes;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Random;

public class StickyNotesAdapter extends RecyclerView.Adapter<StickyNotesAdapter.ViewHolder> {
    private Context context;
    private ArrayList<StickyNotesModel> stickyNotesModels;
    private ArrayList<String> colors;
    private LayoutInflater mInflater;
    //prevents double click
    private long mLastClickTime = 0;

    // data is passed into the constructor
    public StickyNotesAdapter(Context context, ArrayList<StickyNotesModel> stickyNotesModels, ArrayList<String> colors) {
        this.mInflater = LayoutInflater.from(context);
        this.stickyNotesModels = stickyNotesModels;
        this.context = context;
        this.colors = colors;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_item_view, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Random rand = new Random();
        String random = colors.get(rand.nextInt(colors.size()));
        setBackgroundColor(random,holder);
        holder.stickyNoteText.setText(stickyNotesModels.get(position).note);
        holder.date.setText(stickyNotesModels.get(position).date);
        setUpListeners(holder,position);
    }

    /**
     * Setting background color of card.
     * @param random
     * @param holder
     */
    private void setBackgroundColor(String random, ViewHolder holder) {
        if (random.equalsIgnoreCase("yellow")){
            holder.cardViewSingleItem.setCardBackgroundColor(context.getResources().getColor(R.color.yellow));
        }
        else if (random.equalsIgnoreCase("light_blue")){
            holder.cardViewSingleItem.setCardBackgroundColor(context.getResources().getColor(R.color.light_blue));
        }
        else if (random.equalsIgnoreCase("light_green")){
            holder.cardViewSingleItem.setCardBackgroundColor(context.getResources().getColor(R.color.light_green));
        }
        else if (random.equalsIgnoreCase("light_orange")){
            holder.cardViewSingleItem.setCardBackgroundColor(context.getResources().getColor(R.color.light_orange));
        }
        else if (random.equalsIgnoreCase("pink")){
            holder.cardViewSingleItem.setCardBackgroundColor(context.getResources().getColor(R.color.pink));
        }
        else if (random.equalsIgnoreCase("light_purple")){
            holder.cardViewSingleItem.setCardBackgroundColor(context.getResources().getColor(R.color.light_purple));
        }
        else if (random.equalsIgnoreCase("light_brown")){
            holder.cardViewSingleItem.setCardBackgroundColor(context.getResources().getColor(R.color.light_brown));
            setTintWhite(holder);
        }
    }

    /**
     * Setting tints of imageviews to white and text views color to white.
     * @param holder
     */
    private void setTintWhite(ViewHolder holder) {
        holder.stickyNoteText.setTextColor(context.getResources().getColor(R.color.white));
        holder.date.setTextColor(context.getResources().getColor(R.color.white));
        Resources res = context.getResources();
        final int newColor = res.getColor(R.color.white);
        holder.ivEdit.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
        holder.ivDelete.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
    }

    /**
     * Setting up listeners.
     * @param holder
     * @param position
     */
    private void setUpListeners(ViewHolder holder, int position) {
        holder.ivDelete.setOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            showDeleteNoteAlert(position);
        });
        holder.ivEdit.setOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            Intent intent = new Intent(context,AddStickyNote.class);
            intent.putExtra("id",stickyNotesModels.get(position).id);
            intent.putExtra("note",stickyNotesModels.get(position).note);
            intent.putExtra("date",stickyNotesModels.get(position).date);
            ((MainActivity)context).startActivityForResult(intent,RequestCodes.REQUEST_CODE_REFRESH);
        });
        holder.cardViewSingleItem.setOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            Intent intent = new Intent(context,ShowNote.class);
            intent.putExtra("note",stickyNotesModels.get(position).note);
            intent.putExtra("date",stickyNotesModels.get(position).date);
            context.startActivity(intent);
        });
    }

    /**
     * Showing delete alert on deleting the note.
     * @param position
     */
    private void showDeleteNoteAlert(int position) {
        AlertDialog.Builder builderExitDialog = new AlertDialog.Builder(context);
        builderExitDialog.setTitle("Warning");
        builderExitDialog.setCancelable(false);
        builderExitDialog.setMessage("Are you sure you want to delete?");
        builderExitDialog.setPositiveButton("Yes", (dialog, which) -> {
            ((MainActivity)context).deleteNote(stickyNotesModels.get(position).id+"");
        });
        builderExitDialog.setNegativeButton("No", (dialog, which) -> dialog.cancel());
        builderExitDialog.show();
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return stickyNotesModels.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView stickyNoteText;
        TextView date;
        ImageView ivEdit;
        ImageView ivDelete;
        CardView cardViewSingleItem;

        ViewHolder(View itemView) {
            super(itemView);
            stickyNoteText = itemView.findViewById(R.id.stickyNoteText);
            date = itemView.findViewById(R.id.date);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            cardViewSingleItem = itemView.findViewById(R.id.cardViewSingleItem);
        }
    }
}
