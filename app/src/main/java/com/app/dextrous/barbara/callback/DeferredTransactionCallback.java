package com.app.dextrous.barbara.callback;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.app.dextrous.barbara.activity.UserChatActivity;
import com.app.dextrous.barbara.constant.BarbaraConstants;
import com.app.dextrous.barbara.model.CommandResponse;
import com.app.dextrous.barbara.response.GenericBeanResponse;
import com.app.dextrous.barbara.util.AndroidUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.dextrous.barbara.constant.BarbaraConstants.INTENT_PARAM_COMMAND_RESPONSE;
import static com.app.dextrous.barbara.constant.BarbaraConstants.MSG_GENERIC_ERROR;
import static com.app.dextrous.barbara.constant.BarbaraConstants.NOTIFICATION_TITLE;

public class DeferredTransactionCallback implements Callback<GenericBeanResponse<CommandResponse>>{
    private Context context;

    public DeferredTransactionCallback(Context context) {
        this.context = context;
    }

    @Override
    public void onResponse(Call<GenericBeanResponse<CommandResponse>> call, Response<GenericBeanResponse<CommandResponse>> response) {
        // on success , create activity and speak  the command
        GenericBeanResponse<CommandResponse> body = response.body();
        Log.d("HTTP RESPONSE", body.toString());
        if(body != null && body.getSuccess()) {
            Intent intent = new Intent(context, UserChatActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(INTENT_PARAM_COMMAND_RESPONSE, body.getItem());
            PendingIntent pendingIntent = PendingIntent.getActivity(context, BarbaraConstants.REQUEST_CODE_CHAT_ACTIVITY, intent,  PendingIntent.FLAG_ONE_SHOT);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            Notification notification = AndroidUtil.getNotification(context, NOTIFICATION_TITLE, body.getItem().getResponseText(), pendingIntent);
            notificationManager.notify((int) AndroidUtil.getRandomNumber(), notification);

        }
    }

    @Override
    public void onFailure(Call<GenericBeanResponse<CommandResponse>> call, Throwable t) {
        Log.e("HTTP ERROR", t.getMessage(), t);
        Toast.makeText(context, String.format("%s%s", MSG_GENERIC_ERROR, t.getMessage()), Toast.LENGTH_LONG).show();
    }
}
