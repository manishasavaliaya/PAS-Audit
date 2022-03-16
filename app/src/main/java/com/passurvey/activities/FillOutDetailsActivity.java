package com.passurvey.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.passurvey.R;
import com.passurvey.adapters.CommentsAdapter;
import com.passurvey.database.DBUtility;
import com.passurvey.model.CommentsModel;
import com.passurvey.model.ProfileModel;
import com.passurvey.utility.DividerItemDecoration;
import com.passurvey.utility.RecyclerItemClickListener;
import com.passurvey.utility.TextViewUndoRedo;
import com.passurvey.utility.ToggleButtonGroupTableLayout;
import com.passurvey.utility.Utils;
import java.util.ArrayList;

public class FillOutDetailsActivity extends MainActivity implements View.OnClickListener {
    private RelativeLayout rLFillFirst, RLFillThird, RLClientComment;
    private TextView tvContractorsPerfomance;
    private EditText edtContractorsPerfomance;
    /*22-10*/
    // KnifeText edtContractorsPerfomance;
    private TextView tvStatutoryAuditScore;
    private ToggleButtonGroupTableLayout rGonetofive;
    //  private RadioGroup rGsixtoten;
    private TextView tvContractorneedimprovement;
    private EditText edtContractorneedimprovement;
    private Button btnFillNext;
    private RelativeLayout rLFillSecond;
    private TextView tvContractorsComment;

    /*22-10*/
    //private KnifeText edtContractorsComment;
    private EditText edtContractorsComment, edtStatutorycomments, edtClientComment;
    //private KnifeText edtContractorsComment;
    private Button btnShowFirstBack, btnshowThird, btnShowSecondBack, btnFillFinish1, btnClientBack, btnClientNext;
    //btnFillFinish
    RadioButton checkedRadioButtononetofive;
    Cursor cursor;
    String ProfileId, UserID, SurveyID;

    private LinearLayout LLCCSelectComments, LLCPSelectComments, LLClientSelectComments;
    Dialog dialog;
    private RecyclerView RVDialogDefaultAns;
    ArrayList<CommentsModel> CommentsList;
    CommentsAdapter adapter;
    public ImageView img_undo, img_Redo, img_UndoContraComment, img_redoContraComment, img_UndoClientComment, img_redoClientComment;
    /*Undo redo*/
    TextViewUndoRedo mTextViewUndoRedo,mTextViewUndoRedoConsultantComment,mTextViewUndoRedoClientComment;

    @Override
    protected void onPause() {
        super.onPause();
        // handler.removeCallbacks(r);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /// This is the right file to do code
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.container_body); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.fragment_fill_out_details, contentFrameLayout);
        if (savedInstanceState != null) {
            mTextViewUndoRedo = (TextViewUndoRedo) savedInstanceState.getSerializable("mTextViewUndoRedo");
            mTextViewUndoRedoConsultantComment = (TextViewUndoRedo) savedInstanceState.getSerializable("mTextViewUndoRedo");
            mTextViewUndoRedoClientComment = (TextViewUndoRedo) savedInstanceState.getSerializable("mTextViewUndoRedo");
        }

        bindid();
        ProfileId = Utils.getSharedPreString(FillOutDetailsActivity.this, Utils.PREFS_PROFILEID);
        UserID = Utils.getSharedPreString(FillOutDetailsActivity.this, Utils.PREFS_USERID);
        SurveyID = Utils.getSharedPreString(FillOutDetailsActivity.this, Utils.PREFS_SURVEYID);
        cursor = dbHelper.getUserInfo(SurveyID, UserID, ProfileId);

        // First edtContractorsPerfomance
        // second edtContractorsComment
        if (cursor != null) {
            cursor.moveToFirst();
        }
        try {
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_CONTRACTORSPERFOMANCE)).toString().length() > 1) {
                    edtContractorsPerfomance.setText(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_CONTRACTORSPERFOMANCE)));
                }
                if (cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_CONTRACTORIMPROVEPERFOMANCE)).toString().length() > 1) {
                    edtContractorneedimprovement.setText(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_CONTRACTORIMPROVEPERFOMANCE)));
                }
                if (cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_CONSULTANTSCOMMENTS)).toString().length() > 1) {
                    edtContractorsComment.setText(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_CONSULTANTSCOMMENTS)));
                }
                if (cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_CLIENTCOMMENTS)).toString().length() > 1) {
                    edtClientComment.setText(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_CLIENTCOMMENTS)));
                }

