package com.app.dextrous.barbara.callback;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.app.dextrous.barbara.model.CommandResponse;
import com.app.dextrous.barbara.model.User;
import com.app.dextrous.barbara.response.GenericBeanResponse;
import com.app.dextrous.barbara.util.AndroidUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.dextrous.barbara.constant.BarbaraConstants.MSG_GENERIC_ERROR;

public class UserAuthenticationCallback implements Callback<GenericBeanResponse<User>> {

    private CommandResponse commandResponse;
    private Context context;

    public UserAuthenticationCallback(Context context, CommandResponse commandResponse) {
        this.commandResponse = commandResponse;
        this.context = context;
    }

    @Override
    public void onResponse(Call<GenericBeanResponse<User>> call, Response<GenericBeanResponse<User>> response) {
        // just schedule the stupid transfer
        GenericBeanResponse<User> apiResponse = response.body();
        if (apiResponse != null) {
            Log.d("HTTP RESPONSE", apiResponse.toString());
            if (apiResponse.getSuccess() && apiResponse.getItem() != null) {
                AndroidUtil.scheduleNotificationForCommandResponse(context, apiResponse.getItem(), commandResponse);
            }
        }
    }

    @Override
    public void onFailure(Call<GenericBeanResponse<User>> call, Throwable t) {
        Log.e("HTTP ERROR", t.getMessage(), t);
        Toast.makeText(context, String.format("%s%s", MSG_GENERIC_ERROR, t.getMessage()), Toast.LENGTH_LONG).show();
    }
}
