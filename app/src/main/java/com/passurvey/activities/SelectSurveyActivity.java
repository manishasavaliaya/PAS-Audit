package com.passurvey.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.passurvey.R;
import com.passurvey.adapters.DefaultAnswerAdapter;
import com.passurvey.model.DefaultAnswerModel;
import com.passurvey.utility.DividerItemDecoration;
import com.passurvey.utility.RecyclerItemClickListener;
import com.passurvey.utility.Utils;

import java.util.ArrayList;


public class SelectSurveyActivity extends MainActivity implements View.OnClickListener{
    public static int IMAGE_NOTIFICATION_ID=247;
 // private Spinner spinner;
    private Button btnStartSurvey;
    String ProfileId,UserID,SurveyID;
    FrameLayout FRMSelectSurvey;
    TextView tvSelectedSurvey;
    ArrayList<DefaultAnswerModel> DefaultAnsList;
    DefaultAnswerAdapter adapter;
    private RecyclerView RVDialogDefaultAns;
    Dialog dialog;
    private static Dialog popupWindow;
    ProgressBar progress;
    TextView tvprogresspercentage;
    int i=0;

    @Override
    protected void onPause() {
        super.onPause();
       // handler.removeCallbacks(r);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.container_body); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.fragment_select_survey, contentFrameLayout);
        bindid();
        // Log.d("ProfileId",ProfileId);
    }