//                if (cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_CONSULTANTSCOMMENTS)).toString().length() > 1) {
//                    edtContractorsComment.setText(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_CONSULTANTSCOMMENTS)));
//                }

                if (cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_STATUTORYCOMMENTS)) != null && cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_STATUTORYCOMMENTS)).toString().length() > 1) {
                    edtStatutorycomments.setText(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_STATUTORYCOMMENTS)));
                }
                if (cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_CLIENTCOMMENTS)) != null && cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_CLIENTCOMMENTS)).toString().length() > 1) {
                    edtClientComment.setText(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_CLIENTCOMMENTS)));
                }

           /* int AuditScore = cursor.getInt(cursor.getColumnIndex(DBUtility.PROFILE_STATUTOORYAUDITSCORE));
            if (AuditScore == 1) {
                rGonetofive.setSelected(true);
                rGonetofive.onClick(rGonetofive.findViewById(R.id.rad1));
                ((RadioButton) rGonetofive.findViewById(R.id.rad1)).setChecked(true);
            } else if (AuditScore == 2) {
                rGonetofive.onClick(rGonetofive.findViewById(R.id.rad2));
                ((RadioButton) rGonetofive.findViewById(R.id.rad2)).setChecked(true);
            } else if (AuditScore == 3) {
                rGonetofive.onClick(rGonetofive.findViewById(R.id.rad3));
                ((RadioButton) rGonetofive.findViewById(R.id.rad3)).setChecked(true);
            } else if (AuditScore == 4) {
                rGonetofive.onClick(rGonetofive.findViewById(R.id.rad4));
                ((RadioButton) rGonetofive.findViewById(R.id.rad4)).setChecked(true);
            } else if (AuditScore == 5) {
                rGonetofive.onClick(rGonetofive.findViewById(R.id.rad5));
                ((RadioButton) rGonetofive.findViewById(R.id.rad5)).setChecked(true);
            } else if (AuditScore == 6) {
                rGonetofive.onClick(rGonetofive.findViewById(R.id.rad6));
                ((RadioButton) rGonetofive.findViewById(R.id.rad6)).setChecked(true);
            } else if (AuditScore == 7) {
                rGonetofive.onClick(rGonetofive.findViewById(R.id.rad7));
                ((RadioButton) rGonetofive.findViewById(R.id.rad7)).setChecked(true);
            } else if (AuditScore == 8) {
                rGonetofive.onClick(rGonetofive.findViewById(R.id.rad8));
                ((RadioButton) rGonetofive.findViewById(R.id.rad8)).setChecked(true);
            } else if (AuditScore == 9) {
                rGonetofive.onClick(rGonetofive.findViewById(R.id.rad9));
                ((RadioButton) rGonetofive.findViewById(R.id.rad9)).setChecked(true);
            } else if (AuditScore == 10) {
                rGonetofive.onClick(rGonetofive.findViewById(R.id.rad10));
                ((RadioButton) rGonetofive.findViewById(R.id.rad10)).setChecked(true);
            }else if (AuditScore == 11) {
                rGonetofive.onClick(rGonetofive.findViewById(R.id.rad11));
                ((RadioButton) rGonetofive.findViewById(R.id.rad11)).setChecked(true);
            }else if (AuditScore == 12) {
                rGonetofive.onClick(rGonetofive.findViewById(R.id.rad12));
                ((RadioButton) rGonetofive.findViewById(R.id.rad12)).setChecked(true);
            }else if (AuditScore == 13) {
                rGonetofive.onClick(rGonetofive.findViewById(R.id.rad13));
                ((RadioButton) rGonetofive.findViewById(R.id.rad13)).setChecked(true);
            }else if (AuditScore == 14) {
                rGonetofive.onClick(rGonetofive.findViewById(R.id.rad14));
                ((RadioButton) rGonetofive.findViewById(R.id.rad14)).setChecked(true);
            }
*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
    }

    private void bindid() {
        setHeaderTitle(getString(R.string.title_summary));
        rLFillFirst = (RelativeLayout) findViewById(R.id.RLFillFirst);
        tvContractorsPerfomance = (TextView) findViewById(R.id.tvContractorsPerfomance);
        //edtContractorsPerfomance = (EditText) findViewById(R.id.edtContractorsPerfomance);
        /*22-10*/
        edtContractorsPerfomance = (EditText) findViewById(R.id.edtContractorsPerfomance);
        tvStatutoryAuditScore = (TextView) findViewById(R.id.tvStatutoryAuditScore);
        rGonetofive = (ToggleButtonGroupTableLayout) findViewById(R.id.RGonetofive);
        // rGsixtoten = (RadioGroup) findViewById(R.id.RGsixtoten);
        tvContractorneedimprovement = (TextView) findViewById(R.id.tvContractorneedimprovement);
        //edtContractorneedimprovement = (EditText) findViewById(R.id.edtContractorneedimprovement);
        edtContractorneedimprovement = (EditText) findViewById(R.id.edtContractorneedimprovement);
        btnFillNext = (Button) findViewById(R.id.btnFillNext);
        rLFillSecond = (RelativeLayout) findViewById(R.id.RLFillSecond);
        tvContractorsComment = (TextView) findViewById(R.id.tvContractorsComment);
        /*22-10*/
        edtContractorsComment = (EditText) findViewById(R.id.edtContractorsComment);
        edtStatutorycomments = (EditText) findViewById(R.id.edtStatutorycomments);
        edtClientComment = (EditText) findViewById(R.id.edtClientComment);
        //btnFillFinish = (Button) findViewById(R.id.btnFillFinish);
        RLFillThird = (RelativeLayout) findViewById(R.id.RLFillThird);
        RLClientComment = (RelativeLayout) findViewById(R.id.RLFillClient);
        checkedRadioButtononetofive = (RadioButton) rGonetofive.findViewById(rGonetofive.getCheckedRadioButtonId());
        // checkedRadioButtonsixtoten= (RadioButton)rGsixtoten.findViewById(rGsixtoten.getCheckedRadioButtonId());
        btnFillNext.setOnClickListener(this);
        // btnFillFinish.setOnClickListener(this);
        showfirst();

        btnShowFirstBack = (Button) findViewById(R.id.btnShowFirstBack);
        //  btnshowThird = (Button) findViewById(R.id.btnshowThird);
        btnShowSecondBack = (Button) findViewById(R.id.btnShowSecondBack);
        btnFillFinish1 = (Button) findViewById(R.id.btnFillFinish1);
        btnClientNext = (Button) findViewById(R.id.btnClientNext);
        btnClientBack = (Button) findViewById(R.id.btnClientBack);
        btnShowFirstBack.setOnClickListener(this);
        btnClientBack.setOnClickListener(this);
        btnClientNext.setOnClickListener(this);
        // new change 03-01-2017 mena vekariya
        // btnshowThird.setOnClickListener(this);
        // new change 03-01-2017 mena vekariya
        btnShowSecondBack.setOnClickListener(this);
        btnFillFinish1.setOnClickListener(this);
        rGonetofive.getCheckedRadioButtonId();

        /*9-8*/
        LLCCSelectComments = (LinearLayout) findViewById(R.id.LLCCSelectComments);
        LLCPSelectComments = (LinearLayout) findViewById(R.id.LLCPSelectComments);
        LLClientSelectComments = (LinearLayout) findViewById(R.id.LLClientSelectComments);
        LLCCSelectComments.setOnClickListener(this);
        LLCPSelectComments.setOnClickListener(this);
        LLClientSelectComments.setOnClickListener(this);
        img_undo = (ImageView) findViewById(R.id.img_UndoContractorneedimprovement);
        img_Redo = (ImageView) findViewById(R.id.img_RedoContractorneedimprovement);
        img_UndoContraComment = (ImageView) findViewById(R.id.img_UndoContractorsComment);
        img_redoContraComment = (ImageView) findViewById(R.id.img_RedoContractorsComment);
        img_UndoClientComment = (ImageView) findViewById(R.id.img_UndoClientComment);
        img_redoClientComment = (ImageView) findViewById(R.id.img_RedoClientComment);

        //First edtContractorsPerfomance
        //Second
        /*Undo redo*/
        if (mTextViewUndoRedo == null) {
            mTextViewUndoRedo = new TextViewUndoRedo(edtContractorsPerfomance);
        } else {
            mTextViewUndoRedo.TextViewUndoRedoSavedState(edtContractorsPerfomance);
        }

        if (mTextViewUndoRedoConsultantComment == null) {
            mTextViewUndoRedoConsultantComment = new TextViewUndoRedo(edtContractorsComment);
        } else {
            mTextViewUndoRedo.TextViewUndoRedoSavedState(edtContractorsComment);
        }

        if (mTextViewUndoRedoClientComment == null) {
            mTextViewUndoRedoClientComment = new TextViewUndoRedo(edtClientComment);
        } else {
            mTextViewUndoRedoClientComment.TextViewUndoRedoSavedState(edtClientComment);
        }

        img_undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextViewUndoRedo.undo();
            }
        });
        img_Redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextViewUndoRedo.redo();
            }
        });

