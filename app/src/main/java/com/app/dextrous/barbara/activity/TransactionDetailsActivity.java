package com.app.dextrous.barbara.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.app.dextrous.barbara.R;
import com.app.dextrous.barbara.model.Transaction;
import com.app.dextrous.barbara.service.BarbaraService;
import com.app.dextrous.barbara.wrapper.RetrofitWrapper;

import static com.app.dextrous.barbara.constant.BarbaraConstants.INTENT_PARAM_TRANSACTION_ITEM_ID_KEY;
import static com.app.dextrous.barbara.constant.BarbaraConstants.INTENT_PARAM_TRANSACTION_ITEM_KEY;


public class TransactionDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Object temp = getIntent().getSerializableExtra(INTENT_PARAM_TRANSACTION_ITEM_KEY);
        TextView descriptionText = (TextView) findViewById(R.id.descriptionValueLabel);
        TextView fromUserText = (TextView) findViewById(R.id.fromUserValueLabel);
        TextView toUserText = (TextView) findViewById(R.id.toUserValueLabel);
        TextView currencyTypeText = (TextView) findViewById(R.id.currencyTypeValueLabel);
        TextView amountText = (TextView) findViewById(R.id.amountValueLabel);
        TextView transactionDateText = (TextView) findViewById(R.id.transactionDateValueLabel);
        if(temp != null
                && temp instanceof Transaction) {
            Transaction transaction = (Transaction) temp;
            if (descriptionText != null
                    && fromUserText != null
                    && toUserText != null
                    && currencyTypeText != null
                    && amountText != null
                    && transactionDateText != null) {
                descriptionText.setText(transaction.getDescription());
                fromUserText.setText(transaction.getFromUser().getFullName());
                toUserText.setText(transaction.getToUser().getFullName());
                currencyTypeText.setText(transaction.getFromUser().getCurrencyType());
                amountText.setText(String.valueOf(transaction.getAmount()));
                transactionDateText.setText(String.valueOf(transaction.getCreatedTS()));
            }
        } else {
            temp = getIntent().getSerializableExtra(INTENT_PARAM_TRANSACTION_ITEM_ID_KEY);
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
