package com.passurvey.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.passurvey.R;
import com.passurvey.database.DBUtility;
import com.passurvey.model.PhotoModel;
import com.passurvey.requestresponse.ErrorResponse;
import com.passurvey.utility.AndroidMultiPartEntity;
import com.passurvey.utility.MyLog;
import com.passurvey.utility.ProgressRequestBody;
import com.passurvey.utility.Utils;
import com.passurvey.webservices.WebServiceCaller;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class CompleteSurveyAndSync extends MainActivity implements View.OnClickListener,ProgressRequestBody.UploadCallbacks{
    long totalSize = 0;
    // public  static  boolean profilenotcompFlag=false;
    Button btnCompleteandSync,btnNotNow;
    String SurveyId,GroupId,UserId,ProfileId;
    Cursor cursor;
    private ArrayList<PhotoModel> phtolist = new ArrayList<>();
    HashMap<String, MultipartBody.Part> map = new HashMap<>();
    Map<String, RequestBody> mapcomment = new HashMap<>();
    private static Dialog popupWindow;
    ProgressBar progress;
    TextView tvprogresspercentage;
    File[] filelist;
    String[] commntlist;
    int imageUploadCount=1,totalImageCount=0;

    @Override
    protected void onPause() {
        super.onPause();
        // handler.removeCallbacks(r);
    }

    @Override
    public  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.container_body); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.fragment_complete_survey_and_sync, contentFrameLayout);
        bindid();
        SurveyId= Utils.getSharedPreString(CompleteSurveyAndSync.this,Utils.PREFS_SURVEYID);
        GroupId=Utils.getSharedPreString(CompleteSurveyAndSync.this,Utils.PREFS_GROUPID);
        UserId=Utils.getSharedPreString(CompleteSurveyAndSync.this,Utils.PREFS_USERID);
        ProfileId=Utils.getSharedPreString(CompleteSurveyAndSync.this,Utils.PREFS_PROFILEID);
    }

    private void bindid() {
        if (Utils.getSharedPreString(CompleteSurveyAndSync.this,Utils.PREFS_SURVEYNAME)!=null&&!Utils.getSharedPreString(CompleteSurveyAndSync.this,Utils.PREFS_SURVEYNAME).equalsIgnoreCase("")) {
            setHeaderTitle(Utils.getSharedPreString(CompleteSurveyAndSync.this,Utils.PREFS_SURVEYNAME));
        } else {
            setHeaderTitle(getString(R.string.title_survey));
        }
        btnCompleteandSync=(Button)findViewById(R.id.btnCompleteandSync);
        btnNotNow=(Button)findViewById(R.id.btnNotNow);
        btnCompleteandSync.setOnClickListener(this);
        btnNotNow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCompleteandSync:
                //startActivity(new Intent(ThankYouActivity.this,SelectSurveyActivity.class));
              /*  startActivity(new Intent(CompleteSurveyAndSync.this,SplashActivity.class));
                finish();*/
                if(checkalldatacomplete().equalsIgnoreCase(getString(R.string.complete))) {
                    btnCompleteandSync.setEnabled(false);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btnCompleteandSync.setEnabled(true);
                        }
                    },2000);

                    if (!Utils.isNetworkAvailable(CompleteSurveyAndSync.this)) {
                        //  Utils.showToast(CompleteSurveyAndSync.this, getResources().getString(R.string.no_internet_connection));
                        AlertDialog.Builder builder = new AlertDialog.Builder(CompleteSurveyAndSync.this);
                        builder.setMessage(getString(R.string.syncofflinemessage))
                                .setTitle(getString(R.string.no_internet_connection))
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        long surveyflag = dbHelper.updateUserAnsSurveyFlag(SurveyId, UserId, ProfileId);
                                        // amitk 31-1-17
                                        long phootoflag = dbHelper.updatePhotosflagComplete(ProfileId,UserId);
                                        if (phootoflag>0) {
                                            Log.d("Survey Photo", "photo Completed");
                                        } else {
                                            Log.d("Survey Photo", "photo not Completed");
                                        }

                                       /* Utils.showProgress(getActivity());
                                        Utils.removeSharedPref(getActivity());
                                        startActivity(new Intent(getActivity(), SplashActivity.class));
                                        getActivity().finish();*/

                                        Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_PROFILEID, "");
                                        Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_SURVEYNAME, "");
                                        Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_SURVEYID, "");
                                        Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_GROUPID, "");
                                        Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_GROUPNAME, "");

                                        /*amitk 3-1-17 drawrer position */
                                        Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_DRAWEREXPANDNAME, "");
                                        Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_DRAWEREXPANDPOSITION, "");

                                        startActivity(new Intent(CompleteSurveyAndSync.this, ThankYouActivity.class));
                                        finish();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        /* Update complete survey flag after completing all questino group answer */
                        long surveyflag = dbHelper.updateUserAnsSurveyFlag(SurveyId, UserId, ProfileId);
                        // amitk 31-1-17
                        long phootoflag = dbHelper.updatePhotosflagComplete(ProfileId,UserId);
                        if (phootoflag>0) {
                            Log.d("Survey Photo", "photo Completed");
                        } else {
                            Log.d("Survey Photo", "photo not Completed");
                        }

                        if (surveyflag > 0) {
                            Utils.showToast(CompleteSurveyAndSync.this, "Survey Completed");
                            Log.d("Survey Completed status", "Survey Completed");
                            senddatatoserver();
                        } else {
                            // Utils.showToast(QuestionAnswerActivity.this,"Survey  Not Completed");
                            Log.d("Survey Completed status", "Survey not Completed");
                        }
                    }
                }
                else if(checkalldatacomplete().equalsIgnoreCase(getString(R.string.completeProfile)))
                {
                    // profilenotcompFlag=true;
                    Utils.showToast(CompleteSurveyAndSync.this,getString(R.string.completeProfile));
                }
                else if(checkalldatacomplete().equalsIgnoreCase(getString(R.string.completesummary)))
                {
                    Utils.showToast(CompleteSurveyAndSync.this,getString(R.string.completesummary));
                }
                else if(checkalldatacomplete().equalsIgnoreCase(getString(R.string.completesurvey)))
                {
                    Utils.showToast(CompleteSurveyAndSync.this,getString(R.string.completesurvey));
                }

                break;

            case R.id.btnNotNow:
                //startActivity(new Intent(ThankYouActivity.this,SelectSurveyActivity.class));
                startActivity(new Intent(CompleteSurveyAndSync.this,InCompleteSurveyActivity.class));
                finish();
                break;

            /*case R.id.btnProfileNextClient:
                showsecondForm();

                break;
            case R.id.btnprofileaddBack:
                showfirstForm();

                break;
            case R.id.btnprofileaddnext:

                showthirdForm();
                break;
            case R.id.btnprofileAuditBack:
                showsecondForm();

                break;
            case R.id.btnprofileAuditFinish:

                break;*/
        }
    }

    /*Sync data*/
    public  void senddatatoserver() {
        Utils.showProgress(CompleteSurveyAndSync.this);
        JSONObject Jsonrequest;
        Jsonrequest =new JSONObject();
        cursor =dbHelper.getcompleteSurvey(UserId,SurveyId,ProfileId);
        // cursor = dbHelper.getallcompleteSurvey();
        cursor.moveToFirst();
        try {
            if (cursor != null && cursor.getCount() > 0) {
                JSONArray jsonArrayprofiledata = new JSONArray();
                JSONObject profileuserdata = new JSONObject();
                JSONArray jsonArrayanswerdata = new JSONArray();

                try {
                    ProfileId = cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_ID));
                    UserId = cursor.getString(cursor.getColumnIndex(DBUtility.USER_ID));
                    SurveyId = cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_ID));
                    profileuserdata.put("surveyid", cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_ID)));
                    profileuserdata.put("userid", cursor.getString(cursor.getColumnIndex(DBUtility.USER_ID)));
                    // Manisha commented 13-1-20
