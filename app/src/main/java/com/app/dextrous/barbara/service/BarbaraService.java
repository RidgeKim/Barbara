package com.app.dextrous.barbara.service;

import com.app.dextrous.barbara.model.CommandResponse;
import com.app.dextrous.barbara.model.CreditTransaction;
import com.app.dextrous.barbara.model.InvestmentPlan;
import com.app.dextrous.barbara.model.Transaction;
import com.app.dextrous.barbara.model.User;
import com.app.dextrous.barbara.model.UserPreference;
import com.app.dextrous.barbara.response.GenericBeanResponse;
import com.app.dextrous.barbara.response.GenericListResponse;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface BarbaraService {

    @GET("users/all")
    Call<GenericListResponse<User>> getAllUsers();

    @GET("transactions/all")
    Call<GenericListResponse<Transaction>> getAllUserTransactions(@Query("userId") String userId);

    @GET("transactions/credit/all")
    Call<GenericListResponse<CreditTransaction>> getAllUserCreditTransactions(@Query("userId") String userId);

    @GET("users/preferences")
    Call<GenericBeanResponse<UserPreference>> getUserPreference(@Query("userId") String userId);

    @POST("users/save-preferences")
    @FormUrlEncoded
    Call<GenericBeanResponse<UserPreference>> saveUserPreference(@FieldMap Map<String, Object> form);

    @GET("investment/plans")
    Call<GenericListResponse<InvestmentPlan>> getInvetmentPlans();

    @POST("login")
    @FormUrlEncoded
    Call<GenericBeanResponse<User>> loginUser(@FieldMap Map<String, Object> form);

    @POST("command/process")
    @FormUrlEncoded
    Call<GenericBeanResponse<CommandResponse>> processCommand(@FieldMap Map<String, Object> form);

    @POST("transactions/execute-verified-transfer")
    @FormUrlEncoded
    Call<GenericBeanResponse<CommandResponse>> executeAuthenticatedTransaction(@FieldMap Map<String, Object> form);

    @POST("transactions/authenticate-transfer")
    @Multipart
    Call<GenericBeanResponse<CommandResponse>> authenticateTransfer(@PartMap Map<String, RequestBody> params);

    @POST("users/verify-voice")
    @Multipart
    Call<GenericBeanResponse<User>> authenticateUser(@PartMap Map<String, RequestBody> params);
}
