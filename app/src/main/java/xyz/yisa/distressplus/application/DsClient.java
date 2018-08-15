package xyz.yisa.distressplus.application;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import xyz.yisa.distressplus.models.Alert;
import xyz.yisa.distressplus.models.BasicResponse;
import xyz.yisa.distressplus.models.User;

public interface DsClient {
    @POST("user/register")
    Call<BasicResponse> register(@Body User user);

    @POST("user/login")
    Call<BasicResponse> login(@Body User user);

    @GET("user/trust")
    Call<ArrayList<User>> trustedList(@Header("Authorization") String token);

    @POST("user/trust")
    Call<BasicResponse> trust(@Header("Authorization") String token, @Body User user);

    @POST("alert")
    Call<BasicResponse> sendAlert(@Header("Authorization") String token, @Body Alert alert);

    @GET("alert")
    Call<ArrayList<Alert>> fetchAlerts(@Header("Authorization") String token);
}