//                  profileuserdata.put("fund", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_FUND))));
                    profileuserdata.put("fund","");
                    profileuserdata.put("managing_agent", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_COMANAGINGAGENT))));
                    profileuserdata.put("managment_surveyor", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_MANAGEMENTSURVEOR))));
                    profileuserdata.put("facility_manager", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_FACILITIESMANAGER))));
                    /*18-10 */
                    // profileuserdata.put("site_address",Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITEADDRESS))));
                    profileuserdata.put("site_name", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITENAME))));
                    profileuserdata.put("address1", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITEADDRESS1))));
                    profileuserdata.put("address2", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITEADDRESS2))));
                    profileuserdata.put("address3", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITEADDRESS3))));
                    profileuserdata.put("pincode", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITEPOSTALCODE))));
                    profileuserdata.put("report_prepareed_by", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_REPORTPREPAREDBY)));
                    profileuserdata.put("report_checked_by", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_REPORTCHECKEDBY)));
                    profileuserdata.put("audit_visit", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_AUDITVISIT)));
                    profileuserdata.put("audit_date", Utils.getFormatedTime(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_AUDITDATE)), "dd-mm-yyyy", "yyyy-mm-dd"));
                    profileuserdata.put("date_of_issue", Utils.getFormatedTime(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_DATEOFISSUE)), "dd-mm-yyyy", "yyyy-mm-dd"));
                    profileuserdata.put("contractor_performance", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_CONTRACTORSPERFOMANCE))));
                    profileuserdata.put("statutory_audit_score", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_STATUTOORYAUDITSCORE))));
                    profileuserdata.put("contractor_improvement", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_CONTRACTORIMPROVEPERFOMANCE))));
                    profileuserdata.put("consultant_comments", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_CONSULTANTSCOMMENTS))));
                    profileuserdata.put("statutory_certification_comments", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_STATUTORYCOMMENTS))));
                    // Manisha 27-02-20
                    profileuserdata.put("consultants_comments_client", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_CLIENTCOMMENTS))));
                    profileuserdata.put("profileid", cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_ID)));
                    jsonArrayprofiledata.put(profileuserdata);
                    do {
                        JSONObject useranswer = new JSONObject();
                        useranswer.put("id", cursor.getString(cursor.getColumnIndex(DBUtility.QUESTION_ID)));
                        useranswer.put("answer", Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.USER_ANSWER))));
                        useranswer.put("priority", cursor.getString(cursor.getColumnIndex(DBUtility.ANSWER_PRIORITY)));
                        useranswer.put("user_id", cursor.getString(cursor.getColumnIndex(DBUtility.USER_ID)));
                        if (!cursor.getString(cursor.getColumnIndex(DBUtility.CERTIFICATE_DATE)).equalsIgnoreCase("")) {
                            //Utils.getFormatedTime(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_AUDITDATE)),"dd-mm-yyyy","yyyy-mm-dd"));
                            useranswer.put("dateoflastcertificate", Utils.getFormatedTime(cursor.getString(cursor.getColumnIndex(DBUtility.CERTIFICATE_DATE)), "dd-mm-yyyy", "yyyy-mm-dd"));
                        } else {
                            useranswer.put("dateoflastcertificate", "");
                        }
                        jsonArrayanswerdata.put(useranswer);
                        //data[i] = cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME));
                        //data.add(new DefaultAnswerModel(cursor.getString(cursor.getColumnIndex(DBUtility.ID)),cursor.getString(cursor.getColumnIndex(DBUtility.DEFAULT_ANSWER))));
                    } while (cursor.moveToNext());
                    Jsonrequest.put("surveyinfo", jsonArrayprofiledata);
                    Jsonrequest.put("data", jsonArrayanswerdata);
                    Log.d("JsonRequest", Jsonrequest.toString());
                    Utils.hideProgress();

                    String formateddate = getCurrentDateTime();
                    MyLog.writeLogIntoFile("api name::"+"answers call"+"\n" +"surveyid::"  + SurveyId+ "\n"+"profileid::"  + ProfileId +
                            "\n" + "CaptureTime::" + formateddate.toString()+"\n", "PassSurvey");

                    senddatatoserver(Jsonrequest);
                } catch (JSONException e) {
                    e.printStackTrace();
                    String formateddate = getCurrentDateTime();
                    MyLog.writeLogIntoFile("api name::"+"answers catch"+"\n" +"surveyid::"  + SurveyId+ "\n"+"profileid::"  + ProfileId +
                            "\n" + "CaptureTime::" + formateddate.toString()+"\n", "PassSurvey");
                }
            } else {
                Utils.hideProgress();
                Log.d("No Data", "No Data for sync");
            }
        } finally {
            cursor.close();
        }
    }

    public  void senddatatoserver(JSONObject jsonObject) {
        try {
            if (!Utils.isNetworkAvailable(CompleteSurveyAndSync.this)) {
                Utils.showToast(CompleteSurveyAndSync.this, getResources().getString(R.string.no_internet_connection));
            }  else {
                Utils.showProgress(CompleteSurveyAndSync.this);
                String formateddate = getCurrentDateTime();
                MyLog.writeLogIntoFile("api name::"+"answers request:"+jsonObject.toString()+"\n" +"surveyid::"  + SurveyId+ "\n"+"profileid::"  + ProfileId +
                        "\n" + "CaptureTime::" + formateddate.toString()+"\n", "PassSurvey");
                WebServiceCaller.WebServiceApiInterface service = WebServiceCaller.getClient();
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(String.valueOf(jsonObject))).toString());
                Call<ErrorResponse> call = service.UserAnswer(body);
                call.enqueue(new Callback<ErrorResponse>() {
                    @Override
                    public void onResponse(Call<ErrorResponse> call, Response<ErrorResponse> response) {
                        if (response.isSuccessful()) {
                            Utils.hideProgress();
                            ErrorResponse result = response.body();
                            if (result.getSuccess()) {
                                String formateddate = getCurrentDateTime();
                                MyLog.writeLogIntoFile("api name::"+"answers response success"+"\n" +"surveyid::"  + SurveyId+ "\n"+"profileid::"  + ProfileId +
                                        "\n" + "CaptureTime::" + formateddate.toString()+"\n", "PassSurvey");
                                /*20-10*/
                                dbHelper.updateUserAnsSyncFlag(UserId,SurveyId,ProfileId);
                                /* Completed sync to server
                                clear preferance surveyid profileid*/
                                phtolist = dbHelper.getimagepathAndcommentfinal(SurveyId, ProfileId, UserId);
                                if (phtolist!=null&&phtolist.size()>0)
                                {
                                    dbHelper.updatePhotosflagCompleteSurveyData(ProfileId,UserId);
                                    //amitk 31-1-17
                                    ///uploadimage(SurveyId,ProfileId,UserId);
                                    //uploadimageonebyone(SurveyId,ProfileId,UserId);
                                    startimageuploading(SurveyId,ProfileId,UserId);
                                } else {
                                    /*20-10*/
                                    // dbHelper.DeleteSyncUserPhotos();
                                    // dbHelper.DeleteSyncUserAnswer();
                                    ExecsvCreation();
                                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_PROFILEID, "");
                                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_SURVEYNAME, "");
                                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_SURVEYID, "");
                                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_GROUPID, "");
                                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_GROUPNAME, "");
                                    /*amitk 3-1-17 drawrer position */
                                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_DRAWEREXPANDNAME, "");
                                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_DRAWEREXPANDPOSITION, "");
                                    startActivity(new Intent(CompleteSurveyAndSync.this, ThankYouActivity.class));
                                    finish();
                                }

                                Log.d("Data synced","Data Synchronized with surver");
                                // Manisha 18-04-20 commented senddatatoserver
