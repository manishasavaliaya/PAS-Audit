package com.passurvey.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.passurvey.R;
import com.passurvey.adapters.managingAuditorAdapter;
import com.passurvey.database.DBUtility;
import com.passurvey.model.PhotoModel;
import com.passurvey.model.ProfileModel;
import com.passurvey.model.managingAuditorModel;
import com.passurvey.utility.DividerItemDecoration;
import com.passurvey.utility.RecyclerItemClickListener;
import com.passurvey.utility.Utils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import id.zelory.compressor.Compressor;
import okhttp3.internal.Util;

import static com.theartofdev.edmodo.cropper.CropImageActivity.progressbar1;

public class ProfileDrawerActivity extends MainActivity implements View.OnClickListener {
    int Screenno = 0;
    private RelativeLayout rLprofileTop;
    private RelativeLayout rLprofileScreen1;
    private LinearLayout lLProfileFund, sRLprofileScreen4;
//    private View viewLineProfile;
    private TextView tvProfiefund, tvClearDate, tvClear1Date;
    private TextView tvProfielCoManaging;
    private TextView tvProfileManagingSurveyour;
    private TextView tvFacilitiesManager;
    private RelativeLayout FRMSelectPreparedby;
    private RelativeLayout FRMSelectCheckedby;
    private RelativeLayout sRLprofileScreen2;
    private TextView tvProfieSiteAddress;
    private TextView tvProfieReportPreparedby;
    private TextView tvProfileReportCheckedby;
    private RelativeLayout sRLprofileScreen3;
    private TextView tvProfieAuditDate;
    private LinearLayout lLProfileAuditDate;
    private TextView tvProfieDateOfIssue;
    private LinearLayout lLProfileDateOfIssue;
    private EditText edtProfieDateOfIssue;
//    private EditText edtProfilefund;
    private EditText edtProfileCoManaging;
    //private TextView edtProfileManagingSurveyour;
    private EditText edtProfileManagingSurveyour;
    private EditText edtFacilitiesManager;
    private EditText edtProfileSiteAddress;
    /*private EditText edtProfileReportPreparedby;
    private EditText edtProfileReportCheckedby;*/
    private TextView edtProfileReportPreparedby;
    private TextView edtProfileReportCheckedby;
    private EditText edtProfieAuditDate;
    private RecyclerView RVManagingAuditor;
    managingAuditorAdapter adapter;
    ArrayList<managingAuditorModel> managingAudlist;
    // private FrameLayout FRMSelectmmAuditor;

    /*18-10*/
    Dialog dialog;

    private EditText edtProfileSiteAddress1;
    private EditText edtProfileSiteAddress2;
    private EditText edtProfileSiteAddress3;
    private EditText edtProfileSiteAddressPincode;

    private RadioGroup RGonetofive;
    private RadioButton checkedRadioButtononetofive;
    private int year;
    private int month;
    private int day;

    static final int DATE_PICKER_ID = 111;
    static final int DATE_PICKER_ISSUE = 112;

    boolean datesel = true;

    String ProfileId, UserID, SurveyID;
    Cursor cursor;

    private ImageView iconImg, addImage, imgRemove;
    private Button btnSave, btnSkip, btnfinish;

    private TextView txtAddImage;
    private String storagepath;
    private PhotoModel photoModel;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions defaultOptions;
    private DisplayImageOptions options;
    private String strCurrentDate="";
    private Date currentDate = null;

    @Override
    protected void onPause() {
        super.onPause();
        // handler.removeCallbacks(r);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.container_body); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.fragment_profile, contentFrameLayout);
        bindid();

        ProfileId = Utils.getSharedPreString(ProfileDrawerActivity.this, Utils.PREFS_PROFILEID);
        UserID = Utils.getSharedPreString(ProfileDrawerActivity.this, Utils.PREFS_USERID);
        SurveyID = Utils.getSharedPreString(ProfileDrawerActivity.this, Utils.PREFS_SURVEYID);
        strCurrentDate = Utils.getTodayDate("dd-MM-yyyy");
        currentDate = Utils.convertStringToDate(strCurrentDate,"dd-MM-yyyy","dd-MM-yyyy");

         /*amitk 4-2-17*/
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(ProfileDrawerActivity.this));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.add_image_pic)
                .showImageForEmptyUri(R.drawable.add_image_pic)
                .showImageOnFail(R.drawable.add_image_pic)
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        if (savedInstanceState != null) {
            Screenno = savedInstanceState.getInt("Screenno");
            Log.d("Screen no", Screenno + "");
            if (Screenno == 2) {
                showsecondForm();
            } else if (Screenno == 3) {
                showthirdForm();
            } else if (Screenno == 4) {
                showforthForm();
            } else {
                showfirstForm();
            }
        }

        cursor = dbHelper.getUserInfo(SurveyID, UserID, ProfileId);
        try {
            cursor.moveToFirst();
            if (cursor != null && cursor.getCount() > 0) {
                edtProfieDateOfIssue.setText(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_DATEOFISSUE)));
//              edtProfilefund.setText(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_FUND)));
                edtProfileCoManaging.setText(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_COMANAGINGAGENT)));
                edtProfileManagingSurveyour.setText(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_MANAGEMENTSURVEOR)));
                edtFacilitiesManager.setText(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_FACILITIESMANAGER)));
                // edtProfileSiteAddress.setText(Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITEADDRESS))));
                edtProfileSiteAddress.setText(Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITENAME))));
                edtProfileSiteAddress1.setText(Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITEADDRESS1))));
                edtProfileSiteAddress2.setText(Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITEADDRESS2))));
                edtProfileSiteAddress3.setText(Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITEADDRESS3))));
                edtProfileSiteAddressPincode.setText(Utils.ReplaceAsciChar(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_SITEPOSTALCODE))));

                if (cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_REPORTPREPAREDBY)).length() > 1) {
                    edtProfileReportPreparedby.setText(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_REPORTPREPAREDBY)));
                }
                if (cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_REPORTCHECKEDBY)).length() > 1) {
                    edtProfileReportCheckedby.setText(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_REPORTCHECKEDBY)));
                }

                edtProfieAuditDate.setText(cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_AUDITDATE)));
                int AuditVisit = cursor.getInt(cursor.getColumnIndex(DBUtility.PROFILE_AUDITVISIT));
                if (AuditVisit == 1) {
                    RGonetofive.check(findViewById(R.id.rbtnprofileone).getId());
                } else if (AuditVisit == 2) {
                    RGonetofive.check(findViewById(R.id.rbtnprofiletwo).getId());
                } else if (AuditVisit == 3) {
                    RGonetofive.check(findViewById(R.id.rbtnprofilethree).getId());
                } else if (AuditVisit == 4) {
                    RGonetofive.check(findViewById(R.id.rbtnprofilefour).getId());
                }


            //comment by mena
            /*if (CompleteSurveyAndSync.profilenotcompFlag == true) {
                if (cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_DATEOFISSUE)).equalsIgnoreCase("")) {
                    edtProfieDateOfIssue.setError(getString(R.string.dateofissue_error));
                    CompleteSurveyAndSync.profilenotcompFlag = false;
                }
            }
*/
            }
        } finally {
            cursor.close();
        }

        String imagepath = dbHelper.getprofilephoto(SurveyID, ProfileId, UserID, "ProfileImage_" + ProfileId);
