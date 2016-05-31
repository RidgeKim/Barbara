package com.app.dextrous.barbara.callback;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.app.dextrous.barbara.activity.TransactionDetailsActivity;
import com.app.dextrous.barbara.adapter.CreditTransactionArrayAdapter;
import com.app.dextrous.barbara.adapter.TransactionArrayAdapter;
import com.app.dextrous.barbara.model.CreditTransaction;
import com.app.dextrous.barbara.model.Transaction;
import com.app.dextrous.barbara.response.GenericListResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.dextrous.barbara.constant.BarbaraConstants.INTENT_PARAM_TRANSACTION_ITEM_KEY;
import static com.app.dextrous.barbara.constant.BarbaraConstants.MSG_GENERIC_ERROR;

public class CreditTransactionListCallback extends BaseCallback implements Callback<GenericListResponse<CreditTransaction>> {
    private ListView listView;

    public CreditTransactionListCallback(Context context, ListView listView) {
        super(context);
        this.listView = listView;
    }
    @Override
    public void onResponse(Call<GenericListResponse<CreditTransaction>> call, Response<GenericListResponse<CreditTransaction>> response) {
        GenericListResponse<CreditTransaction> apiResponse = response.body();
        System.out.println(response.body());
        if(apiResponse != null) {
            List<CreditTransaction> transactionList = apiResponse.getItems();
            if(transactionList != null && !transactionList.isEmpty()){
                ListAdapter listAdapter = new CreditTransactionArrayAdapter(context, transactionList);
                listView.setAdapter(listAdapter);
            }
            Log.d("HTTP RESPONSE", apiResponse.toString());
        }
        hideDialog();
    }

    @Override
    public void onFailure(Call<GenericListResponse<CreditTransaction>> call, Throwable t) {
        Log.e("HTTP ERROR", t.getMessage(), t);
        Toast.makeText(context, String.format("%s%s", MSG_GENERIC_ERROR, t.getMessage()), Toast.LENGTH_LONG).show();
        hideDialog();
    }
}
