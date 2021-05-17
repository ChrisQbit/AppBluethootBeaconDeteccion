package com.cdsautomatico.apparkame2.utils;

import android.app.Activity;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

import com.cdsautomatico.apparkame2.R;

public class GenericDialogs {

    public static void confirm(Activity activity, int title, int message, final Runnable callback){
        confirm(activity, activity.getText(title).toString(),
                activity.getText(message).toString(), callback);
    }

    public static void confirm(Activity activity, String title, String message, final Runnable callback){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);
        alertBuilder.setTitle(title);
        alertBuilder.setMessage(message);
        alertBuilder.setPositiveButton(R.string.ok, (DialogInterface dialog, int which) -> {
            callback.run();
        });
        alertBuilder.setNegativeButton(R.string.cancel, null);
        alertBuilder.create().show();
    }

    public static void success(Activity activity, String message){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);
        alertBuilder.setTitle("Operación Exitosa");
        alertBuilder.setMessage(message);
        alertBuilder.setPositiveButton(R.string.ok, null);
        alertBuilder.create().show();
    }

    public static void error(Activity activity, String message){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);
        alertBuilder.setTitle("Error");
        alertBuilder.setMessage(message);
        alertBuilder.setPositiveButton(R.string.ok, null);
        alertBuilder.create().show();
    }
}