//        Log.d("path::", "ONCREATE:::"+imagepath);
//        String imagepath = "/storage/emulated/0/PASAudit/1578128243691.jpg";
        if (!imagepath.equalsIgnoreCase("")) {
            addImage.setVisibility(View.VISIBLE);
            imageLoader.displayImage("file://" + imagepath, addImage);
            storagepath = imagepath;
            otherViewInVisible();
        }

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // Show current date
        //comment by mena
       /* edtProfieAuditDate.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(day).append("-").append(month+1).append("-")
                .append(year).append(" "));*/
        //comment by mena
        //comment by mena 16-11-2016
       /* edtProfieDateOfIssue.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(day).append("-").append(month + 1).append("-")
                .append(year).append(" "));*/
        //  edtProfieDateOfIssue.setText("");
        // Button listener to show date picker dialog
    }

    private void bindid() {
        setHeaderTitle(getString(R.string.title_profile));
        rLprofileTop = (RelativeLayout) findViewById(R.id.RLprofileTop);
        rLprofileScreen1 = (RelativeLayout) findViewById(R.id.RLprofileScreen1);
        lLProfileFund = (LinearLayout) findViewById(R.id.LLProfileFund);
        //Manisha commented 13-1-20
//        tvProfiefund = (TextView) findViewById(R.id.tvProfiefund);
        tvProfielCoManaging = (TextView) findViewById(R.id.tvProfielCoManaging);
        //tvProfileManagingSurveyour = (TextView) findViewById(R.id.tvProfileManagingSurveyour);
        tvFacilitiesManager = (TextView) findViewById(R.id.tvFacilitiesManager);
        sRLprofileScreen2 = (RelativeLayout) findViewById(R.id.sRLprofileScreen2);
        tvProfieSiteAddress = (TextView) findViewById(R.id.tvProfieSiteAddress);
        tvProfieReportPreparedby = (TextView) findViewById(R.id.tvProfieReportPreparedby);
        tvProfileReportCheckedby = (TextView) findViewById(R.id.tvProfileReportCheckedby);
        sRLprofileScreen3 = (RelativeLayout) findViewById(R.id.sRLprofileScreen3);
        tvProfieAuditDate = (TextView) findViewById(R.id.tvProfieAuditDate);
        lLProfileAuditDate = (LinearLayout) findViewById(R.id.LLProfileAuditDate);
        tvProfieDateOfIssue = (TextView) findViewById(R.id.tvProfieDateOfIssue);
        lLProfileDateOfIssue = (LinearLayout) findViewById(R.id.LLProfileDateOfIssue);
        sRLprofileScreen4 = (LinearLayout) findViewById(R.id.sRLprofileScreen4);
        RGonetofive = (RadioGroup) findViewById(R.id.RGonetofive);
        checkedRadioButtononetofive = (RadioButton) RGonetofive.findViewById(RGonetofive.getCheckedRadioButtonId());

        lLProfileAuditDate.setOnClickListener(this);
        lLProfileDateOfIssue.setOnClickListener(this);
        /*FRMSelectmmAuditor=(FrameLayout)findViewById(R.id.FRMSelectmmAuditor);
        FRMSelectmmAuditor.setOnClickListener(this);*/
        FRMSelectPreparedby = (RelativeLayout) findViewById(R.id.FRMSelectPreparedby);
        FRMSelectCheckedby = (RelativeLayout) findViewById(R.id.FRMSelectCheckedby);

        FRMSelectPreparedby.setOnClickListener(this);
        FRMSelectCheckedby.setOnClickListener(this);

        findViewById(R.id.btnProfileNextClient).setOnClickListener(this);
        findViewById(R.id.btnprofileaddBack).setOnClickListener(this);
        findViewById(R.id.btnprofileaddnext).setOnClickListener(this);
        findViewById(R.id.btnprofileAuditBack).setOnClickListener(this);
        findViewById(R.id.btnprofileAuditFinish).setOnClickListener(this);

        edtProfieDateOfIssue = (EditText) findViewById(R.id.edtProfieDateOfIssue);
//      edtProfilefund = (EditText) findViewById(R.id.edtProfilefund);
        edtProfileCoManaging = (EditText) findViewById(R.id.edtProfileCoManaging);
        edtProfileManagingSurveyour = (EditText) findViewById(R.id.edtProfileManagingSurveyour);
        edtFacilitiesManager = (EditText) findViewById(R.id.edtFacilitiesManager);
        edtProfileSiteAddress = (EditText) findViewById(R.id.edtProfileSiteAddress);

          /*18-10*/
        edtProfileSiteAddress1 = (EditText) findViewById(R.id.edtProfileSiteAddress1);
        edtProfileSiteAddress2 = (EditText) findViewById(R.id.edtProfileSiteAddress2);
        edtProfileSiteAddress3 = (EditText) findViewById(R.id.edtProfileSiteAddress3);
        edtProfileSiteAddressPincode = (EditText) findViewById(R.id.edtProfileSiteAddressPincode);

        edtProfileReportPreparedby = (TextView) findViewById(R.id.edtProfileReportPreparedby);
        edtProfileReportCheckedby = (TextView) findViewById(R.id.edtProfileReportCheckedby);
        edtProfieAuditDate = (EditText) findViewById(R.id.edtProfieAuditDate);
        tvClearDate = (TextView) findViewById(R.id.tvClearDate);
        tvClearDate.setOnClickListener(this);
        tvClear1Date = (TextView) findViewById(R.id.tvClear1Date);
        tvClear1Date.setOnClickListener(this);

        edtFacilitiesManager.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edtProfileSiteAddressPincode.setImeOptions(EditorInfo.IME_ACTION_DONE);

        edtFacilitiesManager.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    findViewById(R.id.btnProfileNextClient).performClick();
                }
                return false;
            }
        });

        edtProfileSiteAddressPincode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    findViewById(R.id.btnprofileaddnext).performClick();
                }
                return false;
            }
        });

        /*profile photo*/
        defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(ProfileDrawerActivity.this)
                .writeDebugLogs()
                .defaultDisplayImageOptions(defaultOptions)
                .diskCacheExtraOptions(480, 320, null)
                .build();
      //  imageLoader.getInstance().init(config);

        iconImg = (ImageView) findViewById(R.id.iconImg);
        addImage = (ImageView) findViewById(R.id.addImage);
        addImage.setOnClickListener(this);
        btnSave = (Button) findViewById(R.id.btnSave);
        //  btnSkip = (Button) findViewById(R.id.btnSkip);
        txtAddImage = (TextView) findViewById(R.id.txtAddImage);
        ///edtcommit=(EditText)findViewById(R.id.edtcommit);
        btnfinish = (Button) findViewById(R.id.btnFinish);
        iconImg.setOnClickListener(this);
        imgRemove = (ImageView) findViewById(R.id.imgRemove);
        //  btnSkip = (Button) findViewById(R.id.btnSkip);
        txtAddImage = (TextView) findViewById(R.id.txtAddImage);
        ///edtcommit=(EditText)findViewById(R.id.edtcommit);
        btnfinish = (Button) findViewById(R.id.btnFinish);
        iconImg.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnfinish.setVisibility(View.VISIBLE);
        // btnSkip.setOnClickListener(this);
        btnfinish.setOnClickListener(this);
        imgRemove.setOnClickListener(this);
        otherViewVisible();
        clearview();

        edtProfieAuditDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edtProfieAuditDate.getText().length() > 1) {
                    tvClearDate.setVisibility(View.VISIBLE);
                } else {
                    tvClearDate.setVisibility(View.GONE);
                }
            }
        });


        edtProfieDateOfIssue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edtProfieDateOfIssue.getText().length() > 1) {
                    tvClear1Date.setVisibility(View.VISIBLE);
                } else {
                    tvClear1Date.setVisibility(View.GONE);
                }
            }
        });
    }

    public void selectmmPreparedby() {
        dialog = new Dialog(ProfileDrawerActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_select_auditor);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        TextView title = (TextView) dialog.findViewById(R.id.tvdialogtitle);
        title.setText(getString(R.string.sel_report_preparedby));
        RVManagingAuditor = (RecyclerView) dialog.findViewById(R.id.RVManagingAuditor);
        setDefaultdataPreparedby();
        dialog.show();
    }

    private void setDefaultdataPreparedby() {
        managingAudlist = new ArrayList<>();
        managingAudlist = dbHelper.getmanagingAuditorModelArrayList();
        if (managingAudlist != null) {
            adapter = new managingAuditorAdapter(managingAudlist);
            LinearLayoutManager llm = new LinearLayoutManager(ProfileDrawerActivity.this);
            llm.setOrientation(RecyclerView.VERTICAL);

            RVManagingAuditor.setLayoutManager(llm);
            //  RVQuestionGroup.addItemDecoration(new DividerItemDecoration(getActivity()));
            /*or*/
            RVManagingAuditor.addItemDecoration(new DividerItemDecoration(ProfileDrawerActivity.this, R.drawable.divider));
            RVManagingAuditor.setAdapter(adapter);

            RVManagingAuditor.addOnItemTouchListener(
                    new RecyclerItemClickListener(ProfileDrawerActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            // TODO Handle item click
                            ///edtAnswer.setText(((TextView)view.findViewById(R.id.tvDefaultAnswer)).getText());
                            //((Drawer_Activity) getActivity()).ReplaceFragment(new EventosDetailFragment(), "fragemtn");
                            edtProfileReportPreparedby.setText((((TextView) view.findViewById(R.id.tvauditorname)).getText().toString()));
                            dialog.dismiss();
                            edtProfileReportPreparedby.setError(null);
                            // Utils.showToast(SelectSurveyActivity.this,(((TextView)view.findViewById(R.id.tvDefaultAnswer)).getText().toString()));
                        }
                    })
            );
        }
    }

    public void selectmmCheckedby() {
        dialog = new Dialog(ProfileDrawerActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_select_auditor);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        TextView title = (TextView) dialog.findViewById(R.id.tvdialogtitle);
        title.setText(getString(R.string.sel_report_checkeddby));
        RVManagingAuditor = (RecyclerView) dialog.findViewById(R.id.RVManagingAuditor);
        setDefaultdataCheckedby();
        dialog.show();
    }

    private void setDefaultdataCheckedby() {
        managingAudlist = new ArrayList<>();
       /* DefaultAnsList.add(new DefaultAnswerModel("1", "Dummy TExt 1"));
        DefaultAnsList.add(new DefaultAnswerModel("2", "Dummy TExt 2"));
        DefaultAnsList.add(new DefaultAnswerModel("3", "Dummy TExt 1"));
        DefaultAnsList.add(new DefaultAnswerModel("4", "Dummy TExt 1"));
        DefaultAnsList.add(new DefaultAnswerModel("5", "Dummy TExt 1"));
        DefaultAnsList.add(new DefaultAnswerModel("6", "Dummy TExt 1"));
        DefaultAnsList.add(new DefaultAnswerModel("7", "Dummy TExt 1"));*/

        managingAudlist = dbHelper.getmanagingAuditorModelArrayList();
        if (managingAudlist != null) {
            adapter = new managingAuditorAdapter(managingAudlist);

            LinearLayoutManager llm = new LinearLayoutManager(ProfileDrawerActivity.this);
            llm.setOrientation(RecyclerView.VERTICAL);

            RVManagingAuditor.setLayoutManager(llm);
            //  RVQuestionGroup.addItemDecoration(new DividerItemDecoration(getActivity()));
            /*or*/
            RVManagingAuditor.addItemDecoration(new DividerItemDecoration(ProfileDrawerActivity.this, R.drawable.divider));
            RVManagingAuditor.setAdapter(adapter);

            RVManagingAuditor.addOnItemTouchListener(
                    new RecyclerItemClickListener(ProfileDrawerActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            // TODO Handle item click
                            ///edtAnswer.setText(((TextView)view.findViewById(R.id.tvDefaultAnswer)).getText());
                            //((Drawer_Activity) getActivity()).ReplaceFragment(new EventosDetailFragment(), "fragemtn");
                            edtProfileReportCheckedby.setText((((TextView) view.findViewById(R.id.tvauditorname)).getText().toString()));
                            dialog.dismiss();
                            edtProfileReportCheckedby.setError(null);
                            // Utils.showToast(SelectSurveyActivity.this,(((TextView)view.findViewById(R.id.tvDefaultAnswer)).getText().toString()));
                        }
                    })
            );
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.LLProfileDateOfIssue:
                datesel = false;
                showDialog(DATE_PICKER_ISSUE);

                break;
            case R.id.FRMSelectPreparedby:
                selectmmPreparedby();

                break;

            case R.id.tvClearDate:
                edtProfieAuditDate.setText("");
                tvClearDate.setVisibility(View.GONE);
                break;

            case R.id.tvClear1Date:
                edtProfieDateOfIssue.setText("");
                tvClear1Date.setVisibility(View.GONE);
                break;

            case R.id.FRMSelectCheckedby:
                selectmmCheckedby();
                break;

            case R.id.LLProfileAuditDate:
                datesel = true;
                showDialog(DATE_PICKER_ID);
                break;
            case R.id.btnProfileNextClient:
                //comment by mena
              /*  if (edtProfilefund.getText().toString().trim().equalsIgnoreCase("")) {
                    edtProfilefund.setError(getString(R.string.fund_error));
                } else if (edtProfileCoManaging.getText().toString().trim().equalsIgnoreCase("")) {
                    edtProfileCoManaging.setError(getString(R.string.comanging_error));
                }
                else if (edtProfileManagingSurveyour.getText().toString().trim().equalsIgnoreCase("")) {
                    edtProfileManagingSurveyour.setError(getString(R.string.management_error));
                }
                else if (edtFacilitiesManager.getText().toString().trim().equalsIgnoreCase("")) {
                    edtFacilitiesManager.setError(getString(R.string.facilities_error));
                }
                else
                {
                    showsecondForm();
                    new Handler().post(new DisplayKey(ProfileDrawerActivity.this,edtProfileSiteAddress));
                    edtProfileSiteAddress.setSelection(edtProfileSiteAddress.length());
                }*/
                //comment by mena

                  /*Amitk 9-1-17*/
                ProfileModel mprofileModel1 = new ProfileModel();
                mprofileModel1.setProfileID(ProfileId);
                mprofileModel1.setUserID(UserID);
                mprofileModel1.setSurveyID(SurveyID);
