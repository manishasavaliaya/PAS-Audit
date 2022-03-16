package com.passurvey.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.passurvey.R;
import com.passurvey.adapters.QuestionGroupAdapter;
import com.passurvey.fragments.FragmentDrawer;
import com.passurvey.model.QuestionGroupModel;
import com.passurvey.utility.DividerItemDecoration;
import com.passurvey.utility.RecyclerItemClickListener;
import com.passurvey.utility.Utils;

import java.util.ArrayList;
import java.util.List;

public class SelectQuestionGroupActivity extends MainActivity implements View.OnClickListener {
    private TextView tvQGNodata;
    private RecyclerView RVQuestionGroup;
    //ArrayList<QuestionGroupModel> QuestionGroupList;
    List<QuestionGroupModel> QuestionGroupList;
    QuestionGroupAdapter adapter;
    String SurveyId,ProfileId,Userid;
    Cursor cursor;

    @Override
    protected void onPause() {
        super.onPause();
       // handler.removeCallbacks(r);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.container_body); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.fragment_select_question_group, contentFrameLayout);
        // Utils.hideProgress();
        MainActivity.hideProgress();
        bindid();
    }

    private void bindid() {
        if (Utils.getSharedPreString(SelectQuestionGroupActivity.this,Utils.PREFS_SURVEYNAME)!=null&&!Utils.getSharedPreString(SelectQuestionGroupActivity.this,Utils.PREFS_SURVEYNAME).equalsIgnoreCase("")) {
            setHeaderTitle(Utils.getSharedPreString(SelectQuestionGroupActivity.this,Utils.PREFS_SURVEYNAME));
        }
        else {
            setHeaderTitle(getString(R.string.title_survey));
        }
        FragmentDrawer drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        //drawerFragment.updateDrawer();
        drawerFragment.expandable_drawer();

        RVQuestionGroup=(RecyclerView)findViewById(R.id.RVQuestionGroup);
        tvQGNodata=(TextView)findViewById(R.id.tvQGNodata);
        SurveyId = Utils.getSharedPreString(SelectQuestionGroupActivity.this, Utils.PREFS_SURVEYID);
        ProfileId= Utils.getSharedPreString(SelectQuestionGroupActivity.this, Utils.PREFS_PROFILEID);
        Userid = Utils.getSharedPreString(SelectQuestionGroupActivity.this, Utils.PREFS_USERID);
        // QuestionGroupList =dbHelper.getQuestionGroupModelArrayList(SurveyId,ProfileId);
        QuestionGroupList = dbHelper.getQuestionGroupModelArrayListDrawer(SurveyId,Userid,ProfileId);

         if(!checkalldatacomplete().equalsIgnoreCase(getString(R.string.completeProfile))) {
             if (QuestionGroupList != null) {
                 adapter = new QuestionGroupAdapter(QuestionGroupList, SelectQuestionGroupActivity.this);
                 LinearLayoutManager llm = new LinearLayoutManager(SelectQuestionGroupActivity.this);
                 llm.setOrientation(LinearLayoutManager.VERTICAL);

                 RVQuestionGroup.setLayoutManager(llm);
                 //  RVQuestionGroup.addItemDecoration(new DividerItemDecoration(getActivity()));
                 /*or*/
                 RVQuestionGroup.addItemDecoration(new DividerItemDecoration(SelectQuestionGroupActivity.this, R.drawable.divider));
                 RVQuestionGroup.setAdapter(adapter);

                 RVQuestionGroup.addOnItemTouchListener(
                         new RecyclerItemClickListener(SelectQuestionGroupActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                             @Override
                             public void onItemClick(View view, int position) {
                                 // TODO Handle item click
                                 // MainActivity.showProgress(SelectQuestionGroupActivity.this);
                                 // Utils.showToast(SelectQuestionGroupActivity.this,((TextView)view.findViewById(R.id.tvQGRowQuestionName)).getText().toString()+"Demo"+dbHelper.getGroupId(SurveyId,((TextView)view.findViewById(R.id.tvQGRowQuestionName)).getText().toString()));
                                 String Userid = "", Surveyid = "", Profileid = "", GroupId = "";
                                 Userid = Utils.getSharedPreString(SelectQuestionGroupActivity.this, Utils.PREFS_USERID);
                                 Surveyid = Utils.getSharedPreString(SelectQuestionGroupActivity.this, Utils.PREFS_SURVEYID);
                                 Profileid = Utils.getSharedPreString(SelectQuestionGroupActivity.this, Utils.PREFS_PROFILEID);
                                 GroupId = Utils.getSharedPreString(SelectQuestionGroupActivity.this, Utils.PREFS_GROUPID);
                                 int count = -1;
                                 if (GroupId == "") {
                                     count = 0;
                                 }

                                 MainActivity.showProgress(SelectQuestionGroupActivity.this);
                                 Utils.setSharedPreString(SelectQuestionGroupActivity.this, Utils.PREFS_GROUPID, dbHelper.getGroupId(SurveyId, ((TextView) view.findViewById(R.id.tvQGRowQuestionName)).getText().toString()));
                                 Utils.setSharedPreString(SelectQuestionGroupActivity.this, Utils.PREFS_GROUPNAME, ((TextView) view.findViewById(R.id.tvQGRowQuestionName)).getText().toString());
                                 Utils.setSharedPreString(SelectQuestionGroupActivity.this, Utils.PREFS_DRAWER, ((TextView) view.findViewById(R.id.tvQGRowQuestionName)).getText().toString());
                                 //comment by mena
                                 startActivity(new Intent(SelectQuestionGroupActivity.this, QuestionAnswerActivity.class));
                                 finish();
                           /* count = MainActivity.dbHelper.getQuestionGroupCount(Surveyid,Userid,Profileid,GroupId);
                            if (dbHelper.getGroupId(SurveyId, ((TextView) view.findViewById(R.id.tvQGRowQuestionName)).getText().toString()).equalsIgnoreCase(GroupId.toString())||count==0)
                            {
                                MainActivity.showProgress(SelectQuestionGroupActivity.this);
                                Utils.setSharedPreString(SelectQuestionGroupActivity.this, Utils.PREFS_GROUPID, dbHelper.getGroupId(SurveyId, ((TextView) view.findViewById(R.id.tvQGRowQuestionName)).getText().toString()));
                                Utils.setSharedPreString(SelectQuestionGroupActivity.this, Utils.PREFS_GROUPNAME,((TextView) view.findViewById(R.id.tvQGRowQuestionName)).getText().toString());
                                Utils.setSharedPreString(SelectQuestionGroupActivity.this, Utils.PREFS_DRAWER,((TextView) view.findViewById(R.id.tvQGRowQuestionName)).getText().toString());
                                //comment by mena
                                startActivity(new Intent(SelectQuestionGroupActivity.this,QuestionAnswerActivity.class));
                                finish();
                            }
                            else if(count==-1||count>0)
                            {
                                Toast.makeText(SelectQuestionGroupActivity.this, getString(R.string.toast_completequstiongroupfirst)+" "+MainActivity.dbHelper.getGroupNamefromGroupid(Surveyid, GroupId),
                                        Toast.LENGTH_SHORT).show();
                            }*/
                             }
                         })
                 );
             } else {
                 Utils.showToast(SelectQuestionGroupActivity.this, "No Question Group Found");
             }
         } else{
             //  profilenotcompFlag=true;
             Utils.showToast(SelectQuestionGroupActivity.this, getString(R.string.completeProfile));
         }
        //setdummydata();
    }

    public String  checkalldatacomplete() {
        cursor = dbHelper.checkIncompleteSurveyLogin1(Utils.getSharedPreString(SelectQuestionGroupActivity.this, Utils.PREFS_USERID));
        try {
            if (cursor != null && cursor.getCount() > 0) {
                return getString(R.string.completesurvey);
            } else {
                if (dbHelper.checkprofilecomplete(ProfileId) && dbHelper.checksummary(ProfileId)) {
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
    public void onClick(View v) {
        switch (v.getId()) {


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


    private  void setdummydata() {
        QuestionGroupList = new ArrayList<>();
        QuestionGroupList.add(new QuestionGroupModel("1", "Dummy TExt 1", "1"));
        QuestionGroupList.add(new QuestionGroupModel("2", "Dummy TExt 2", "2"));
        QuestionGroupList.add(new QuestionGroupModel("3", "Dummy TExt 1", "3"));
        QuestionGroupList.add(new QuestionGroupModel("4", "Dummy TExt 1", "4"));
        QuestionGroupList.add(new QuestionGroupModel("5", "Dummy TExt 1", "5"));
        QuestionGroupList.add(new QuestionGroupModel("6", "Dummy TExt 1", "6"));
        QuestionGroupList.add(new QuestionGroupModel("7", "Dummy TExt 1", "7"));
        adapter = new QuestionGroupAdapter(QuestionGroupList,SelectQuestionGroupActivity.this);

        LinearLayoutManager llm = new LinearLayoutManager(SelectQuestionGroupActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        RVQuestionGroup.setLayoutManager(llm);
        //  RVQuestionGroup.addItemDecoration(new DividerItemDecoration(getActivity()));
        /*or*/
        RVQuestionGroup.addItemDecoration(new DividerItemDecoration(SelectQuestionGroupActivity.this,R.drawable.divider));
        RVQuestionGroup.setAdapter(adapter);

        RVQuestionGroup.addOnItemTouchListener(
                new RecyclerItemClickListener(SelectQuestionGroupActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        //((Drawer_Activity) getActivity()).ReplaceFragment(new EventosDetailFragment(), "fragemtn");
                      startActivity(new Intent(SelectQuestionGroupActivity.this,QuestionAnswerActivity.class));
                        finish();
                    }
                })
        );


    }
     public void refresh()
    {
        QuestionGroupList =dbHelper.getQuestionGroupModelArrayListDrawer(SurveyId,Userid,ProfileId);
        adapter=null;
        adapter = new QuestionGroupAdapter(QuestionGroupList,SelectQuestionGroupActivity.this);
       // RVQuestionGroup.invalidate();
        //LinearLayoutManager llm = new LinearLayoutManager(SelectQuestionGroupActivity.this);
      //  llm.setOrientation(LinearLayoutManager.VERTICAL);

       // RVQuestionGroup.setLayoutManager(llm);
        //  RVQuestionGroup.addItemDecoration(new DividerItemDecoration(getActivity()));
        /*or*/
        //RVQuestionGroup.addItemDecoration(new DividerItemDecoration(SelectQuestionGroupActivity.this,R.drawable.divider));
        RVQuestionGroup.setAdapter(adapter);


        FragmentDrawer drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        //drawerFragment.updateDrawer();
        drawerFragment.expandable_drawer();


    }
}