//                                 senddatatoserver();
                                // Utils.showToast(SplashActivity.this,response.body().getMessage());
                            } else {
                                // Toast.makeText(LoginActivity.this, result.getmessage(), Toast.LENGTH_SHORT).show();
                                Log.d("Data synced","Data Synchronized error");
                                String formateddate = getCurrentDateTime();

                                MyLog.writeLogIntoFile("api name::"+"answers response fail"+"\n" +"surveyid::"  + SurveyId+ "\n"+"profileid::"  + ProfileId +
                                        "\n" + "CaptureTime::" + formateddate.toString()+"\n", "PassSurvey");
                            }
                        } else {
                            Utils.hideProgress();
                            String formateddate = getCurrentDateTime();
                            MyLog.writeLogIntoFile("api name::"+"answers response not success"+"\n" +"surveyid::"  + SurveyId+ "\n"+"profileid::"  + ProfileId +
                                    "\n" + "CaptureTime::" + formateddate.toString()+"\n", "PassSurvey");

                            Converter<ResponseBody, ErrorResponse> converter = WebServiceCaller.client.responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                            try {
                                ErrorResponse errors = converter.convert(response.errorBody());
                                Log.d("Data synced","Data Synchronized error");
                                // Toast.makeText(SplashActivity.this, errors.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                // Toast.makeText(SplashActivity.this, R.string.server_error, Toast.LENGTH_LONG).show();
                                Log.d("Data synced","Data Synchronized error");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ErrorResponse> call, Throwable t) {
                        Log.v("onFailure", "onFailure");
                        String formateddate = getCurrentDateTime();
                        MyLog.writeLogIntoFile("api name::"+"answers response onFailure"+"\n" +"surveyid::"  + SurveyId+ "\n"+"profileid::"  + ProfileId +
                                "\n" + "CaptureTime::" + formateddate.toString()+"\n", "PassSurvey");

                        Utils.showToast(CompleteSurveyAndSync.this, getResources().getString(R.string.server_error));
                        Utils.hideProgress();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            String formateddate = getCurrentDateTime();
            MyLog.writeLogIntoFile("api name::"+"answers catch"+"\n" +"surveyid::"  + SurveyId+ "\n"+"profileid::"  + ProfileId +
                    "\n" + "CaptureTime::" + formateddate.toString()+"\n", "PassSurvey");
        }
    }