//                mprofileModel1.setFund(edtProfilefund.getText().toString());
                mprofileModel1.setCOManagingAgent(edtProfileCoManaging.getText().toString());
                mprofileModel1.setManagementSurveor(edtProfileManagingSurveyour.getText().toString());
                mprofileModel1.setFacilitiesManager(edtFacilitiesManager.getText().toString());
                /*18-10*/
                mprofileModel1.setSiteName(Utils.ReplaceAsciChar(edtProfileSiteAddress.getText().toString()));
                mprofileModel1.setSiteAddress1(Utils.ReplaceAsciChar(edtProfileSiteAddress1.getText().toString()));
                mprofileModel1.setSiteAddress2(Utils.ReplaceAsciChar(edtProfileSiteAddress2.getText().toString()));
                mprofileModel1.setSiteAddress3(Utils.ReplaceAsciChar(edtProfileSiteAddress3.getText().toString()));
                mprofileModel1.setSitePostalCode(Utils.ReplaceAsciChar(edtProfileSiteAddressPincode.getText().toString()));

                //add by amit
                mprofileModel1.setReportPreparedby(edtProfileReportPreparedby.getText().toString());
                mprofileModel1.setReportCheckedby(edtProfileReportCheckedby.getText().toString());
                mprofileModel1.setAuditDate(edtProfieAuditDate.getText().toString());
                mprofileModel1.setDateofIssue(edtProfieDateOfIssue.getText().toString());

                if (RGonetofive.getCheckedRadioButtonId() <= 0) {
                    //Utils.showToast(ProfileDrawerActivity.this, getString(R.string.auditvisit_error));
                    mprofileModel1.setAuditVisit("");
                } else {
                    mprofileModel1.setAuditVisit(((RadioButton) findViewById(RGonetofive.getCheckedRadioButtonId())).getText().toString());
                }

                mprofileModel1.setContractorsPerfomance("");
                mprofileModel1.setStatutoryAuditScore("");
                mprofileModel1.setContractorImprovePerfomance("");
                mprofileModel1.setConsultantsComments("");
                mprofileModel1.setStatutoryComments("");
                mprofileModel1.setCreatedDate(Utils.getDate());
                mprofileModel1.setCreatedTime(Utils.getTime());
                dbHelper.UpdateProfile(mprofileModel1);
                edtProfileSiteAddress.requestFocus();
            {
                new Handler().post(new DisplayKey(ProfileDrawerActivity.this, edtProfileSiteAddress));
                edtProfileSiteAddress.setSelection(edtProfileSiteAddress.length());
                showthirdForm();
                Screenno = 3;
            }
            break;
            case R.id.btnprofileaddBack:
                hideSoftKeyboard();
                showfirstForm();
                Screenno = 1;

                break;
            case R.id.btnprofileaddnext:
                /*18-10*/
                //comment by mena
                /*if (edtProfileSiteAddress.getText().toString().trim().equalsIgnoreCase("")) {
                    edtProfileSiteAddress.setError(getString(R.string.siteName_error));
                }
                else if (edtProfileSiteAddress1.getText().toString().trim().equalsIgnoreCase("")) {
                    edtProfileSiteAddress1.setError(getString(R.string.siteaddress_error)+" 1");
                }*/
               /* else if (edtProfileSiteAddress2.getText().toString().trim().equalsIgnoreCase("")) {
                    edtProfileSiteAddress2.setError(getString(R.string.siteaddress_error)+" 2");
                }
                else if (edtProfileSiteAddress3.getText().toString().trim().equalsIgnoreCase("")) {
                    edtProfileSiteAddress3.setError(getString(R.string.siteaddress_error)+" 3");
                }*/

               /* else if (edtProfileSiteAddressPincode.getText().toString().trim().equalsIgnoreCase("")) {
                    edtProfileSiteAddressPincode.setError(getString(R.string.sitepostalcode_error));
                }

                else
                {
                    showthirdForm();
                    new Handler().post(new DisplayKey(ProfileDrawerActivity.this,edtProfileReportPreparedby));
                    edtProfileReportPreparedby.setSelection(edtProfileReportPreparedby.length());
                }*/
                //comment by mena

                /*Amitk 9-1-17*/
                ProfileModel mprofileModel3 = new ProfileModel();
                mprofileModel3.setProfileID(ProfileId);
                mprofileModel3.setUserID(UserID);
                mprofileModel3.setSurveyID(SurveyID);
