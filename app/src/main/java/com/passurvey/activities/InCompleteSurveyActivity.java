package com.passurvey.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.passurvey.R;
import com.passurvey.database.DBUtility;
import com.passurvey.utility.Utils;


public class InCompleteSurveyActivity extends MainActivity implements View.OnClickListener{
    private Spinner spinner;
    private Button btnStartSurvey;
    private RelativeLayout rLSurveyTop;
    private LinearLayout lLIncompleteMain;
    private ImageView imgCalendar;
    private TextView tvIncompleteDate,tvIncompleteSurveyName,tvIncompleteSiteAddressTitle,tvIncompleteSiteAddressTitleValue;
    private ImageView imgWatch;
    private TextView tvIncompleteTime;
    private ImageView imgIncompleteImage,imgdiscard;
    private RelativeLayout rlAuditSite,rlAuditDate;
            private LinearLayout llAuditName;
    Cursor cursor,cursorprofile;
    String UserId,SurveyId,ProfileId;

    @Override
    protected void onPause() {
        super.onPause();
       // handler.removeCallbacks(r);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.container_body); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.fragment_incomplete_survey, contentFrameLayout);
        bindid();
        UserId=Utils.getSharedPreString(InCompleteSurveyActivity.this,Utils.PREFS_USERID);
        SurveyId=Utils.getSharedPreString(InCompleteSurveyActivity.this,Utils.PREFS_SURVEYID);
        ProfileId=Utils.getSharedPreString(InCompleteSurveyActivity.this,Utils.PREFS_PROFILEID);
        cursor=  dbHelper.checkIncompleteSurvey(Utils.getSharedPreString(InCompleteSurveyActivity.this,Utils.PREFS_USERID),Utils.getSharedPreString(InCompleteSurveyActivity.this,Utils.PREFS_SURVEYID));

        cursor.moveToFirst();
        try {
            if (cursor != null && cursor.getCount() > 0) {
                setHeaderTitle(cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME)));
                tvIncompleteSurveyName.setText(cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_NAME)));
                cursorprofile = dbHelper.getsurveyProfiledata(UserId, SurveyId, ProfileId);
                try {
                    cursorprofile.moveToFirst();
                    if (cursorprofile != null && cursorprofile.getCount() > 0) {
                        tvIncompleteDate.setText(cursorprofile.getString(cursorprofile.getColumnIndex(DBUtility.PROFILE_CREATEDDATE)));
                        tvIncompleteTime.setText(cursorprofile.getString(cursorprofile.getColumnIndex(DBUtility.PROFILE_CREATEDTIME)));
                        tvIncompleteSiteAddressTitleValue.setText(cursorprofile.getString(cursorprofile.getColumnIndex(DBUtility.PROFILE_SITENAME)));
                    }
                } finally {
                    cursorprofile.close();
                }
            } else {

            }
        } finally {
            cursor.close();
        }
    }

    private void bindid() {
        rLSurveyTop = (RelativeLayout) findViewById(R.id.RLSurveyTop);
        lLIncompleteMain = (LinearLayout) findViewById(R.id.LLIncompleteMain);
        imgCalendar = (ImageView) findViewById(R.id.imgCalendar);
        tvIncompleteDate = (TextView) findViewById(R.id.tvIncompleteDate);
        tvIncompleteSurveyName= (TextView) findViewById(R.id.tvIncompleteSurveyName);
        tvIncompleteSiteAddressTitle= (TextView) findViewById(R.id.tvIncompleteSiteAddressTitle);
        tvIncompleteSiteAddressTitleValue= (TextView) findViewById(R.id.tvIncompleteSiteAddressTitleValue);
        imgWatch = (ImageView) findViewById(R.id.imgWatch);
        tvIncompleteTime = (TextView) findViewById(R.id.tvIncompleteTime);
        imgIncompleteImage = (ImageView) findViewById(R.id.imgIncompleteImage);
        imgIncompleteImage.setOnClickListener(this);
        imgdiscard=(ImageView)findViewById(R.id.imgdiscard);
        rlAuditSite=(RelativeLayout)findViewById(R.id.rlAuditSite);
        rlAuditDate=(RelativeLayout)findViewById(R.id.rlAuditDate);
        llAuditName=(LinearLayout) findViewById(R.id.llAuditName);

        imgdiscard.setOnClickListener(this);
        //lLIncompleteMain.setOnClickListener(this);
        rlAuditSite.setOnClickListener(this);
        rlAuditDate.setOnClickListener(this);
        llAuditName.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlAuditSite:
                MainActivity.showProgress(InCompleteSurveyActivity.this);
                startActivity(new Intent(InCompleteSurveyActivity.this,SelectQuestionGroupActivity.class));
                finish();
                break;

            case R.id.rlAuditDate:
               rlAuditSite.performClick();
                break;

            case R.id.llAuditName:
                rlAuditSite.performClick();
                break;

            case R.id.imgIncompleteImage:
                MainActivity.showProgress(InCompleteSurveyActivity.this);
                startActivity(new Intent(InCompleteSurveyActivity.this,SelectQuestionGroupActivity.class));
                finish();
                break;

            case R.id.imgdiscard:
              /*dbHelper.truncateUserAnswer(ProfileId);
                dbHelper.truncateSurveyProfile(ProfileId);
                dbHelper.truncateSurveyPhotos(ProfileId);
                MainActivity.showProgress(InCompleteSurveyActivity.this);
                startActivity(new Intent(InCompleteSurveyActivity.this,SelectSurveyActivity.class));
                finish();*/
                Dialog();
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

    private void Dialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(InCompleteSurveyActivity.this);
        //alertDialog.setTitle("Confirm Delete");
        alertDialog.setMessage("Are you sure you want to discard this Audit?");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                dbHelper.truncateUserAnswer(ProfileId);
                dbHelper.truncateSurveyProfile(ProfileId);
                dbHelper.truncateSurveyPhotos(ProfileId);

                Utils.setSharedPreString(InCompleteSurveyActivity.this, Utils.PREFS_PROFILEID, "");
                Utils.setSharedPreString(InCompleteSurveyActivity.this, Utils.PREFS_SURVEYNAME, "");
                Utils.setSharedPreString(InCompleteSurveyActivity.this, Utils.PREFS_SURVEYID, "");
                Utils.setSharedPreString(InCompleteSurveyActivity.this, Utils.PREFS_GROUPID, "");
                Utils.setSharedPreString(InCompleteSurveyActivity.this, Utils.PREFS_GROUPNAME, "");

                /* amitk 3-1-17 drawrer position */
                Utils.setSharedPreString(InCompleteSurveyActivity.this, Utils.PREFS_DRAWEREXPANDNAME, "");
                Utils.setSharedPreString(InCompleteSurveyActivity.this, Utils.PREFS_DRAWEREXPANDPOSITION, "");

                //Manisha20-5-20
                Utils.setSharedPreString(InCompleteSurveyActivity.this, Utils.PREFS_PROFILE_AUDIT_DATE, "");
                Utils.setSharedPreString(InCompleteSurveyActivity.this, Utils.PREFS_PROFILECOMPLETED, "");

                MainActivity.showProgress(InCompleteSurveyActivity.this);
                startActivity(new Intent(InCompleteSurveyActivity.this,SelectSurveyActivity.class));
                finish();
                dialog.cancel();
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //   Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
}
