package com.example.stickynotes;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    /**
     * Set up status bar here.
     */
    public static void setStatusBar(Activity activity) {
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= 23) {}


            window.setStatusBarColor(ContextCompat.getColor(activity, R.color.white));
        } else {
            try {
                window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get formatted date time.

     */
    public static String getFormattedDateTime(String date,String time) {
        String completedateTime = "";
        DateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        DateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
        Date parsedDate = null;


        try {
            parsedDate = originalFormat.parse(date+" "+time);
            completedateTime = targetFormat.format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return completedateTime;
    }
    /**
     * Showing message in the toast.
     *  message
     */
    public static void showToast(String message, Context context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
