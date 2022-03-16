package com.passurvey.activities;

import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.passurvey.R;
import com.passurvey.database.DBUtility;
import com.passurvey.requestresponse.ErrorResponse;
import com.passurvey.requestresponse.PASLoginRequestResponse;
import com.passurvey.utility.Utils;
import com.passurvey.webservices.WebServiceCaller;

import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener{
    View view;
    private EditText edtUserName,edtPassword;
    private Button btnLogin;
    Cursor cursor;

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void setLayoutView() {
        view = LayoutInflater.from(this).inflate(R.layout.activity_login, lnrContainer);
        hideTopBar();
        edtUserName =(EditText)findViewById(R.id.edtUserName);
        edtPassword =(EditText)findViewById(R.id.edtPassword);
        btnLogin =(Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        edtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                   btnLogin.performClick();
                }
                return false;
            }
        });
    }

    @Override
    public void initialization() {

    }

    @Override
    public void setupData() {

    }

    @Override
    public void setListeners() {

    }

    @Override
    public void onClick(View v) {
     switch (v.getId()) {
         case R.id.btnLogin:
             //startActivity(new Intent(LoginActivity.this,MainActivity.class));
            getUserLogin();
             break;
     }
    }

    /*Login*/
    private void getUserLogin() {
//                 admin1234
//                admin1234
//       UNAme: pas.admin
//              pwd:  admin@123
        try {
            if (!Utils.isNetworkAvailable(LoginActivity.this)) {
                Utils.showToast(LoginActivity.this, getResources().getString(R.string.no_internet_connection));
            } else if (edtUserName.getText().toString().trim().equalsIgnoreCase("")) {
                edtUserName.setError(getString(R.string.username_error));
            } else if (edtPassword.getText().toString().trim().equalsIgnoreCase("")) {
                edtPassword.setError(getString(R.string.password_error));
            } else
            {
                Utils.showProgress(LoginActivity.this);
                WebServiceCaller.WebServiceApiInterface service = WebServiceCaller.getClient();
                Call<PASLoginRequestResponse> call = service.getUserLogin(edtUserName.getText().toString().trim(), edtPassword.getText().toString().trim());
                call.enqueue(new Callback<PASLoginRequestResponse>() {
                    @Override
                    public void onResponse(Call<PASLoginRequestResponse> call, Response<PASLoginRequestResponse> response) {
                        Utils.hideProgress();
                        if (response.isSuccessful()) {
                             PASLoginRequestResponse result = response.body();
                            if (result.getSucess()) {
                                //Utils.showToast(LoginActivity.this,response.body().getData().getDisplayName().toString()+"");
                                Utils.setSharedPreString(LoginActivity.this,Utils.PREFS_USERID,response.body().getData().getId().toString());
                                Utils.setSharedPreString(LoginActivity.this,Utils.PREFS_DISPLAYNAME,response.body().getData().getDisplayName().toString());
                                try {
                                    Utils.setSharedPreString(LoginActivity.this, Utils.PREFS_PROFILECOMPLETED, response.body().getData().getProfileCompleted().toString());
                                }
                                catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                                cursor=  dbHelper.checkIncompleteSurveyLogin(Utils.getSharedPreString(LoginActivity.this,Utils.PREFS_USERID));
                                try {
                                    if (cursor != null && cursor.getCount() > 0) {
                                        Utils.setSharedPreString(LoginActivity.this, Utils.PREFS_SURVEYID, cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_ID)));
                                        Utils.setSharedPreString(LoginActivity.this, Utils.PREFS_PROFILEID, cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_ID)));

                                        Utils.setSharedPreString(LoginActivity.this, Utils.PREFS_SURVEYNAME, cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME)));
                                        startActivity(new Intent(LoginActivity.this, InCompleteSurveyActivity.class));
                                        finish();
                                    } else {
                                        //edtPassword.setText("");
                                        startActivity(new Intent(LoginActivity.this, SelectSurveyActivity.class));
                                        finish();
                                    }
                                }
                                finally {
                                    cursor.close();
                                }
                               /* startActivity(new Intent(LoginActivity.this,SelectSurveyActivity.class));
                                finish();*/
                            } else {
                                edtPassword.setText("");
                                Toast.makeText(LoginActivity.this, result.getmessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {

                                Converter<ResponseBody, ErrorResponse> converter = WebServiceCaller.client.responseBodyConverter(ErrorResponse.class, new Annotation[0]);

                            try {
                                ErrorResponse errors = converter.convert(response.errorBody());
                               edtPassword.setText("");
                                Toast.makeText(LoginActivity.this, errors.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                edtPassword.setText("");
                                Toast.makeText(LoginActivity.this, R.string.answer, Toast.LENGTH_LONG).show();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<PASLoginRequestResponse> call, Throwable t) {
                        Log.v("onFailure", "onFailure");
                        Utils.showToast(LoginActivity.this, getResources().getString(R.string.server_error));
                        Utils.hideProgress();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
