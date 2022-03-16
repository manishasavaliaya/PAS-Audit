package com.passurvey.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.textservice.SentenceSuggestionsInfo;
import android.view.textservice.SpellCheckerSession;
import android.view.textservice.SuggestionsInfo;
import android.view.textservice.TextServicesManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.passurvey.R;
import com.passurvey.adapters.CustomPriorityAdapter;
import com.passurvey.adapters.DefaultAnswerAdapter;
import com.passurvey.database.DBUtility;
import com.passurvey.fragments.FragmentDrawer;
import com.passurvey.model.DefaultAnswerModel;
import com.passurvey.model.QuestionAnswerModel;
import com.passurvey.utility.DividerItemDecoration;
import com.passurvey.utility.RecyclerItemClickListener;
import com.passurvey.utility.TextViewUndoRedo;
import com.passurvey.utility.Utils;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import okhttp3.internal.Util;

public class QuestionAnswerActivity extends MainActivity implements View.OnClickListener{ //, SpellCheckerSession.SpellCheckerSessionListener {
    private RelativeLayout rLSurveyTop,RLQAPriority;
    private TextView tvQuestionName,tvQApriority,tvQuestionId,tvQuestionGroupName,tvClearDate;
    private ImageView imganswerIcon;
    // private Spinner SpinPriority;
    private LinearLayout lLSelectDefaultAns;
    private ImageView imgansSelIcon,imgQApriority;
    private EditText edtAnswer;
    //RichEditor edtAnswer;
   // KnifeText edtAnswer;
    Button btnQABack,btnQANext;
    static final int DATE_PICKER_ID = 1111;
    private int year;
    private int month;
    private int day;
    Dialog dialog;
    String[] priority={"Compliant, no further action","Compliant, prompt action required","Non-compliant, immediate action required","Not applicable for this audit and inspection"};
    int icons[] = {R.drawable.green_circle,
            R.drawable.orange_circle,
            R.drawable.red_circle,
            R.drawable.gray_circle};

    HashMap<String,Integer> mapPriority;
    /* Default ans*/
    private RecyclerView RVDialogDefaultAns;
    ArrayList<DefaultAnswerModel> DefaultAnsList;
    DefaultAnswerAdapter adapter;
    String SurveyId,GroupId,CurrentId,NextId,PreviousId="0",Question,strPriority,UserId,ProfileId,QuestionSrno,HasCertificate,strAudit;
    Date dateAudit;
    Cursor mCursor;
    CustomPriorityAdapter customAdapterPriority;
    LinearLayout LLCertificateDate, LLCertificateDateClick;
    EditText edtCertificateDate;
    /*Undo redo*/
    TextViewUndoRedo mTextViewUndoRedo;
    ImageView img_Undo,img_Redo;
    public boolean isDefaultAnsFound=false;
    final Calendar c = null;
    // private SpellCheckerSession mScs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.container_body); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.fragment_question_answer, contentFrameLayout);

        if (savedInstanceState != null) {
            mTextViewUndoRedo = (TextViewUndoRedo) savedInstanceState.getSerializable("mTextViewUndoRedo");
        }
//        if (savedInstanceState!=null) {
//            if(savedInstanceState.getParcelable("mTextViewUndoRedo") != null) {
//                mTextViewUndoRedo = (TextViewUndoRedo) savedInstanceState.getParcelable("mTextViewUndoRedo");
//            }
//        }

        bindid();
        MainActivity.hideProgress();
        mapPriority = new HashMap<>();
        mapPriority.put("Compliant, no further action",0);
        mapPriority.put("Compliant, prompt action required",1);
        mapPriority.put("Non-compliant, immediate action required",2);
        mapPriority.put("Not applicable for this audit and inspection",3);
        SurveyId= Utils.getSharedPreString(QuestionAnswerActivity.this,Utils.PREFS_SURVEYID);
        GroupId=Utils.getSharedPreString(QuestionAnswerActivity.this,Utils.PREFS_GROUPID);
        UserId=Utils.getSharedPreString(QuestionAnswerActivity.this,Utils.PREFS_USERID);
        ProfileId=Utils.getSharedPreString(QuestionAnswerActivity.this,Utils.PREFS_PROFILEID);
        strAudit = Utils.getSharedPreString(QuestionAnswerActivity.this, Utils.PREFS_PROFILE_AUDIT_DATE);
        if(Utils.validateString(strAudit)) {
            dateAudit = Utils.convertStringToDate(strAudit,"dd-M-yyyy","dd-MM-yyyy");
        }

        Intent intent = getIntent();
        if (savedInstanceState!=null) {
            QuestionSrno=savedInstanceState.getString("QuestionSrNo");
        }

