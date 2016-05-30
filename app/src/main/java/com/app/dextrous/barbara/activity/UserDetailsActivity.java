package com.app.dextrous.barbara.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.app.dextrous.barbara.R;
import com.app.dextrous.barbara.model.User;
import com.app.dextrous.barbara.service.BarbaraService;
import com.app.dextrous.barbara.wrapper.RetrofitWrapper;

import static com.app.dextrous.barbara.constant.BarbaraConstants.INTENT_PARAM_USER_ITEM_ID_KEY;
import static com.app.dextrous.barbara.constant.BarbaraConstants.INTENT_PARAM_USER_ITEM_KEY;


public class UserDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Object temp = getIntent().getSerializableExtra(INTENT_PARAM_USER_ITEM_KEY);
        TextView fullNameText = (TextView) findViewById(R.id.fullNameValueLabel);
        TextView usernameText = (TextView) findViewById(R.id.usernameValueLabel);
        TextView speakerProfileText = (TextView) findViewById(R.id.speakerProfileValueLabel);
        TextView currencyTypeText = (TextView) findViewById(R.id.currencyTypeValueLabel);
        TextView walletAmountText = (TextView) findViewById(R.id.walletAmountValue);
        TextView creditAmountText = (TextView) findViewById(R.id.creditAmountValue);
        if(temp != null
                && temp instanceof User) {
            User userDetail = (User) temp;
            if (fullNameText != null
                    && usernameText != null
                    && speakerProfileText != null
                    && currencyTypeText != null
                    && walletAmountText != null
                    && creditAmountText != null) {
                fullNameText.setText(userDetail.getFullName());
                usernameText.setText(userDetail.getUsername());
                speakerProfileText.setText(userDetail.getSpeakerProfileId());
                currencyTypeText.setText(userDetail.getCurrencyType());
                walletAmountText.setText(String.valueOf(userDetail.getWallet()));
                creditAmountText.setText(String.valueOf(userDetail.getCredit()));
            }
        } else {
            temp = getIntent().getSerializableExtra(INTENT_PARAM_USER_ITEM_ID_KEY);
            if (temp != null) {
                // Create a callback
                // send the input fields
                // execute the service function
                String BASE_URL = getResources().getString(R.string.base_api_url);
                Log.d("BASE_URL", BASE_URL);

                BarbaraService apiService = RetrofitWrapper.build(BASE_URL);

            }
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