//        /*Undo redo ContractersComment*/
//        if (mTextViewUndoRedo==null) {
//            mTextViewUndoRedo = new TextViewUndoRedo(edtContractorsComment);
//        }
//        else
//        {
//            mTextViewUndoRedo.TextViewUndoRedoSavedState(edtContractorsComment);
//        }
        img_UndoContraComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextViewUndoRedoConsultantComment.undo();
            }
        });

        img_redoContraComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextViewUndoRedoConsultantComment.redo();
            }
        });

//        /*Undo redo*/
//        if (mTextViewUndoRedo==null) {
//            mTextViewUndoRedo = new TextViewUndoRedo(edtClientComment);
//        }
//        else
//        {
//            mTextViewUndoRedo.TextViewUndoRedoSavedState(edtClientComment);
//        }

        img_UndoClientComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextViewUndoRedoClientComment.undo();
            }
        });
        img_redoClientComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextViewUndoRedoClientComment.redo();
            }
        });

        edtContractorsPerfomance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if( edtContractorsPerfomance.getText().length()>0)
                {
                    edtContractorsPerfomance.setError(null);
                }
            }
        });
        edtContractorsComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if( edtContractorsComment.getText().length()>0)
                {
                    edtContractorsComment.setError(null);
                }
            }
        });

        edtClientComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if( edtClientComment.getText().length()>0) {
                    edtClientComment.setError(null);
                }
            }
        });

       /* rGonetofive.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked)
                {
                    // Changes the textview's text to "Checked: example radiobutton text"
                    //tv.setText("Checked:" + checkedRadioButton.getText());
                    Utils.showToast(getApplicationContext(),"Checked:" + checkedRadioButton.getText());
                }
            }
        });*/
       /* setupBold();
        setupItalic();
        setupUnderline();
        setupStrikethrough();
        setupBullet();
        setupQuote();
        setupLink();
        setupClear();*/

      /*  setupBoldi();
        setupItalici();
        setupUnderlinei();
        setupStrikethroughi();
        setupBulleti();
        setupQuotei();
        setupLinki();
        setupCleari();*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFillNext:
//                mTextViewUndoRedo = null;
//                if (mTextViewUndoRedo == null) {
//                    mTextViewUndoRedo = new TextViewUndoRedo(edtContractorsComment);
//                } else {
//                    mTextViewUndoRedo.TextViewUndoRedoSavedState(edtContractorsComment);
//                }
                if(edtContractorsComment.getText().toString().trim().length()>0){
                    edtContractorsComment.setError(null);
                }
//                img_UndoContraComment.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mTextViewUndoRedo.undo();
//                    }
//                });
//
//               img_redoContraComment.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mTextViewUndoRedo.redo();
//                    }
//                });

                if (edtContractorsPerfomance.getText().toString().equalsIgnoreCase("")) {
                    edtContractorsPerfomance.setError(getString(R.string.contractorperfomance_error));
                } /*else if (edtContractorneedimprovement.getText().toString().equalsIgnoreCase("")) {
                    edtContractorneedimprovement.setError(getString(R.string.contractorsneedimprove_error));
                }*/ /*else if (rGonetofive.getCheckedRadioButtonId() <= 0) {
                    Utils.showToast(FillOutDetailsActivity.this, getString(R.string.statutoryaudit_error));
                } */ else {
                    edtContractorsPerfomance.setError(null);
                    ProfileId = Utils.getSharedPreString(FillOutDetailsActivity.this, Utils.PREFS_PROFILEID);
                    UserID = Utils.getSharedPreString(FillOutDetailsActivity.this, Utils.PREFS_USERID);
                    SurveyID = Utils.getSharedPreString(FillOutDetailsActivity.this, Utils.PREFS_SURVEYID);
                    ProfileModel mprofileModel = new ProfileModel();
                    mprofileModel.setProfileID(ProfileId);
                    mprofileModel.setUserID(UserID);
                    mprofileModel.setSurveyID(SurveyID);

                    // mprofileModel.setFund(edtProfilefund.getText().toString());
                    // mprofileModel.setCOManagingAgent(edtProfileCoManaging.getText().toString());
                    // mprofileModel.setManagementSurveor(edtProfileManagingSurveyour.getText().toString());
                    // mprofileModel.setFacilitiesManager(edtFacilitiesManager.getText().toString());
                    // mprofileModel.setSiteAddress(edtProfileSiteAddress.getText().toString());
                    // mprofileModel.setReportPreparedby(edtProfileReportPreparedby.getText().toString());
                    // mprofileModel.setReportCheckedby(edtProfileReportCheckedby.getText().toString());
                    // mprofileModel.setAuditDate(edtProfieAuditDate.getText().toString());
                    // mprofileModel.setDateofIssue(edtProfieDateOfIssue.getText().toString());
                    // mprofileModel.setAuditVisit(((RadioButton)findViewById(RGonetofive.getCheckedRadioButtonId())).getText().toString());
                    mprofileModel.setContractorsPerfomance(edtContractorsPerfomance.getText().toString());
                    //  Utils.showToast(FillOutDetailsActivity.this,((RadioButton)findViewById(rGonetofive.getCheckedRadioButtonId())).getText().toString());
                    //comment by mena 30-01-2017
                    /*
                    mprofileModel.setStatutoryAuditScore(((RadioButton) findViewById(rGonetofive.getCheckedRadioButtonId())).getText().toString());
*/
                    mprofileModel.setContractorImprovePerfomance(edtContractorneedimprovement.getText().toString());
                    //  mprofileModel.setConsultantsComments(edtContractorsComment.getText().toString());
                    //mprofileModel.setCreatedDate(Utils.getDate());
                    //mprofileModel.setCreatedTime(Utils.getTime());
                    //    String data =dbHelper.copyandstoreuserans(SurveyID,UserID,ProfileId);
                    //Utils.showToast(ProfileActivity.this,((RadioButton)findViewById(RGonetofive.getCheckedRadioButtonId())).getText().toString());
                    dbHelper.UpdateSummary(mprofileModel);
                    edtContractorsComment.requestFocus();
                    //Manisha
                    showsecond();
//                  showClient();
                }
                break;

            /*case R.id.btnshowThird:
                if (edtContractorsComment.getText().toString().equalsIgnoreCase("")) {
                    edtContractorsComment.setError(getString(R.string.consultantscoomment_error));
                } else {
                    ProfileId = Utils.getSharedPreString(FillOutDetailsActivity.this, Utils.PREFS_PROFILEID);
                    UserID = Utils.getSharedPreString(FillOutDetailsActivity.this, Utils.PREFS_USERID);
                    SurveyID = Utils.getSharedPreString(FillOutDetailsActivity.this, Utils.PREFS_SURVEYID);
                    ProfileModel mprofileModel = new ProfileModel();
                    mprofileModel.setProfileID(ProfileId);
                    mprofileModel.setUserID(UserID);
                    mprofileModel.setSurveyID(SurveyID);
                    //mprofileModel.setFund(edtProfilefund.getText().toString());
                    // mprofileModel.setCOManagingAgent(edtProfileCoManaging.getText().toString());
                    // mprofileModel.setManagementSurveor(edtProfileManagingSurveyour.getText().toString());
                    // mprofileModel.setFacilitiesManager(edtFacilitiesManager.getText().toString());
                    //  mprofileModel.setSiteAddress(edtProfileSiteAddress.getText().toString());
                    //mprofileModel.setReportPreparedby(edtProfileReportPreparedby.getText().toString());
                    //mprofileModel.setReportCheckedby(edtProfileReportCheckedby.getText().toString());
                    // mprofileModel.setAuditDate(edtProfieAuditDate.getText().toString());
                    // mprofileModel.setDateofIssue(edtProfieDateOfIssue.getText().toString());
                    // mprofileModel.setAuditVisit(((RadioButton)findViewById(RGonetofive.getCheckedRadioButtonId())).getText().toString());
                    mprofileModel.setContractorsPerfomance(edtContractorsPerfomance.getText().toString());
                    //  Utils.showToast(FillOutDetailsActivity.this,((RadioButton)findViewById(rGonetofive.getCheckedRadioButtonId())).getText().toString());
                    mprofileModel.setStatutoryAuditScore(((RadioButton) findViewById(rGonetofive.getCheckedRadioButtonId())).getText().toString());
                    mprofileModel.setContractorImprovePerfomance(edtContractorneedimprovement.getText().toString());
                    mprofileModel.setConsultantsComments(edtContractorsComment.getText().toString());
                    //mprofileModel.setCreatedDate(Utils.getDate());
                    //mprofileModel.setCreatedTime(Utils.getTime());
                    //    String data =dbHelper.copyandstoreuserans(SurveyID,UserID,ProfileId);
                    //Utils.showToast(ProfileActivity.this,((RadioButton)findViewById(RGonetofive.getCheckedRadioButtonId())).getText().toString());

                    dbHelper.UpdateSummary(mprofileModel);
                    edtStatutorycomments.requestFocus();
                    showthird();
                    *//*cursor = dbHelper.checkIncompleteSurveyLogin1(Utils.getSharedPreString(FillOutDetailsActivity.this, Utils.PREFS_USERID));
                    if (cursor != null && cursor.getCount() > 0) {
                        Utils.setSharedPreString(FillOutDetailsActivity.this, Utils.PREFS_SURVEYID, cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_ID)));
                        Utils.setSharedPreString(FillOutDetailsActivity.this, Utils.PREFS_PROFILEID, cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_ID)));
                        startActivity(new Intent(FillOutDetailsActivity.this, InCompleteSurveyActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(FillOutDetailsActivity.this, CompleteSurveyAndSync.class));
                        finish();
                    }*//*
             *//*
                    startActivity(new Intent(FillOutDetailsActivity.this, CompleteSurveyAndSync.class));
                    finish();*//*
                }
                break;*/