        if (intent!=null&&(QuestionSrno==null||QuestionSrno.equalsIgnoreCase(""))) {
             QuestionSrno =intent.getStringExtra("QuestionSrNo");
             // Utils.showToast(QuestionAnswerActivity.this,intent.getStringExtra("QuestionSrNo"));
        }

        if (QuestionSrno != null&&!QuestionSrno.equalsIgnoreCase("")) {
            mCursor = dbHelper.getDrawerQuestionUserAns(SurveyId, GroupId, CurrentId, UserId, ProfileId, QuestionSrno);
        } else {
            mCursor = dbHelper.getQuestionUserAns(SurveyId, GroupId, UserId, ProfileId);
        }
        PreviousId="0";

        // amitk  21-2-17 try cache cursor close
        try {
            if (mCursor != null && mCursor.getCount() > 0) {
                mCursor.moveToFirst();
                CurrentId = mCursor.getInt(mCursor.getColumnIndex(DBUtility.QUESTION_ID)) + "";
                QuestionSrno = mCursor.getInt(mCursor.getColumnIndex(DBUtility.QUESTION_SRNO)) + "";
                Question = mCursor.getString(mCursor.getColumnIndex(DBUtility.QUESTION_NAME));
                tvQuestionName.setText(Question);
                tvQuestionId.setText("Q-" + mCursor.getString(mCursor.getColumnIndex(DBUtility.QUESTION_SRNO)));
                tvQuestionGroupName.setText(Utils.getSharedPreString(QuestionAnswerActivity.this, Utils.PREFS_GROUPNAME));
                HasCertificate = mCursor.getString(mCursor.getColumnIndex(DBUtility.HAS_CERTIFICATE));

                String Piority = mCursor.getString(mCursor.getColumnIndex(DBUtility.ANSWER_PRIORITY));
                if (Piority != null && !Piority.equalsIgnoreCase("")) {
                        // SpinPriority.setSelection(mapPriority.get(Piority));
                        imgQApriority.setVisibility(View.VISIBLE);
                    if (Piority.toString().equalsIgnoreCase("High")) {
                        tvQApriority.setText("Compliant, no further action");
                    } else if (Piority.toString().equalsIgnoreCase("Medium")) {
                        tvQApriority.setText("Compliant, prompt action required");
                    } else if (Piority.toString().equalsIgnoreCase("Low")) {
                        tvQApriority.setText("Non-compliant, immediate action required");
                    } else if (Piority.toString().equalsIgnoreCase("Not Applicable")) {
                        tvQApriority.setText("Not applicable for this audit and inspection");
                    }
                    setpriority(tvQApriority.getText().toString());
                } else {
                    tvQApriority.setText(getString(R.string.select_priority));
                    setpriority(getString(R.string.select_priority));
                }
                String answer = mCursor.getString(mCursor.getColumnIndex(DBUtility.USER_ANSWER));
                edtAnswer.setText(answer);
                edtAnswer.setSelection(answer.length());

//              edtAnswer.setSelection(edtAnswer.getEditableText().length());
                if (QuestionSrno != null && !QuestionSrno.equalsIgnoreCase("")) {
                    btnQABack.setEnabled(true);
                } else {
                    btnQABack.setEnabled(false);
                }

                if (HasCertificate.equalsIgnoreCase("YES")) {
                    LLCertificateDate.setVisibility(View.VISIBLE);
                    edtCertificateDate.setText(mCursor.getString(mCursor.getColumnIndex(DBUtility.CERTIFICATE_DATE)));
                } else {
                    LLCertificateDate.setVisibility(View.GONE);
                }
            }
        }