//              mprofileModel3.setFund(edtProfilefund.getText().toString());
                mprofileModel3.setCOManagingAgent(edtProfileCoManaging.getText().toString());
                mprofileModel3.setManagementSurveor(edtProfileManagingSurveyour.getText().toString());
                mprofileModel3.setFacilitiesManager(edtFacilitiesManager.getText().toString());
                /*18-10*/
                mprofileModel3.setSiteName(Utils.ReplaceAsciChar(edtProfileSiteAddress.getText().toString()));
                mprofileModel3.setSiteAddress1(Utils.ReplaceAsciChar(edtProfileSiteAddress1.getText().toString()));
                mprofileModel3.setSiteAddress2(Utils.ReplaceAsciChar(edtProfileSiteAddress2.getText().toString()));
                mprofileModel3.setSiteAddress3(Utils.ReplaceAsciChar(edtProfileSiteAddress3.getText().toString()));
                mprofileModel3.setSitePostalCode(Utils.ReplaceAsciChar(edtProfileSiteAddressPincode.getText().toString()));

                // add by amit
                mprofileModel3.setReportPreparedby(edtProfileReportPreparedby.getText().toString());
                mprofileModel3.setReportCheckedby(edtProfileReportCheckedby.getText().toString());
                mprofileModel3.setAuditDate(edtProfieAuditDate.getText().toString());
                mprofileModel3.setDateofIssue(edtProfieDateOfIssue.getText().toString());

                if (RGonetofive.getCheckedRadioButtonId() <= 0) {
                    //Utils.showToast(ProfileDrawerActivity.this, getString(R.string.auditvisit_error));
                    mprofileModel3.setAuditVisit("");
                } else {
                    mprofileModel3.setAuditVisit(((RadioButton) findViewById(RGonetofive.getCheckedRadioButtonId())).getText().toString());
                }

                mprofileModel3.setContractorsPerfomance("");
                mprofileModel3.setStatutoryAuditScore("");
                mprofileModel3.setContractorImprovePerfomance("");
                mprofileModel3.setConsultantsComments("");
                mprofileModel3.setStatutoryComments("");
                mprofileModel3.setCreatedDate(Utils.getDate());
                mprofileModel3.setCreatedTime(Utils.getTime());

                dbHelper.UpdateProfile(mprofileModel3);
                edtProfileReportPreparedby.requestFocus();

                /*new Handler().post(new DisplayKey(ProfileDrawerActivity.this, edtProfileReportPreparedby));
                edtProfileReportPreparedby.setSelection(edtProfileReportPreparedby.length());*/
                showforthForm();
                Screenno = 4;
                break;

            case R.id.btnprofileAuditBack:
                showkeyboard(edtProfileCoManaging);
                showsecondForm();
                Screenno = 2;
                break;

            case R.id.btnprofileAuditFinish:
                //comment by mena
               /* if (edtProfileReportPreparedby.getText().toString().trim().equalsIgnoreCase("")) {
                    edtProfileReportPreparedby.setError(getString(R.string.reportprepared_error));
                }
                else if (edtProfileReportCheckedby.getText().toString().trim().equalsIgnoreCase("")) {
                    edtProfileReportCheckedby.setError(getString(R.string.reportcheck_error));
                }
                else if (edtProfieAuditDate.getText().toString().trim().equalsIgnoreCase("")) {
                    edtProfieAuditDate.setError(getString(R.string.auditdate_error));
                } else if (edtProfieDateOfIssue.getText().toString().trim().equalsIgnoreCase("")) {
                    edtProfieDateOfIssue.setError(getString(R.string.dateofissue_error));
                }

                else if (RGonetofive.getCheckedRadioButtonId()<=0) {
                    Utils.showToast(ProfileDrawerActivity.this,getString(R.string.auditvisit_error));
                }
                else*/
                //comment by mena
                 /*Amitk 9-1-17*/
                ProfileModel mprofileModel2 = new ProfileModel();
                mprofileModel2.setProfileID(ProfileId);
                mprofileModel2.setUserID(UserID);
                mprofileModel2.setSurveyID(SurveyID);