//            Manisha
//            case R.id.btnFillFinish1:

            case R.id.btnFillFinish1:
//                mTextViewUndoRedo = null;
//                if (mTextViewUndoRedo == null) {
//                    mTextViewUndoRedo = new TextViewUndoRedo(edtClientComment);
//                } else {
//                    mTextViewUndoRedo.TextViewUndoRedoSavedState(edtClientComment);
//                }
//                if(edtClientComment.getText().toString().trim().length()>0){
//                    edtClientComment.setError(null);
//                }
                hideKeyboard(edtContractorsComment);
                if (edtContractorsComment.getText().toString().equalsIgnoreCase("")) {
                    edtContractorsComment.setError(getString(R.string.consultantscoomment_error));
                } else {
                    edtContractorsComment.setError(null);
                    ProfileId = Utils.getSharedPreString(FillOutDetailsActivity.this, Utils.PREFS_PROFILEID);
                    UserID = Utils.getSharedPreString(FillOutDetailsActivity.this, Utils.PREFS_USERID);
                    SurveyID = Utils.getSharedPreString(FillOutDetailsActivity.this, Utils.PREFS_SURVEYID);
                    ProfileModel mprofileModel = new ProfileModel();
                    mprofileModel.setProfileID(ProfileId);
                    mprofileModel.setUserID(UserID);
                    mprofileModel.setSurveyID(SurveyID);
                    //mprofileModel.setFund(edtProfilefund.getText().toString());
                    // mprofileModel.setCOManagingAgent(edtProfileCoManaging.getText().toString());
                    // mprofileModel.setManagementSurveor(edtProfileManagingSurveyour.getText().toString());
                    // mprofileModel.setFacilitiesManager(edtFacilitiesManager.getText().toString());
                    //  mprofileModel.setSiteAddress(edtProfileSiteAddress.getText().toString());
                    //mprofileModel.setReportPreparedby(edtProfileReportPreparedby.getText().toString());
                    //mprofileModel.setReportCheckedby(edtProfileReportCheckedby.getText().toString());
                    // mprofileModel.setAuditDate(edtProfieAuditDate.getText().toString());
                    // mprofileModel.setDateofIssue(edtProfieDateOfIssue.getText().toString());
                    // mprofileModel.setAuditVisit(((RadioButton)findViewById(RGonetofive.getCheckedRadioButtonId())).getText().toString());
                    mprofileModel.setContractorsPerfomance(edtContractorsPerfomance.getText().toString());
                    //  Utils.showToast(FillOutDetailsActivity.this,((RadioButton)findViewById(rGonetofive.getCheckedRadioButtonId())).getText().toString());
                    //comment by mena 30-01-2017
/*
                    mprofileModel.setStatutoryAuditScore(((RadioButton) findViewById(rGonetofive.getCheckedRadioButtonId())).getText().toString());
*/
                    mprofileModel.setContractorImprovePerfomance(edtContractorneedimprovement.getText().toString());
                    mprofileModel.setConsultantsComments(edtContractorsComment.getText().toString());
                    mprofileModel.setStatutoryComments(edtStatutorycomments.getText().toString());
                    // mprofileModel.setCreatedDate(Utils.getDate());
                    // mprofileModel.setCreatedTime(Utils.getTime());
                    // String data =dbHelper.copyandstoreuserans(SurveyID,UserID,ProfileId);
                    // Utils.showToast(ProfileActivity.this,((RadioButton)findViewById(RGonetofive.getCheckedRadioButtonId())).getText().toString());

                    dbHelper.UpdateSummary(mprofileModel);
                    edtClientComment.requestFocus();
                    showClient();
                }
                break;

            case R.id.btnShowFirstBack:
                showfirst();
                break;

            case R.id.btnShowSecondBack:
                showClient();
