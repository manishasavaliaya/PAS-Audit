package com.passurvey.activities;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.passurvey.R;
import com.passurvey.database.DBUtility;
import com.passurvey.model.PhotoModel;
import com.passurvey.requestresponse.DefaultansRequestResponse;
import com.passurvey.requestresponse.ErrorResponse;
import com.passurvey.requestresponse.GetCommentsRequestResponse;
import com.passurvey.requestresponse.ManagementAuditorRequestResponse;
import com.passurvey.requestresponse.SurveydataRequestResponse;
import com.passurvey.utility.Utils;
import com.passurvey.webservices.WebServiceCaller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import au.com.bytecode.opencsv.CSVWriter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class SplashActivity extends BaseActivity {
    private static int SPLASH_TIME_OUT = 3000;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    public static int IMAGE_NOTIFICATION_ID=247;
    Handler handler;
    Runnable r;
    View view;
    // int synccount=0,synccount1=0;
    Cursor cursor,cursordata;
    String Profileid;
    String SurveyId,GroupId,UserId,ProfileId;
    private ArrayList<PhotoModel> phtolist = new ArrayList<>();
    Map<String, RequestBody> map = new HashMap<>();
    Map<String, RequestBody> mapcomment = new HashMap<>();
    int synccount=0,synccount1=0,totalSynced=0,totalSyncedImage=0,firstime=0,totalImageCount=0;
    String oldprofile="";
    int groupbyphotototal=0,groupbyphotocount=0;

    @Override
    protected void onPause() {
        super.onPause();
       /* if (handler!=null)
        handler.removeCallbacks(r);*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler!=null)
            handler.removeCallbacks(r);
    }

    @Override
    public void setLayoutView() {
        view = LayoutInflater.from(this).inflate(R.layout.activity_splash, lnrContainer);
        hideTopBar();
        //Utils.hideProgress();
        Utils.setSharedPreString(SplashActivity.this, Utils.PREFS_DRAWER,"");
        /*amitk 7-2-17*/
        //FirebaseCrash.report(new Exception("My first Android non-fatal error"));
        dbHelper.DeleteSyncUserAnswerMonth(Utils.getmonthbeforedate());
        dbHelper.DeleteSyncUserPhotosMonth(Utils.getmonthbeforedate());

        if (Utils.isNetworkAvailable(SplashActivity.this)) {
            getSurveyData();
        } else {
            /*Updating survey data to server if not sync with server*/
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //  checkAndRequestPermissions();
                if (checkAndRequestPermissions()) {
                    Log.d("permission status", "permission granted");
                    if (Utils.getSharedPreString(SplashActivity.this, "UserId") != null && !Utils.getSharedPreString(SplashActivity.this, "UserId").equalsIgnoreCase("")) {
                        handler = new Handler();
                        handler.postDelayed(r = new Runnable() {
                            @Override
                            public void run() {
                                cursor = dbHelper.checkIncompleteSurvey(Utils.getSharedPreString(SplashActivity.this, Utils.PREFS_USERID), Utils.getSharedPreString(SplashActivity.this, Utils.PREFS_SURVEYID));
                                if (cursor != null && cursor.getCount() > 0) {
                                    startActivity(new Intent(SplashActivity.this, InCompleteSurveyActivity.class));
                                    finish();
                                } else {
                                    startActivity(new Intent(SplashActivity.this, SelectSurveyActivity.class));
                                    finish();
                                }
                            }
                        }, SPLASH_TIME_OUT);
                        /*exporting data to csv 21-11-2016*/
                        new ExportDatabaseCSVTask().execute();
                    } else {
                        getSurveyData();
                    }
                } else {
                    Log.d("permission status", "permission not granted");
                }
            } else {
                if (Utils.getSharedPreString(SplashActivity.this, "UserId") != null && !Utils.getSharedPreString(SplashActivity.this, "UserId").equalsIgnoreCase("")) {
                    handler = new Handler();
                    handler.postDelayed(r = new Runnable() {
                        @Override
                        public void run() {
                            cursor = dbHelper.checkIncompleteSurvey(Utils.getSharedPreString(SplashActivity.this, Utils.PREFS_USERID), Utils.getSharedPreString(SplashActivity.this, Utils.PREFS_SURVEYID));
                            if (cursor != null && cursor.getCount() > 0) {
                                startActivity(new Intent(SplashActivity.this, InCompleteSurveyActivity.class));
                                finish();
                            } else {
                                startActivity(new Intent(SplashActivity.this, SelectSurveyActivity.class));
                                finish();
                            }
                        }
                    }, SPLASH_TIME_OUT);
                    /*exporting data to csv 21-11-2016*/
                    new ExportDatabaseCSVTask().execute();
                } else {
                    getSurveyData();
                }
            }
        }
        if (!Utils.isNetworkAvailable(SplashActivity.this)) {
            //Utils.showToast(SplashActivity.this, getResources().getString(R.string.no_internet_connection));
        }  else {
            // senddatatoserver();
            // mena change on 14-11-2016 for server error issue
            ServerOnOff();
        }
        // Utils.showToast(SplashActivity.this,Utils.getmonthbeforedate()+"");
        Log.d("month before date",Utils.getmonthbeforedate()+"");
    }

    @Override
    public void initialization() {
    }

    //mena change
    private  void setdataforIncompleteSurvey(){
        Utils.hideProgress();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            //  checkAndRequestPermissions();
            if (checkAndRequestPermissions()) {
                Log.d("permission status","permission granted");
                if (Utils.getSharedPreString(SplashActivity.this,"UserId")!=null&&!Utils.getSharedPreString(SplashActivity.this,"UserId").equalsIgnoreCase("")) {
                    handler = new Handler();
                    handler.postDelayed(r = new Runnable() {
                        @Override
                        public void run() {
                            cursor=  dbHelper.checkIncompleteSurvey(Utils.getSharedPreString(SplashActivity.this,Utils.PREFS_USERID),Utils.getSharedPreString(SplashActivity.this,Utils.PREFS_SURVEYID));
                            if (cursor!=null&&cursor.getCount()>0) {
                                startActivity(new Intent(SplashActivity.this,InCompleteSurveyActivity.class));
                                finish();
                            } else {
                                startActivity(new Intent(SplashActivity.this,SelectSurveyActivity.class));
                                finish();
                            }
                        }
                    }, SPLASH_TIME_OUT);
                    /*exporting data to csv 21-11-2016*/
                    new ExportDatabaseCSVTask().execute();
                } else {
                    // getSurveyData();
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    finish();
                }
            }
            else
            {
                Log.d("permission status","permission not granted");

            }
        }
        else {
            if (Utils.getSharedPreString(SplashActivity.this,"UserId")!=null&&!Utils.getSharedPreString(SplashActivity.this,"UserId").equalsIgnoreCase(""))
            {
                handler = new Handler();
                handler.postDelayed(r = new Runnable() {
                    @Override
                    public void run() {

                        cursor=  dbHelper.checkIncompleteSurvey(Utils.getSharedPreString(SplashActivity.this,Utils.PREFS_USERID),Utils.getSharedPreString(SplashActivity.this,Utils.PREFS_SURVEYID));
                        if (cursor!=null&&cursor.getCount()>0)
                        {
                            startActivity(new Intent(SplashActivity.this,InCompleteSurveyActivity.class));
                            finish();
                        }
                        else
                        {
                            startActivity(new Intent(SplashActivity.this,SelectSurveyActivity.class));
                            finish();
                        }


                    }
                }, SPLASH_TIME_OUT);
                /*exporting data to csv 21-11-2016*/
                new ExportDatabaseCSVTask().execute();
            }
            else
            {
                //getSurveyData();
                startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                finish();
            }
        }
    }

    @Override
    public void setupData() {

    }

    @Override
    public void setListeners() {

    }

    /*Survey Data*/
    private void getSurveyData() {
        try {
            if (!Utils.isNetworkAvailable(SplashActivity.this)) {
                Utils.showToast(SplashActivity.this, getResources().getString(R.string.no_internet_connection));
            }  else {
                Utils.showProgress(SplashActivity.this);
                WebServiceCaller.WebServiceApiInterface service = WebServiceCaller.getClient();

                Call<SurveydataRequestResponse> call = service.getSurveyData();
                call.enqueue(new Callback<SurveydataRequestResponse>() {
                    @Override
                    public void onResponse(Call<SurveydataRequestResponse> call, Response<SurveydataRequestResponse> response) {
                        if (response.isSuccessful()) {
                            Log.e("RESPONSE::","RESPONSE:" +"getSurveyData");

                            SurveydataRequestResponse result = response.body();
                            if (result.getSuccess()) {
                                List<SurveydataRequestResponse.Datum> data = response.body().getData();
                                if (data!=null&&data.size()>0) {
                                    dbHelper.truncateQuestion();
                                    for (int i = 0; i <data.size() ; i++) {
                                        try {
                                            dbHelper.insertQuestion(data.get(i));
                                        }
                                        catch (Exception ex)
                                        {
                                            dbHelper.UpdateQuestion(data.get(i));
                                        }
                                    }
                                }

                                getDefaultans();
                              //  getManagementAuditor();
//                                getConsultantsComments();
//                                getContractorsPerfomanceComments();
                                // Manisha 27/02/20
//                                getClientConsultantsComments();
//                                setdataforIncompleteSurvey();

                               /* handler = new Handler();
                                handler.postDelayed(r = new Runnable() {
                                    @Override
                                    public void run() {
                                        Utils.hideProgress();
                                        startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                                        finish();

                                    }
                                }, SPLASH_TIME_OUT);*/
                               /* Utils.showToast(LoginActivity.this,response.body().getData().getDisplayName().toString()+"");
                                Utils.setSharedPreString(LoginActivity.this,"UserId",response.body().getData().getId().toString());
                                Utils.setSharedPreString(LoginActivity.this,"display_name",response.body().getData().getDisplayName().toString());
                                Utils.setSharedPreString(LoginActivity.this,"profile_completed",response.body().getData().getProfileCompleted().toString());
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));*/
                            } else {
                                //Toast.makeText(LoginActivity.this, result.getmessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Utils.hideProgress();
                            Converter<ResponseBody, ErrorResponse> converter = WebServiceCaller.client.responseBodyConverter(ErrorResponse.class, new Annotation[0]);

                            try {
                                ErrorResponse errors = converter.convert(response.errorBody());
                                Toast.makeText(SplashActivity.this, errors.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(SplashActivity.this, R.string.server_error, Toast.LENGTH_LONG).show();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<SurveydataRequestResponse> call, Throwable t) {
                        Log.v("onFailure", "onFailure");
                        Utils.showToast(SplashActivity.this, getResources().getString(R.string.server_error));
                        Utils.hideProgress();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ServerOnOff() {
        try {
            if (!Utils.isNetworkAvailable(SplashActivity.this)) {
                Utils.showToast(SplashActivity.this, getResources().getString(R.string.no_internet_connection));
            } else {
                //  Utils.showProgress(SplashActivity.this);
                WebServiceCaller.WebServiceApiInterface service = WebServiceCaller.getClient();

                Call<SurveydataRequestResponse> call = service.getSurveyData();
                call.enqueue(new Callback<SurveydataRequestResponse>() {
                    @Override
                    public void onResponse(Call<SurveydataRequestResponse> call, Response<SurveydataRequestResponse> response) {
                        if (response.isSuccessful()) {
                            //senddatatoserver();
                            senddatatoservernew();
                            if (!Utils.isImageSyncing) {
                                sendPendingImageToServer();
                            }
                        }
                    }

                    public void onFailure(Call<SurveydataRequestResponse> call, Throwable t) {
                        Log.v("onFailure", "onFailure");
                        Utils.showToast(SplashActivity.this, getResources().getString(R.string.server_error));
                        Utils.hideProgress();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*Default ans*/
    private void getDefaultans() {
        try {
            if (!Utils.isNetworkAvailable(SplashActivity.this)) {
                Utils.showToast(SplashActivity.this, getResources().getString(R.string.no_internet_connection));
            }  else {
               // Utils.showProgress(SplashActivity.this);
                WebServiceCaller.WebServiceApiInterface service = WebServiceCaller.getClient();
                Call<DefaultansRequestResponse> call = service.getDefaultans();
                call.enqueue(new Callback<DefaultansRequestResponse>() {
                    @Override
                    public void onResponse(Call<DefaultansRequestResponse> call, Response<DefaultansRequestResponse> response) {
                        if (response.isSuccessful()) {
                            Log.e("RESPONSE::","RESPONSE:" +"getDefaultans");
                            DefaultansRequestResponse result = response.body();
                            if (result.getSuccess()) {
                                List<DefaultansRequestResponse.Datum> data =response.body().getData();
                                if (data!=null&&data.size()>0) {
                                    dbHelper.truncateDefaultAns();
                                    for (int i = 0; i <data.size() ; i++) {
                                        try {
                                            dbHelper.insertDefaultAnswer(data.get(i));
                                        } catch (Exception ex) {
                                            dbHelper.UpdateDefaultAnswer(data.get(i));
                                        }
                                    }
                                    // Utils.hideProgress();
                                }
                                getManagementAuditor();
                               /* Utils.showToast(LoginActivity.this,response.body().getData().getDisplayName().toString()+"");
                                Utils.setSharedPreString(LoginActivity.this,"UserId",response.body().getData().getId().toString());
                                Utils.setSharedPreString(LoginActivity.this,"display_name",response.body().getData().getDisplayName().toString());
                                Utils.setSharedPreString(LoginActivity.this,"profile_completed",response.body().getData().getProfileCompleted().toString());
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));*/
                            } else {
                                //Toast.makeText(LoginActivity.this, result.getmessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Utils.hideProgress();
                            Converter<ResponseBody, ErrorResponse> converter = WebServiceCaller.client.responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                            try {
                                ErrorResponse errors = converter.convert(response.errorBody());
                                Toast.makeText(SplashActivity.this, errors.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(SplashActivity.this, R.string.server_error, Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DefaultansRequestResponse> call, Throwable t) {
                        Log.v("onFailure", "onFailure");
                        Utils.showToast(SplashActivity.this, getResources().getString(R.string.server_error));
                        Utils.hideProgress();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*Default ans*/
    private void getManagementAuditor() {
        try {
            if (!Utils.isNetworkAvailable(SplashActivity.this)) {
                Utils.showToast(SplashActivity.this, getResources().getString(R.string.no_internet_connection));
            }  else

            {
                //  Utils.showProgress(SplashActivity.this);
                WebServiceCaller.WebServiceApiInterface service = WebServiceCaller.getClient();

                Call<ManagementAuditorRequestResponse> call = service.getmangAuditor();
                call.enqueue(new Callback<ManagementAuditorRequestResponse>() {

                    @Override
                    public void onResponse(Call<ManagementAuditorRequestResponse> call, Response<ManagementAuditorRequestResponse> response) {
                        if (response.isSuccessful())
                        {
                            Log.e("RESPONSE::","RESPONSE:" +"getManagementAuditor");
                            // Toast.makeText(SplashActivity.this, "sucess", Toast.LENGTH_SHORT).show();

                            ManagementAuditorRequestResponse result = response.body();
                            if (result.getSuccess()) {

                                List<ManagementAuditorRequestResponse.Datum> data =response.body().getData();

                                if (data!=null&&data.size()>0)
                                {
                                    dbHelper.truncateManagementAuditor();
                                    for (int i = 0; i <data.size() ; i++) {
                                        try {
                                            dbHelper.insertManagementAuditor(data.get(i));
                                        }
                                        catch (Exception ex)
                                        {
                                            dbHelper.UpdateManagementAuditor(data.get(i));
                                        }
                                    }
                                    // Utils.hideProgress();
                                }
                                getConsultantsComments();
                               /* Utils.showToast(LoginActivity.this,response.body().getData().getDisplayName().toString()+"");
                                Utils.setSharedPreString(LoginActivity.this,"UserId",response.body().getData().getId().toString());
                                Utils.setSharedPreString(LoginActivity.this,"display_name",response.body().getData().getDisplayName().toString());
                                Utils.setSharedPreString(LoginActivity.this,"profile_completed",response.body().getData().getProfileCompleted().toString());
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));*/
                            } else {
                                //Toast.makeText(LoginActivity.this, result.getmessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Utils.hideProgress();
                            Converter<ResponseBody, ErrorResponse> converter = WebServiceCaller.client.responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                            try {
                                ErrorResponse errors = converter.convert(response.errorBody());
                                Toast.makeText(SplashActivity.this, errors.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(SplashActivity.this, R.string.server_error, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<ManagementAuditorRequestResponse> call, Throwable t) {
                        Log.v("onFailure", "onFailure");
                        Utils.showToast(SplashActivity.this, getResources().getString(R.string.server_error));
                        Utils.hideProgress();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* 9-8-17*/
    private void getConsultantsComments() {
        try {
            if (!Utils.isNetworkAvailable(SplashActivity.this)) {
                Utils.showToast(SplashActivity.this, getResources().getString(R.string.no_internet_connection));
            }  else {
                // Utils.showProgress(SplashActivity.this);
                WebServiceCaller.WebServiceApiInterface service = WebServiceCaller.getClient();
                Call<GetCommentsRequestResponse> call = service.getConsultantsCommentsComments();
                call.enqueue(new Callback<GetCommentsRequestResponse>() {
                    @Override
                    public void onResponse(Call<GetCommentsRequestResponse> call, Response<GetCommentsRequestResponse> response) {
                        if (response.isSuccessful()) {
                            // Toast.makeText(SplashActivity.this, "sucess", Toast.LENGTH_SHORT).show();
                            Log.e("RESPONSE::","RESPONSE:" +"getConsultantsComments");
                            GetCommentsRequestResponse result = response.body();
                            if (result.getSuccess()) {
                                List<GetCommentsRequestResponse.Datum> data =response.body().getData();
                                if (data!=null&&data.size()>0) {
                                    dbHelper.truncateComments();
                                    for (int i = 0; i <data.size() ; i++) {
                                        try {
                                            dbHelper.insertComments(data.get(i));
                                        }
                                        catch (Exception ex)
                                        {
                                            dbHelper.UpdateComments(data.get(i));
                                        }
                                    }
                                    // Utils.hideProgress();
                                }
                                getContractorsPerfomanceComments();
                               /* Utils.showToast(LoginActivity.this,response.body().getData().getDisplayName().toString()+"");
                                Utils.setSharedPreString(LoginActivity.this,"UserId",response.body().getData().getId().toString());
                                Utils.setSharedPreString(LoginActivity.this,"display_name",response.body().getData().getDisplayName().toString());
                                Utils.setSharedPreString(LoginActivity.this,"profile_completed",response.body().getData().getProfileCompleted().toString());
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));*/
                            } else {
                                //Toast.makeText(LoginActivity.this, result.getmessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Utils.hideProgress();
                            Converter<ResponseBody, ErrorResponse> converter = WebServiceCaller.client.responseBodyConverter(ErrorResponse.class, new Annotation[0]);

                            try {
                                ErrorResponse errors = converter.convert(response.errorBody());
                                Toast.makeText(SplashActivity.this, errors.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(SplashActivity.this, R.string.server_error, Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GetCommentsRequestResponse> call, Throwable t) {
                        Log.v("onFailure", "onFailure");
                        /// Utils.showToast(SplashActivity.this, getResources().getString(R.string.server_error));
                        Utils.hideProgress();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getContractorsPerfomanceComments() {
        try {
            if (!Utils.isNetworkAvailable(SplashActivity.this)) {
                Utils.showToast(SplashActivity.this, getResources().getString(R.string.no_internet_connection));
            }  else {
                //  Utils.showProgress(SplashActivity.this);
                WebServiceCaller.WebServiceApiInterface service = WebServiceCaller.getClient();
                Call<GetCommentsRequestResponse> call = service.getContractorsPerformanceComments();
                call.enqueue(new Callback<GetCommentsRequestResponse>() {
                    @Override
                    public void onResponse(Call<GetCommentsRequestResponse> call, Response<GetCommentsRequestResponse> response) {
                        if (response.isSuccessful()) {
                            // Toast.makeText(SplashActivity.this, "sucess", Toast.LENGTH_SHORT).show();
                            Log.e("RESPONSE::","RESPONSE:" +"getContractorsPerfomanceComments");
                            GetCommentsRequestResponse result = response.body();
                            if (result.getSuccess()) {
                                List<GetCommentsRequestResponse.Datum> data =response.body().getData();
                                if (data!=null&&data.size()>0) {
                                    dbHelper.truncatePerfomanceComments();
                                    for (int i = 0; i <data.size() ; i++) {
                                        try {
                                            dbHelper.insertPerfomanceComments(data.get(i));
                                        }
                                        catch (Exception ex)
                                        {
                                            dbHelper.UpdatePerfomanceComments(data.get(i));
                                        }
                                    }
                                    // Utils.hideProgress();
                                }
                                getClientConsultantsComments();
                               /* Utils.showToast(LoginActivity.this,response.body().getData().getDisplayName().toString()+"");
                                Utils.setSharedPreString(LoginActivity.this,"UserId",response.body().getData().getId().toString());
                                Utils.setSharedPreString(LoginActivity.this,"display_name",response.body().getData().getDisplayName().toString());
                                Utils.setSharedPreString(LoginActivity.this,"profile_completed",response.body().getData().getProfileCompleted().toString());
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));*/
                            } else {
                                //Toast.makeText(LoginActivity.this, result.getmessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            //  Utils.hideProgress();
                            Converter<ResponseBody, ErrorResponse> converter = WebServiceCaller.client.responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                            try {
                                ErrorResponse errors = converter.convert(response.errorBody());
                                Toast.makeText(SplashActivity.this, errors.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(SplashActivity.this, R.string.server_error, Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GetCommentsRequestResponse> call, Throwable t) {
                        Log.v("onFailure", "onFailure");
                        /// Utils.showToast(SplashActivity.this, getResources().getString(R.string.server_error));
                        Utils.hideProgress();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*Manisha 27/2/20*/
    private void getClientConsultantsComments() {
        try {
            if (!Utils.isNetworkAvailable(SplashActivity.this)) {
                Utils.showToast(SplashActivity.this, getResources().getString(R.string.no_internet_connection));
            }  else {
                // Utils.showProgress(SplashActivity.this);
                WebServiceCaller.WebServiceApiInterface service = WebServiceCaller.getClient();
                Call<GetCommentsRequestResponse> call = service.getClientConsultantsComments();
                call.enqueue(new Callback<GetCommentsRequestResponse>() {
                    @Override
                    public void onResponse(Call<GetCommentsRequestResponse> call, Response<GetCommentsRequestResponse> response) {
                        if (response.isSuccessful()) {
                            // Toast.makeText(SplashActivity.this, "sucess", Toast.LENGTH_SHORT).show();
                            Log.e("RESPONSE::","RESPONSE:" +"getClientConsultantsComments");
                            GetCommentsRequestResponse result = response.body();
                            if (result.getSuccess()) {
                                List<GetCommentsRequestResponse.Datum> data =response.body().getData();
                                if (data!=null&&data.size()>0) {
                                    dbHelper.truncateClientsComments();
                                    for (int i = 0; i <data.size() ; i++) {
                                        try {
                                            dbHelper.insertClientComments(data.get(i));
                                        }
                                        catch (Exception ex)
                                        {
                                            dbHelper.UpdateClientComments(data.get(i));
                                        }
                                    }
                                    // Utils.hideProgress();
                                }
                                setdataforIncompleteSurvey();
                               /* Utils.showToast(LoginActivity.this,response.body().getData().getDisplayName().toString()+"");
                                Utils.setSharedPreString(LoginActivity.this,"UserId",response.body().getData().getId().toString());
                                Utils.setSharedPreString(LoginActivity.this,"display_name",response.body().getData().getDisplayName().toString());
                                Utils.setSharedPreString(LoginActivity.this,"profile_completed",response.body().getData().getProfileCompleted().toString());
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));*/
                            } else {
                                // Toast.makeText(LoginActivity.this, result.getmessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                              Utils.hideProgress();
                            Converter<ResponseBody, ErrorResponse> converter = WebServiceCaller.client.responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                            try {
                                ErrorResponse errors = converter.convert(response.errorBody());
                                Toast.makeText(SplashActivity.this, errors.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(SplashActivity.this, R.string.server_error, Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GetCommentsRequestResponse> call, Throwable t) {
                        Log.v("onFailure", "onFailure");
                        /// Utils.showToast(SplashActivity.this, getResources().getString(R.string.server_error));
                        Utils.hideProgress();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*Sync data*/
    /*public  void senddatatoserver()
    {
        JSONObject Jsonrequest;
        Jsonrequest =new JSONObject();
        cursor =dbHelper.getallcompleteSurvey();
        cursor.moveToFirst();
        if (cursor!=null&&cursor.getCount()>0)
        {
            try {
                JSONArray jsonArrayprofiledata;
                JSONArray jsonArrayanswerdata;
                do {
                    jsonArrayprofiledata = new JSONArray();
                    JSONObject profileuserdata = new JSONObject();
                    jsonArrayanswerdata = new JSONArray();

                    Profileid = cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_ID));
                    UserId = cursor.getString(cursor.getColumnIndex(DBUtility.USER_ID));
                    SurveyId = cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_ID));
                    profileuserdata.put("surveyid", cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_ID)));
                    profileuserdata.put("userid", cursor.getString(cursor.getColumnIndex(DBUtility.USER_ID)));
                    profileuserdata.put("fund", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_FUND)));
                    profileuserdata.put("managing_agent", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_COMANAGINGAGENT)));
                    profileuserdata.put("managment_surveyor", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_MANAGEMENTSURVEOR)));
                    profileuserdata.put("facility_manager", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_FACILITIESMANAGER)));
                    profileuserdata.put("site_address", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITEADDRESS))));
                    profileuserdata.put("report_prepareed_by", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_REPORTPREPAREDBY)));
                    profileuserdata.put("report_checked_by", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_REPORTCHECKEDBY)));
                    //Utils.getFormatedTime(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_AUDITDATE)),"dd-mm-yyyy","yyyy-mm-dd");
                    //Utils.getFormatedTime(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_DATEOFISSUE)),"dd-mm-yyyy","yyyy-mm-dd");
                    profileuserdata.put("audit_visit", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_AUDITVISIT)));
                    profileuserdata.put("audit_date", Utils.getFormatedTime(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_AUDITDATE)), "dd-mm-yyyy", "yyyy-mm-dd"));
                    profileuserdata.put("date_of_issue", Utils.getFormatedTime(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_DATEOFISSUE)), "dd-mm-yyyy", "yyyy-mm-dd"));
                    profileuserdata.put("contractor_performance", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_CONTRACTORSPERFOMANCE)));
                    profileuserdata.put("statutory_audit_score", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_STATUTOORYAUDITSCORE)));
                    profileuserdata.put("contractor_improvement", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_CONTRACTORIMPROVEPERFOMANCE)));
                    profileuserdata.put("consultant_comments", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_CONSULTANTSCOMMENTS))));
                    profileuserdata.put("profileid", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_ID)));
                    jsonArrayprofiledata.put(profileuserdata);


                    cursordata = dbHelper.getallcompleteSurveyAnsProfileid(Profileid);

                    if (cursordata != null && cursordata.getCount() > 0) {
                        do {
                            JSONObject useranswer = new JSONObject();
                            useranswer.put("id", cursordata.getString(cursordata.getColumnIndex(DBUtility.QUESTION_ID)));
                            useranswer.put("answer", Utils.ReplaceAsciChar(cursordata.getString(cursordata.getColumnIndex(DBUtility.USER_ANSWER))));
                            useranswer.put("priority", cursordata.getString(cursordata.getColumnIndex(DBUtility.ANSWER_PRIORITY)));
                            useranswer.put("user_id", cursordata.getString(cursordata.getColumnIndex(DBUtility.USER_ID)));
                            //useranswer.put("dateoflastcertificate",cursor.getString(cursor.getColumnIndex(DBUtility.CERTIFICATE_DATE)));
                            if (!cursordata.getString(cursordata.getColumnIndex(DBUtility.CERTIFICATE_DATE)).equalsIgnoreCase("")) {
                                //Utils.getFormatedTime(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_AUDITDATE)),"dd-mm-yyyy","yyyy-mm-dd"));
                                useranswer.put("dateoflastcertificate", Utils.getFormatedTime(cursordata.getString(cursor.getColumnIndex(DBUtility.CERTIFICATE_DATE)),"dd-mm-yyyy","yyyy-mm-dd"));
                            }
                            else {
                                useranswer.put("dateoflastcertificate","");
                            }
                            jsonArrayanswerdata.put(useranswer);

                            //data[i] = cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME));
                            //data.add(new DefaultAnswerModel(cursor.getString(cursor.getColumnIndex(DBUtility.ID)),cursor.getString(cursor.getColumnIndex(DBUtility.DEFAULT_ANSWER))));
                        } while (cursordata.moveToNext());

                    }
					*//*JSONObject useranswer = new JSONObject();
					useranswer.put("id",cursor.getString(cursor.getColumnIndex(DBUtility.QUESTION_ID)));
					useranswer.put("answer",cursor.getString(cursor.getColumnIndex(DBUtility.USER_ANSWER)));
					useranswer.put("priority",cursor.getString(cursor.getColumnIndex(DBUtility.ANSWER_PRIORITY)));
					useranswer.put("user_id",cursor.getString(cursor.getColumnIndex(DBUtility.USER_ID)));
					jsonArrayanswerdata .put(useranswer);*//*

                    //data[i] = cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME));
                    //data.add(new DefaultAnswerModel(cursor.getString(cursor.getColumnIndex(DBUtility.ID)),cursor.getString(cursor.getColumnIndex(DBUtility.DEFAULT_ANSWER))));
                } while (cursor.moveToNext());
                Jsonrequest.put("surveyinfo", jsonArrayprofiledata);
                Jsonrequest.put("data", jsonArrayanswerdata);
                Log.d("JsonRequest", Jsonrequest.toString());
                senddatatoserver(Jsonrequest);

            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
        else
        {
          *//*  if (synccount>0)
            {
                shownotification(synccount);
                synccount=0;
            }*//*
            Log.d("No Data","No Data for sync");
        }
    }


    public  void senddatatoserver(JSONObject jsonObject) {
        try {
            if (!Utils.isNetworkAvailable(SplashActivity.this)) {
                //Utils.showToast(c, c.getResources().getString(R.string.no_internet_connection));
            }  else

            {
                //  Utils.showProgress(SplashActivity.this);
                WebServiceCaller.WebServiceApiInterface service = WebServiceCaller.getClient();
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(String.valueOf(jsonObject))).toString());
                Call<ErrorResponse> call = service.UserAnswer(body);
                call.enqueue(new Callback<ErrorResponse>() {

                    @Override
                    public void onResponse(Call<ErrorResponse> call, Response<ErrorResponse> response) {


                        if (response.isSuccessful())
                        {

                            ErrorResponse result = response.body();
                            if (result.getSuccess()) {
                                dbHelper.updateUserAnsSyncFlag(UserId,SurveyId,Profileid);
                                Log.d("Data synced","Data Synchronized with surver");

                                phtolist = dbHelper.getimagepathAndcommentfinal(SurveyId, Profileid, UserId);
                                if (phtolist!=null&&phtolist.size()>0)
                                {
                                    uploadimage(SurveyId,Profileid,UserId);
                                }
                                else {
                                    dbHelper.DeleteSyncUserPhotos();
                                    dbHelper.DeleteSyncUserAnswer();
                                    senddatatoserver();
                                *//*    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_PROFILEID, "");
                                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_SURVEYNAME, "");
                                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_SURVEYID, "");
                                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_GROUPID, "");
                                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_GROUPNAME, "");
                                    startActivity(new Intent(CompleteSurveyAndSync.this, ThankYouActivity.class));
                                    finish();*//*
                                }

                                //Utils.showToast(SplashActivity.this,response.body().getMessage());

                            } else {
                                //Toast.makeText(LoginActivity.this, result.getmessage(), Toast.LENGTH_SHORT).show();
                                Log.d("Data synced","Data Synchronized error");
                            }
                        }
                        else {
                            //  Utils.hideProgress();
                            Converter<ResponseBody, ErrorResponse> converter = WebServiceCaller.client.responseBodyConverter(ErrorResponse.class, new Annotation[0]);

                            try {
                                ErrorResponse errors = converter.convert(response.errorBody());
                                Log.d("Data synced","Data Synchronized error");
                                //	Toast.makeText(SplashActivity.this, errors.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                //Toast.makeText(SplashActivity.this, R.string.server_error, Toast.LENGTH_LONG).show();
                                Log.d("Data synced","Data Synchronized error");
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<ErrorResponse> call, Throwable t) {
                        Log.v("onFailure", "onFailure");
                        //Utils.showToast(SplashActivity.this, getResources().getString(R.string.server_error));
                        //Utils.hideProgress();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    *//*Image sync code*//*

    public void uploadimage(String surveyid,String profileId,String Userid)
    {

        ProfileId=profileId;
        UserId=Userid;
        SurveyId=surveyid;

        phtolist = dbHelper.getimagepathAndcommentfinal(SurveyId, ProfileId, UserId);

        for (int i = 0; i < phtolist.size(); i++) {
            if (Utils.checkfile(phtolist.get(i).getPhotoPath())) {
                File file = new File(phtolist.get(i).getPhotoPath());
                RequestBody fileBody = RequestBody.create(MediaType.parse("image*//*"), file);
                map.put("image[" + i + "]\"; filename=\"" + file.getName() + "\" ", fileBody);
                //  map.put("image["+i+"]", fileBody);
                mapcomment.put("comment[" + i + "]", phtolist.get(i).getPhotoComment());
            }

        }
        InsertPhoto(ProfileId,UserId);
    }

    private void InsertPhoto(final String Profileid, final String Userid) {
        try {

           // Utils.showProgress(CompleteSurveyAndSync.this);
            WebServiceCaller.WebServiceApiInterface service = WebServiceCaller.getClient();

            RequestBody profileidbody =Utils.getRequestBody(ProfileId);

            Call<ErrorResponse> call = service.insertphoto(profileidbody,mapcomment,map);
            call.enqueue(new Callback<ErrorResponse>() {

                @Override
                public void onResponse(Call<ErrorResponse> call, Response<ErrorResponse> response) {
                    //Utils.hideProgress();

                    if (response.isSuccessful()) {

                        ErrorResponse result = response.body();
                        if (result.getSuccess()) {
                            // Toast.makeText(CompleteSurveyAndSync.this,"sucess",Toast.LENGTH_LONG).show();
                            long dbstatus=dbHelper.updatePhotosflag(Profileid,Userid);
                            if (dbstatus>0)
                            { //Utils.hideProgress();
                                Log.d("image synced","Successfully");

                            }
                            else
                            {
                               // Utils.hideProgress();

                                Log.d("image synced","not successfully");
                            }
                            dbHelper.DeleteSyncUserPhotos();
                            dbHelper.DeleteSyncUserAnswer();
                            senddatatoserver();
                           *//* Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_PROFILEID, "");
                            Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_SURVEYNAME, "");
                            Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_SURVEYID, "");
                            Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_GROUPID, "");
                            Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_GROUPNAME, "");
                            startActivity(new Intent(CompleteSurveyAndSync.this, ThankYouActivity.class));
                            finish();*//*

                        }
                    } else {
                       // Utils.hideProgress();
                        Converter<ResponseBody, ErrorResponse> converter = WebServiceCaller.client.responseBodyConverter(ErrorResponse.class, new Annotation[0]);


                    }


                }

                @Override
                public void onFailure(Call<ErrorResponse> call, Throwable t) {
                    Log.v("onFailure", "onFailure");
                   // Utils.showToast(CompleteSurveyAndSync.this, getResources().getString(R.string.server_error));
                   // Utils.hideProgress();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/

    /*Sync data new*/

    public  void senddatatoserver()
    {
        JSONObject Jsonrequest;
        Jsonrequest =new JSONObject();
        cursor =dbHelper.getallcompleteSurvey();
        cursor.moveToFirst();
        try {
            if (cursor != null && cursor.getCount() > 0) {
                synccount = cursor.getCount();
                try {
                    JSONArray jsonArrayprofiledata;
                    JSONArray jsonArrayanswerdata;
                    do {
                        jsonArrayprofiledata = new JSONArray();
                        JSONObject profileuserdata = new JSONObject();
                        jsonArrayanswerdata = new JSONArray();
                        Profileid = cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_ID));
                        UserId = cursor.getString(cursor.getColumnIndex(DBUtility.USER_ID));
                        SurveyId = cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_ID));
                        profileuserdata.put("surveyid", cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_ID)));
                        profileuserdata.put("userid", cursor.getString(cursor.getColumnIndex(DBUtility.USER_ID)));
                        // Manisha commented 13-1-20
                        // profileuserdata.put("fund", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_FUND)));
                        profileuserdata.put("fund","");
                        profileuserdata.put("managing_agent", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_COMANAGINGAGENT)));
                        profileuserdata.put("managment_surveyor", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_MANAGEMENTSURVEOR)));
                        profileuserdata.put("facility_manager", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_FACILITIESMANAGER)));

                        /*18-10 */
                        // profileuserdata.put("site_address",Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITEADDRESS))));
                        profileuserdata.put("site_name", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITENAME))));
                        profileuserdata.put("address1", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITEADDRESS1))));
                        profileuserdata.put("address2", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITEADDRESS2))));
                        profileuserdata.put("address3", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITEADDRESS3))));
                        profileuserdata.put("pincode", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITEPOSTALCODE))));
                        profileuserdata.put("report_prepareed_by", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_REPORTPREPAREDBY)));
                        profileuserdata.put("report_checked_by", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_REPORTCHECKEDBY)));
                        //Utils.getFormatedTime(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_AUDITDATE)),"dd-mm-yyyy","yyyy-mm-dd");
                        //Utils.getFormatedTime(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_DATEOFISSUE)),"dd-mm-yyyy","yyyy-mm-dd");
                        profileuserdata.put("audit_visit", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_AUDITVISIT)));
                        profileuserdata.put("audit_date", Utils.getFormatedTime(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_AUDITDATE)), "dd-mm-yyyy", "yyyy-mm-dd"));
                        profileuserdata.put("date_of_issue", Utils.getFormatedTime(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_DATEOFISSUE)), "dd-mm-yyyy", "yyyy-mm-dd"));
                        profileuserdata.put("contractor_performance", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_CONTRACTORSPERFOMANCE))));
                        profileuserdata.put("statutory_audit_score", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_STATUTOORYAUDITSCORE))));
                        profileuserdata.put("contractor_improvement", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_CONTRACTORIMPROVEPERFOMANCE))));
                        profileuserdata.put("consultant_comments", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_CONSULTANTSCOMMENTS))));
                        profileuserdata.put("consultants_comments_client", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_CLIENTCOMMENTS))));

                        profileuserdata.put("statutory_certification_comments", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_STATUTORYCOMMENTS))));
                        profileuserdata.put("profileid", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_ID)));
                        jsonArrayprofiledata.put(profileuserdata);

                        cursordata = dbHelper.getallcompleteSurveyAnsProfileid(Profileid);
                        try {
                            if (cursordata != null && cursordata.getCount() > 0) {
                                do {
                                    JSONObject useranswer = new JSONObject();
                                    useranswer.put("id", cursordata.getString(cursordata.getColumnIndex(DBUtility.QUESTION_ID)));
                                    useranswer.put("answer", Utils.ReplaceAsciChar(cursordata.getString(cursordata.getColumnIndex(DBUtility.USER_ANSWER))));
                                    useranswer.put("priority", cursordata.getString(cursordata.getColumnIndex(DBUtility.ANSWER_PRIORITY)));
                                    useranswer.put("user_id", cursordata.getString(cursordata.getColumnIndex(DBUtility.USER_ID)));
                                    //useranswer.put("dateoflastcertificate",cursor.getString(cursor.getColumnIndex(DBUtility.CERTIFICATE_DATE)));
                                    if (!cursordata.getString(cursordata.getColumnIndex(DBUtility.CERTIFICATE_DATE)).equalsIgnoreCase("")) {
                                        //Utils.getFormatedTime(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_AUDITDATE)),"dd-mm-yyyy","yyyy-mm-dd"));
                                        useranswer.put("dateoflastcertificate", Utils.getFormatedTime(cursordata.getString(cursordata.getColumnIndex(DBUtility.CERTIFICATE_DATE)), "dd-mm-yyyy", "yyyy-mm-dd"));
                                    } else {
                                        useranswer.put("dateoflastcertificate", "");
                                    }
                                    jsonArrayanswerdata.put(useranswer);

                                    //data[i] = cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME));
                                    //data.add(new DefaultAnswerModel(cursor.getString(cursor.getColumnIndex(DBUtility.ID)),cursor.getString(cursor.getColumnIndex(DBUtility.DEFAULT_ANSWER))));
                                } while (cursordata.moveToNext());

                            }
                        }
                        finally {
                            cursordata.close();
                        }
                    /*JSONObject useranswer = new JSONObject();
					useranswer.put("id",cursor.getString(cursor.getColumnIndex(DBUtility.QUESTION_ID)));
					useranswer.put("answer",cursor.getString(cursor.getColumnIndex(DBUtility.USER_ANSWER)));
					useranswer.put("priority",cursor.getString(cursor.getColumnIndex(DBUtility.ANSWER_PRIORITY)));
					useranswer.put("user_id",cursor.getString(cursor.getColumnIndex(DBUtility.USER_ID)));
					jsonArrayanswerdata .put(useranswer);*/

                        //data[i] = cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME));
                        //data.add(new DefaultAnswerModel(cursor.getString(cursor.getColumnIndex(DBUtility.ID)),cursor.getString(cursor.getColumnIndex(DBUtility.DEFAULT_ANSWER))));

                        Jsonrequest.put("surveyinfo", jsonArrayprofiledata);
                        Jsonrequest.put("data", jsonArrayanswerdata);
                        Log.d("JsonRequest", Jsonrequest.toString());
                        senddatatoserver(Jsonrequest, Profileid);
                    } while (cursor.moveToNext());
				/*Jsonrequest.put("surveyinfo", jsonArrayprofiledata);
				Jsonrequest.put("data", jsonArrayanswerdata);
				Log.d("JsonRequest", Jsonrequest.toString());
				senddatatoserver(Jsonrequest);*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {
                if (synccount > 0) {
                    shownotification(synccount);
                    synccount = 0;
                }
                Log.d("No Data", "No Data for sync" + synccount);
            }
        }
        finally {
            cursor.close();
        }
    }


    public  void senddatatoserver(JSONObject jsonObject, final String profid) {
        try {
            if (!Utils.isNetworkAvailable(SplashActivity.this)) {
                //Utils.showToast(c, c.getResources().getString(R.string.no_internet_connection));
            }  else {
                //  Utils.showProgress(SplashActivity.this);
                WebServiceCaller.WebServiceApiInterface service = WebServiceCaller.getClient();
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(String.valueOf(jsonObject))).toString());
                Call<ErrorResponse> call = service.UserAnswer(body);
                call.enqueue(new Callback<ErrorResponse>() {
                    @Override
                    public void onResponse(Call<ErrorResponse> call, Response<ErrorResponse> response) {
                        if (response.isSuccessful()) {
                            ErrorResponse result = response.body();
                            if (result.getSuccess()) {
                                dbHelper.updateUserAnsSyncFlag(UserId,SurveyId,profid);
                                Log.d("Data synced","Data Synchronized with surver");
                                //senddatatoserver();
                                //Utils.showToast(SplashActivity.this,response.body().getMessage());
                                phtolist = dbHelper.getimagepathAndcommentfinal(SurveyId, profid, UserId);
                                if (phtolist!=null&&phtolist.size()>0) {
                                    uploadimage(SurveyId,profid,UserId);
                                    Log.d("Proflid for image",profid);
                                } else {
                                    //  dbHelper.DeleteSyncUserPhotos();
                                    // dbHelper.DeleteSyncUserAnswer();
                                    synccount1++;
                                    Log.d("Sync count","No Data for sync"+synccount);
                                    if(synccount==synccount1){
                                        if (synccount>0) {
                                            new ExportDatabaseCSVTask().execute();
                                            shownotification(synccount);
                                            Log.d("Sync count","No Data for sync"+synccount);
                                            synccount=0;
                                        }
                                        Log.d("No Data","No Data for sync"+synccount);
                                    }
                                    //	senddatatoserver();
                                /*    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_PROFILEID, "");
                                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_SURVEYNAME, "");
                                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_SURVEYID, "");
                                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_GROUPID, "");
                                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_GROUPNAME, "");
                                    startActivity(new Intent(CompleteSurveyAndSync.this, ThankYouActivity.class));
                                    finish();*/
                                }
                            } else {
                                //Toast.makeText(LoginActivity.this, result.getmessage(), Toast.LENGTH_SHORT).show();
                                Log.d("Data synced","Data Synchronized error");
                            }
                        }
                        else {
                            //  Utils.hideProgress();
                            Converter<ResponseBody, ErrorResponse> converter = WebServiceCaller.client.responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                            try {
                                ErrorResponse errors = converter.convert(response.errorBody());
                                Log.d("Data synced","Data Synchronized error");
                                //	Toast.makeText(SplashActivity.this, errors.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                //Toast.makeText(SplashActivity.this, R.string.server_error, Toast.LENGTH_LONG).show();
                                Log.d("Data synced","Data Synchronized error");
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<ErrorResponse> call, Throwable t) {
                        Log.v("onFailure", "onFailure");
                        //Utils.showToast(SplashActivity.this, getResources().getString(R.string.server_error));
                        //Utils.hideProgress();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*Image sync code*/

    public void uploadimage(String surveyid,String profileId,String Userid) {
        phtolist = dbHelper.getimagepathAndcommentfinal(surveyid, profileId, Userid);
        for (int i = 0; i < phtolist.size(); i++) {
            if (Utils.checkfile(phtolist.get(i).getPhotoPath())) {
                File file = new File(phtolist.get(i).getPhotoPath());
                RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
                map.put("image[" + i + "]\"; filename=\"" + file.getName() + "\" ", fileBody);
                //  map.put("image["+i+"]", fileBody);
                // mapcomment.put("comment[" + i + "]", phtolist.get(i).getPhotoComment());
                mapcomment.put("comment[" + i + "]",Utils.getRequestBody(phtolist.get(i).getPhotoComment()));
            }
        }
        InsertPhoto(profileId,Userid);
    }

    private void InsertPhoto(final String Profileid1, final String Userid1) {
        try {
            // Utils.showProgress(CompleteSurveyAndSync.this);
            WebServiceCaller.WebServiceApiInterface service = WebServiceCaller.getClient();
            RequestBody profileidbody =Utils.getRequestBody(Profileid1);
            Call<ErrorResponse> call = service.insertphoto(profileidbody,mapcomment,map);
            //Call<ErrorResponse> call = service.insertphoto(ProfileId,mapcomment,map);
            call.enqueue(new Callback<ErrorResponse>() {
                @Override
                public void onResponse(Call<ErrorResponse> call, Response<ErrorResponse> response) {
                    //Utils.hideProgress();
                    if (response.isSuccessful()) {
                        ErrorResponse result = response.body();
                        if (result.getSuccess()) {
                            // Toast.makeText(CompleteSurveyAndSync.this,"sucess",Toast.LENGTH_LONG).show();
                            long dbstatus=dbHelper.updatePhotosflag(Profileid1,Userid1);
                            if (dbstatus>0) { //Utils.hideProgress();
                                Log.d("image synced","Successfully");
                            }
                            else {
                                // Utils.hideProgress();
                                Log.d("image synced","not successfully");
                            }
                            // dbHelper.DeleteSyncUserPhotos();
                            // dbHelper.DeleteSyncUserAnswer();
                            synccount1++;
                            Log.d("Sync count","No Data for sync"+synccount);
                            if(synccount==synccount1){
                                if (synccount>0)
                                {
                                    shownotification(synccount);
                                    synccount=0;
                                }
                                Log.d("No Data","No Data for sync"+synccount);
                            }
                            new ExportDatabaseCSVTask().execute();
                            //senddatatoserver();
                            //synccount++;
                           /* Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_PROFILEID, "");
                            Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_SURVEYNAME, "");
                            Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_SURVEYID, "");
                            Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_GROUPID, "");
                            Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_GROUPNAME, "");
                            startActivity(new Intent(CompleteSurveyAndSync.this, ThankYouActivity.class));
                            finish();*/
                        }
                    } else {
                        // Utils.hideProgress();
                        Converter<ResponseBody, ErrorResponse> converter = WebServiceCaller.client.responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                    }
                }
                @Override
                public void onFailure(Call<ErrorResponse> call, Throwable t) {
                    Log.v("onFailure", "onFailure");
                    // Utils.showToast(CompleteSurveyAndSync.this, getResources().getString(R.string.server_error));
                    // Utils.hideProgress();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shownotification(Integer synccount) {
        Intent notificationIntent = new Intent(SplashActivity.this, SplashActivity.class);
        TaskStackBuilder stackBuilder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            stackBuilder = TaskStackBuilder.create(SplashActivity.this);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            stackBuilder.addParentStack(SplashActivity.class);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            stackBuilder.addNextIntent(notificationIntent);
        }

        PendingIntent pendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(SplashActivity.this);
        Notification notification = builder.setContentTitle("PAS Survey")
                .setContentText(synccount+" Survey Sync successfully")
                //.setContentText("Survey Sync successfully")
                .setTicker("PAS Survey")
                .setSmallIcon(getNotificationIcon())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent).build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        notificationManager.notify(m, notification);
    }

    private  boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Handle permission granted

                    //*success*//*

                    if (Utils.getSharedPreString(SplashActivity.this,"UserId")!=null&&!Utils.getSharedPreString(SplashActivity.this,"UserId").equalsIgnoreCase(""))
                    {
                        /*exporting data to csv 21-11-2016*/

                        new ExportDatabaseCSVTask().execute();
                        handler = new Handler();
                        handler.postDelayed(r = new Runnable() {
                            @Override
                            public void run() {

                                cursor=  dbHelper.checkIncompleteSurvey(Utils.getSharedPreString(SplashActivity.this,Utils.PREFS_USERID),Utils.getSharedPreString(SplashActivity.this,Utils.PREFS_SURVEYID));
                                if (cursor!=null&&cursor.getCount()>0)
                                {
                                    startActivity(new Intent(SplashActivity.this,InCompleteSurveyActivity.class));
                                    finish();
                                }
                                else
                                {
                                    startActivity(new Intent(SplashActivity.this,SelectSurveyActivity.class));
                                    finish();
                                }


                            }
                        }, SPLASH_TIME_OUT);


                    }
                    else
                    {
                        getSurveyData();
                    }

                } else {
                    //*success*//*


                    if (Utils.getSharedPreString(SplashActivity.this,"UserId")!=null&&!Utils.getSharedPreString(SplashActivity.this,"UserId").equalsIgnoreCase(""))
                    {
                        /*exporting data to csv 21-11-2016*/
                        new ExportDatabaseCSVTask().execute();
                        handler = new Handler();
                        handler.postDelayed(r = new Runnable() {
                            @Override
                            public void run() {

                                cursor=  dbHelper.checkIncompleteSurvey(Utils.getSharedPreString(SplashActivity.this,Utils.PREFS_USERID),Utils.getSharedPreString(SplashActivity.this,Utils.PREFS_SURVEYID));
                                if (cursor!=null&&cursor.getCount()>0)
                                {
                                    startActivity(new Intent(SplashActivity.this,InCompleteSurveyActivity.class));
                                    finish();
                                }
                                else
                                {
                                    startActivity(new Intent(SplashActivity.this,SelectSurveyActivity.class));
                                    finish();
                                }


                            }
                        }, SPLASH_TIME_OUT);


                    }
                    else
                    {
                        getSurveyData();
                    }

                }
                break;
        }
    }

    /* export sqlite to csv */
    public class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean> {
        //private final ProgressDialog dialog = new ProgressDialog(SplashActivity.this);
        @Override
        protected void onPreExecute() {
            /*this.dialog.setMessage("Exporting database...");
            this.dialog.show();*/
        }
        protected Boolean doInBackground(final String... args) {
            File dbFile=getDatabasePath("PASSURVEYDB");
            //AABDatabaseManager dbhelper = new AABDatabaseManager(getApplicationContext());
            // AABDatabaseManager dbhelper = new AABDatabaseManager(DatabaseExampleActivity.this) ;
            System.out.println(dbFile);  // displays the data base path in your logcat
            File exportDir = new File(Environment.getExternalStorageDirectory()+File.separator+"PASAuditData", "");

            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }
            File file = new File(exportDir, "PasAudit.csv");
            try {
                if (file.createNewFile()){
                    System.out.println("File is created!");
                    System.out.println("myfile.csv "+file.getAbsolutePath());
                }else{
                    System.out.println("File already exists.");
                }

                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                // SQLiteDatabase db = dbhelper.getWritableDatabase();

                try {
                    Cursor curCSV=dbHelper.getalluserans();

                    if(curCSV!=null&&curCSV.getCount()>0) {

                        csvWrite.writeNext(curCSV.getColumnNames());

                        while (curCSV.moveToNext())

                        {

                            String arrStr[] = {curCSV.getString(0), curCSV.getString(1), curCSV.getString(2), curCSV.getString(3), curCSV.getString(4), curCSV.getString(5), curCSV.getString(6), curCSV.getString(7), curCSV.getString(8), curCSV.getString(9), curCSV.getString(10), curCSV.getString(11), curCSV.getString(12), curCSV.getString(13), curCSV.getString(14), curCSV.getString(15), curCSV.getString(16), curCSV.getString(17), curCSV.getString(18)};

                            /*curCSV.getString(3),curCSV.getString(4)};*/

                            csvWrite.writeNext(arrStr);


                        }

                        csvWrite.close();
                        curCSV.close();
                    }
                }catch (Exception ex){

                }

        /*String data="";
        data=readSavedData();
        data= data.replace(",", ";");
        writeData(data);*/

                return true;

            }

            catch(SQLException sqlEx)

            {

                Log.e("MainActivity", sqlEx.getMessage(), sqlEx);

                return false;

            }

            catch (IOException e)

            {

                Log.e("MainActivity", e.getMessage(), e);

                return false;

            }

        }

        protected void onPostExecute(final Boolean success)

        {

           /* if (this.dialog.isShowing())

            {

                this.dialog.dismiss();

            }*/

            if (success)

            {

                //Toast.makeText(SplashActivity.this, "Export succeed", Toast.LENGTH_SHORT).show();

            }

            else

            {

                //   Toast.makeText(SplashActivity.this, "Export failed", Toast.LENGTH_SHORT).show();

            }
        }}

    // ---------------------------------------------------------------------------------------
    /*amitk send data to server 31-1-17*/
    public  void senddatatoservernew()
    {
        JSONObject Jsonrequest;
        Jsonrequest =new JSONObject();
        cursor =dbHelper.getallcompleteSurvey();
        cursor.moveToFirst();
        try {
            if (cursor != null && cursor.getCount() > 0) {
                synccount = cursor.getCount();
                try {
                    JSONArray jsonArrayprofiledata;
                    JSONArray jsonArrayanswerdata;
                    jsonArrayprofiledata = new JSONArray();
                    JSONObject profileuserdata = new JSONObject();
                    jsonArrayanswerdata = new JSONArray();
                    Profileid = cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_ID));
                    UserId = cursor.getString(cursor.getColumnIndex(DBUtility.USER_ID));
                    SurveyId = cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_ID));
                    profileuserdata.put("surveyid", cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_ID)));
                    profileuserdata.put("userid", cursor.getString(cursor.getColumnIndex(DBUtility.USER_ID)));
                    //Manisha commented 13-1-20
//                    profileuserdata.put("fund", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_FUND)));

                    profileuserdata.put("managing_agent", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_COMANAGINGAGENT)));
                    profileuserdata.put("managment_surveyor", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_MANAGEMENTSURVEOR)));
                    profileuserdata.put("facility_manager", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_FACILITIESMANAGER)));
                    //profileuserdata.put("site_address",Utils.ReplaceAsciChar( cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITEADDRESS))));
                    /*18-10 */

                    //profileuserdata.put("site_address",Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITEADDRESS))));

                    profileuserdata.put("site_name", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITENAME))));
                    profileuserdata.put("address1", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITEADDRESS1))));
                    profileuserdata.put("address2", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITEADDRESS2))));
                    profileuserdata.put("address3", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITEADDRESS3))));
                    profileuserdata.put("pincode", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITEPOSTALCODE))));
                    profileuserdata.put("report_prepareed_by", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_REPORTPREPAREDBY)));
                    profileuserdata.put("report_checked_by", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_REPORTCHECKEDBY)));
                    //Utils.getFormatedTime(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_AUDITDATE)),"dd-mm-yyyy","yyyy-mm-dd");
                    //Utils.getFormatedTime(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_DATEOFISSUE)),"dd-mm-yyyy","yyyy-mm-dd");
                    profileuserdata.put("audit_visit", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_AUDITVISIT)));
                    profileuserdata.put("audit_date", Utils.getFormatedTime(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_AUDITDATE)), "dd-mm-yyyy", "yyyy-mm-dd"));
                    profileuserdata.put("date_of_issue", Utils.getFormatedTime(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_DATEOFISSUE)), "dd-mm-yyyy", "yyyy-mm-dd"));
                    profileuserdata.put("contractor_performance", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_CONTRACTORSPERFOMANCE))));
                    profileuserdata.put("statutory_audit_score", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_STATUTOORYAUDITSCORE))));
                    profileuserdata.put("contractor_improvement", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_CONTRACTORIMPROVEPERFOMANCE))));
                    profileuserdata.put("consultant_comments", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_CONSULTANTSCOMMENTS))));
                    profileuserdata.put("statutory_certification_comments", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_STATUTORYCOMMENTS))));
                    //Manisha
                    profileuserdata.put("consultants_comments_client", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_CLIENTCOMMENTS))));

                    profileuserdata.put("profileid", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_ID)));
                    jsonArrayprofiledata.put(profileuserdata);

                    cursordata = dbHelper.getallcompleteSurveyAnsProfileid(Profileid);
                    try {
                        if (cursordata != null && cursordata.getCount() > 0) {
                            do {
                                JSONObject useranswer = new JSONObject();
                                useranswer.put("id", cursordata.getString(cursordata.getColumnIndex(DBUtility.QUESTION_ID)));
                                useranswer.put("answer", Utils.ReplaceAsciChar(cursordata.getString(cursordata.getColumnIndex(DBUtility.USER_ANSWER))));
                                useranswer.put("priority", cursordata.getString(cursordata.getColumnIndex(DBUtility.ANSWER_PRIORITY)));
                                useranswer.put("user_id", cursordata.getString(cursordata.getColumnIndex(DBUtility.USER_ID)));
                                //useranswer.put("dateoflastcertificate",cursor.getString(cursor.getColumnIndex(DBUtility.CERTIFICATE_DATE)));
                                if (!cursordata.getString(cursordata.getColumnIndex(DBUtility.CERTIFICATE_DATE)).equalsIgnoreCase("")) {
                                    //Utils.getFormatedTime(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_AUDITDATE)),"dd-mm-yyyy","yyyy-mm-dd"));
                                    useranswer.put("dateoflastcertificate", Utils.getFormatedTime(cursordata.getString(cursordata.getColumnIndex(DBUtility.CERTIFICATE_DATE)), "dd-mm-yyyy", "yyyy-mm-dd"));
                                } else {
                                    useranswer.put("dateoflastcertificate", "");
                                }
                                jsonArrayanswerdata.put(useranswer);

                                //data[i] = cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME));
                                //data.add(new DefaultAnswerModel(cursor.getString(cursor.getColumnIndex(DBUtility.ID)),cursor.getString(cursor.getColumnIndex(DBUtility.DEFAULT_ANSWER))));
                            } while (cursordata.moveToNext());

                        }
                    }
                    finally {
                        cursordata.close();
                    }
                    /*JSONObject useranswer = new JSONObject();
					useranswer.put("id",cursor.getString(cursor.getColumnIndex(DBUtility.QUESTION_ID)));
					useranswer.put("answer",cursor.getString(cursor.getColumnIndex(DBUtility.USER_ANSWER)));
					useranswer.put("priority",cursor.getString(cursor.getColumnIndex(DBUtility.ANSWER_PRIORITY)));
					useranswer.put("user_id",cursor.getString(cursor.getColumnIndex(DBUtility.USER_ID)));
					jsonArrayanswerdata .put(useranswer);*/

                    //data[i] = cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME));
                    //data.add(new DefaultAnswerModel(cursor.getString(cursor.getColumnIndex(DBUtility.ID)),cursor.getString(cursor.getColumnIndex(DBUtility.DEFAULT_ANSWER))));

                    Jsonrequest.put("surveyinfo", jsonArrayprofiledata);
                    Jsonrequest.put("data", jsonArrayanswerdata);
                    Log.d("JsonRequest", Jsonrequest.toString());
                    senddatatoservernew(Jsonrequest, Profileid);

				/*Jsonrequest.put("surveyinfo", jsonArrayprofiledata);
				Jsonrequest.put("data", jsonArrayanswerdata);
				Log.d("JsonRequest", Jsonrequest.toString());
				senddatatoserver(Jsonrequest);*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                if (totalSynced > 0) {
                    totalImageCount = dbHelper.getonebyoneimagePendingcount();
                    shownotification(totalSynced);
                    totalSynced = 0;
                }
                Log.d("No Data", "No Data for sync" + totalSynced);
            }
        }
        finally {
            cursor.close();
        }
    }

    /*new amitk 31-1-17*/
    public  void senddatatoservernew(JSONObject jsonObject, final String profid) {
        try {
            if (!Utils.isNetworkAvailable(SplashActivity.this)) {
                //Utils.showToast(c, c.getResources().getString(R.string.no_internet_connection));
            }  else {
                //  Utils.showProgress(SplashActivity.this);
                WebServiceCaller.WebServiceApiInterface service = WebServiceCaller.getClient();
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(String.valueOf(jsonObject))).toString());
                Call<ErrorResponse> call = service.UserAnswer(body);
                call.enqueue(new Callback<ErrorResponse>() {
                    @Override
                    public void onResponse(Call<ErrorResponse> call, Response<ErrorResponse> response) {
                        if (response.isSuccessful()) {
                            ErrorResponse result = response.body();
                            if (result.getSuccess()) {
                                dbHelper.updateUserAnsSyncFlag(UserId,SurveyId,profid);
                                Log.d("Data synced","Data Synchronized with surver");
                                totalSynced++;
                                // senddatatoserver();
                                // Utils.showToast(SplashActivity.this,response.body().getMessage());
                                phtolist = dbHelper.getimagepathAndcommentfinal(SurveyId, profid, UserId);
                                if (phtolist!=null&&phtolist.size()>0) {
                                    /*amitk 31-1-17 background image*/
                                    dbHelper.updatePhotosflagCompleteSurveyData(profid,UserId);
                                    if (firstime==0) {
                                        sendPendingImageToServer();
                                        firstime=1;
                                    }
                                    //	uploadimage(SurveyId,profid,UserId);
                                    //	Log.d("Proflid for image",profid);
                                }
                                else {
                                    //dbHelper.DeleteSyncUserPhotos();
                                    //dbHelper.DeleteSyncUserAnswer();

									/*synccount1++;
									Log.d("Sync count","No Data for sync"+synccount);
									if(synccount==synccount1){
										if (synccount>0)
										{
											shownotification(synccount);
											Log.d("Sync count","No Data for sync"+synccount);
											synccount=0;
										}
										Log.d("No Data","No Data for sync"+synccount);
									}*/
									/*new ExportDatabaseCSVTask().execute();

									senddatatoservernew();*/
                                    //	senddatatoserver();
                                /*    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_PROFILEID, "");
                                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_SURVEYNAME, "");
                                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_SURVEYID, "");
                                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_GROUPID, "");
                                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_GROUPNAME, "");
                                    startActivity(new Intent(CompleteSurveyAndSync.this, ThankYouActivity.class));
                                    finish();*/
                                }
                                new ExportDatabaseCSVTask().execute();
                                //Manisha commented 18-04-20
//                                senddatatoservernew();

                            } else {
                                //Toast.makeText(LoginActivity.this, result.getmessage(), Toast.LENGTH_SHORT).show();
                                Log.d("Data synced","Data Synchronized error");
                            }
                        }
                        else {
                            //  Utils.hideProgress();
                            Converter<ResponseBody, ErrorResponse> converter = WebServiceCaller.client.responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                            try {
                                ErrorResponse errors = converter.convert(response.errorBody());
                                Log.d("Data synced","Data Synchronized error");
                                Toast.makeText(SplashActivity.this, errors.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                //Toast.makeText(SplashActivity.this, R.string.server_error, Toast.LENGTH_LONG).show();
                                Log.d("Data synced","Data Synchronized error");
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<ErrorResponse> call, Throwable t) {
                        Log.v("onFailure", "onFailure");
                        //Utils.showToast(c, getResources().getString(R.string.server_error));
                        //Toast.makeText(c, R.string.server_error, Toast.LENGTH_SHORT).show();

                        //Utils.hideProgress();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* send pending image to server amitk 31-1-17*/
    private void sendPendingImageToServer() {
        PhotoModel photo =dbHelper.getonebyoneimagePending();
        if (!Utils.isImageSyncing)
        {
            totalImageCount= dbHelper.getonebyoneimagePendingcount();

        }
        if(photo!=null)
        {
            if (oldprofile.equalsIgnoreCase("")) {
                oldprofile = photo.getProfileId();
                groupbyphotototal = dbHelper.getonebyoneimagePendingcountbyGroup(photo.getProfileId());
            }
            if (!oldprofile.equalsIgnoreCase(photo.getProfileId())) {
                oldprofile=photo.getProfileId();
                groupbyphotocount=0;
                groupbyphotototal = dbHelper.getonebyoneimagePendingcountbyGroup(photo.getProfileId());
            }

            if (map!=null)
            {
                map.clear();
            }
            if (mapcomment!=null)
            {
                mapcomment.clear();
            }
            if (Utils.checkfile(photo.getPhotoPath())) {
                File file = new File(photo.getPhotoPath());
                RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
                map.put("image[" + 0 + "]\"; filename=\"" + file.getName() + "\" ", fileBody);
                //  map.put("image["+i+"]", fileBody);
                //mapcomment.put("comment[" + i + "]", phtolist.get(i).getPhotoComment());
                mapcomment.put("comment[" + 0 + "]", Utils.getRequestBody(photo.getPhotoComment()));
                InsertPhotonew(photo.getProfileId(),photo.getUserId(),photo.getPhotoPath(),photo.getPhotoComment());
            }
        } else {
            if (totalSyncedImage>0)
            {
                cancelNotification();
                shownotificationImage(totalSyncedImage);
                Log.d("Total synced image "," => "+totalSyncedImage+"");
            }
        }

    }


    /*amitk send photo service call android*/

    private void InsertPhotonew(final String Profileid1, final String Userid1,final String photoPath,final String photoComment) {
        try {

            totalSyncedImage++;
            groupbyphotocount++;
            if (groupbyphotocount>groupbyphotototal)
            {
                groupbyphotocount=1;
                groupbyphotototal = dbHelper.getonebyoneimagePendingcountbyGroup(Profileid1);
            }
            shownotificationImagewithcount(groupbyphotocount,Profileid1);
            // Utils.showProgress(CompleteSurveyAndSync.this);
            WebServiceCaller.WebServiceApiInterface service = WebServiceCaller.getClient();
            RequestBody profileidbody =Utils.getRequestBody(Profileid1);

            Call<ErrorResponse> call = service.insertphoto(profileidbody,mapcomment,map);

            //Call<ErrorResponse> call = service.insertphoto(ProfileId,mapcomment,map);
            call.enqueue(new Callback<ErrorResponse>() {

                @Override
                public void onResponse(Call<ErrorResponse> call, Response<ErrorResponse> response) {
                    //Utils.hideProgress();

                    if (response.isSuccessful()) {

                        ErrorResponse result = response.body();
                        if (result.getSuccess()) {
                            // Toast.makeText(CompleteSurveyAndSync.this,"sucess",Toast.LENGTH_LONG).show();


                            long dbstatus=dbHelper.updatePhotosSyncFlag(Profileid1,Userid1,photoPath,photoComment);
                            if (dbstatus>0)
                            {
                                //popupWindow.dismiss();
                                Log.d("image synced","Successfully");
                            }
                            else
                            {
                                // popupWindow.dismiss();
                                Log.d("image synced","not successfully");
                            }

                            //dbHelper.DeleteSyncUserPhotos();
                            //dbHelper.DeleteSyncUserAnswer();
							/*synccount1++;
							Log.d("Sync count","No Data for sync"+synccount);
							if(synccount==synccount1){
								if (synccount>0)
								{
									shownotification(synccount);
									synccount=0;
								}
								Log.d("No Data","No Data for sync"+synccount);
							}*/
                            new ExportDatabaseCSVTask().execute();

                            sendPendingImageToServer();
                            //senddatatoserver();
                            //synccount++;
                           /* Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_PROFILEID, "");
                            Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_SURVEYNAME, "");
                            Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_SURVEYID, "");
                            Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_GROUPID, "");
                            Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_GROUPNAME, "");
                            startActivity(new Intent(CompleteSurveyAndSync.this, ThankYouActivity.class));
                            finish();*/
                        }
                    } else {
                        // Utils.hideProgress();
                        Converter<ResponseBody, ErrorResponse> converter = WebServiceCaller.client.responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                    }
                }

                @Override
                public void onFailure(Call<ErrorResponse> call, Throwable t) {
                    Log.v("onFailure", "onFailure");
                    // Utils.showToast(CompleteSurveyAndSync.this, getResources().getString(R.string.server_error));
                    // Utils.hideProgress();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void shownotificationImage(Integer synccount)
    {

        Intent notificationIntent = new Intent(SplashActivity.this, SplashActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(SplashActivity.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            stackBuilder.addParentStack(SplashActivity.class);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            stackBuilder.addNextIntent(notificationIntent);
        }

        PendingIntent pendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(SplashActivity.this);

        Notification notification = builder.setContentTitle(getString(R.string.app_name))
                .setContentText(synccount+" Image Sync successfully")
                //.setContentText(" Survey Sync successfully")
                .setTicker(getString(R.string.app_name))
                .setSmallIcon(getNotificationIcon())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent).build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        notificationManager.notify(m, notification);
    }
    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.app_icon_white : R.mipmap.ic_launcher;
    }

    /*amitk 15-2-17*/

    public void shownotificationImagewithcount(Integer synccount,String Profileid) {
        Intent notificationIntent = new Intent(SplashActivity.this, SplashActivity.class);
        TaskStackBuilder stackBuilder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            stackBuilder = TaskStackBuilder.create(SplashActivity.this);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            stackBuilder.addParentStack(SplashActivity.class);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            stackBuilder.addNextIntent(notificationIntent);
        }

        PendingIntent pendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(SplashActivity.this);
        String message="";
        /*if (synccount==totalImageCount)
        {
            message=synccount+" Image Sync successfully";
        }
        else
        {
        }*/
        try {
            //message = dbHelper.getSurveyName(Profileid) + ": " + synccount + "/" + groupbyphotototal + " Image Sync in progress";
            message = dbHelper.getSurveyNameSiteName(Profileid) + ": " + synccount + "/" + groupbyphotototal + " Image Sync in progress";

        }catch (Exception ex)
        {

        }

        Notification notification = builder.setContentTitle(getString(R.string.app_name))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message)
                //.setContentText(" Survey Sync successfully")
                .setTicker(getString(R.string.app_name))
                .setAutoCancel(true)
                .setSmallIcon(getNotificationIcon())
                .setContentIntent(pendingIntent).build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		/*Random random = new Random();
		int m = random.nextInt(9999 - 1000) + 1000;*/
        notificationManager.notify(IMAGE_NOTIFICATION_ID, notification);
    }

    public  void cancelNotification() {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) getSystemService(ns);
        nMgr.cancel(IMAGE_NOTIFICATION_ID);
    }
}