        finally {
            mCursor.close();
        }
        FragmentDrawer drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        //drawerFragment.updateDrawer();
        drawerFragment.expandable_drawer();
    }

    private void bindid() {
        if (Utils.getSharedPreString(QuestionAnswerActivity.this,Utils.PREFS_SURVEYNAME)!=null&&!Utils.getSharedPreString(QuestionAnswerActivity.this,Utils.PREFS_SURVEYNAME).equalsIgnoreCase(""))
        {
            setHeaderTitle(Utils.getSharedPreString(QuestionAnswerActivity.this,Utils.PREFS_SURVEYNAME));
        }
        else {
            setHeaderTitle(getString(R.string.title_survey));
        }

        edtAnswer=(EditText) findViewById(R.id.edtAnswer);
        tvClearDate=(TextView)findViewById(R.id.tvClearDate);
        tvClearDate.setOnClickListener(this);

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);
        rLSurveyTop = (RelativeLayout) findViewById(R.id.RLSurveyTop);
        tvQuestionName = (TextView) findViewById(R.id.tvQuestionName);
        imganswerIcon = (ImageView) findViewById(R.id.imganswer_icon);
        lLSelectDefaultAns = (LinearLayout) findViewById(R.id.LLSelectDefaultAns);
        imgansSelIcon = (ImageView) findViewById(R.id.imgans_sel_icon);
        RLQAPriority = (RelativeLayout) findViewById(R.id.RLQAPriority);
        imgQApriority= (ImageView) findViewById(R.id.imgQApriority);
        tvQApriority= (TextView) findViewById(R.id.tvQApriority);
        tvQuestionId= (TextView) findViewById(R.id.tvQuestionId);
                tvQuestionGroupName= (TextView) findViewById(R.id.tvQuestionGroupName);
        btnQABack=(Button)findViewById(R.id.btnQABack);
        btnQANext=(Button)findViewById(R.id.btnQANext);
        findViewById(R.id.btnQABack).setOnClickListener(this);
        findViewById(R.id.btnQANext).setOnClickListener(this);
        lLSelectDefaultAns.setOnClickListener(this);
        RLQAPriority.setOnClickListener(this);

        LLCertificateDate=(LinearLayout)findViewById(R.id.LLCertificateDate);
        LLCertificateDateClick=(LinearLayout)findViewById(R.id.LLCertificateDateClick);
        edtCertificateDate=(EditText)findViewById(R.id.edtCertificateDate);
        LLCertificateDateClick.setOnClickListener(this);
        LLCertificateDate.setOnClickListener(this);

        edtCertificateDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(edtCertificateDate.getText().length()>1)
                {
                    tvClearDate.setVisibility(View.VISIBLE);
                }
                else
                {
                    tvClearDate.setVisibility(View.GONE);
                }
            }
        });

        /*Undo redo*/
        if (mTextViewUndoRedo==null) {
            mTextViewUndoRedo = new TextViewUndoRedo(edtAnswer);
        }
        else
        {
            mTextViewUndoRedo.TextViewUndoRedoSavedState(edtAnswer);
        }
        img_Undo=(ImageView)findViewById(R.id.img_Undo);
        img_Redo=(ImageView)findViewById(R.id.img_Redo);
        img_Undo.setOnClickListener(new View.OnClickListener() {
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvClearDate:
                edtCertificateDate.setText("");
                tvClearDate.setVisibility(View.GONE);
                break;

            case R.id.LLCertificateDate:
                showDialog(DATE_PICKER_ID);
                break;

            case R.id.LLCertificateDateClick:
                showDialog(DATE_PICKER_ID);
                break;

            case R.id.btnQANext:
                clickNext();
                break;

            case R.id.btnQABack:
                clickBack();
                break;

            case R.id.LLSelectDefaultAns:
                if (!tvQApriority.getText().toString().equalsIgnoreCase(getString(R.string.select_priority))) {
                    selectDefaultAns(GroupId,CurrentId);
                }
                else {
                    Utils.showToast(QuestionAnswerActivity.this,getString(R.string.select_priority));
                }
                    break;
            case R.id.RLQAPriority:
                selectPriority();
                break;
        }
    }

    public void selectDefaultAns(String groupId,String questionId)
    {
        dialog = new Dialog(QuestionAnswerActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_default_answer);
        RVDialogDefaultAns=(RecyclerView)dialog.findViewById(R.id.RVDialogDefaultAns);

        if (tvQApriority.getText().toString().equalsIgnoreCase(getString(R.string.select_priority)))
        {
                setDefaultdata(groupId,questionId);
        }
        else
        {
            if (tvQApriority.getText().toString().equalsIgnoreCase("Not applicable for this audit and inspection"))
            {
                // setDefaultdata("NA");
                setDefaultdata("Not Applicable",groupId,questionId);
            }
            else {
                if(tvQApriority.getText().toString().equalsIgnoreCase("Compliant, no further action")){
                    setDefaultdata("High",groupId,questionId);

                }else if (tvQApriority.getText().toString().equalsIgnoreCase("Compliant, prompt action required")){
                    setDefaultdata("Medium",groupId,questionId);

                }else if (tvQApriority.getText().toString().equalsIgnoreCase("Non-compliant, immediate action required")){
                    setDefaultdata("Low",groupId,questionId);
                }
            }
        }
        dialog.show();
    }

    // Manisha 7-2-20 added groupId,questionId
    private  void setDefaultdata(String GroupId,String questionId) {
        DefaultAnsList = new ArrayList<>();

        DefaultAnsList = dbHelper.getDefaultAnswerModelArrayList(Integer.parseInt(GroupId),Integer.parseInt(questionId));
        if (DefaultAnsList!=null) {
        adapter = new DefaultAnswerAdapter(DefaultAnsList);

        LinearLayoutManager llm = new LinearLayoutManager(QuestionAnswerActivity.this);
        llm.setOrientation(RecyclerView.VERTICAL);

        RVDialogDefaultAns.setLayoutManager(llm);
        // RVQuestionGroup.addItemDecoration(new DividerItemDecoration(getActivity()));
        /*or*/
        RVDialogDefaultAns.addItemDecoration(new DividerItemDecoration(QuestionAnswerActivity.this, R.drawable.divider));
        RVDialogDefaultAns.setAdapter(adapter);

        RVDialogDefaultAns.addOnItemTouchListener(
                new RecyclerItemClickListener(QuestionAnswerActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        if (edtAnswer.getText().toString().length()>0) {
                            edtAnswer.setText(edtAnswer.getText().toString() + "\n\n" + ((TextView) view.findViewById(R.id.tvDefaultAnswer)).getText());
                        }
                        else
                        {
                            edtAnswer.setText(((TextView) view.findViewById(R.id.tvDefaultAnswer)).getText());
                        }
                     /*   edtAnswer.setText((String) ((TextView)view.findViewById(R.id.tvDefaultAnswer)).getText());
*/
                        dialog.dismiss();
                        Handler mHandler = new Handler();
                        mHandler.post(new DisplayKey(QuestionAnswerActivity.this));
                        edtAnswer.setSelection(edtAnswer.getEditableText().length());
                    }
                }));
        }
