package com.app.court.retrofit;


import com.app.court.entities.CaseMessagesEntity;
import com.app.court.entities.CommentResponseEnt;
import com.app.court.entities.EntityCms;
import com.app.court.entities.EntitySpinnerListing;
import com.app.court.entities.FindLawyerEntity;
import com.app.court.entities.GoogleGeoCodeResponse;
import com.app.court.entities.GoogleServiceResponse;
import com.app.court.entities.LibraryEntity;
import com.app.court.entities.MainPaymentEntity;
import com.app.court.entities.MessagePushEnt;
import com.app.court.entities.MessageThreadEntity;
import com.app.court.entities.MyCaseEntity;
import com.app.court.entities.NotificationEntity;
import com.app.court.entities.ResponseWrapper;
import com.app.court.entities.SignUpEntity;
import com.app.court.entities.UserProfileEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface WebService {

    @GET("/maps/api/geocode/json")
    Call<GoogleServiceResponse<List<GoogleGeoCodeResponse>>> getLatLongInfo(
            @Query("address") String address,
            @Query("sensor") String sensor
    );

    @FormUrlEncoded
    @POST("UserRegister")
    Call<ResponseWrapper<SignUpEntity>> signup(
            @Field("full_name") String full_name,
            @Field("email") String email,
            @Field("phone_no") String phone_no,
            @Field("password") String password,
            @Field("password_confirmation") String password_confirmation,
            @Field("device_type") String device_type,
            @Field("device_token") String device_token,
            @Field("profile_image") String profile_image
    );

    @GET("getUserProfile")
    Call<ResponseWrapper<UserProfileEntity>> getProfile(
    );

    @Multipart
    @POST("updateUserProfile")
    Call<ResponseWrapper<UserProfileEntity>> updateUserProfile(
            @Part("full_name") RequestBody full_name,
            @Part("email") RequestBody email,
            @Part("phone_no") RequestBody phone_no,
            @Part("location") RequestBody location,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part MultipartBody.Part profile_image
    );

    @FormUrlEncoded
    @POST("login")
    Call<ResponseWrapper<SignUpEntity>> login(
            @Field("email") String email,
            @Field("password") String password,
            @Field("device_token") String device_token,
            @Field("device_type") String device_type
    );

    @FormUrlEncoded
    @POST("forgotPassword")
    Call<ResponseWrapper<SignUpEntity>> forgotPassword(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("signupCodeVerification")
    Call<ResponseWrapper<SignUpEntity>> signupCodeVerification(
            @Field("user_id") int user_id,
            @Field("verification_code") String verification_code
    );

    @FormUrlEncoded
    @POST("codeVerification")
    Call<ResponseWrapper<SignUpEntity>> codeVerification(
            @Field("user_id") String user_id,
            @Field("verification_code") String verification_code
    );

    @FormUrlEncoded
    @POST("resetPassword")
    Call<ResponseWrapper> resetPassword(
            @Field("user_id") int user_id,
            @Field("password") String password,
            @Field("password_confirmation") String password_confirmation
    );

    @FormUrlEncoded
    @POST("resendCode")
    Call<ResponseWrapper<SignUpEntity>> resendCode(
            @Field("user_id") int user_id
    );

    @FormUrlEncoded
    @POST("pushOnOff")
    Call<ResponseWrapper<SignUpEntity>> pushOnOff(
            @Field("is_notify") int is_notify
    );

    @FormUrlEncoded
    @POST("languageChange")
    Call<ResponseWrapper<SignUpEntity>> languageChange(
            @Field("current_lang") String current_lang
    );

    @FormUrlEncoded
    @POST("updateDeviceToken")
    Call<ResponseWrapper<SignUpEntity>> updateDeviceToken(
            @Field("device_token") String device_token,
            @Field("device_type") String device_type
    );

    @FormUrlEncoded
    @POST("changePassword")
    Call<ResponseWrapper> changePassword(
            @Field("old_password") String old_password,
            @Field("password") String password,
            @Field("password_confirmation") String password_confirmation
    );

    @GET("getNotification")
    Call<ResponseWrapper<ArrayList<NotificationEntity>>> getNotifications(
    );

    @GET("getCms")
    Call<ResponseWrapper<EntityCms>> getCms(
            @Query("type") String type
    );

    @FormUrlEncoded
    @POST("logout")
    Call<ResponseWrapper> logout(
            @Field("device_token") String device_token
    );

    @GET("getAffilation")
    Call<ResponseWrapper<ArrayList<EntitySpinnerListing>>> getAffilation(
    );

    @GET("getLanguage")
    Call<ResponseWrapper<ArrayList<EntitySpinnerListing>>> getLanguage(
    );

    @GET("getSpecialization")
    Call<ResponseWrapper<ArrayList<EntitySpinnerListing>>> getSpecialization(
    );

    @GET("getExperience")
    Call<ResponseWrapper<ArrayList<EntitySpinnerListing>>> getExperience(
    );

    @GET("getLawyerRecord")
    Call<ResponseWrapper<ArrayList<FindLawyerEntity>>> getLawyers(
            @Query("latitude") String latitude,
            @Query("longitude") String longitude,
            @Query("rate") String rate,
            @Query("specialization_ids") String specialization_ids,
            @Query("language_ids") String language_ids,
            @Query("affiliation_id") String affiliation_id,
            @Query("name") String name
    );



/*    @PartMap Map<String, RequestBody> documents,
    @PartMap Map<String, RequestBody> thumbnail,
    @PartMap Map<String, RequestBody> type*/

    @GET("getUserCase")
    Call<ResponseWrapper<ArrayList<MyCaseEntity>>> getMyCase(
    );

    @GET("getUserCase")
    Call<ResponseWrapper<ArrayList<MyCaseEntity>>> getMyCaseFilter(
            @Query("filter") String filter
    );


    @GET("getCase")
    Call<ResponseWrapper<MyCaseEntity>> updateComments(
    );

    @GET("getCasePayment")
    Call<ResponseWrapper<MainPaymentEntity>> casePayment(
            @Query("case_id") int case_id,
            @Query("sort") String sort
    );

    @FormUrlEncoded
    @POST("paymentPay")
    Call<ResponseWrapper> payDuePayment(
            @Field("payment_id") int payment_id
    );

    @FormUrlEncoded
    @POST("CompletedCase")
    Call<ResponseWrapper> completeCase(
            @Field("case_id") int case_id
    );


    @FormUrlEncoded
    @POST("createComment")
    Call<ResponseWrapper<CommentResponseEnt>> createComments(
            @Field("case_id") int case_id,
            @Field("message") String message
    );

    @GET("getThread")
    Call<ResponseWrapper<ArrayList<MessageThreadEntity>>> getMessageThread(
    );

    @GET("getMessage")
    Call<ResponseWrapper<ArrayList<CaseMessagesEntity>>> getMessage(
            @Query("thread_id") String thread_id
    );

    @GET("getCaseMessage")
    Call<ResponseWrapper<ArrayList<CaseMessagesEntity>>> getCaseMessage(
            @Query("case_id") int case_id
    );

    @Multipart
    @POST("addMessage")
    Call<ResponseWrapper<ArrayList<CaseMessagesEntity>>> addMessage(
            @Part("lawyer_id") RequestBody case_id,
            @Part("subject") RequestBody receiver_id,
            @Part("detail") RequestBody message,
            @Part ArrayList<MultipartBody.Part> thumbnail,
            @Part("documents[0][type]") RequestBody type,
            @Part ArrayList<MultipartBody.Part> file
    );

    @GET("getUserAllFile")
    Call<ResponseWrapper<LibraryEntity>> getLibrary(
    );

    @FormUrlEncoded
    @POST("addMessage")
    Call<ResponseWrapper<ArrayList<CaseMessagesEntity>>> sendMsg(
            @Field("case_id") String case_id,
            @Field("receiver_id") String receiver_id,
            @Field("message") String message

    );

    @Multipart
    @POST("addAndroidMessage")
    Call<ResponseWrapper<ArrayList<CaseMessagesEntity>>> sendMsg(
            @Part("case_id") RequestBody case_id,
            @Part("receiver_id") RequestBody receiver_id,
            @Part("message") RequestBody message,
            @Part ArrayList<MultipartBody.Part> file,
            @Part ArrayList<MultipartBody.Part> thumb_nail,
            @Part ArrayList<MultipartBody.Part> type
    );

    @Multipart
    @POST("CaseRegister")
    Call<ResponseWrapper> submitCase(
            @Part("lawyer_id") RequestBody lawyer_id,
            @Part("subject") RequestBody subject,
            @Part("detail") RequestBody detail,
            @PartMap Map<String, ArrayList<MultipartBody.Part>> file

    );

    @Multipart
    @POST("androidCaseRegister")
    Call<ResponseWrapper> submitCase(
            @Part("lawyer_id") RequestBody lawyer_id,
            @Part("subject") RequestBody subject,
            @Part("detail") RequestBody detail,
            @Part ArrayList<MultipartBody.Part> file,
            @Part ArrayList<MultipartBody.Part> thumb_nail,
            @Part ArrayList<MultipartBody.Part> type

    );


    @GET("getMessageThread")
    Call<ResponseWrapper<MessagePushEnt>> getMsgPushData(
            @Query("thread_id") String thread_id
    );

    @GET("getCase")
    Call<ResponseWrapper<MyCaseEntity>> getCaseDetail(
            @Query("case_id") String case_id
    );

    @FormUrlEncoded
    @POST("LawyerRate")
    Call<ResponseWrapper> lawyerRate(
            @Field("lawyer_id") String lawyer_id,
            @Field("case_id") String case_id,
            @Field("rate") int rate
    );

    @FormUrlEncoded
    @POST("CaseWithdraw")
    Call<ResponseWrapper> caseWithdraw(
            @Field("case_id") int case_id

    );


}