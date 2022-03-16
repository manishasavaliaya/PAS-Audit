/*
package com.passurvey.activities;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.consumer.chowexpress.R;
import com.consumer.chowexpress.customviews.CustomButton;
import com.consumer.chowexpress.customviews.CustomEditText;
import com.consumer.chowexpress.customviews.CustomTextView;
import com.consumer.chowexpress.requestresponse.LoginRequestResponse;
import com.consumer.chowexpress.utility.Utils;
import com.consumer.chowexpress.webservices.WebServiceCaller;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {
    private LinearLayout lnrLogin;
    private RelativeLayout rlLogo;
    private CustomEditText edtUserName;
    private CustomEditText edtPassword;
    private LinearLayout lnRememberMe;
    private ImageView imgRememberMe;
    private CustomTextView txtRememberMe;
    private CustomTextView txtForgotPassword;
    private CustomButton btnSignIn;
    private CustomTextView txtDont;
    private CustomTextView txtRegister;
    private View view;
    private int isRemember = 1;

    @Override
    public void setLayoutView() {
        view = LayoutInflater.from(this).inflate(R.layout.activity_login, lnrContainer);
        hideFooter();
        hideTopBar();
        lnrLogin = (LinearLayout) findViewById(R.id.lnrLogin);
        rlLogo = (RelativeLayout) findViewById(R.id.rlLogo);
        edtUserName = (CustomEditText) findViewById(R.id.edtUserName);
        edtPassword = (CustomEditText) findViewById(R.id.edtPassword);
        lnRememberMe = (LinearLayout) findViewById(R.id.lnRememberMe);
        imgRememberMe = (ImageView) findViewById(R.id.imgRememberMe);
        txtRememberMe = (CustomTextView) findViewById(R.id.txtRememberMe);
        txtForgotPassword = (CustomTextView) findViewById(R.id.txtForgotPassword);
        btnSignIn = (CustomButton) findViewById(R.id.btnSignIn);
        txtDont = (CustomTextView) findViewById(R.id.txtDont);
        txtRegister = (CustomTextView) findViewById(R.id.txtRegister);
        lnRememberMe.setSelected(true);
    }

    @Override
    public void initialization() {

    }

    @Override
    public void setupData() {

    }

    @Override
    public void setListeners() {
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserLogin();
            }
        });
        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(i);

            }
        });
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(i);
                finish();
            }
        });

        lnRememberMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnRememberMe.setSelected(!lnRememberMe.isSelected());
                if (lnRememberMe.isSelected()) {
                    lnRememberMe.setSelected(true);
                    isRemember = 1;
                    imgRememberMe.setImageDrawable(getResources().getDrawable(R.drawable.check_icon));
                } else {
                    isRemember = 0;
                    lnRememberMe.setSelected(false);
                    imgRememberMe.setImageDrawable(getResources().getDrawable(R.drawable.uncheck_icon));
                }

            }
        });


    }

    private void getUserLogin() {
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

                Call<LoginRequestResponse> call = service.getUserLogin(edtUserName.getText().toString().trim(), edtPassword.getText().toString().trim(), Utils.DeviceType, Utils.GCM_ID, Utils.Latitude, Utils.Longitude, Utils.Language);
                call.enqueue(new Callback<LoginRequestResponse>() {

                    @Override
                    public void onResponse(Call<LoginRequestResponse> call, Response<LoginRequestResponse> response) {
                        Utils.hideProgress();
                        if (response.isSuccessful()) {
                            LoginRequestResponse result = response.body();
                            if (result.getStatus() == 1) {
                                dbHelper.truncateData();
                                dbHelper.insertUserLoginData(result.getData(), isRemember);
                                Intent i = new Intent(LoginActivity.this, RestaurantListActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Utils.hideProgress();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginRequestResponse> call, Throwable t) {
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
*/