//                mprofileModel2.setFund(edtProfilefund.getText().toString());
                mprofileModel2.setCOManagingAgent(edtProfileCoManaging.getText().toString());
                mprofileModel2.setManagementSurveor(edtProfileManagingSurveyour.getText().toString());
                mprofileModel2.setFacilitiesManager(edtFacilitiesManager.getText().toString());

                mprofileModel2.setSiteName(Utils.ReplaceAsciChar(edtProfileSiteAddress.getText().toString()));
                mprofileModel2.setSiteAddress1(Utils.ReplaceAsciChar(edtProfileSiteAddress1.getText().toString()));
                mprofileModel2.setSiteAddress2(Utils.ReplaceAsciChar(edtProfileSiteAddress2.getText().toString()));
                mprofileModel2.setSiteAddress3(Utils.ReplaceAsciChar(edtProfileSiteAddress3.getText().toString()));
                mprofileModel2.setSitePostalCode(Utils.ReplaceAsciChar(edtProfileSiteAddressPincode.getText().toString()));

               /* mprofileModel2.setReportPreparedby("");
                mprofileModel2.setReportCheckedby("");
                mprofileModel2.setAuditDate("");
                mprofileModel2.setDateofIssue("");
                mprofileModel2.setAuditVisit("");*/

                //add by amit

                mprofileModel2.setReportPreparedby(edtProfileReportPreparedby.getText().toString());
                mprofileModel2.setReportCheckedby(edtProfileReportCheckedby.getText().toString());
                mprofileModel2.setAuditDate(edtProfieAuditDate.getText().toString());
                mprofileModel2.setDateofIssue(edtProfieDateOfIssue.getText().toString());
                if (RGonetofive.getCheckedRadioButtonId() <= 0) {
                    //Utils.showToast(ProfileDrawerActivity.this, getString(R.string.auditvisit_error));
                } else {
                    mprofileModel2.setAuditVisit(((RadioButton) findViewById(RGonetofive.getCheckedRadioButtonId())).getText().toString());
                }

                mprofileModel2.setContractorsPerfomance("");
                mprofileModel2.setStatutoryAuditScore("");
                mprofileModel2.setContractorImprovePerfomance("");
                mprofileModel2.setConsultantsComments("");
                mprofileModel2.setStatutoryComments("");
                mprofileModel2.setCreatedDate(Utils.getDate());
                mprofileModel2.setCreatedTime(Utils.getTime());
                if (iconImg.getVisibility() == View.GONE) {
                    //Adding photo in database
                    AddPhotoInDatabase("ProfileImage_" + ProfileId, storagepath);
                } else {
                    if (storagepath != null && !storagepath.equalsIgnoreCase(""))
                        dbHelper.DeleteAttachedImage(UserID, SurveyID, ProfileId, "ProfileImage_" + ProfileId, storagepath);
                }
                dbHelper.UpdateProfile(mprofileModel2);
                edtProfileCoManaging.requestFocus();
            {
                new Handler().post(new DisplayKey(ProfileDrawerActivity.this, edtProfileCoManaging));
                edtProfileCoManaging.setSelection(edtProfileCoManaging.length());
                showsecondForm();
                Screenno = 2;
/*
                    ProfileModel mprofileModel = new ProfileModel();
                    mprofileModel.setProfileID(ProfileId);
                    mprofileModel.setUserID(UserID);
                    mprofileModel.setSurveyID(SurveyID);
                    mprofileModel.setFund(edtProfilefund.getText().toString());
                    mprofileModel.setCOManagingAgent(edtProfileCoManaging.getText().toString());
                    mprofileModel.setManagementSurveor(edtProfileManagingSurveyour.getText().toString());
                    mprofileModel.setFacilitiesManager(edtFacilitiesManager.getText().toString());
                    mprofileModel.setSiteAddress(Utils.ReplaceAsciChar(edtProfileSiteAddress.getText().toString()));
                    mprofileModel.setReportPreparedby(edtProfileReportPreparedby.getText().toString());
                    mprofileModel.setReportCheckedby(edtProfileReportCheckedby.getText().toString());
                    mprofileModel.setAuditDate(edtProfieAuditDate.getText().toString());
                    mprofileModel.setDateofIssue(edtProfieDateOfIssue.getText().toString());
                    mprofileModel.setAuditVisit(((RadioButton)findViewById(RGonetofive.getCheckedRadioButtonId())).getText().toString());
                    mprofileModel.setContractorsPerfomance("");
                    mprofileModel.setStatutoryAuditScore("");
                    mprofileModel.setContractorImprovePerfomance("");
                    mprofileModel.setConsultantsComments("");
                    mprofileModel.setCreatedDate(Utils.getDate());
                    mprofileModel.setCreatedTime(Utils.getTime());
                    String data =dbHelper.copyandstoreuserans(SurveyID,UserID,ProfileId);
                    //Utils.showToast(ProfileActivity.this,((RadioButton)findViewById(RGonetofive.getCheckedRadioButtonId())).getText().toString());
                    dbHelper.insertProfile(mprofileModel);
                    startActivity(new Intent(ProfileActivity.this,SelectQuestionGroupActivity.class));
                    finish();*/
            }

            break;
            case R.id.btnSave:
                showSoftKeyboard(edtProfileSiteAddress);
                showthirdForm();
                Screenno = 3;
                /*if (iconImg.getVisibility()==View.GONE) {
                    AddPhotoInDatabase("ProfileImage_" + ProfileId, storagepath);
                    ProfileModel mprofileModel = new ProfileModel();
                    mprofileModel.setProfileID(ProfileId);
                    mprofileModel.setUserID(UserID);
                    mprofileModel.setSurveyID(SurveyID);
                    mprofileModel.setFund(edtProfilefund.getText().toString());
                    mprofileModel.setCOManagingAgent(edtProfileCoManaging.getText().toString());
                    mprofileModel.setManagementSurveor(edtProfileManagingSurveyour.getText().toString());
                    mprofileModel.setFacilitiesManager(edtFacilitiesManager.getText().toString());
                    mprofileModel.setSiteAddress(edtProfileSiteAddress.getText().toString());
                    mprofileModel.setReportPreparedby(edtProfileReportPreparedby.getText().toString());
                    mprofileModel.setReportCheckedby(edtProfileReportCheckedby.getText().toString());
                    mprofileModel.setAuditDate(edtProfieAuditDate.getText().toString());
                    mprofileModel.setDateofIssue(edtProfieDateOfIssue.getText().toString());
                    mprofileModel.setAuditVisit(((RadioButton)findViewById(RGonetofive.getCheckedRadioButtonId())).getText().toString());
                    mprofileModel.setContractorsPerfomance("");
                    mprofileModel.setStatutoryAuditScore("");
                    mprofileModel.setContractorImprovePerfomance("");
                    mprofileModel.setConsultantsComments("");
                    mprofileModel.setCreatedDate(Utils.getDate());
                    mprofileModel.setCreatedTime(Utils.getTime());

                    //    String data =dbHelper.copyandstoreuserans(SurveyID,UserID,ProfileId);
                    //Utils.showToast(ProfileActivity.this,((RadioButton)findViewById(RGonetofive.getCheckedRadioButtonId())).getText().toString());

                    dbHelper.UpdateProfile(mprofileModel);

                    cursor=  dbHelper.checkIncompleteSurveyLogin(Utils.getSharedPreString(ProfileDrawerActivity.this,Utils.PREFS_USERID));
                    if (cursor!=null&&cursor.getCount()>0)
                    {
                        Utils.setSharedPreString(ProfileDrawerActivity.this,Utils.PREFS_SURVEYID,cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_ID)));
                        Utils.setSharedPreString(ProfileDrawerActivity.this,Utils.PREFS_PROFILEID,cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_ID)));
                        startActivity(new Intent(ProfileDrawerActivity.this,InCompleteSurveyActivity.class));
                        finish();
                    }
                    else
                    {
                        startActivity(new Intent(ProfileDrawerActivity.this,InCompleteSurveyActivity.class));
                        finish();
                    }
                }
                else
                {
                    Utils.showToast(ProfileDrawerActivity.this,"Attache Image");
                }*/

                break;
            case R.id.btnFinish:
                hideSoftKeyboard();

                //edit by mena for validation