//        else{
////            dialog.dismiss();
//            msgDialog(getResources().getString(R.string.msgDefaultAnswer));
//        }
    }

    /*Conditional default answer*/
    private  void setDefaultdata(String Priority,String groupId,String questionId){
        DefaultAnsList = new ArrayList<>();
        DefaultAnsList = dbHelper.getDefaultAnswerModelArrayList(Priority,Integer.parseInt(groupId),Integer.parseInt(questionId));
        if (DefaultAnsList!=null)
        {
            dialog.show();
            adapter = new DefaultAnswerAdapter(DefaultAnsList);

            LinearLayoutManager llm = new LinearLayoutManager(QuestionAnswerActivity.this);
            llm.setOrientation(RecyclerView.VERTICAL);

            RVDialogDefaultAns.setLayoutManager(llm);
            // RVQuestionGroup.addItemDecoration(new DividerItemDecoration(getActivity()));
            /*or*/

            RVDialogDefaultAns.addItemDecoration(new DividerItemDecoration(QuestionAnswerActivity.this,R.drawable.divider));
            RVDialogDefaultAns.setAdapter(adapter);
            RVDialogDefaultAns.addOnItemTouchListener(
                new RecyclerItemClickListener(QuestionAnswerActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                    if (edtAnswer.getText().toString().length()>0) {
                        edtAnswer.setText(edtAnswer.getText().toString() + "\n\n" + ((TextView) view.findViewById(R.id.tvDefaultAnswer)).getText());
                    } else {
                        edtAnswer.setText(((TextView) view.findViewById(R.id.tvDefaultAnswer)).getText());
                    }
                    // edtAnswer.setText((String) ((TextView)view.findViewById(R.id.tvDefaultAnswer)).getText());
                    // ((Drawer_Activity) getActivity()).ReplaceFragment(new EventosDetailFragment(), "fragment");
                    dialog.dismiss();
                    Handler mHandler = new Handler();
                    mHandler.post(new DisplayKey(QuestionAnswerActivity.this));
                    }
                })
            );
        }
