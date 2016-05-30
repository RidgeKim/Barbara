package com.app.dextrous.barbara.callback;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.app.dextrous.barbara.activity.UserDetailsActivity;
import com.app.dextrous.barbara.adapter.UserArrayAdapter;
import com.app.dextrous.barbara.constant.BarbaraConstants;
import com.app.dextrous.barbara.model.User;
import com.app.dextrous.barbara.response.GenericListResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.dextrous.barbara.constant.BarbaraConstants.MSG_GENERIC_ERROR;

public class UserListCallback extends BaseCallback implements Callback<GenericListResponse<User>> {
    private ListView listView;

    public UserListCallback(Context context, ListView listView) {
        super(context);
        this.listView = listView;
    }
    @Override
    public void onResponse(Call<GenericListResponse<User>> call, Response<GenericListResponse<User>> response) {
        GenericListResponse<User> apiResponse = response.body();
        if(apiResponse != null) {
            List<User> userList = apiResponse.getItems();
            if(userList != null && !userList.isEmpty()){
                ListAdapter listAdapter = new UserArrayAdapter(context, userList);
                listView.setAdapter(listAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view,
                                            int position, long id) {
                        final User item = (User) parent.getItemAtPosition(position);
                        Log.d("Item Selected", item.toString());
                        Intent intent = new Intent(context, UserDetailsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(BarbaraConstants.INTENT_PARAM_USER_ITEM_KEY, item);
                        context.startActivity(intent);
                    }

                });
            }
            Log.d("HTTP RESPONSE", apiResponse.toString());
        }
        hideDialog();
    }

    @Override
    public void onFailure(Call<GenericListResponse<User>> call, Throwable t) {
        Log.e("HTTP ERROR", t.getMessage(), t);
        Toast.makeText(context, MSG_GENERIC_ERROR + t.getMessage(), Toast.LENGTH_LONG).show();
        hideDialog();
    }
}