    private void bindid() {
        setHeaderTitle(getString(R.string.title_survey));
       // spinner = (Spinner) findViewById(R.id.SpinSelectSurvey);
        btnStartSurvey =(Button)findViewById(R.id.btnStartSurvey);
        btnStartSurvey.setOnClickListener(this);
        FRMSelectSurvey=(FrameLayout)findViewById(R.id.FRMSelectSurvey);
        FRMSelectSurvey.setOnClickListener(this);
        tvSelectedSurvey=(TextView)findViewById(R.id.tvSelectedSurvey);
        //  String[] years = {"1996"};
        ProfileId = Utils.getSharedPreString(SelectSurveyActivity.this,Utils.PREFS_PROFILEID);
        UserID=Utils.getSharedPreString(SelectSurveyActivity.this,Utils.PREFS_USERID);
        SurveyID=Utils.getSharedPreString(SelectSurveyActivity.this,Utils.PREFS_SURVEYID);

        // ArrayAdapter<CharSequence> langAdapter = new ArrayAdapter<CharSequence>(getActivity(),R.layout.spinner_survey_row, years);
        ArrayAdapter<CharSequence> langAdapter = new ArrayAdapter<CharSequence>(SelectSurveyActivity.this,R.layout.spinner_survey_row, dbHelper.getSurveyList() );
        langAdapter.setDropDownViewResource(R.layout.spinner_survey_row_white);
       // spinner.setAdapter(langAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStartSurvey:
              //  Utils.showToast(SelectSurveyActivity.this,spinner.getSelectedItem().toString()+" selected id"+dbHelper.getSurveyId(spinner.getSelectedItem().toString()));
                //!spinner.getSelectedItem().toString().equalsIgnoreCase("Select Survey")&&

                if (!tvSelectedSurvey.getText().toString().equalsIgnoreCase("Select Audit")) {
                    btnStartSurvey.setEnabled(false);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btnStartSurvey.setEnabled(true);
                        }
                    },2000);

                    ProfileId = Utils.getDeviceid(SelectSurveyActivity.this)+"_"+Utils.getDateTime();
                    Utils.setSharedPreString(SelectSurveyActivity.this,Utils.PREFS_PROFILEID,ProfileId);
                    if (dbHelper.checkSurveyHasQuestion( dbHelper.getSurveyId(tvSelectedSurvey.getText().toString()))) {
                        Log.d("Surveyid", dbHelper.getSurveyId(tvSelectedSurvey.getText().toString()));
                        Utils.setSharedPreString(SelectSurveyActivity.this, Utils.PREFS_SURVEYID, dbHelper.getSurveyId(tvSelectedSurvey.getText().toString()));
                        Utils.setSharedPreString(SelectSurveyActivity.this, Utils.PREFS_SURVEYNAME, tvSelectedSurvey.getText().toString());

                        // String data =dbHelper.copyandstoreuserans(dbHelper.getSurveyId(spinner.getSelectedItem().toString()),Utils.getSharedPreString(SelectSurveyActivity.this,Utils.PREFS_USERID),ProfileId);
                        // Utils.showToast(SelectSurveyActivity.this,data+"");
                       /* startActivity(new Intent(SelectSurveyActivity.this, ProfileActivity.class));
                        finish();*/
                       // Intent intent = new Intent(SelectSurveyActivity.this, ProfileActivity.class);
                        Intent intent = new Intent(SelectSurveyActivity.this, ProfileActivity.class);
                        hideSoftKeyboard();
                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        finish();
                        /*Intent intent =new Intent(SelectSurveyActivity.this, ProfileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);*/
                    }
                    else
                    {
                        Utils.showToast(SelectSurveyActivity.this,tvSelectedSurvey.getText().toString()+" "+getString(R.string.hasnoquestion));
                    }
                }
                else
                {
                    Utils.showToast(SelectSurveyActivity.this,getString(R.string.select_survey_first));
                    //shownotificationImagewithcount();
                }
                break;
            case R.id.FRMSelectSurvey:
                selectDefaultAns();

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

    public void selectDefaultAns()
    {

        dialog = new Dialog(SelectSurveyActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_select_survey);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        RVDialogDefaultAns=(RecyclerView)dialog.findViewById(R.id.RVDialogDefaultAns);

       /* LLbtnFB=(LinearLayout)dialog.findViewById(R.id.LLbtnFB);
        LLbtnTwitter=(LinearLayout)dialog.findViewById(R.id.LLbtnTwitter);
        LLbtnGplus=(LinearLayout)dialog.findViewById(R.id.LLbtnGplus);
        LLbtnGplus.setOnClickListener(this);
        LLbtnFB.setOnClickListener(this);
        LLbtnTwitter.setOnClickListener(this);*/

        setDefaultdata();
        dialog.show();
    }

    private  void setDefaultdata() {
        DefaultAnsList = new ArrayList<>();
       /* DefaultAnsList.add(new DefaultAnswerModel("1", "Dummy TExt 1"));
        DefaultAnsList.add(new DefaultAnswerModel("2", "Dummy TExt 2"));
        DefaultAnsList.add(new DefaultAnswerModel("3", "Dummy TExt 1"));
        DefaultAnsList.add(new DefaultAnswerModel("4", "Dummy TExt 1"));
        DefaultAnsList.add(new DefaultAnswerModel("5", "Dummy TExt 1"));
        DefaultAnsList.add(new DefaultAnswerModel("6", "Dummy TExt 1"));
        DefaultAnsList.add(new DefaultAnswerModel("7", "Dummy TExt 1"));*/

        DefaultAnsList = dbHelper.getArraylistSurveyList();
        if (DefaultAnsList!=null)
        {
            adapter = new DefaultAnswerAdapter(DefaultAnsList);

            LinearLayoutManager llm = new LinearLayoutManager(SelectSurveyActivity.this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);

            RVDialogDefaultAns.setLayoutManager(llm);
            //  RVQuestionGroup.addItemDecoration(new DividerItemDecoration(getActivity()));
        /*or*/
            RVDialogDefaultAns.addItemDecoration(new DividerItemDecoration(SelectSurveyActivity.this,R.drawable.divider));
            RVDialogDefaultAns.setAdapter(adapter);

            RVDialogDefaultAns.addOnItemTouchListener(
                    new RecyclerItemClickListener(SelectSurveyActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override public void onItemClick(View view, int position) {
                            // TODO Handle item click
                            // Manisha added 0n 23-04-20
//                            if(!dbHelper.checkcopyedornot(ProfileId)) {
//                                String data1 = dbHelper.copyandstoreuserans(SurveyID, UserID, ProfileId);
//                                //  Utils.showToast(ProfileActivity.this,"copied");
//                                Log.d("copystatus","copied");
//                            }

                            ///edtAnswer.setText(((TextView)view.findViewById(R.id.tvDefaultAnswer)).getText());
                            //((Drawer_Activity) getActivity()).ReplaceFragment(new EventosDetailFragment(), "fragemtn");
                            tvSelectedSurvey.setText((((TextView)view.findViewById(R.id.tvDefaultAnswer)).getText().toString()));
                            dialog.dismiss();
                           // Utils.showToast(SelectSurveyActivity.this,(((TextView)view.findViewById(R.id.tvDefaultAnswer)).getText().toString()));

                        }
                    })
            );
        }
    }

    public  void showProgressimage() {
        try {

            //View layout = LayoutInflater.from(CompleteSurveyAndSync.this).inflate(R.layout.popup_loading_image, null);
            popupWindow = new Dialog(SelectSurveyActivity.this);
             popupWindow.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //popupWindow.setTitle(getString(R.string.photouploading));
            popupWindow.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            popupWindow.setContentView(R.layout.popup_loading_image);
            popupWindow.setCancelable(false);

            progress =(ProgressBar)popupWindow.findViewById(R.id.progress);
            tvprogresspercentage=(TextView)popupWindow.findViewById(R.id.tvprogresspercentage);

            if (!((Activity) SelectSurveyActivity.this).isFinishing()) {
                popupWindow.show();
            }


          final Handler  handler = new Handler();
            Runnable r;
            handler.postDelayed(r = new Runnable() {
                @Override
                public void run() {
                i++;

                    handler.postDelayed(this, 1000);
                    progress.setProgress(i);
                    tvprogresspercentage.setText(i+" %");
                    if (i==100)
                    {
                     popupWindow.dismiss();
                    }

                }
            }, 3000);



        } catch (Exception e)

        {
            e.printStackTrace();
        }
    }
    public void shownotificationImagewithcount()
    {

        Intent notificationIntent = new Intent(SelectSurveyActivity.this, SplashActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(SelectSurveyActivity.this);
        stackBuilder.addParentStack(SplashActivity.class);
        stackBuilder.addNextIntent(notificationIntent);


        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(SelectSurveyActivity.this);
        String message ="";
		/*if (synccount==totalImageCount)
		{
			message=synccount+" Image Sync successfully";
		}
		else
		{
		}
		*/
        try {
            //message = dbHelper.getSurveyName(Profileid) + ": " + synccount + "/" + groupbyphotototal + " Image Sync in progress";
//            message = dbHelper.getSurveyNameSiteName(Profileid) + ": " + synccount + "/" + groupbyphotototal + " Image Sync in progress";

            message = "Site Name"+ ": My Site \nReview Audit: " + 1 + "/" + 5+ " Image Sync in progress";

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
    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.app_icon_white : R.mipmap.ic_launcher;
    }

}
