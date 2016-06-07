package com.app.dextrous.barbara.callback;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.app.dextrous.barbara.model.User;
import com.app.dextrous.barbara.response.GenericBeanResponse;
import com.app.dextrous.barbara.util.AndroidUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.dextrous.barbara.constant.BarbaraConstants.MSG_GENERIC_ERROR;
import static com.app.dextrous.barbara.constant.BarbaraConstants.TAG;
import static com.app.dextrous.barbara.constant.BarbaraConstants.USER_AUTH_OBJECT_PREFERENCE_KEY;


public class LoginResponseCallback extends BaseCallback implements Callback<GenericBeanResponse<User>> {

    private int indexOfTab;
    private ViewFlipper flipper;

    public LoginResponseCallback(Context context, ViewFlipper flipper, int tabIndex) {
        super(context);
        this.flipper = flipper;
        this.indexOfTab = tabIndex;
    }

    @Override
    public void onResponse(Call<GenericBeanResponse<User>> call, Response<GenericBeanResponse<User>> response) {

        GenericBeanResponse<User> apiResponse = response.body();
        if(apiResponse != null ) {
            Log.d("HTTP RESPONSE", response.body().toString());
            if(apiResponse.getSuccess()){
                User user = apiResponse.getItem();
                AndroidUtil.setObjectPreferenceAsString(context, USER_AUTH_OBJECT_PREFERENCE_KEY, user);
                Log.d(TAG,"Setting index = " + indexOfTab);
                flipper.setDisplayedChild(indexOfTab);
            } else {
                // show error message
                Toast.makeText(context, "Error authenticating!!!" + apiResponse.getError(), Toast.LENGTH_LONG).show();
            }
        }
        hideDialog();
    }

    @Override
    public void onFailure(Call<GenericBeanResponse<User>> call, Throwable t) {
        Log.e("HTTP ERROR", t.getMessage(), t);
        Toast.makeText(context, String.format("%s%s", MSG_GENERIC_ERROR, t.getMessage()), Toast.LENGTH_LONG).show();
        hideDialog();
    }
}