//        else{
////            dialog.dismiss();
//            msgDialog(getResources().getString(R.string.msgDefaultAnswer));
//        }
    }

    public void clickNext() {
        if (!tvQApriority.getText().toString().equalsIgnoreCase(getString(R.string.select_priority))) {
            if ((HasCertificate.equalsIgnoreCase("NO") || tvQApriority.getText().toString().equalsIgnoreCase("Non-compliant, immediate action required") || tvQApriority.getText().toString().equalsIgnoreCase("Not applicable for this audit and inspection") || (HasCertificate.equalsIgnoreCase("YES") && Utils.validateString(edtCertificateDate.getText().toString())))) {
                //if(isValidateManualDate()){
                //((FragmentDrawer)getApplicationContext()).updateDrawer();
                QuestionAnswerModel data = new QuestionAnswerModel();
                data.setUserId(Utils.getSharedPreString(QuestionAnswerActivity.this, Utils.PREFS_USERID));
                /*21-10*/
                // data.setUserAnswer(Utils.ReplaceAsciChar(edtAnswer.getHtml().toString()));
                data.setUserAnswer(edtAnswer.getText().toString());
                data.setSurveyId(SurveyId);
                data.setGroupId(GroupId);
                data.setQuestionId(CurrentId);
                data.setProfileId(ProfileId);
                // change by mena 15-11-2016 for priorty
                if (tvQApriority.getText().toString().equalsIgnoreCase("Compliant, no further action")) {
                    data.setAnswerPriority("High");

                } else if (tvQApriority.getText().toString().equalsIgnoreCase("Compliant, prompt action required")) {
                    data.setAnswerPriority("Medium");

                } else if (tvQApriority.getText().toString().equalsIgnoreCase("Non-compliant, immediate action required")) {
                    data.setAnswerPriority("Low");

                } else if (tvQApriority.getText().toString().equalsIgnoreCase("Not applicable for this audit and inspection")) {
                    data.setAnswerPriority("Not Applicable");
                }

                // data.setAnswerPriority(tvQApriority.getText().toString());
                // change by mena 15-11-2016 for priorty
                // data.setCreateDateTime(Utils.getDateTime());
                data.setCreateDateTime(Utils.getDateTimeNew());
                data.setCompleteFlag("1");
                data.setCompleteSurveyFlag("0");
                data.setSyncFlag("0");
                String certificatedate = edtCertificateDate.getText().toString();

                if (certificatedate != null && !certificatedate.equalsIgnoreCase("")) {
                    data.setCertificateDate(edtCertificateDate.getText().toString());
                } else {
                    data.setCertificateDate("");
                }

                long insert = dbHelper.updateUserAns(data);
                if (insert > 0) {
                    //  Utils.showToast(QuestionAnswerActivity.this, "Data Updated");
                } else {
                    //   Utils.showToast(QuestionAnswerActivity.this, "Data not updated");
                }

                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                FragmentDrawer drawerFragment = (FragmentDrawer)
                        getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
                //drawerFragment.updateDrawer();
                drawerFragment.expandable_drawer();

                edtAnswer.setText("");
                edtCertificateDate.setText("");
                // SpinPriority.setSelection(0);
                mCursor = dbHelper.getNextQuestionUserAns(SurveyId, GroupId, CurrentId, UserId, ProfileId, QuestionSrno);
                // amitk 21-2-17 try cache cursor close
                try {
                    PreviousId = CurrentId;
                    if (mCursor != null && mCursor.getCount() > 0) {
                        mCursor.moveToFirst();
                        CurrentId = mCursor.getInt(mCursor.getColumnIndex(DBUtility.QUESTION_ID)) + "";
                        Question = mCursor.getString(mCursor.getColumnIndex(DBUtility.QUESTION_NAME));
                        QuestionSrno = mCursor.getString(mCursor.getColumnIndex(DBUtility.QUESTION_SRNO));
                        tvQuestionName.setText(Question);
                        tvQuestionId.setText("Q-" + mCursor.getString(mCursor.getColumnIndex(DBUtility.QUESTION_SRNO)));
                        tvQuestionGroupName.setText(Utils.getSharedPreString(QuestionAnswerActivity.this, Utils.PREFS_GROUPNAME));
                        HasCertificate = mCursor.getString(mCursor.getColumnIndex(DBUtility.HAS_CERTIFICATE));

                        String Piority = mCursor.getString(mCursor.getColumnIndex(DBUtility.ANSWER_PRIORITY));
                        if (Piority != null && !Piority.equalsIgnoreCase("")) {
                            // SpinPriority.setSelection(mapPriority.get(Piority));
                            //comment by mena
                            if (Piority.toString().equalsIgnoreCase("High")) {
                                tvQApriority.setText("Compliant, no further action");
                            } else if (Piority.toString().equalsIgnoreCase("Medium")) {
                                tvQApriority.setText("Compliant, prompt action required");
                            } else if (Piority.toString().equalsIgnoreCase("Low")) {
                                tvQApriority.setText("Non-compliant, immediate action required");
                            } else if (Piority.toString().equalsIgnoreCase("Not Applicable")) {
                                tvQApriority.setText("Not applicable for this audit and inspection");
                            }

                            // comment by mena
                            // tvQApriority.setText(Piority);
                            setpriority(tvQApriority.getText().toString());
                        } else {
                            tvQApriority.setText(getString(R.string.select_priority));
                            setpriority(getString(R.string.select_priority));
                        }
                        /*21-10*/
                        //edtAnswer.setHtml(Utils.ReplaceAsciChar(mCursor.getString(mCursor.getColumnIndex(DBUtility.USER_ANSWER))));
                        edtAnswer.setText(mCursor.getString(mCursor.getColumnIndex(DBUtility.USER_ANSWER)));
                        // edtAnswer.setSelection(edtAnswer.getText().length());
                        btnQABack.setEnabled(true);

                        if (HasCertificate.equalsIgnoreCase("YES")) {
                            LLCertificateDate.setVisibility(View.VISIBLE);
                            edtCertificateDate.setText(mCursor.getString(mCursor.getColumnIndex(DBUtility.CERTIFICATE_DATE)));
                        } else {
                            LLCertificateDate.setVisibility(View.GONE);
                        }
                    } else {
                        /* Checking any question is remaing in question group*/

                        int count = -1;
                        count = dbHelper.getQuestionGroupCount(SurveyId, UserId, ProfileId, GroupId);

                        if (count == 0) {
                            String[] datanextgroup = new String[2];
                            datanextgroup = dbHelper.getIncompleteGroupId(SurveyId, Utils.getSharedPreString(QuestionAnswerActivity.this, Utils.PREFS_USERID));
                            if (datanextgroup != null) {
                                hideSoftKeyboard();
                                startActivity(new Intent(QuestionAnswerActivity.this, SelectQuestionGroupActivity.class));
                                finish();
                            } else {
                                startactivity();
                            }
                        } else if (count == -1 || count > 0) {
                            mCursor = dbHelper.getQuestionUserAnsRemaining(SurveyId, GroupId, UserId, ProfileId);
                            try {
                                if (mCursor != null && mCursor.getCount() > 0) {
                                    mCursor.moveToFirst();
                                    CurrentId = mCursor.getInt(mCursor.getColumnIndex(DBUtility.QUESTION_ID)) + "";
                                    QuestionSrno = mCursor.getInt(mCursor.getColumnIndex(DBUtility.QUESTION_SRNO)) + "";
                                    Question = mCursor.getString(mCursor.getColumnIndex(DBUtility.QUESTION_NAME));
                                    tvQuestionName.setText(Question);
                                    tvQuestionId.setText("Q-" + mCursor.getString(mCursor.getColumnIndex(DBUtility.QUESTION_SRNO)));
                                    tvQuestionGroupName.setText(Utils.getSharedPreString(QuestionAnswerActivity.this, Utils.PREFS_GROUPNAME));

                                    HasCertificate = mCursor.getString(mCursor.getColumnIndex(DBUtility.HAS_CERTIFICATE));
                                    String Piority = mCursor.getString(mCursor.getColumnIndex(DBUtility.ANSWER_PRIORITY));
                                    if (Piority != null && !Piority.equalsIgnoreCase("")) {
                                        // SpinPriority.setSelection(mapPriority.get(Piority));

                                        imgQApriority.setVisibility(View.VISIBLE);
                                        if (Piority.toString().equalsIgnoreCase("High")) {
                                            tvQApriority.setText("Compliant, no further action");

                                        } else if (Piority.toString().equalsIgnoreCase("Medium")) {
                                            tvQApriority.setText("Compliant, prompt action required");

                                        } else if (Piority.toString().equalsIgnoreCase("Low")) {
                                            tvQApriority.setText("Non-compliant, immediate action required");

                                        } else if (Piority.toString().equalsIgnoreCase("Not Applicable")) {
                                            tvQApriority.setText("Not applicable for this audit and inspection");
                                        }
                                        // tvQApriority.setText(Piority);
                                        setpriority(tvQApriority.getText().toString());
                                    } else {
                                        tvQApriority.setText(getString(R.string.select_priority));
                                        setpriority(getString(R.string.select_priority));
                                    }
                                    edtAnswer.setText(mCursor.getString(mCursor.getColumnIndex(DBUtility.USER_ANSWER)));
                                    edtAnswer.setSelection(edtAnswer.getEditableText().length());
                                    btnQABack.setEnabled(true);
                                    if (HasCertificate.equalsIgnoreCase("YES")) {
                                        LLCertificateDate.setVisibility(View.VISIBLE);
                                        edtCertificateDate.setText(mCursor.getString(mCursor.getColumnIndex(DBUtility.CERTIFICATE_DATE)));
                                    } else {
                                        LLCertificateDate.setVisibility(View.GONE);
                                    }
                                }
                            } finally {
                                mCursor.close();
                            }
                        }
                    }
                } finally {
                    mCursor.close();
                }
            } else if (HasCertificate.equalsIgnoreCase("YES") && !Utils.validateString(edtCertificateDate.getText().toString())) {
                Utils.showToast(QuestionAnswerActivity.this, "Select Certificate date");
            }
        }
                //Manisha added
//                else if (HasCertificate.equalsIgnoreCase("YES") && !edtCertificateDate.getText().toString().equalsIgnoreCase("")) {
//                    isValidateManualDate();
//                }
//            }
        else
        {
            Utils.showToast(QuestionAnswerActivity.this,getString(R.string.select_priority));
        }
    }

    public boolean isValidateManualDate(){
        boolean isvalidate = true;
//      String auditTime="", certificateTime="";
        String endTime = edtCertificateDate.getText().toString();
        try {
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            Date auditDate = null, certificateDate = null;
            if (Utils.validateString(strAudit))
                auditDate = df.parse(strAudit);

            if (Utils.validateString(endTime))
                certificateDate = df.parse(endTime);

            if (certificateDate.after(auditDate)) {
                Utils.showToast(QuestionAnswerActivity.this, "Certificate date should be smaller than Audit date");
                isvalidate = false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return isvalidate;
    }

    public void clickBack()
    {
        mCursor=dbHelper.getPreviousQuestionUserAns(SurveyId,GroupId,CurrentId,UserId,ProfileId,QuestionSrno);
        try {
            if (mCursor != null && mCursor.getCount() > 0) {
                mCursor.moveToFirst();
                CurrentId = mCursor.getInt(mCursor.getColumnIndex(DBUtility.QUESTION_ID)) + "";
                Question = mCursor.getString(mCursor.getColumnIndex(DBUtility.QUESTION_NAME));
                QuestionSrno = mCursor.getString(mCursor.getColumnIndex(DBUtility.QUESTION_SRNO));
                tvQuestionName.setText(Question);
                tvQuestionId.setText("Q-" + mCursor.getString(mCursor.getColumnIndex(DBUtility.QUESTION_SRNO)));

                HasCertificate = mCursor.getString(mCursor.getColumnIndex(DBUtility.HAS_CERTIFICATE));

                String Piority = mCursor.getString(mCursor.getColumnIndex(DBUtility.ANSWER_PRIORITY));
                if (Piority != null && !Piority.equalsIgnoreCase("")) {
                    // SpinPriority.setSelection(mapPriority.get(Piority));
                    // comment by mena
                    if (Piority.toString().equalsIgnoreCase("High")) {
                        tvQApriority.setText("Compliant, no further action");

                    } else if (Piority.toString().equalsIgnoreCase("Medium")) {
                        tvQApriority.setText("Compliant, prompt action required");

                    } else if (Piority.toString().equalsIgnoreCase("Low")) {
                        tvQApriority.setText("Non-compliant, immediate action required");

                    } else if (Piority.toString().equalsIgnoreCase("Not Applicable")) {
                        tvQApriority.setText("Not applicable for this audit and inspection");
                    }
                    setpriority(tvQApriority.getText().toString());
                } else {
                    tvQApriority.setText(getString(R.string.select_priority));
                    setpriority(getString(R.string.select_priority));
                }
                /*21-10*/
                //edtAnswer.setText(Utils.ReplaceAsciChar(mCursor.getString(mCursor.getColumnIndex(DBUtility.USER_ANSWER))));
                edtAnswer.setText(Utils.ReplaceAsciChar(mCursor.getString(mCursor.getColumnIndex(DBUtility.USER_ANSWER))));
                // edtAnswer.setSelection(edtAnswer.getText().length());
                btnQABack.setEnabled(true);

                if (HasCertificate.equalsIgnoreCase("YES")) {
                    LLCertificateDate.setVisibility(View.VISIBLE);
                    edtCertificateDate.setText(mCursor.getString(mCursor.getColumnIndex(DBUtility.CERTIFICATE_DATE)));
                } else {
                    LLCertificateDate.setVisibility(View.GONE);
                }
            } else {
                PreviousId = "0";
                btnQABack.setEnabled(false);

            }
        } finally {
            mCursor.close();
        }
    }

    public void startactivity() {
        startActivity(new Intent(QuestionAnswerActivity.this, PhotoActivity.class));
        finish();
    }

    public void selectPriority() {
        dialog = new Dialog(QuestionAnswerActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_select_priority);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        RVDialogDefaultAns=(RecyclerView)dialog.findViewById(R.id.RVDialogDefaultAns);
        setPrioritydata();
        dialog.show();
    }

    private  void setPrioritydata() {
            customAdapterPriority=new CustomPriorityAdapter(QuestionAnswerActivity.this,icons,priority);
            LinearLayoutManager llm = new LinearLayoutManager(QuestionAnswerActivity.this);
            llm.setOrientation(RecyclerView.VERTICAL);
            RVDialogDefaultAns.setLayoutManager(llm);
        /*or*/
            RVDialogDefaultAns.addItemDecoration(new DividerItemDecoration(QuestionAnswerActivity.this,R.drawable.divider));
            RVDialogDefaultAns.setAdapter(customAdapterPriority);
            RVDialogDefaultAns.addOnItemTouchListener(
                    new RecyclerItemClickListener(QuestionAnswerActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override public void onItemClick(View view, int position) {
                            imgQApriority.setVisibility(View.VISIBLE);
                            tvQApriority.setText((((TextView)view.findViewById(R.id.textView)).getText().toString()));
                            imgQApriority.setImageDrawable(((ImageView)view.findViewById(R.id.imageView)).getDrawable());
                            dialog.dismiss();
                        }
                    })
            );
    }

    public void setpriority(String Priority)
    {
        if (Priority.equalsIgnoreCase(priority[0]))
        {
            imgQApriority.setVisibility(View.VISIBLE);
            imgQApriority.setImageDrawable(getResources().getDrawable(R.drawable.green_circle));
        }
        else if (Priority.equalsIgnoreCase(priority[1]))
        {
            imgQApriority.setVisibility(View.VISIBLE);
            imgQApriority.setImageDrawable(getResources().getDrawable(R.drawable.orange_circle));
        }
        else  if (Priority.equalsIgnoreCase(priority[2]))
        {
            imgQApriority.setVisibility(View.VISIBLE);
            imgQApriority.setImageDrawable(getResources().getDrawable(R.drawable.red_circle));
        }
        else if (Priority.equalsIgnoreCase(priority[3]))
        {
            imgQApriority.setVisibility(View.VISIBLE);
            imgQApriority.setImageDrawable(getResources().getDrawable(R.drawable.gray_circle));
        }
        else
        {
            imgQApriority.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
                // open datepicker dialog.
                // set date picker for current date
                // add pickerListener listner to date pickerDatePickerDialog
                // Manisha 04-04-20 added
                DatePickerDialog dialog = new DatePickerDialog(this, pickerListener, year, month, day);
                dialog.getDatePicker().setMaxDate(dateAudit.getTime());
                return dialog;
//              return new DatePickerDialog(this, pickerListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;
            // Show selected date
            DecimalFormat mFormat= new DecimalFormat("00");
            edtCertificateDate.setText(mFormat.format(Double.valueOf(day)) + "-" +  mFormat.format(Double.valueOf(month+1)) + "-" +  mFormat.format(Double.valueOf(year)));
        }
    };

    /* Code for show keyboard */
    public class DisplayKey implements Runnable {
        private final Context mContext;
        public DisplayKey(Context mContext) {
            this.mContext = mContext;
        }
        public void run() {
            hidekeybooard(edtAnswer);
            showkeyboard(edtAnswer);
            edtAnswer.setSelection(edtAnswer.getEditableText().length());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("QuestionSrNo",QuestionSrno);
        outState.putSerializable("mTextViewUndoRedo",mTextViewUndoRedo);
    }

    private void msgDialog(String msg) {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_msg);
        dialog.getWindow().setDimAmount(0.6f);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//      dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        Button dialogButtonyes = (Button) dialog.findViewById(R.id.btnyes);
        TextView txtMsg = (TextView) dialog.findViewById(R.id.txt_msg);
        txtMsg.setText(msg);
        // if button is clicked, close the custom dialog

        Button dialogButtonno = (Button) dialog.findViewById(R.id.btnno);
        // if button is clicked, close the custom dialog
        dialogButtonno.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.show();
    }

//    public void onGetSuggestions(final SuggestionsInfo[] arg0) {
//        final StringBuilder sb = new StringBuilder();
//
//        for (int i = 0; i < arg0.length; ++i) {
//            // Returned suggestions are contained in SuggestionsInfo
//            final int len = arg0[i].getSuggestionsCount();
//            sb.append('\n');
//
//            for (int j = 0; j < len; ++j) {
//                sb.append("," + arg0[i].getSuggestionAt(j));
//            }
//
//            sb.append(" (" + len + ")");
//        }
//        runOnUiThread(new Runnable() {
//            public void run() {
//                edtAnswer.append(sb.toString());
//            }
//        });
//    }
//
//    @Override
//    public void onGetSentenceSuggestions(SentenceSuggestionsInfo[] arg0) {
//        // TODO Auto-generated method stub
//    }
//
//    @Override
    protected void onResume() {
        super.onResume();

//        final TextServicesManager tsm = (TextServicesManager) getSystemService(Context.TEXT_SERVICES_MANAGER_SERVICE);
//        mScs = tsm.newSpellCheckerSession(null, null, this, true);
    }
//

    @Override
    protected void onStop() {
        super.onStop();

    }

    public void onPause() {
        super.onPause();

//        if (mScs != null) {
//            mScs.close();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
