package com.app.dextrous.barbara.callback;

import android.content.Context;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.app.dextrous.barbara.adapter.CreditTransactionArrayAdapter;
import com.app.dextrous.barbara.adapter.InvestmentArrayAdapter;
import com.app.dextrous.barbara.model.CreditTransaction;
import com.app.dextrous.barbara.model.InvestmentPlan;
import com.app.dextrous.barbara.response.GenericListResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.dextrous.barbara.constant.BarbaraConstants.MSG_GENERIC_ERROR;

public class InvestmentPlanListCallback extends BaseCallback implements Callback<GenericListResponse<InvestmentPlan>> {
    private ListView listView;

    public InvestmentPlanListCallback(Context context, ListView listView) {
        super(context);
        this.listView = listView;
    }
    @Override
    public void onResponse(Call<GenericListResponse<InvestmentPlan>> call, Response<GenericListResponse<InvestmentPlan>> response) {
        GenericListResponse<InvestmentPlan> apiResponse = response.body();
        System.out.println(response.body());
        if(apiResponse != null) {
            List<InvestmentPlan> investmentPlanList = apiResponse.getItems();
            if(investmentPlanList != null && !investmentPlanList.isEmpty()){
                ListAdapter listAdapter = new InvestmentArrayAdapter(context, investmentPlanList);
                listView.setAdapter(listAdapter);
            }
            Log.d("HTTP RESPONSE", apiResponse.toString());
        }
        hideDialog();
    }

    @Override
    public void onFailure(Call<GenericListResponse<InvestmentPlan>> call, Throwable t) {
        Log.e("HTTP ERROR", t.getMessage(), t);
        Toast.makeText(context, String.format("%s%s", MSG_GENERIC_ERROR, t.getMessage()), Toast.LENGTH_LONG).show();
        hideDialog();
    }
}
