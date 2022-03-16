package com.passurvey.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.passurvey.R;
import com.passurvey.database.DBHelper;
import com.passurvey.model.PhotoModel;
import com.passurvey.requestresponse.ErrorResponse;
import com.passurvey.utility.Utils;
import com.passurvey.webservices.WebServiceCaller;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by iadmin on 1/10/16.
 */
public class demoActivity extends MainActivity {


    private ArrayList<PhotoModel> phtolist = new ArrayList<>();
    String ProfileId, UserID, SurveyID;
    Map<String, RequestBody> map = new HashMap<>();
    Map<String, RequestBody> mapcomment = new HashMap<>();


    public static DBHelper dbHelper;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.container_body); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.demo_layout, contentFrameLayout);
        dbHelper = DBHelper.getInstance(demoActivity.this);
        ProfileId = Utils.getSharedPreString(demoActivity.this,Utils.PREFS_PROFILEID);
        UserID=Utils.getSharedPreString(demoActivity.this,Utils.PREFS_USERID);
        SurveyID=Utils.getSharedPreString(demoActivity.this,Utils.PREFS_SURVEYID);

        phtolist = dbHelper.getimagepathAndcomment(SurveyID, ProfileId, UserID);

        for (int i = 0; i < phtolist.size(); i++) {

            File file = new File(phtolist.get(i).getPhotoPath());
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
            map.put("image["+i+"]\"; filename=\"" + file.getName() + "\" ", fileBody);
          //  map.put("image["+i+"]", fileBody);
            //mapcomment.put("comment["+i+"]",phtolist.get(i).getPhotoComment());
            mapcomment.put("comment["+i+"]",Utils.getRequestBody(phtolist.get(i).getPhotoComment()));
        }
        InsertPhoto();
    }


    private void InsertPhoto() {
        try {

            Utils.showProgress(demoActivity.this);
            WebServiceCaller.WebServiceApiInterface service = WebServiceCaller.getClient();
            RequestBody profileidbody =Utils.getRequestBody(ProfileId);

            Call<ErrorResponse> call = service.insertphoto(profileidbody,mapcomment,map);
        //    Call<ErrorResponse> call = service.insertphoto(ProfileId,mapcomment,map);
            call.enqueue(new Callback<ErrorResponse>() {

                @Override
                public void onResponse(Call<ErrorResponse> call, Response<ErrorResponse> response) {
                    Utils.hideProgress();

                    if (response.isSuccessful()) {

                        ErrorResponse result = response.body();
                        if (result.getSuccess()) {
                            Toast.makeText(demoActivity.this,"sucess",Toast.LENGTH_LONG).show();

                        }
                    } else {

                        Converter<ResponseBody, ErrorResponse> converter = WebServiceCaller.client.responseBodyConverter(ErrorResponse.class, new Annotation[0]);


                    }


                }

                @Override
                public void onFailure(Call<ErrorResponse> call, Throwable t) {
                    Log.v("onFailure", "onFailure");
                    Utils.showToast(demoActivity.this, getResources().getString(R.string.server_error));
                    Utils.hideProgress();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
