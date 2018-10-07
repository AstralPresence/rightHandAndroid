package in.presence.astral.righthand.rest;



import org.json.JSONObject;

import in.presence.astral.righthand.model.AccessGroup;
import in.presence.astral.righthand.model.ControlsResponse;
import in.presence.astral.righthand.model.User;
import in.presence.astral.righthand.model.UsersResponse;
import in.presence.astral.righthand.model.Control;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by DevOpsTrends on 5/26/2017.
 */

public interface ApiInterface {

    // User Management

    @GET("users/get")
    Call<UsersResponse> getUsers(@Header("Authorization") String authorization);

    @POST("users/delete")
    Call<JSONObject> deleteUser(@Header("Authorization") String authorization, @Body User User);

    @POST("users/create")
    Call<JSONObject> createUser(@Header("Authorization") String authorization, @Body User User);

    @POST("users/edit")
    Call<JSONObject> editUser(@Header("Authorization") String authorization, @Body User User);

    // Access Management

    @GET("accessGroup/get")
    Call<UsersResponse> getAccessGroups(@Header("Authorization") String authorization);

    @POST("accessGroup/create")
    Call<JSONObject> createAccessGroup(@Header("Authorization") String authorization, @Body AccessGroup accessGroup);

    @POST("accessGroup/edit")
    Call<JSONObject> editAccessGroup(@Header("Authorization") String authorization, @Body AccessGroup accessGroup);

    // Controls

    @GET("controls/get")
    Call<ControlsResponse> getControls(@Header("Authorization") String authorization);

    @POST("controls/edit")
    Call<JSONObject> editControls(@Header("Authorization") String authorization, @Body Control control);

}