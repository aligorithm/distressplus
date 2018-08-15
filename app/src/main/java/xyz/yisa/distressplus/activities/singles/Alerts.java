package xyz.yisa.distressplus.activities.singles;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import xyz.yisa.distressplus.R;

import static android.content.Context.VIBRATOR_SERVICE;

public class Alerts {
    private static final Alerts ourInstance = new Alerts();

    public static Alerts getInstance() {
        return ourInstance;
    }

    private Alerts() {
    }

    public void showError(Activity activity,String message){
        Vibrator vibrator = (Vibrator) activity.getSystemService(VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
        } else{
            vibrator.vibrate(500);
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity, R.style.AppDialogTheme);
        alertDialogBuilder
                .setMessage("\uD83D\uDE22 " + message)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void showNotice(Activity activity, String message){
        Vibrator vibrator = (Vibrator) activity.getSystemService(VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
        } else{
            vibrator.vibrate(500);
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity, R.style.AppDialogTheme);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public void showNotice(Activity activity, String message, String title){
        Vibrator vibrator = (Vibrator) activity.getSystemService(VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
        } else{
            vibrator.vibrate(500);
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity, R.style.AppDialogTheme);
        alertDialogBuilder
                .setTitle(title)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public void showNotice(Activity activity, String message, DialogInterface.OnDismissListener dismissListener){
        Vibrator vibrator = (Vibrator) activity.getSystemService(VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
        } else{
            vibrator.vibrate(500);
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity, R.style.AppDialogTheme);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.setOnDismissListener(dismissListener);
    }
    public AlertDialog showProgress(Activity activity){
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View alertView = layoutInflater.inflate(R.layout.progress_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setView(alertView);
        alertDialogBuilder
                .setCancelable(false);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        return alertDialog;
    }
}

