package com.app.dextrous.barbara.callback;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.dextrous.barbara.activity.UserPreferencesActivity;
import com.app.dextrous.barbara.constant.BarbaraConstants;
import com.app.dextrous.barbara.model.UserPreference;
import com.app.dextrous.barbara.response.GenericBeanResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.dextrous.barbara.constant.BarbaraConstants.MSG_GENERIC_ERROR;

public class UserPreferencesCallback implements Callback<GenericBeanResponse<UserPreference>> {

    private TextView nickNameTextView;
    private TextView securityQuestionTextView;
    private TextView budgetTextView;
    private Button editPreferenceButton;
    private Context context;

    public UserPreferencesCallback(Context context, TextView budgetTextView,
                                   TextView securityQuestionTextView,
                                   TextView nickNameTextView,
                                   Button editPreferenceButton) {
        this.context = context;
        this.nickNameTextView = nickNameTextView;
        this.securityQuestionTextView = securityQuestionTextView;
        this.budgetTextView = budgetTextView;
        this.editPreferenceButton = editPreferenceButton;
    }

    @Override
    public void onResponse(Call<GenericBeanResponse<UserPreference>> call, Response<GenericBeanResponse<UserPreference>> response) {
        GenericBeanResponse<UserPreference> apiResponse = response.body();
        if (apiResponse != null) {
            Log.d("HTTP RESPONSE", apiResponse.toString());
            if (apiResponse.getSuccess() && apiResponse.getItem() != null) {
                final UserPreference item = apiResponse.getItem();
                if(item.getNickName() != null
                        && nickNameTextView!= null) {
                    nickNameTextView.setText(item.getNickName());
                }
                if(item.getBudget() != 0.0
                        && budgetTextView != null) {
                    budgetTextView.setText(String.valueOf(item.getBudget()));
                }
                if(item.getSecurityQuestion() != null
                        && securityQuestionTextView != null) {
                    securityQuestionTextView.setText(item.getSecurityQuestion());
                }
                if (editPreferenceButton != null) {
                    editPreferenceButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, UserPreferencesActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra(BarbaraConstants.INTENT_PARAM_USER_PREFERENCE_ITEM_KEY, item);
                            context.startActivity(intent);
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onFailure(Call<GenericBeanResponse<UserPreference>> call, Throwable t) {
        Log.e("HTTP ERROR", t.getMessage(), t);
        Toast.makeText(context, String.format("%s%s", MSG_GENERIC_ERROR, t.getMessage()), Toast.LENGTH_LONG).show();
    }
}