//    public void uploadimage(String surveyid,String profileId,String Userid) {
//        ProfileId=profileId;
//        UserId=Userid;
//        SurveyId=surveyid;
//
//        phtolist = dbHelper.getimagepathAndcommentfinal(SurveyId, ProfileId, UserId);
//        commntlist = new String[phtolist.size()];
//        filelist = new File[phtolist.size()];
//        for (int i = 0; i < phtolist.size(); i++) {
//            if (Utils.checkfile(phtolist.get(i).getPhotoPath())) {
//                File file = new File(phtolist.get(i).getPhotoPath());
//                ProgressRequestBody filey = new ProgressRequestBody(file, (ProgressRequestBody.UploadCallbacks) this);
//                MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", file.getName(), filey);
//                RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
//                //map.put("image[" + i + "]\"; filename=\"" + file.getName() + "\" ", fileBody);
//                filelist[i] =file;
//                commntlist[i]=Utils.ReplaceAsciChar(phtolist.get(i).getPhotoComment().toString());
//                map.put("image[" + i + "]\"; filename=\"" + file.getName() + "\" ", filePart);
//                //  map.put("image["+i+"]", fileBody);
//                //mapcomment.put("comment[" + i + "]", phtolist.get(i).getPhotoComment().toString());
//                mapcomment.put("comment[" + i + "]", Utils.getRequestBody(phtolist.get(i).getPhotoComment().toString()));
//                Log.d("commtnlength", phtolist.get(i).getPhotoComment().toString().length()+"");
//            }
//        }
//        //InsertPhoto(ProfileId,UserId);
//        new UploadFileToServer().execute();
//    }

    /*amitk upload image new one by one 31-1-17*/
    public void uploadimageonebyone(String surveyid,String profileId,String Userid) {
        ProfileId=profileId;
        UserId=Userid;
        SurveyId=surveyid;
        PhotoModel photomodel = dbHelper.getonebyoneimage(SurveyId, ProfileId, UserId);
        // phtolist = dbHelper.getimagepathAndcommentfinal(SurveyId, ProfileId, UserId);
        tvprogresspercentage.setText("Photo Uploading "+imageUploadCount+" / "+totalImageCount);

        if (photomodel!=null) {
            if (Utils.checkfile(photomodel.getPhotoPath())) {
                File file = new File(photomodel.getPhotoPath());
                Log.e("UPLOAD::","FILE::"+photomodel.getPhotoPath());
                new UploadFileToServerOneByOne(file,photomodel.getPhotoComment()).execute();
            }
        } else {
            if(popupWindow != null && popupWindow.isShowing()) {
                if (!CompleteSurveyAndSync.this.isFinishing() && popupWindow != null) {
                    popupWindow.dismiss();
                }
            }
            ExecsvCreation();
            Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_PROFILEID, "");
            Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_SURVEYNAME, "");
            Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_SURVEYID, "");
            Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_GROUPID, "");
            Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_GROUPNAME, "");

            /* amitk 3-1-17 drawrer position */
            Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_DRAWEREXPANDNAME, "");
            Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_DRAWEREXPANDPOSITION, "");
            startActivity(new Intent(CompleteSurveyAndSync.this, ThankYouActivity.class));
            finish();
        }

        /*commntlist = new String[phtolist.size()];
        filelist = new File[phtolist.size()];
        for (int i = 0; i < phtolist.size(); i++) {
            if (Utils.checkfile(phtolist.get(i).getPhotoPath())) {
                File file = new File(phtolist.get(i).getPhotoPath());
                ProgressRequestBody filey = new ProgressRequestBody(file, (ProgressRequestBody.UploadCallbacks) this);
                MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", file.getName(), filey);
                RequestBody fileBody = RequestBody.create(MediaType.parse("image*//*"), file);
                //map.put("image[" + i + "]\"; filename=\"" + file.getName() + "\" ", fileBody);
                filelist[i] =file;
                commntlist[i]=Utils.ReplaceAsciChar(phtolist.get(i).getPhotoComment().toString());
                map.put("image[" + i + "]\"; filename=\"" + file.getName() + "\" ", filePart);
                //  map.put("image["+i+"]", fileBody);
                //mapcomment.put("comment[" + i + "]", phtolist.get(i).getPhotoComment().toString());
                mapcomment.put("comment[" + i + "]", Utils.getRequestBody(phtolist.get(i).getPhotoComment().toString()));
                Log.d("commtnlength", phtolist.get(i).getPhotoComment().toString().length()+"");
            }
        }*/
        // InsertPhoto(ProfileId,UserId);
        // new UploadFileToServer().execute();
    }

    private void InsertPhoto(final String Profileid, final String Userid) {
        try {
            showProgressimage();
            // Utils.showProgress(CompleteSurveyAndSync.this);
            WebServiceCaller.WebServiceApiInterface service = WebServiceCaller.getClient();
            RequestBody profileidbody =Utils.getRequestBody(ProfileId);
            Call<ErrorResponse> call = service.insertphoto1(profileidbody,mapcomment,map);
            // Call<ErrorResponse> call = service.insertphoto(ProfileId,mapcomment,map);

            call.enqueue(new Callback<ErrorResponse>() {
                @Override
                public void onResponse(Call<ErrorResponse> call, Response<ErrorResponse> response) {
                    //Utils.hideProgress();
                    if (response.isSuccessful()) {
                        ErrorResponse result = response.body();
                        if (result.getSuccess()) {
                            // Toast.makeText(CompleteSurveyAndSync.this,"sucess",Toast.LENGTH_LONG).show();

                            /*20-10*/
                           /*long dbstatus=dbHelper.updatePhotosflag(Profileid,Userid);
                            if (dbstatus>0)
                            {
                                popupWindow.dismiss();
                                Log.d("image synced","Successfully");
                            }
                            else
                            {
                                popupWindow.dismiss();
                                Log.d("image synced","not successfully");
                            }
                            dbHelper.DeleteSyncUserPhotos();
                            dbHelper.DeleteSyncUserAnswer();


                            Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_PROFILEID, "");
                            Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_SURVEYNAME, "");
                            Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_SURVEYID, "");
                            Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_GROUPID, "");
                            Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_GROUPNAME, "");
                            */
                            /*20-10*/
                            startActivity(new Intent(CompleteSurveyAndSync.this, ThankYouActivity.class));
                            finish();
                        }
                    } else {
                        if(popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                        Converter<ResponseBody, ErrorResponse> converter = WebServiceCaller.client.responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                    }
                }
                @Override
                public void onFailure(Call<ErrorResponse> call, Throwable t) {
                    Log.v("onFailure", "onFailure");
                    Utils.showToast(CompleteSurveyAndSync.this, getResources().getString(R.string.server_error));
                    if(popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String  checkalldatacomplete() {
        cursor = dbHelper.checkIncompleteSurveyLogin1(Utils.getSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_USERID));
        try {
            if (cursor != null && cursor.getCount() > 0) {
                return getString(R.string.completesurvey);
            } else {
                if (dbHelper.checkprofilecomplete(ProfileId) && dbHelper.checksummary(ProfileId)) {
                    btnCompleteandSync.setEnabled(false);
                    return getString(R.string.complete);
                } else if (!dbHelper.checkprofilecomplete(ProfileId)) {
                    return getString(R.string.completeProfile);
                } else if(!dbHelper.checksummary(ProfileId)) {
                    return getString(R.string.completesummary);
                }
            }
        } finally {
            cursor.close();
        }
        return "null";
        // return false;
    }

    @Override
    public void onProgressUpdate(int percentage) {
        progress.setProgress(percentage);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {
        progress.setProgress(100);
        if(popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    public  void showProgressimage() {
        try {
            //View layout = LayoutInflater.from(CompleteSurveyAndSync.this).inflate(R.layout.popup_loading_image, null);
            popupWindow = new Dialog(CompleteSurveyAndSync.this);
            popupWindow.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //popupWindow.setTitle(getString(R.string.photouploading));
            popupWindow.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            popupWindow.setContentView(R.layout.popup_loading_image);
            popupWindow.setCancelable(false);
            progress =(ProgressBar)popupWindow.findViewById(R.id.progress);
            tvprogresspercentage=(TextView)popupWindow.findViewById(R.id.tvprogresspercentage);

            if (!((Activity) CompleteSurveyAndSync.this).isFinishing()) {
                popupWindow.show();
            }
        } catch (Exception e)

        {
            e.printStackTrace();
        }
    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            showProgressimage();
            //progressBar.setProgress(0);
            progress.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progressa) {
            // Making progress bar visible
            //progressBar.setVisibility(View.VISIBLE);
            // updating progress bar value
            progress.setProgress(progressa[0]);
            // updating percentage value
            tvprogresspercentage.setText(String.valueOf(progressa[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }
        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Utils.BASE_URL+"insertphoto");
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {
                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });
                // File sourceFile = new File(filePath);
                // Adding file data to http body
                entity.addPart("profileid",
                        new StringBody(ProfileId));
                for (int i = 0; i < filelist.length; i++) {
                    entity.addPart("image["+i+"]", new FileBody(filelist[i]));
                }
                for (int j = 0; j < commntlist.length; j++) {
                    entity.addPart( "comment[" + j + "]", new StringBody(commntlist[j]));
                }
                //entity.addPart("image", new FileBody(sourceFile));
                // Extra parameters if you want to pass to server
               /* entity.addPart("website",
                        new StringBody("www.androidhive.info"));
                entity.addPart("email", new StringBody("abc@gmail.com"));*/
                totalSize = entity.getContentLength();
                httppost.setEntity(entity);
                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }
            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("dd", "Response from server: " + result);
            // showing the server response in an alert dialog
            //showAlert(result);
            try {
                JSONObject  jsonobject = new JSONObject(result);
                if (jsonobject.has("success")&&jsonobject.getBoolean("success"))
                {
                    long dbstatus=dbHelper.updatePhotosflag(ProfileId,UserId);
                    if (dbstatus>0) {
                        if(popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                        Log.d("image synced","Successfully");
                    }
                    else {
                        if(popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                        Log.d("image synced","not successfully");
                    }
                    // dbHelper.DeleteSyncUserPhotos();
                    // dbHelper.DeleteSyncUserAnswer();
                    ExecsvCreation();
                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_PROFILEID, "");
                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_SURVEYNAME, "");
                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_SURVEYID, "");
                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_GROUPID, "");
                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_GROUPNAME, "");
                    /*amitk 3-1-17 drawrer position */
                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_DRAWEREXPANDNAME, "");
                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_DRAWEREXPANDPOSITION, "");
                    /*20-10*/
                }
                startActivity(new Intent(CompleteSurveyAndSync.this, ThankYouActivity.class));
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
            super.onPostExecute(result);
        }
    }

    /*amitk 31-1-17 upload image one by one*/
    private class UploadFileToServerOneByOne extends AsyncTask<Void, Integer, String> {
        File file1;
        String Comment1;
        public UploadFileToServerOneByOne(File file,String Comment) {
            file1= file;
            Comment1=Comment;
        }

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            // showProgressimage();
            // progressBar.setProgress(0);
            progress.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progressa) {
            // Making progress bar visible
            //progressBar.setVisibility(View.VISIBLE);
            // updating progress bar value
            progress.setProgress(progressa[0]);
            // updating percentage value
            // tvprogresspercentage.setText(String.valueOf(progressa[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        private String uploadFile() {
            String responseString = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Utils.BASE_URL+"insertphoto");
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {
                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                // File sourceFile = new File(filePath);
                // Adding file data to http body
                entity.addPart("profileid", new StringBody(ProfileId));
                entity.addPart("image["+0+"]", new FileBody(file1));
                entity.addPart( "comment[" + 0 + "]", new StringBody(Comment1));

                String formateddate = getCurrentDateTime();
                MyLog.writeLogIntoFile("api name::"+"insertphoto call"+"\n" +"profileid::"  + ProfileId +
                        "\n" + "CaptureTime::" + formateddate.toString()+"\n", "PassSurvey");

              /*  for (int i = 0; i < filelist.length; i++) {
                    entity.addPart("image["+i+"]", new FileBody(filelist[i]));
                }

                for (int j = 0; j < commntlist.length; j++) {
                    entity.addPart( "comment[" + j + "]", new StringBody(commntlist[j]));
                }*/

                //entity.addPart("image", new FileBody(sourceFile));
                // Extra parameters if you want to pass to server
               /* entity.addPart("website",
                        new StringBody("www.androidhive.info"));
                entity.addPart("email", new StringBody("abc@gmail.com"));*/

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);
                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: " + statusCode;
                }
            } catch (ClientProtocolException e) {
                responseString = e.toString();
                String formateddate = getCurrentDateTime();
                MyLog.writeLogIntoFile("api name::"+"insertphoto ClientProtocolException"+"\n" +"profileid::"  + ProfileId +
                        "\n" + "CaptureTime::" + formateddate.toString()+"\n", "PassSurvey");
            } catch (IOException e) {
                responseString = e.toString();
                String formateddate = getCurrentDateTime();
                MyLog.writeLogIntoFile("api name::"+"insertphoto IOException"+"\n" +"profileid::"  + ProfileId +
                        "\n" + "CaptureTime::" + formateddate.toString()+"\n", "PassSurvey");
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("dd", "Response from server: " + result);
            // showing the server response in an alert dialog
            // showAlert(result);

            try {
                JSONObject  jsonobject = new JSONObject(result);
                if (jsonobject.has("success")&&jsonobject.getBoolean("success")) {

                    String formateddate = getCurrentDateTime();
                    MyLog.writeLogIntoFile("api name::"+"insertphoto response success"+"\n" +"profileid::"  +ProfileId +
                            "\n" + "CaptureTime::" + formateddate.toString()+"\n", "PassSurvey");

                    // long dbstatus = dbHelper.updatePhotosflag(ProfileId,UserId);
                    long dbstatus = dbHelper.updatePhotosSyncFlag(ProfileId,UserId,file1.getAbsolutePath(),Comment1);
                    if (dbstatus > 0) {
                        // popupWindow.dismiss();
                        Log.d("image synced","Successfully");
                    } else {
                        // popupWindow.dismiss();
                        Log.d("image synced","not successfully");
                    }
                    // dbHelper.DeleteSyncUserPhotos();
                    // dbHelper.DeleteSyncUserAnswer();
                    /*ExecsvCreation();
                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_PROFILEID, "");
                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_SURVEYNAME, "");
                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_SURVEYID, "");
                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_GROUPID, "");
                    Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_GROUPNAME, "");*/
                    /*20-10*/
                    imageUploadCount++;
                    uploadimageonebyone(SurveyId,ProfileId,UserId);
                } else{
                    String formateddate = getCurrentDateTime();
                    MyLog.writeLogIntoFile("api name::"+"insertphoto response failer"+"\n" +"profileid::"  +ProfileId +
                            "\n" + "CaptureTime::" + formateddate.toString()+"\n", "PassSurvey");
                }
                /*startActivity(new Intent(CompleteSurveyAndSync.this, ThankYouActivity.class));
                finish();*/
            } catch (JSONException e) {
                e.printStackTrace();
                String formateddate = getCurrentDateTime();
                MyLog.writeLogIntoFile("api name::"+"insertphoto JSONException"+"\n" +"profileid::"  +ProfileId +
                        "\n" + "CaptureTime::" + formateddate.toString()+"\n", "PassSurvey");
                if(popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                Utils.showToast(CompleteSurveyAndSync.this,"Your images will uploaded autometically when internet is available");
                ExecsvCreation();
                Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_PROFILEID, "");
                Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_SURVEYNAME, "");
                Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_SURVEYID, "");
                Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_GROUPID, "");
                Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_GROUPNAME, "");

                /*amitk 3-1-17 drawrer position */
                Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_DRAWEREXPANDNAME, "");
                Utils.setSharedPreString(CompleteSurveyAndSync.this, Utils.PREFS_DRAWEREXPANDPOSITION, "");
                startActivity(new Intent(CompleteSurveyAndSync.this, ThankYouActivity.class));
                finish();
            }
            // popupWindow.dismiss();
            super.onPostExecute(result);
        }
    }

    public void startimageuploading(String surveyId,String profileId,String userId) {
        showProgressimage();
        totalImageCount = dbHelper.getonebyoneimagecount(surveyId,profileId,userId);
        tvprogresspercentage.setText("Photo Uploading "+imageUploadCount+" / "+totalImageCount);
        uploadimageonebyone(surveyId,profileId,userId);
    }

    public static String getCurrentDateTime(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date startdate = c.getTime();
        df.format(startdate);
        return df.format(startdate);
    }
}
