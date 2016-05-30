package com.app.dextrous.barbara.util;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;

import com.google.gson.Gson;

import static com.app.dextrous.barbara.constant.BarbaraConstants.APP_SHARED_PREFERENCE_KEY;
import static com.app.dextrous.barbara.constant.BarbaraConstants.STRING_BLANK;
import static com.app.dextrous.barbara.constant.BarbaraConstants.STRING_CANCEL_LINK_FOR_ALERT;
import static com.app.dextrous.barbara.constant.BarbaraConstants.STRING_SETTING_LINK_FOR_ALERT;


public class AndroidUtil {
    static String TAG = AndroidUtil.class.getName();

    public static String getStringPreferenceValue(Context context, String preferenceKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP_SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceKey, "");
    }

    public static void setObjectPreferenceAsString(Context context, String preferenceKey, Object value) {
        SharedPreferences mPrefs = context.getSharedPreferences(APP_SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(value); // myObject - instance of MyObject
        prefsEditor.putString(preferenceKey, json);
        prefsEditor.commit();
    }

    public static  <T extends Object>  T getPreferenceAsObject(Context context, String preferenceKey, Class<T> type){
        String json = getStringPreferenceValue(context, preferenceKey);
        Gson gson = new Gson();
        return STRING_BLANK.equalsIgnoreCase(json) ? null : gson.fromJson(json, type) ;
    }

    public static void showAlertDialog(final Activity activity, String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // On pressing the Settings button.
        alertDialog.setPositiveButton(STRING_SETTING_LINK_FOR_ALERT, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.getBaseContext().startActivity(intent);
            }
        });

        // On pressing the cancel button
        alertDialog.setNegativeButton(STRING_CANCEL_LINK_FOR_ALERT, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    public static ProgressDialog showProgressDialog(Context context, String titleText, String processingText) {
        ProgressDialog progress = new ProgressDialog(context);
        progress.setTitle(titleText);
        progress.setCancelable(Boolean.FALSE);
        progress.setCanceledOnTouchOutside(Boolean.FALSE);
        progress.setMessage(processingText);
        progress.show();
        return progress;
    }

}
