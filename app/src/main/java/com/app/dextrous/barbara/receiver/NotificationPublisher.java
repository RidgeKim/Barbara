package com.app.dextrous.barbara.receiver;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.app.dextrous.barbara.R;
import com.app.dextrous.barbara.callback.DeferredTransactionCallback;
import com.app.dextrous.barbara.model.CommandResponse;
import com.app.dextrous.barbara.response.GenericBeanResponse;
import com.app.dextrous.barbara.service.BarbaraService;
import com.app.dextrous.barbara.util.AndroidUtil;
import com.app.dextrous.barbara.wrapper.RetrofitWrapper;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

import static com.app.dextrous.barbara.constant.BarbaraConstants.FIELD_COMMAND_STRING;
import static com.app.dextrous.barbara.constant.BarbaraConstants.FIELD_USER_ID;
import static com.app.dextrous.barbara.constant.BarbaraConstants.INTENT_PARAM_IS_TRANSACTION_REQUEST;
import static com.app.dextrous.barbara.constant.BarbaraConstants.INTENT_PARAM_SCHEDULED_RESPONSE_TEXT;
import static com.app.dextrous.barbara.constant.BarbaraConstants.INTENT_PARAM_USER_ITEM_ID_KEY;
import static com.app.dextrous.barbara.constant.BarbaraConstants.NOTIFICATION_TITLE;
import static com.app.dextrous.barbara.constant.BarbaraConstants.STRING_BLANK;

public class NotificationPublisher extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        System.out.println("Getting notifications....");
        boolean isTransactionRequest = intent.getBooleanExtra(INTENT_PARAM_IS_TRANSACTION_REQUEST, false);
        String scheduledResponseText = intent.getStringExtra(INTENT_PARAM_SCHEDULED_RESPONSE_TEXT);
        int userId = intent.getIntExtra(INTENT_PARAM_USER_ITEM_ID_KEY, -1);
        // here get commandResponseInstead of notification
        if(isTransactionRequest
                && userId!= -1) {
            String BASE_URL = context.getResources().getString(R.string.base_api_url);
            System.out.println("Transaction handling");
            final BarbaraService apiService = RetrofitWrapper.build(BASE_URL);
            Map<String,Object> form = new HashMap<>();
            form.put(FIELD_USER_ID, userId);
            form.put(FIELD_COMMAND_STRING, scheduledResponseText);
            Call<GenericBeanResponse<CommandResponse>> responseCall = apiService.executeAuthenticatedTransaction(form);

            // in response call call back open the chat activity and make her speak the command
            responseCall.enqueue(new DeferredTransactionCallback(context));
        } else if (!STRING_BLANK.equals(scheduledResponseText)) {
            System.out.println("Reminder schedule");
            Notification notification = AndroidUtil.getNotification(context, NOTIFICATION_TITLE, scheduledResponseText, null);
            notificationManager.notify((int) AndroidUtil.getRandomNumber(), notification);
        } else {
            Log.e("ERROR", "Invalid command response from bundle");
        }

    }
}