//              showsecond();
                break;

            case R.id.btnClientBack:
                if(edtContractorsComment.getText().toString().trim().length()>0){
                    edtContractorsComment.setError(null);
                }
                showsecond();
                break;

            case R.id.btnClientNext:
                hideKeyboard(edtClientComment);
                if (edtClientComment.getText().toString().equalsIgnoreCase("")) {
                    edtClientComment.setError(getString(R.string.clientcoomment_error));

                } else {
                    edtClientComment.setError(null);
                    ProfileId = Utils.getSharedPreString(FillOutDetailsActivity.this, Utils.PREFS_PROFILEID);
                    UserID = Utils.getSharedPreString(FillOutDetailsActivity.this, Utils.PREFS_USERID);
                    SurveyID = Utils.getSharedPreString(FillOutDetailsActivity.this, Utils.PREFS_SURVEYID);
                    ProfileModel mprofileModel = new ProfileModel();
                    mprofileModel.setProfileID(ProfileId);
                    mprofileModel.setUserID(UserID);
                    mprofileModel.setSurveyID(SurveyID);

                    // mprofileModel.setFund(edtProfilefund.getText().toString());
                    // mprofileModel.setCOManagingAgent(edtProfileCoManaging.getText().toString());
                    // mprofileModel.setManagementSurveor(edtProfileManagingSurveyour.getText().toString());
                    // mprofileModel.setFacilitiesManager(edtFacilitiesManager.getText().toString());
                    // mprofileModel.setSiteAddress(edtProfileSiteAddress.getText().toString());
                    // mprofileModel.setReportPreparedby(edtProfileReportPreparedby.getText().toString());
                    // mprofileModel.setReportCheckedby(edtProfileReportCheckedby.getText().toString());
                    // mprofileModel.setAuditDate(edtProfieAuditDate.getText().toString());
                    // mprofileModel.setDateofIssue(edtProfieDateOfIssue.getText().toString());
                    // mprofileModel.setAuditVisit(((RadioButton)findViewById(RGonetofive.getCheckedRadioButtonId())).getText().toString());
                    mprofileModel.setContractorsPerfomance(edtContractorsPerfomance.getText().toString());
                    //  Utils.showToast(FillOutDetailsActivity.this,((RadioButton)findViewById(rGonetofive.getCheckedRadioButtonId())).getText().toString());
                    //comment by mena 30-01-2017
/*
                    mprofileModel.setStatutoryAuditScore(((RadioButton) findViewById(rGonetofive.getCheckedRadioButtonId())).getText().toString());
*/
                    mprofileModel.setContractorImprovePerfomance(edtContractorneedimprovement.getText().toString());
                    mprofileModel.setConsultantsComments(edtContractorsComment.getText().toString());
                    mprofileModel.setClientsComments(edtClientComment.getText().toString());
                    mprofileModel.setStatutoryComments(edtStatutorycomments.getText().toString());
                    //mprofileModel.setCreatedDate(Utils.getDate());
                    //mprofileModel.setCreatedTime(Utils.getTime());
                    //String data =dbHelper.copyandstoreuserans(SurveyID,UserID,ProfileId);
                    //Utils.showToast(ProfileActivity.this,((RadioButton)findViewById(RGonetofive.getCheckedRadioButtonId())).getText().toString());

                    dbHelper.UpdateSummary(mprofileModel);
                    cursor = dbHelper.checkIncompleteSurveyLogin1(Utils.getSharedPreString(FillOutDetailsActivity.this, Utils.PREFS_USERID));
                    if (cursor != null && cursor.getCount() > 0) {
                        Utils.setSharedPreString(FillOutDetailsActivity.this, Utils.PREFS_SURVEYID, cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_ID)));
                        Utils.setSharedPreString(FillOutDetailsActivity.this, Utils.PREFS_PROFILEID, cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_ID)));
                        startActivity(new Intent(FillOutDetailsActivity.this, InCompleteSurveyActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(FillOutDetailsActivity.this, CompleteSurveyAndSync.class));
                        finish();
                    }
                }
                break;
