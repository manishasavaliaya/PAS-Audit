package com.passurvey.webservices;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.passurvey.requestresponse.DefaultansRequestResponse;
import com.passurvey.requestresponse.ErrorResponse;
import com.passurvey.requestresponse.GetCommentsRequestResponse;
import com.passurvey.requestresponse.ManagementAuditorRequestResponse;
import com.passurvey.requestresponse.PASLoginRequestResponse;
import com.passurvey.requestresponse.SurveydataRequestResponse;
import com.passurvey.utility.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public class WebServiceCaller {
    private static WebServiceApiInterface webApiInterface;
    public static Retrofit client;
    public static WebServiceApiInterface getClient() {
        if (webApiInterface == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okclient = new OkHttpClient.Builder().connectTimeout(15, TimeUnit.MINUTES).writeTimeout(15,TimeUnit.MINUTES).readTimeout(15,TimeUnit.MINUTES).addInterceptor(logging).build();

            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

             client = new Retrofit.Builder()
                    .baseUrl(Utils.BASE_URL)
                    .client(okclient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            webApiInterface = client.create(WebServiceApiInterface.class);
        }
        return webApiInterface;
    }

    public interface WebServiceApiInterface {
        @FormUrlEncoded
        @POST("login")
        Call<PASLoginRequestResponse> getUserLogin(@Field("username") String email,
                                                   @Field("password") String password);

        @FormUrlEncoded
        @POST("profile")
        Call<ErrorResponse> ProfileUpdate(@Field("id") String id,
                                          @Field("managing_agent") String managing_agent,
                                          @Field("fund") float email,
                                          @Field("facility_manager") String facility_manager,
                                          @Field("managment_surveyor") String managment_surveyor);

        @GET("surveydata")
        Call<SurveydataRequestResponse> getSurveyData();

        @GET("defaultans")
        Call<DefaultansRequestResponse> getDefaultans();

//        @GET("defaultans")
//        Call<Response> getDefaultans();
//add by mena
        @GET("managingauditors")
        Call<ManagementAuditorRequestResponse> getmangAuditor();

        @POST("answers")
        Call<ErrorResponse> UserAnswer(@Body RequestBody parameters);

        @Multipart
        @POST("insertphoto")
        Call<ErrorResponse> insertphoto(@Part("profileid") RequestBody param,
                                        @PartMap
                                        Map<String, RequestBody> survey_comment,
                                        @PartMap
                                        Map<String, RequestBody> survey_image);

        @Multipart
        @POST("insertphoto")
        Call<ErrorResponse> insertphoto1(@Part("profileid") RequestBody param,
                                        @PartMap
                                        Map<String, RequestBody> survey_comment,
                                        @PartMap
                                         HashMap<String, MultipartBody.Part> survey_image);

        @Multipart
        @POST("insertphoto")
        Call<ErrorResponse> insertphotoNew(@Part("profileid") RequestBody param,
                                         @PartMap
                                                 Map<String, RequestBody> survey_comment,
                                         @PartMap
                                                 HashMap<String, MultipartBody.Part> survey_image);

        /*Map<String, RequestBody> survey_image);*/

      /*  Call<ErrorResponse> insertphoto(@Part("profileid") String profileid,
                                        @PartMap
                                        Map<String, String> survey_comment,
                                        @PartMap
                                        Map<String, RequestBody> survey_image);*/
       /* @FormUrlEncoded
        @POST("user/login")
        Call<LoginRequestResponse> getUserLogin1(@Field("email") String email,
                                                @Field("password") String password,
                                                @Field("device_type") String device_type,
                                                @Field("device_token") String device_token,
                                                @Field("user_latitude") String user_latitude,
                                                @Field("user_longitude") String user_longitude,
                                                @Field("user_language") String user_language);*/
        /*amitk 9-8-2017 getcomments for contracterperfomance*/

        @GET("getConsultantsCommentsComments")
        Call<GetCommentsRequestResponse> getConsultantsCommentsComments();

        @GET("getContractorsPerformanceComments")
        Call<GetCommentsRequestResponse> getContractorsPerformanceComments();

        @GET("getConsultantsCommentsClient")
        Call<GetCommentsRequestResponse> getClientConsultantsComments();
    }
}