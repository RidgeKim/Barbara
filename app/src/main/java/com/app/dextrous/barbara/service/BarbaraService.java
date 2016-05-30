package com.app.dextrous.barbara.service;

import com.app.dextrous.barbara.model.CreditTransaction;
import com.app.dextrous.barbara.model.Transaction;
import com.app.dextrous.barbara.model.User;
import com.app.dextrous.barbara.response.GenericBeanResponse;
import com.app.dextrous.barbara.response.GenericListResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BarbaraService {

    @GET("users/all")
    Call<GenericListResponse<User>> getAllUsers();

    @GET("transactions/all")
    Call<GenericListResponse<Transaction>> getAllUserTransactions(@Query("userId") String userId);

    @GET("transactions/credit/all")
    Call<GenericListResponse<CreditTransaction>> getAllUserCreditTransactions(@Query("userId") String userId);

    @POST("login")
    @FormUrlEncoded
    Call<GenericBeanResponse<User>> loginUser(@FieldMap Map<String, Object> form);
}