/*
              startActivity(new Intent(FillOutDetailsActivity.this, CompleteSurveyAndSync.class));
              finish();*/
            /*9-8*/

            case R.id.LLCPSelectComments:
                selectPerfomanceComments();
                break;

            case R.id.LLCCSelectComments:
                selectComments();
                break;

            case R.id.LLClientSelectComments:
                selectClientComments();
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

    public void showfirst() {
        rLFillFirst.setVisibility(View.VISIBLE);
        rLFillSecond.setVisibility(View.GONE);
        RLFillThird.setVisibility(View.GONE);
        //Manisha 27-02-20
        RLClientComment.setVisibility(View.GONE);
    }

    public void showsecond() {
        rLFillFirst.setVisibility(View.GONE);
        rLFillSecond.setVisibility(View.VISIBLE);
        RLFillThird.setVisibility(View.GONE);
        //Manisha 27-02-20
        RLClientComment.setVisibility(View.GONE);
    }

    public void showthird() {
        RLFillThird.setVisibility(View.VISIBLE);
        rLFillSecond.setVisibility(View.GONE);
        rLFillFirst.setVisibility(View.GONE);
        //Manisha 27-02-20
        RLClientComment.setVisibility(View.GONE);
    }

    public void showClient() {
        RLFillThird.setVisibility(View.GONE);
        rLFillSecond.setVisibility(View.GONE);
        rLFillFirst.setVisibility(View.GONE);
        // Manisha 27-02-20
        RLClientComment.setVisibility(View.VISIBLE);
    }

    public void selectComments() {
        try {
            dialog = new Dialog(FillOutDetailsActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog_default_answer);
            TextView TVTitleName = (TextView) dialog.findViewById(R.id.TVTitleName);
            TVTitleName.setText(getString(R.string.Select_comment));
            RVDialogDefaultAns = (RecyclerView) dialog.findViewById(R.id.RVDialogDefaultAns);
            setDefaultdata();
            dialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setDefaultdata() {
        try {
            CommentsList = new ArrayList<>();
            CommentsList = dbHelper.getCommentsModelArrayList(Integer.parseInt(SurveyID));
            if (CommentsList != null) {
                adapter = new CommentsAdapter(CommentsList);

                LinearLayoutManager llm = new LinearLayoutManager(FillOutDetailsActivity.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);

                RVDialogDefaultAns.setLayoutManager(llm);
                // RVQuestionGroup.addItemDecoration(new DividerItemDecoration(getActivity()));
                /*or*/
                RVDialogDefaultAns.addItemDecoration(new DividerItemDecoration(FillOutDetailsActivity.this, R.drawable.divider));
                RVDialogDefaultAns.setAdapter(adapter);

                RVDialogDefaultAns.addOnItemTouchListener(
                        new RecyclerItemClickListener(FillOutDetailsActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                // TODO Handle item click
                                if (edtContractorsComment.getText().toString().length()>0) {
                                    edtContractorsComment.setError(null);
                                    edtContractorsComment.setText(edtContractorsComment.getText().toString() + "\n" + ((TextView) view.findViewById(R.id.tvDefaultAnswer)).getText());
                                }
                                else {
                                    edtContractorsComment.setText(((TextView) view.findViewById(R.id.tvDefaultAnswer)).getText());
                                }
                                dialog.dismiss();
                                Handler mHandler = new Handler();
                                mHandler.post(new DisplayKey(FillOutDetailsActivity.this));
                                // edtContractorsComment.setText((String) ((TextView) view.findViewById(R.id.tvDefaultAnswer)).getText());
                                edtContractorsComment.setSelection(edtContractorsComment.getEditableText().length());
                            }
                        })
                );
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /* 10-8-2017*/
    public void selectPerfomanceComments() {
        try {
            dialog = new Dialog(FillOutDetailsActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog_default_answer);
            TextView TVTitleName = (TextView) dialog.findViewById(R.id.TVTitleName);
            TVTitleName.setText(getString(R.string.Select_comment));
            RVDialogDefaultAns = (RecyclerView) dialog.findViewById(R.id.RVDialogDefaultAns);

            setDefaultdataPerfomance();

            dialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setDefaultdataPerfomance() {
        try {
            CommentsList = new ArrayList<>();
            CommentsList = dbHelper.getPerfomanceCommentsModelArrayList(Integer.parseInt(SurveyID));
            if (CommentsList != null) {
                adapter = new CommentsAdapter(CommentsList);

                LinearLayoutManager llm = new LinearLayoutManager(FillOutDetailsActivity.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);

                RVDialogDefaultAns.setLayoutManager(llm);
                //  RVQuestionGroup.addItemDecoration(new DividerItemDecoration(getActivity()));
                /*or*/
                RVDialogDefaultAns.addItemDecoration(new DividerItemDecoration(FillOutDetailsActivity.this, R.drawable.divider));
                RVDialogDefaultAns.setAdapter(adapter);

                RVDialogDefaultAns.addOnItemTouchListener(
                        new RecyclerItemClickListener(FillOutDetailsActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                // TODO Handle item click
                                if (edtContractorsPerfomance.getText().toString().length()>0) {
                                    edtContractorsPerfomance.setError(null);
                                    edtContractorsPerfomance.setText(edtContractorsPerfomance.getText().toString() + "\n" + ((TextView) view.findViewById(R.id.tvDefaultAnswer)).getText());
                                }
                                else
                                {
                                    edtContractorsPerfomance.setText(((TextView) view.findViewById(R.id.tvDefaultAnswer)).getText());
                                }
                                dialog.dismiss();
                                Handler mHandler = new Handler();
                                mHandler.post(new DisplayKey(FillOutDetailsActivity.this));

//                              edtContractorsPerfomance.setText((String) ((TextView) view.findViewById(R.id.tvDefaultAnswer)).getText());
                                // edtContractorsPerfomance.getEditableText().length();
                                edtContractorsPerfomance.setSelection(edtContractorsPerfomance.getEditableText().length());
                            }
                        })
                );
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /* 10-8-2017*/
    public void selectClientComments() {
        try {
            dialog = new Dialog(FillOutDetailsActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog_default_answer);
            TextView TVTitleName = (TextView) dialog.findViewById(R.id.TVTitleName);
            TVTitleName.setText(getString(R.string.Select_comment));
            RVDialogDefaultAns = (RecyclerView) dialog.findViewById(R.id.RVDialogDefaultAns);

            setDefaultdataClient();

            dialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setDefaultdataClient() {
        try {
            CommentsList = new ArrayList<>();
            CommentsList = dbHelper.getClientCommentsModelArrayList(Integer.parseInt(SurveyID));
            if (CommentsList != null) {
                adapter = new CommentsAdapter(CommentsList);

                LinearLayoutManager llm = new LinearLayoutManager(FillOutDetailsActivity.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);

                RVDialogDefaultAns.setLayoutManager(llm);
                //  RVQuestionGroup.addItemDecoration(new DividerItemDecoration(getActivity()));
                /*or*/
                RVDialogDefaultAns.addItemDecoration(new DividerItemDecoration(FillOutDetailsActivity.this, R.drawable.divider));
                RVDialogDefaultAns.setAdapter(adapter);

                RVDialogDefaultAns.addOnItemTouchListener(
                        new RecyclerItemClickListener(FillOutDetailsActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                // TODO Handle item click
                                if (edtClientComment.getText().toString().length()>0) {
                                    edtClientComment.setError(null);
                                    edtClientComment.setText(edtClientComment.getText().toString() + "\n" + ((TextView) view.findViewById(R.id.tvDefaultAnswer)).getText());
                                }
                                else
                                {
                                    edtClientComment.setText(((TextView) view.findViewById(R.id.tvDefaultAnswer)).getText());
                                }

                                dialog.dismiss();
                                Handler mHandler = new Handler();
                                mHandler.post(new DisplayKey(FillOutDetailsActivity.this));

//                              edtClientComment.setText((String) ((TextView) view.findViewById(R.id.tvDefaultAnswer)).getText());
                                edtClientComment.setSelection(edtClientComment.getEditableText().length());

                            }
                        })
                );
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public class DisplayKey implements Runnable {
        private final Context mContext;

        public DisplayKey(Context mContext) {
            this.mContext = mContext;
        }

        public void run() {
            // Toast.makeText(mContext, "Show code keyboard", Toast.LENGTH_SHORT).show();
            if (rLFillFirst.getVisibility() == View.VISIBLE) {
                hidekeybooard(edtContractorsPerfomance);
                showkeyboard(edtContractorsPerfomance);
                edtContractorsPerfomance.setSelection(edtContractorsPerfomance.getEditableText().length());

            } else if (rLFillSecond.getVisibility() == View.VISIBLE) {
                hidekeybooard(edtContractorsComment);
                showkeyboard(edtContractorsComment);
                edtContractorsComment.setSelection(edtContractorsComment.getEditableText().length());

            } else if (RLClientComment.getVisibility() == View.VISIBLE) {
                hidekeybooard(edtClientComment);
                showkeyboard(edtClientComment);
                edtClientComment.setSelection(edtClientComment.getEditableText().length());
            }
        }
    }
}

/*For Rich Textbox*/
/*22-10*/
    /*private void setupBold() {
        ImageButton bold = (ImageButton) findViewById(R.id.bold);
        ImageButton bold1 = (ImageButton) findViewById(R.id.bold1);

        bold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtContractorsPerfomance.bold(!edtContractorsPerfomance.contains(KnifeText.FORMAT_BOLD));
            }
        });

        bold.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(FillOutDetailsActivity.this, R.string.toast_bold, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        bold1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtContractorsPerfomance.bold(!edtContractorsPerfomance.contains(KnifeText.FORMAT_BOLD));
            }
        });

        bold1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(FillOutDetailsActivity.this, R.string.toast_bold, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupItalic() {
        ImageButton italic = (ImageButton) findViewById(R.id.italic);


        italic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtContractorsPerfomance.italic(!edtContractorsPerfomance.contains(KnifeText.FORMAT_ITALIC));
            }
        });

        italic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(FillOutDetailsActivity.this, R.string.toast_italic, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        ImageButton italic1 = (ImageButton) findViewById(R.id.italic1);


        italic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtContractorsPerfomance.italic(!edtContractorsPerfomance.contains(KnifeText.FORMAT_ITALIC));
            }
        });

        italic1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(FillOutDetailsActivity.this, R.string.toast_italic, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupUnderline() {
        ImageButton underline = (ImageButton) findViewById(R.id.underline);

        underline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtContractorsPerfomance.underline(!edtContractorsPerfomance.contains(KnifeText.FORMAT_UNDERLINED));
            }
        });

        underline.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(FillOutDetailsActivity.this, R.string.toast_underline, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        ImageButton underline1 = (ImageButton) findViewById(R.id.underline1);

        underline1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtContractorsPerfomance.underline(!edtContractorsPerfomance.contains(KnifeText.FORMAT_UNDERLINED));
            }
        });

        underline1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(FillOutDetailsActivity.this, R.string.toast_underline, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupStrikethrough() {
        ImageButton strikethrough = (ImageButton) findViewById(R.id.strikethrough);

        strikethrough.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtContractorsPerfomance.strikethrough(!edtContractorsPerfomance.contains(KnifeText.FORMAT_STRIKETHROUGH));
            }
        });

        strikethrough.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(FillOutDetailsActivity.this, R.string.toast_strikethrough, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        ImageButton strikethrough1 = (ImageButton) findViewById(R.id.strikethrough1);

        strikethrough1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtContractorsPerfomance.strikethrough(!edtContractorsPerfomance.contains(KnifeText.FORMAT_STRIKETHROUGH));
            }
        });

        strikethrough1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(FillOutDetailsActivity.this, R.string.toast_strikethrough, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupBullet() {
        ImageButton bullet = (ImageButton) findViewById(R.id.bullet);

        bullet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtContractorsPerfomance.bullet(!edtContractorsPerfomance.contains(KnifeText.FORMAT_BULLET));
            }
        });


        bullet.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(FillOutDetailsActivity.this, R.string.toast_bullet, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        ImageButton bullet1 = (ImageButton) findViewById(R.id.bullet1);

        bullet1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtContractorsPerfomance.bullet(!edtContractorsPerfomance.contains(KnifeText.FORMAT_BULLET));
            }
        });


        bullet1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(FillOutDetailsActivity.this, R.string.toast_bullet, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupQuote() {
        ImageButton quote = (ImageButton) findViewById(R.id.quote);

        quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtContractorsPerfomance.quote(!edtContractorsPerfomance.contains(KnifeText.FORMAT_QUOTE));
            }
        });

        quote.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(FillOutDetailsActivity.this, R.string.toast_quote, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        ImageButton quote1 = (ImageButton) findViewById(R.id.quote1);

        quote1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtContractorsPerfomance.quote(!edtContractorsPerfomance.contains(KnifeText.FORMAT_QUOTE));
            }
        });

        quote1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(FillOutDetailsActivity.this, R.string.toast_quote, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupLinki() {
        ImageButton link = (ImageButton) findViewById(R.id.linki);

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLinkDialog();
            }
        });

        link.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(FillOutDetailsActivity.this, R.string.toast_insert_link, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        ImageButton link1 = (ImageButton) findViewById(R.id.linki1);

        link1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLinkDialog();
            }
        });

        link1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(FillOutDetailsActivity.this, R.string.toast_insert_link, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupClear() {
        ImageButton clear = (ImageButton) findViewById(R.id.clear);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtContractorsPerfomance.clearFormats();
            }
        });

        clear.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(FillOutDetailsActivity.this, R.string.toast_format_clear, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        ImageButton clear1 = (ImageButton) findViewById(R.id.clear1);

        clear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtContractorsPerfomance.clearFormats();
            }
        });

        clear1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(FillOutDetailsActivity.this, R.string.toast_format_clear, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }*/

   /* private void showLinkDialogi() {
        final int start = edtContractorsPerfomance.getSelectionStart();
        final int end = edtContractorsPerfomance.getSelectionEnd();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        View view = getLayoutInflater().inflate(R.layout.dialog_link, null, false);
        final EditText editText = (EditText) view.findViewById(R.id.edit);
        builder.setView(view);
        builder.setTitle(R.string.dialog_title);

        builder.setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String link = editText.getText().toString().trim();
                if (TextUtils.isEmpty(link)) {
                    return;
                }

                // When KnifeText lose focus, use this method
                edtContractorsPerfomance.link(link, start, end);
            }
        });

        builder.setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // DO NOTHING HERE
            }
        });

        builder.create().show();
    }
*/

/*Improve Perfomance*/

    /*private void setupBoldi() {
        ImageButton bold = (ImageButton) findViewById(R.id.boldi);
        ImageButton bold1 = (ImageButton) findViewById(R.id.boldi1);

        bold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtContractorsComment.bold(!edtContractorsComment.contains(KnifeText.FORMAT_BOLD));
            }
        });

        bold.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(FillOutDetailsActivity.this, R.string.toast_bold, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        bold1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtContractorsComment.bold(!edtContractorsComment.contains(KnifeText.FORMAT_BOLD));
            }
        });

        bold1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(FillOutDetailsActivity.this, R.string.toast_bold, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupItalici() {
        ImageButton italic = (ImageButton) findViewById(R.id.italici);


        italic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtContractorsComment.italic(!edtContractorsComment.contains(KnifeText.FORMAT_ITALIC));
            }
        });

        italic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(FillOutDetailsActivity.this, R.string.toast_italic, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        ImageButton italic1 = (ImageButton) findViewById(R.id.italici1);


        italic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtContractorsComment.italic(!edtContractorsComment.contains(KnifeText.FORMAT_ITALIC));
            }
        });

        italic1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(FillOutDetailsActivity.this, R.string.toast_italic, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupUnderlinei() {
        ImageButton underline = (ImageButton) findViewById(R.id.underlinei);

        underline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtContractorsComment.underline(!edtContractorsComment.contains(KnifeText.FORMAT_UNDERLINED));
            }
        });

        underline.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(FillOutDetailsActivity.this, R.string.toast_underline, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        ImageButton underline1 = (ImageButton) findViewById(R.id.underlinei1);

        underline1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtContractorsComment.underline(!edtContractorsComment.contains(KnifeText.FORMAT_UNDERLINED));
            }
        });

        underline1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(FillOutDetailsActivity.this, R.string.toast_underline, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupStrikethroughi() {
        ImageButton strikethrough = (ImageButton) findViewById(R.id.strikethroughi);

        strikethrough.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtContractorsComment.strikethrough(!edtContractorsComment.contains(KnifeText.FORMAT_STRIKETHROUGH));
            }
        });

        strikethrough.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(FillOutDetailsActivity.this, R.string.toast_strikethrough, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        ImageButton strikethrough1 = (ImageButton) findViewById(R.id.strikethroughi1);

        strikethrough1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtContractorsComment.strikethrough(!edtContractorsComment.contains(KnifeText.FORMAT_STRIKETHROUGH));
            }
        });

        strikethrough1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(FillOutDetailsActivity.this, R.string.toast_strikethrough, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupBulleti() {
        ImageButton bullet = (ImageButton) findViewById(R.id.bulleti);

        bullet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtContractorsComment.bullet(!edtContractorsComment.contains(KnifeText.FORMAT_BULLET));
            }
        });


        bullet.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(FillOutDetailsActivity.this, R.string.toast_bullet, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        ImageButton bullet1 = (ImageButton) findViewById(R.id.bulleti1);

        bullet1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtContractorsComment.bullet(!edtContractorsComment.contains(KnifeText.FORMAT_BULLET));
            }
        });


        bullet1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(FillOutDetailsActivity.this, R.string.toast_bullet, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupQuotei() {
        ImageButton quote = (ImageButton) findViewById(R.id.quotei);

        quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtContractorsComment.quote(!edtContractorsComment.contains(KnifeText.FORMAT_QUOTE));
            }
        });

        quote.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(FillOutDetailsActivity.this, R.string.toast_quote, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        ImageButton quote1 = (ImageButton) findViewById(R.id.quotei1);

        quote1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtContractorsComment.quote(!edtContractorsComment.contains(KnifeText.FORMAT_QUOTE));
            }
        });

        quote1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(FillOutDetailsActivity.this, R.string.toast_quote, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupLink() {
        ImageButton link = (ImageButton) findViewById(R.id.linki);

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLinkDialogi();
            }
        });

        link.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(FillOutDetailsActivity.this, R.string.toast_insert_link, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        ImageButton link1 = (ImageButton) findViewById(R.id.linki1);

        link1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLinkDialogi();
            }
        });

        link1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(FillOutDetailsActivity.this, R.string.toast_insert_link, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupCleari() {
        ImageButton clear = (ImageButton) findViewById(R.id.cleari);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtContractorsComment.clearFormats();
            }
        });

        clear.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(FillOutDetailsActivity.this, R.string.toast_format_clear, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        ImageButton clear1 = (ImageButton) findViewById(R.id.cleari1);

        clear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtContractorsComment.clearFormats();
            }
        });

        clear1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(FillOutDetailsActivity.this, R.string.toast_format_clear, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void showLinkDialog() {
        final int start = edtContractorsComment.getSelectionStart();
        final int end = edtContractorsComment.getSelectionEnd();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        View view = getLayoutInflater().inflate(R.layout.dialog_link, null, false);
        final EditText editText = (EditText) view.findViewById(R.id.edit);
        builder.setView(view);
        builder.setTitle(R.string.dialog_title);

        builder.setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String link = editText.getText().toString().trim();
                if (TextUtils.isEmpty(link)) {
                    return;
                }

                // When KnifeText lose focus, use this method
                edtContractorsComment.link(link, start, end);
            }
        });

        builder.setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // DO NOTHING HERE
            }
        });

        builder.create().show();
    }
}*/