//11-1-2017 edit by mena for validation change
               /* if (edtProfilefund.getText().toString().trim().equalsIgnoreCase("")) {
                    edtProfilefund.setError(getString(R.string.fund_error));
                    Utils.showToast(ProfileDrawerActivity.this, getResources().getString(R.string.fund_error));

                } else if (edtProfileCoManaging.getText().toString().trim().equalsIgnoreCase("")) {
                    edtProfileCoManaging.setError(getString(R.string.comanging_error));
                    Utils.showToast(ProfileDrawerActivity.this, getResources().getString(R.string.comanging_error));

                } else if (edtProfileManagingSurveyour.getText().toString().trim().equalsIgnoreCase("")) {
                    edtProfileManagingSurveyour.setError(getString(R.string.management_surveyor_error));
                    Utils.showToast(ProfileDrawerActivity.this, getResources().getString(R.string.management_surveyor_error));

                } else if (edtFacilitiesManager.getText().toString().trim().equalsIgnoreCase("")) {
                    edtFacilitiesManager.setError(getString(R.string.facilities_error));
                    Utils.showToast(ProfileDrawerActivity.this, getResources().getString(R.string.facilities_error));

                } else*/
                //11-1-2017 edit by mena for validation change

                if (edtProfileSiteAddress.getText().toString().trim().equalsIgnoreCase("")) {
                    edtProfileSiteAddress.setError(getString(R.string.siteName_error));
                    Utils.showToast(ProfileDrawerActivity.this, getResources().getString(R.string.siteName_error));


                }
                //11-1-2017 edit by mena for validation change

                /*else if (edtProfileSiteAddress1.getText().toString().trim().equalsIgnoreCase("")) {
                    edtProfileSiteAddress1.setError(getString(R.string.siteaddress_error) + " 1");
                    Utils.showToast(ProfileDrawerActivity.this, getResources().getString(R.string.siteaddress_error) + " 1");


                } else if (edtProfileSiteAddressPincode.getText().toString().trim().equalsIgnoreCase("")) {
                    edtProfileSiteAddressPincode.setError(getString(R.string.sitepostalcode_error));
                    Utils.showToast(ProfileDrawerActivity.this, getResources().getString(R.string.sitepostalcode_error));


                }*/                //11-1-2017 edit by mena for validation change

                else if (edtProfileReportPreparedby.getText().toString().trim().equalsIgnoreCase(getString(R.string.sel_report_preparedby))) {
                    edtProfileReportPreparedby.setError(getString(R.string.sel_report_preparedby));
                    Utils.showToast(ProfileDrawerActivity.this, getResources().getString(R.string.sel_report_preparedby));

                }
                //11-1-2017 edit by mena for validation change

                /*else if (edtProfileReportCheckedby.getText().toString().trim().equalsIgnoreCase(getString(R.string.sel_report_checkeddby))) {
                    edtProfileReportCheckedby.setError(getString(R.string.sel_report_checkeddby));
                    Utils.showToast(ProfileDrawerActivity.this, getResources().getString(R.string.sel_report_checkeddby));

                }*/
                //11-1-2017 edit by mena for validation change

                else if (edtProfieAuditDate.getText().toString().trim().equalsIgnoreCase("")) {
                    edtProfieAuditDate.setError(getString(R.string.auditdate_error));
                    Utils.showToast(ProfileDrawerActivity.this, getResources().getString(R.string.auditdate_error));

                }

                //11-1-2017 edit by mena for validation change

               /* else if (RGonetofive.getCheckedRadioButtonId() <= 0) {
                    Utils.showToast(ProfileDrawerActivity.this, getString(R.string.auditvisit_error));
                }*/
                //11-1-2017 edit by mena for validation change
                else {
                    ProfileModel mprofileModel = new ProfileModel();
                    mprofileModel.setProfileID(ProfileId);
                    mprofileModel.setUserID(UserID);
                    mprofileModel.setSurveyID(SurveyID);
//                    mprofileModel.setFund(edtProfilefund.getText().toString());
                    mprofileModel.setCOManagingAgent(edtProfileCoManaging.getText().toString());
                    mprofileModel.setManagementSurveor(edtProfileManagingSurveyour.getText().toString());
                    mprofileModel.setFacilitiesManager(edtFacilitiesManager.getText().toString());
                /*18-10*/
                 /*18-10*/
                    mprofileModel.setSiteName(Utils.ReplaceAsciChar(edtProfileSiteAddress.getText().toString()));
                    mprofileModel.setSiteAddress1(Utils.ReplaceAsciChar(edtProfileSiteAddress1.getText().toString()));
                    mprofileModel.setSiteAddress2(Utils.ReplaceAsciChar(edtProfileSiteAddress2.getText().toString()));
                    mprofileModel.setSiteAddress3(Utils.ReplaceAsciChar(edtProfileSiteAddress3.getText().toString()));
                    mprofileModel.setSitePostalCode(Utils.ReplaceAsciChar(edtProfileSiteAddressPincode.getText().toString()));

                    mprofileModel.setReportPreparedby(edtProfileReportPreparedby.getText().toString());

                    //edit by mena 11-01-2017
                    if (edtProfileReportCheckedby.getText().toString().equalsIgnoreCase(getResources().getString(R.string.sel_report_checkeddby))) {
                        mprofileModel.setReportCheckedby("");

                    } else {
                        mprofileModel.setReportCheckedby(edtProfileReportCheckedby.getText().toString());
                    }
                    //edit by mena 11-01-2017
                    // mprofileModel.setReportCheckedby(edtProfileReportCheckedby.getText().toString());
                    mprofileModel.setAuditDate(edtProfieAuditDate.getText().toString());
                    mprofileModel.setDateofIssue(edtProfieDateOfIssue.getText().toString());
                    Utils.setSharedPreString(this, Utils.PREFS_PROFILE_AUDIT_DATE, edtProfieAuditDate.getText().toString());

//edit by mena 11-01-2017
                    if (RGonetofive.getCheckedRadioButtonId() <= 0) {
                        //Utils.showToast(ProfileDrawerActivity.this, getString(R.string.auditvisit_error));
                        mprofileModel.setAuditVisit("");
                    } else {
                        mprofileModel.setAuditVisit(((RadioButton) findViewById(RGonetofive.getCheckedRadioButtonId())).getText().toString());
                    }

//edit by mena 11-01-2017
                    // mprofileModel.setAuditVisit(((RadioButton) findViewById(RGonetofive.getCheckedRadioButtonId())).getText().toString());
                    mprofileModel.setContractorsPerfomance("");
                    mprofileModel.setStatutoryAuditScore("");
                    mprofileModel.setContractorImprovePerfomance("");
                    mprofileModel.setConsultantsComments("");
                    mprofileModel.setStatutoryComments("");
                    mprofileModel.setCreatedDate(Utils.getDate());
                    mprofileModel.setCreatedTime(Utils.getTime());

                    if (iconImg.getVisibility() == View.GONE) {
                        AddPhotoInDatabase("ProfileImage_" + ProfileId, storagepath);
                    } else {
                        if (storagepath != null && !storagepath.equalsIgnoreCase(""))
                            dbHelper.DeleteAttachedImage(UserID, SurveyID, ProfileId, "ProfileImage_" + ProfileId, storagepath);
                    }
                    //    String data =dbHelper.copyandstoreuserans(SurveyID,UserID,ProfileId);
                    //Utils.showToast(ProfileActivity.this,((RadioButton)findViewById(RGonetofive.getCheckedRadioButtonId())).getText().toString());

                    dbHelper.UpdateProfile(mprofileModel);

                    //edit by mena

                    if (checkalldatacomplete().equalsIgnoreCase(getString(R.string.complete))) {
                        startActivity(new Intent(ProfileDrawerActivity.this, CompleteSurveyAndSync.class));
                        finish();
                    } else {
                        cursor = dbHelper.checkIncompleteSurveyLogin(Utils.getSharedPreString(ProfileDrawerActivity.this, Utils.PREFS_USERID));
                        try {
                            if (cursor != null && cursor.getCount() > 0) {
                                Utils.setSharedPreString(ProfileDrawerActivity.this, Utils.PREFS_SURVEYID, cursor.getString(cursor.getColumnIndex(DBUtility.SURVEY_ID)));
                                Utils.setSharedPreString(ProfileDrawerActivity.this, Utils.PREFS_PROFILEID, cursor.getString(cursor.getColumnIndex(DBUtility.PROFILE_ID)));
                                startActivity(new Intent(ProfileDrawerActivity.this, InCompleteSurveyActivity.class));
                                finish();
                            } else {

                                startActivity(new Intent(ProfileDrawerActivity.this, InCompleteSurveyActivity.class));
                                finish();
                            }
                        }
                        finally {
                            cursor.close();
                        }
                    }
                    //edit by mena
                    /*startActivity(new Intent(ProfileDrawerActivity.this,SelectQuestionGroupActivity.class));
                    finish();*/
                }
                break;

            case R.id.iconImg:
                selectImage();
                break;
            case  R.id.addImage:
                try {


                    if (storagepath == null || storagepath.equalsIgnoreCase("")) {

                    } else {
                        showImageDialog(storagepath);
                    }
                }catch (Exception ex)
                {}

                break;
            case R.id.imgRemove:

                otherViewVisible();
                clearview();
                storagepath="";
                break;
        }
    }

    public String checkalldatacomplete() {

        cursor = dbHelper.checkIncompleteSurveyLogin1(Utils.getSharedPreString(ProfileDrawerActivity.this, Utils.PREFS_USERID));
        try {
            if (cursor != null && cursor.getCount() > 0) {

                return getString(R.string.completesurvey);


            } else {


                if (dbHelper.checkprofilecomplete(ProfileId) && dbHelper.checksummary(ProfileId)) {
                    return getString(R.string.complete);
                } else if (!dbHelper.checkprofilecomplete(ProfileId)) {

                    return getString(R.string.completeProfile);
                } else if (!dbHelper.checksummary(ProfileId)) {
                    return getString(R.string.completesummary);
                }

            }
        }finally {
            cursor.close();
        }
        return "null";
        //return false;
    }

    public void showfirstForm() {


        rLprofileScreen1.setVisibility(View.GONE);
        sRLprofileScreen2.setVisibility(View.GONE);
        sRLprofileScreen3.setVisibility(View.GONE);
        sRLprofileScreen4.setVisibility(View.VISIBLE);


    }

    public void showsecondForm() {

        rLprofileScreen1.setVisibility(View.VISIBLE);
        sRLprofileScreen2.setVisibility(View.GONE);
        sRLprofileScreen3.setVisibility(View.GONE);
        sRLprofileScreen4.setVisibility(View.GONE);


    }

    public void showthirdForm() {

        rLprofileScreen1.setVisibility(View.GONE);
        sRLprofileScreen2.setVisibility(View.VISIBLE);
        sRLprofileScreen3.setVisibility(View.GONE);
        sRLprofileScreen4.setVisibility(View.GONE);


    }

    public void showforthForm() {
        rLprofileScreen1.setVisibility(View.GONE);
        sRLprofileScreen2.setVisibility(View.GONE);
        sRLprofileScreen3.setVisibility(View.VISIBLE);
        sRLprofileScreen4.setVisibility(View.GONE);
    }

