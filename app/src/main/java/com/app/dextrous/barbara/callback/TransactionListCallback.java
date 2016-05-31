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
import com.app.dextrous.barbara.activity.UserDetailsActivity;
import com.app.dextrous.barbara.adapter.TransactionArrayAdapter;
import com.app.dextrous.barbara.adapter.UserArrayAdapter;
import com.app.dextrous.barbara.constant.BarbaraConstants;
import com.app.dextrous.barbara.model.Transaction;
import com.app.dextrous.barbara.model.User;
import com.app.dextrous.barbara.response.GenericListResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.dextrous.barbara.constant.BarbaraConstants.*;
import static com.app.dextrous.barbara.constant.BarbaraConstants.MSG_GENERIC_ERROR;

public class TransactionListCallback extends BaseCallback implements Callback<GenericListResponse<Transaction>> {
    private ListView listView;

    public TransactionListCallback(Context context, ListView listView) {
        super(context);
        this.listView = listView;
    }
    @Override
    public void onResponse(Call<GenericListResponse<Transaction>> call, Response<GenericListResponse<Transaction>> response) {
        GenericListResponse<Transaction> apiResponse = response.body();
        System.out.println(response.body());
        if(apiResponse != null) {
            List<Transaction> transactionList = apiResponse.getItems();
            if(transactionList != null && !transactionList.isEmpty()){
                ListAdapter listAdapter = new TransactionArrayAdapter(context, transactionList);
                listView.setAdapter(listAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view,
                                            int position, long id) {
                        final Transaction item = (Transaction) parent.getItemAtPosition(position);
                        Log.d("Item Selected", item.toString());
                        Intent intent = new Intent(context, TransactionDetailsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(INTENT_PARAM_TRANSACTION_ITEM_KEY, item);
                        context.startActivity(intent);
                    }

                });
            }
            Log.d("HTTP RESPONSE", apiResponse.toString());
        }
        hideDialog();
    }

    @Override
    public void onFailure(Call<GenericListResponse<Transaction>> call, Throwable t) {
        Log.e("HTTP ERROR", t.getMessage(), t);
        Toast.makeText(context, String.format("%s%s", MSG_GENERIC_ERROR, t.getMessage()), Toast.LENGTH_LONG).show();
        hideDialog();
    }
}
