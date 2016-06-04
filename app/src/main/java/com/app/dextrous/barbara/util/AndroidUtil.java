package com.app.dextrous.barbara.util;


import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.app.dextrous.barbara.model.CommandResponse;
import com.app.dextrous.barbara.model.User;
import com.app.dextrous.barbara.receiver.NotificationPublisher;
import com.google.gson.Gson;

import java.util.Date;

import static com.app.dextrous.barbara.constant.BarbaraConstants.APP_SHARED_PREFERENCE_KEY;
import static com.app.dextrous.barbara.constant.BarbaraConstants.INTENT_PARAM_IS_TRANSACTION_REQUEST;
import static com.app.dextrous.barbara.constant.BarbaraConstants.INTENT_PARAM_SCHEDULED_RESPONSE_TEXT;
import static com.app.dextrous.barbara.constant.BarbaraConstants.INTENT_PARAM_USER_ITEM_ID_KEY;
import static com.app.dextrous.barbara.constant.BarbaraConstants.RANDOM_NUMBER_PRECESSION;
import static com.app.dextrous.barbara.constant.BarbaraConstants.REQUEST_PERMISSION_SELF_CHECK;
import static com.app.dextrous.barbara.constant.BarbaraConstants.STRING_BLANK;
import static com.app.dextrous.barbara.constant.BarbaraConstants.STRING_CANCEL_LINK_FOR_ALERT;
import static com.app.dextrous.barbara.constant.BarbaraConstants.STRING_SETTING_LINK_FOR_ALERT;
import static com.app.dextrous.barbara.constant.BarbaraConstants.WORDS_INDICATING_RELATIONS_IN_CONTACTS;


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
        prefsEditor.apply();
    }

    public static <T> T getPreferenceAsObject(Context context, String preferenceKey, Class<T> type) {
        String json = getStringPreferenceValue(context, preferenceKey);
        Gson gson = new Gson();
        return STRING_BLANK.equalsIgnoreCase(json) ? null : gson.fromJson(json, type);
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

    public static boolean isFileReadWritePermissionEnabled(Activity activity) {
        return ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isMicrophonePermissionEnabled(Activity activity) {
        return ContextCompat.checkSelfPermission(activity,
                Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isContactsPermissionEnabled(Activity activity) {
        return ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static void checkAndRequestNecessaryPermissions(Activity activity) {
        checkAndRequestPermission(activity, Manifest.permission.RECORD_AUDIO);
        checkAndRequestPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        checkAndRequestPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        checkAndRequestPermission(activity, Manifest.permission.READ_CONTACTS);
    }

    public static void checkAndRequestPermission(Activity activity, String permissionString) {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(activity,
                permissionString)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    permissionString)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(activity,
                        new String[]{permissionString},
                        REQUEST_PERMISSION_SELF_CHECK);

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(activity,
                        new String[]{permissionString},
                        REQUEST_PERMISSION_SELF_CHECK);
            }
        }
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

    public static Notification getNotification(Context context, String title, String content, PendingIntent pendingIntent) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(title);
        builder.setContentText(content);
        if (pendingIntent != null) {
            builder.setContentIntent(pendingIntent);
        }
        builder.setSmallIcon(android.R.drawable.ic_dialog_alert);
        return builder.build();
    }

    public static void scheduleNotificationForCommandResponse(Context context,
                                                              User user,
                                                              CommandResponse commandResponse) {
        System.out.println("Scheduling notification");
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(INTENT_PARAM_IS_TRANSACTION_REQUEST,
                (!commandResponse.getIsReminderRequest()
                        && commandResponse.getIsTransactionRequest()));
        notificationIntent.putExtra(INTENT_PARAM_SCHEDULED_RESPONSE_TEXT, commandResponse.getScheduledResponseText());
        notificationIntent.putExtra(INTENT_PARAM_USER_ITEM_ID_KEY, user.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Date associatedTime = commandResponse.getAssociatedTime();
        long futureInMillis = associatedTime.getTime();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    public static double getRandomNumber() {
        return Math.random() * RANDOM_NUMBER_PRECESSION;
    }

    public static String checkIdThereIsARelationShipKeyInString(String sentence) {
        String chosenRelationship = null;
        for (String relation : WORDS_INDICATING_RELATIONS_IN_CONTACTS) {
            if (sentence.contains(relation)) {
                chosenRelationship = relation;
                break;
            }
        }
        return chosenRelationship;
    }

    public static String replaceRelationshipWordsWithNames(Context context, String textCommand) {
        String relation = checkIdThereIsARelationShipKeyInString(textCommand);
        if (relation != null) {
            String name = readContactsWithRelationshipAndGetName(context, relation);
            if (!STRING_BLANK.equals(name)) {
                // replace relation with name
                textCommand = textCommand.replace(relation, name);
            }
        }
        return textCommand;
    }

    private static String readContactsWithRelationshipAndGetName(Context context, String relation) {
        Uri uri = ContactsContract.Data.CONTENT_URI;
        String where = String.format(
                "%s = ?",
                ContactsContract.CommonDataKinds.Relation.NAME);

        String[] whereParams = new String[]{
                relation,
        };

        String[] selectColumns = new String[]{
                ContactsContract.CommonDataKinds.Identity.DISPLAY_NAME,
                // add additional columns here
        };
        Cursor relationCursor = context.getContentResolver().query(
                uri,
                selectColumns,
                where,
                whereParams,
                null);
        if (relationCursor != null
                && relationCursor.moveToFirst()) {
            return relationCursor.getString(
                    relationCursor.getColumnIndex(ContactsContract.CommonDataKinds.Identity.DISPLAY_NAME));
        }
        return relation;
    }
}