//    @Override
//    protected Dialog onCreateDialog(int id) {
//        switch (id) {
//            case DATE_PICKER_ID:
//                // open datepicker dialog.
//                // set date picker for current date
//                // add pickerListener listner to date picker
////                return new DatePickerDialog(this, pickerListener, year, month, day);

//                //Manisha 04-04-20 added
//                if (datesel) {
//
//                     DatePickerDialog dialog = new DatePickerDialog(this, pickerListener, year, month, day);
//                     dialog.getDatePicker().setMaxDate(currentDate.getTime());
//                    return dialog;
//                }else{
//
//                    DatePickerDialog dialog = new DatePickerDialog(this, pickerListener, year, month, day);
//                    dialog.getDatePicker().setMinDate(currentDate.getTime());
//                    return dialog;
//                }
//        }
//        return null;
//    }

    @Override
    protected Dialog onCreateDialog(int id) {
        DatePickerDialog dialog;
        DatePickerDialog dialog1;
        switch(id) {
            case DATE_PICKER_ID:
                dialog = new DatePickerDialog(this, pickerListener, year, month, day);
                dialog.getDatePicker().setMaxDate(currentDate.getTime());
                return dialog;

            case DATE_PICKER_ISSUE:
                dialog1 = new DatePickerDialog(this, pickerListener1, year, month, day);
                dialog1.getDatePicker().setMinDate(currentDate.getTime());
                return dialog1;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            DecimalFormat mFormat = new DecimalFormat("00");
           /* year  = Integer.valueOf(mFormat.format(Double.valueOf(selectedYear)));
            month =  Integer.valueOf(mFormat.format(Double.valueOf(selectedMonth)));
            day   = Integer.valueOf(mFormat.format(Double.valueOf(selectedDay)));*/

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

//            if (datesel) {
                edtProfieAuditDate.setText(new StringBuilder().append(day)
                        .append("-").append(month + 1).append("-").append(year)
                        .append(" "));
                edtProfieAuditDate.setError(null);
//            } else {
//                edtProfieDateOfIssue.setText(new StringBuilder().append(day)
//                        .append("-").append(month + 1).append("-").append(year)
//                        .append(" "));
//                edtProfieDateOfIssue.setError(null);
//                //  edtProfieAuditDate.setError(null);
//            }
        }
    };

    private DatePickerDialog.OnDateSetListener pickerListener1 = new DatePickerDialog.OnDateSetListener() {
        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            DecimalFormat mFormat = new DecimalFormat("00");
           /* year  = Integer.valueOf(mFormat.format(Double.valueOf(selectedYear)));
            month =  Integer.valueOf(mFormat.format(Double.valueOf(selectedMonth)));
            day   = Integer.valueOf(mFormat.format(Double.valueOf(selectedDay)));*/

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

//            if (datesel) {
//                edtProfieAuditDate.setText(new StringBuilder().append(day)
//                        .append("-").append(month + 1).append("-").append(year)
//                        .append(" "));
//                edtProfieAuditDate.setError(null);
//            } else {
                edtProfieDateOfIssue.setText(new StringBuilder().append(day)
                        .append("-").append(month + 1).append("-").append(year)
                        .append(" "));
                edtProfieDateOfIssue.setError(null);
                //  edtProfieAuditDate.setError(null);
//            }
        }
    };

    private void selectImage() {
        startCropImageActivity(null);
        /*final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"}
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileDrawerActivity.this);
        //  builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();*/
    }

    private void otherViewVisible() {
        iconImg.setVisibility(View.VISIBLE);
        txtAddImage.setVisibility(View.VISIBLE);
        imgRemove.setVisibility(View.GONE);

    }

    private void otherViewInVisible() {
        iconImg.setVisibility(View.GONE);
        txtAddImage.setVisibility(View.GONE);
        imgRemove.setVisibility(View.VISIBLE);

    }

    private void clearview() {
        addImage.setImageBitmap(null);
        //   edtcommit.setText("");
    }
    /**
     * Start crop image activity for the given image.
     */
    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setInitialCropWindowPaddingRatio(0)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);

    }

//    private void copyFile(String inputPath, String inputFile, String outputPath) {
//
//        InputStream in = null;
//        OutputStream out = null;
//        try {
//
//            //create output directory if it doesn't exist
//            File dir = new File(outputPath);
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
//
//
//            in = new FileInputStream(inputPath + inputFile);
//            out = new FileOutputStream(outputPath + inputFile);
//
//            byte[] buffer = new byte[1024];
//            int read;
//            while ((read = in.read(buffer)) != -1) {
//                out.write(buffer, 0, read);
//            }
//            in.close();
//            in = null;
//
//            // write the output file (You have now copied the file)
//            out.flush();
//            out.close();
//            out = null;
//
//
//        } catch (FileNotFoundException fnfe1) {
//            Log.e("tag", fnfe1.getMessage());
//        } catch (Exception e) {
//            Log.e("tag", e.getMessage());
//        }
//    }
     /*amitk 3-2-17*/

//    private void copyFile(File input, File  outputPath) throws FileNotFoundException {
//        InputStream in = null;
//        FileOutputStream out = null;
//        try {
//
//            //create output directory if it doesn't exist
//
//            in = new FileInputStream(input);
//            out = new FileOutputStream(outputPath);
//
//            byte[] buffer = new byte[1024];
//            int read;
//            while ((read = in.read(buffer)) != -1) {
//                out.write(buffer, 0, read);
//            }
//            in.close();
//            in = null;
//
//            // write the output file (You have now copied the file)
//            out.flush();
//            out.close();
//            out = null;
//
////        }  catch (FileNotFoundException fnfe1) {
////            Log.e("tag", fnfe1.getMessage());
//        }
//        catch (Exception e) {
//            Log.e("", e.getMessage());
//        }
//    }

    public static String copyFile(File src, File dst) throws IOException {
        File filetemp = null;
        File tempdirReport = new File(dst.getAbsolutePath());
        try {
            if (!tempdirReport.exists())
                tempdirReport.mkdir();
        }
        catch(Exception exe)
        {
            exe.printStackTrace();
        }
        if(!dst.isFile()){
            filetemp = new File(dst.getAbsolutePath() + "/" +String.valueOf(System.currentTimeMillis())+".jpg");
        }else{
            filetemp = new File(dst.getAbsolutePath() + "/" +String.valueOf(System.currentTimeMillis())+".jpg");
        }
        dst = filetemp;
        InputStream in = new FileInputStream(src);
        try {
            FileOutputStream out = new FileOutputStream(filetemp);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
        return dst.getAbsolutePath();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                // ((ImageView) findViewById(R.id.quick_start_cropped_image)).setImageURI(result.getUri());
              //  Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();

                Log.d("path::",result.getUri().getPath());
                Uri imageUri = result.getUri();
                Log.d("path::", "Image URi:::"+imageUri);
                try {
                 //   Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    try {
                       // progressbar.show();
                       // Bitmap resizedBitmap = ThumbnailUtils.extractThumbnail(bitmap, 500, 500);
                       // addImage.setImageBitmap(bitmap);
                        otherViewInVisible();
                       // progressbar.dismiss();

//                        String camaraimagepath = Environment.getExternalStorageDirectory() + "/" + "PASAudit";
//                        File mFolder = new File(camaraimagepath);
//                        File imgFile = new File(camaraimagepath, String.valueOf(System.currentTimeMillis()) + ".jpg");

//                        Log.d("path::", "camaraimagepath:::"+imgFile);
                        //storagepath=imgFile.getPath();

//                        if (!mFolder.exists()) {
//                            mFolder.mkdir();
//                        }

                        //Manisha
//                        File compressedImageFile = null;
//                        compressedImageFile = new File(imageUri.getPath());
//
//                        try {
//                            compressedImageFile = new Compressor(this).setQuality(100).setMaxHeight(1334).setMaxWidth(750)
//                                    .compressToFile(compressedImageFile);
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        if (compressedImageFile.exists()) {

                          File destFile = new File(Environment.getExternalStorageDirectory() + "/" +  "PASAudit"+File.separator);
                             File finalFile = new File(imageUri.getPath());
                        if (finalFile.exists()) {
                          // String path = copyFile(finalFile, destFile);
                            String copypath;

                            if (imageUri.toString().contains("content://")) {
                                storagepath = destFile.getPath();
                                Log.d("path::", "storage Path:::" + storagepath);
                                Uri selectedImage = data.getData();
                                String[] filePath = {MediaStore.Images.Media.DATA};
                                Cursor c = getContentResolver().query(imageUri, filePath, null, null, null);
                                c.moveToFirst();
                                int columnIndex = c.getColumnIndex(filePath[0]);
                                copypath = c.getString(columnIndex);
                                c.close();
                                Log.d("path::", "camera copy Path:::" + copypath);
                                // copyFile(new File(c.getString(columnIndex)),imgFile);
                            } else {
                                storagepath = destFile.getPath();
                                copypath = imageUri.getPath();
                                Log.d("path::", "gallery copy Path:::" + storagepath);
                            }
                            storagepath = copyFile(finalFile,destFile);

                            Log.d("path::", "display onResult copy Path:::"+"file://"+copypath);
                            imageLoader.displayImage("file://"+copypath,addImage,options);
                        }

                        /*FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(imgFile);
                            bitmap.compress(Bitmap.CompressFormat.PNG,70, fos);
                            fos.flush();
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            otherViewVisible();
                            progressbar1.dismiss();
                        }*/

                    }catch (Exception e) {
                        e.printStackTrace();
                        otherViewVisible();
                        progressbar1.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    otherViewVisible();
                    progressbar1.dismiss();
                }
                progressbar1.dismiss();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                //Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
              *//*  File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);

                    Matrix matrix = new Matrix();

                    matrix.postRotate(90);

                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, addImage.getWidth(), addImage.getHeight(), true);

                    Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                    addImage.setImageBitmap(rotatedBitmap);*//*

                try {
                Bitmap photo = (Bitmap)data.getExtras().get("data");
                Bitmap resizedBitmap = ThumbnailUtils.extractThumbnail(photo, 500, 500);
                addImage.setImageBitmap(resizedBitmap);

                    otherViewInVisible();

                    // String camaraimagepath=Environment.getExternalStorageDirectory()+File.separator+"Pass_Survey"+File.separator;
                    String camaraimagepath = Environment.getExternalStorageDirectory() + File.separator + "PASAudit" + File.separator;

                    File dir = new File(camaraimagepath);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    // String path=Environment.getExternalStorageDirectory()+File.separator+"Pass_Survey"+File.separator;
                    String path = Environment.getExternalStorageDirectory() + File.separator + "PASAudit" + File.separator;

                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    file.getPath();
                    storagepath = file.getPath();

                    try {
                        outFile = new FileOutputStream(file);
                        //   bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        otherViewVisible();


                    } catch (IOException e) {
                        e.printStackTrace();
                        otherViewVisible();


                    } catch (Exception e) {
                        e.printStackTrace();
                        otherViewVisible();


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    otherViewVisible();


                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                // Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.w("image from gallery..", picturePath + "");
                // otherViewInVisible();
                imageLoader.displayImage("file://" + picturePath, addImage);
//                addImage.setImageBitmap(thumbnail);
                otherViewInVisible();


                File f = new File(picturePath);
                Uri contentUri = Uri.fromFile(f);
                String inputfilepath = f.getName();
                //storagepath=Environment.getExternalStorageDirectory()+File.separator+"Pass_Survey"+"/"+inputfilepath;
                //copyFile(f.getParent()+File.separator, f.getName(), Environment.getExternalStorageDirectory()+File.separator+"Pass_Survey"+File.separator);

                storagepath = Environment.getExternalStorageDirectory() + File.separator + "PASAudit" + "/" + inputfilepath;
                copyFile(f.getParent() + File.separator, f.getName(), Environment.getExternalStorageDirectory() + File.separator + "PASAudit" + File.separator);
            }
        }


    }
*/

    private void AddPhotoInDatabase(String photoComment, String photoPath) {
        ProfileId = Utils.getSharedPreString(ProfileDrawerActivity.this, Utils.PREFS_PROFILEID);
        UserID = Utils.getSharedPreString(ProfileDrawerActivity.this, Utils.PREFS_USERID);
        SurveyID = Utils.getSharedPreString(ProfileDrawerActivity.this, Utils.PREFS_SURVEYID);

        photoModel = new PhotoModel();
        photoModel.setSurveyId(SurveyID);
        photoModel.setProfileId(ProfileId);
        photoModel.setUserId(UserID);
        photoModel.setPhotoComment(photoComment);
        photoModel.setPhotoPath(photoPath);
        photoModel.setSynFlag("0");
        photoModel.setSYNC_FLAG_IMAGE("0");
        photoModel.setSYNC_FLAG_SURVEYCOMPLETE("0");
        //photoModel.setCreatedDateTime(Utils.getDateTime());
        photoModel.setCreatedDateTime(Utils.getDateTimeNew());

        if (!dbHelper.getProfileimagepathAndcomment(SurveyID, ProfileId, UserID, "ProfileImage_" + ProfileId)) {
            dbHelper.insertPhotos(photoModel);

        } else {
            dbHelper.updateProfilephoto(ProfileId, UserID, photoPath, "ProfileImage_" + ProfileId);

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("Screenno", Screenno);
    }

    public void showImageDialog(String photopath)
    {
        final Dialog dialog;
        ImageView attachedimg;
        dialog = new Dialog(ProfileDrawerActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.profile_dialog_layout);
        attachedimg=(ImageView)dialog.findViewById(R.id.dialogImg);


        imageLoader.displayImage("file://"+photopath,attachedimg,options);

        dialog.show();

    }
}
