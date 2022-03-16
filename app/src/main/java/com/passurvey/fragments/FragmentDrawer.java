package com.passurvey.fragments;

/**
 * Created by Ravi on 29/07/15.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.UiThread;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.passurvey.R;
import com.passurvey.activities.AttachedImagesActivity;
import com.passurvey.activities.CompleteSurveyAndSync;
import com.passurvey.activities.FillOutDetailsDrawerActivity;
import com.passurvey.activities.InCompleteSurveyActivity;
import com.passurvey.activities.MainActivity;
import com.passurvey.activities.ProfileActivity;
import com.passurvey.activities.ProfileDrawerActivity;
import com.passurvey.activities.QuestionAnswerActivity;
import com.passurvey.activities.SelectSurveyActivity;
import com.passurvey.activities.SplashActivity;
import com.passurvey.adapters.NavigationDrawerAdapterQG;
import com.passurvey.adapters.SelectQuestionNameAdapter;
import com.passurvey.database.DBHelper;
import com.passurvey.expandablelistModel.Ingredient;
import com.passurvey.expandablelistModel.Recipe;
import com.passurvey.expandablelistModel.RecipeAdapter;
import com.passurvey.model.QuestionGroupModel;
import com.passurvey.model.QuestionNameModel;
import com.passurvey.utility.DividerItemDecoration;
import com.passurvey.utility.RecyclerItemClickListener;
import com.passurvey.utility.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class FragmentDrawer extends Fragment implements View.OnClickListener {

    private static String TAG = FragmentDrawer.class.getSimpleName();
    Dialog dialog;
    private RecyclerView RVQuestionName;

    private RecipeAdapter mAdapter;

private  ArrayList<QuestionNameModel>questionNameList;
    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationDrawerAdapterQG adapter;
    private View containerView;
    private static String[] titles = null;
    private FragmentDrawerListener drawerListener;
    private Button btnDrawerProfile;
    private LinearLayout LLDrawerLogout;
    public static   DBHelper dbHelper;
    FrameLayout imgMenu;
    private SelectQuestionNameAdapter questionnameAdapter;
    private TextView tvUserName,tvHeaderSurveyName;
    private LinearLayout LLDrawerAuditHeader;


    String UserId,Surveyid,ProfileId,GroupId;

    public FragmentDrawer() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    public  List<QuestionGroupModel> getData() {
        List<QuestionGroupModel> data = new ArrayList<>();
        String Userid = null,Surveyid = null;
        Utils.getSharedPreString(getActivity(),Utils.PREFS_USERID);
        Utils.getSharedPreString(getActivity(),Utils.PREFS_SURVEYID);
        ProfileId=Utils.getSharedPreString(getActivity(),Utils.PREFS_PROFILEID);
        if (Userid!=null&&!Userid.equalsIgnoreCase("")&&Surveyid!=null&&!Surveyid.equalsIgnoreCase("")) {
            data = dbHelper.getQuestionGroupModelArrayListDrawer(Surveyid,Userid,ProfileId);
        }

        // preparing navigation drawer items
        /*for (int i = 0; i < titles.length; i++) {
            QuestionGroupModel navItem = new QuestionGroupModel();
            navItem.setQuestionGroupName(titles[i]);
            data.add(navItem);
        }*/
        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = DBHelper.getInstance(getActivity());
        // drawer labels
        titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        tvUserName =(TextView)layout.findViewById(R.id.tvUserName) ;
        tvHeaderSurveyName=(TextView)layout.findViewById(R.id.tvHeaderSurveyName) ;
        btnDrawerProfile=(Button)layout.findViewById(R.id.btnDrawerProfile);
        LLDrawerLogout=(LinearLayout) layout.findViewById(R.id.LLDrawerLogout);
        LLDrawerAuditHeader=(LinearLayout) layout.findViewById(R.id.LLDrawerAuditHeader);
        LLDrawerAuditHeader.setOnClickListener(this);
        if (Utils.getSharedPreString(getActivity(),"display_name")!=null&&!Utils.getSharedPreString(getActivity(),"display_name").equalsIgnoreCase(""))
        {
            tvUserName.setText(Utils.getSharedPreString(getActivity(),"display_name"));
        }
        if (Utils.getSharedPreString(getActivity(),Utils.PREFS_SURVEYNAME)!=null&&!Utils.getSharedPreString(getActivity(),Utils.PREFS_SURVEYNAME).equalsIgnoreCase(""))
        {
            LLDrawerAuditHeader.setVisibility(View.VISIBLE);
            tvHeaderSurveyName.setText(Utils.getSharedPreString(getActivity(),Utils.PREFS_SURVEYNAME));
        }
        else {
            LLDrawerAuditHeader.setVisibility(View.GONE);
            //setHeaderTitle(getString(R.string.title_survey));
        }
        //updateDrawer();

        expandable_drawer();

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
       /* List<QuestionGroupModel> data = new ArrayList<>();
        String Userid = null,Surveyid = null;
        Userid= Utils.getSharedPreString(getActivity(),Utils.PREFS_USERID);
        Surveyid= Utils.getSharedPreString(getActivity(),Utils.PREFS_SURVEYID);
        if (Userid!=null&&!Userid.equalsIgnoreCase("")&&Surveyid!=null&&!Surveyid.equalsIgnoreCase("")) {
            data = dbHelper.getQuestionGroupModelArrayListDrawer(Surveyid,Userid);
        }
        if (data!=null) {
            adapter = new NavigationDrawerAdapterQG(getActivity(), data);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(llm);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
     //   recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),R.drawable.divider));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                drawerListener.onDrawerItemSelected(view, position);
                mDrawerLayout.closeDrawer(containerView);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
        }*/

        LLDrawerLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(getString(R.string.wantlogout))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                              //  Utils.showProgress(getActivity());
                                Utils.removeSharedPref(getActivity());
                                startActivity(new Intent(getActivity(), SplashActivity.class));
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        btnDrawerProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserId=Utils.getSharedPreString(getActivity(),Utils.PREFS_USERID);
                Surveyid=Utils.getSharedPreString(getActivity(),Utils.PREFS_SURVEYID);
                ProfileId=Utils.getSharedPreString(getActivity(),Utils.PREFS_PROFILEID);
                //GroupId=Utils.getSharedPreString(getActivity(),Utils.PREFS_GROUPID);
                if (!ProfileId.equalsIgnoreCase("")&&!Surveyid.equalsIgnoreCase("")) {
                    Cursor cursor=dbHelper.getUserInfo(Surveyid,UserId,ProfileId);
                    try {
                        cursor.moveToFirst();
                        if (cursor != null && cursor.getCount() > 0) {
                            startActivity(new Intent(getActivity(), ProfileDrawerActivity.class));
                            getActivity().finish();
                        }
                        else {
//                            Utils.showToast(getActivity(), getString(R.string.select_and_fill));
                            Intent intent = new Intent(getActivity(), ProfileActivity.class);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    } finally {
                        cursor.close();
                    }
                } else {
                    Utils.showToast(getActivity(),getString(R.string.select_and_fill));
                }
            }
        });
        return layout;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        imgMenu =(FrameLayout)toolbar.findViewById(R.id.imgMenu);
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerToggle.setDrawerIndicatorEnabled(false);

        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_menu();

            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v == LLDrawerAuditHeader){
            startActivity(new Intent(getActivity(), InCompleteSurveyActivity.class));
            getActivity().finish();
        }
    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }


    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }
    public  void open_menu()
    {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    }

    public void openQuestionNameDiaolg()
    {

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_question_name);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        RVQuestionName=(RecyclerView)dialog.findViewById(R.id.RVQuestionName);

        setQuestionName();

        dialog.show();
    }

    private  void setQuestionName() {
        Surveyid= Utils.getSharedPreString(getActivity(),Utils.PREFS_SURVEYID);
        GroupId=Utils.getSharedPreString(getActivity(),Utils.PREFS_GROUPID);
        UserId=Utils.getSharedPreString(getActivity(),Utils.PREFS_USERID);
        ProfileId=Utils.getSharedPreString(getActivity(),Utils.PREFS_PROFILEID);
        questionNameList = new ArrayList<>();

        questionNameList = dbHelper.getQuestionNameModelArrayList(Surveyid,GroupId,UserId,ProfileId);
        if (questionNameList!=null)
        {
            questionnameAdapter = new SelectQuestionNameAdapter(questionNameList);

            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.VERTICAL);

            RVQuestionName.setLayoutManager(llm);
            RVQuestionName.addItemDecoration(new DividerItemDecoration(getActivity(),R.drawable.divider));
            RVQuestionName.setAdapter(questionnameAdapter);

            RVQuestionName.addOnItemTouchListener(
                    new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                        @Override public void onItemClick(View view, int position) {
                            // TODO Handle item click
                            dialog.dismiss();
                        }
                    })
            );
        }
    }

    public void updateDrawer() {
        List<QuestionGroupModel> data = new ArrayList<>();
        String Userid = "", Surveyid = "",Profileid="";
        Userid = Utils.getSharedPreString(getActivity(), Utils.PREFS_USERID);
        Surveyid = Utils.getSharedPreString(getActivity(), Utils.PREFS_SURVEYID);
        Profileid= Utils.getSharedPreString(getActivity(), Utils.PREFS_PROFILEID);
        if (Userid != null && !Userid.equalsIgnoreCase("") && Surveyid != null && !Surveyid.equalsIgnoreCase("")&& !Profileid.equalsIgnoreCase("")) {
            data = dbHelper.getQuestionGroupModelArrayListDrawer(Surveyid, Userid,Profileid);
        }
        if (data != null) {
            if (data.size()>0) {
                data.add(new QuestionGroupModel("Photos", "Support Photos", "Photos"));
                data.add(new QuestionGroupModel("Summary", "Summary", "Summary"));
                data.add(new QuestionGroupModel("Complete & Sync", "Complete & Sync", "Complete & Sync"));
            }
            adapter = new NavigationDrawerAdapterQG(getActivity(), data);

            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.VERTICAL);

            recyclerView.setLayoutManager(llm);
            //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            //   recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),R.drawable.divider));
            recyclerView.setAdapter(adapter);
            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    //comment by mena
                    drawerListener.onDrawerItemSelected(view, position);
                    mDrawerLayout.closeDrawer(containerView);
                    //comment by mena

                    String SurveyId = Utils.getSharedPreString(getActivity(),Utils.PREFS_SURVEYID);
                    if (!((TextView)view.findViewById(R.id.tvQGRowQuestionName)).getText().toString().equalsIgnoreCase("Summary")&&!((TextView)view.findViewById(R.id.tvQGRowQuestionName)).getText().toString().equalsIgnoreCase("Support Photos")&&!((TextView)view.findViewById(R.id.tvQGRowQuestionName)).getText().toString().equalsIgnoreCase("Complete & Sync")) {
                        //comment by mena
                         ((MainActivity)getActivity()).showprogressnew();
                        //comment by mena

                        Utils.setSharedPreString(getActivity(), Utils.PREFS_GROUPID, dbHelper.getGroupId(SurveyId, ((TextView) view.findViewById(R.id.tvQGRowQuestionName)).getText().toString()));
                        Utils.setSharedPreString(getActivity(), Utils.PREFS_GROUPNAME,((TextView) view.findViewById(R.id.tvQGRowQuestionName)).getText().toString());
                        Utils.setSharedPreString(getActivity(), Utils.PREFS_DRAWER,((TextView) view.findViewById(R.id.tvQGRowQuestionName)).getText().toString());

                        //comment by mena

                        Intent intent = new Intent(((MainActivity)getActivity()), QuestionAnswerActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        getActivity().finish();
                        //comment by mena
                        //edit by mena
                       // openQuestionNameDiaolg();
                        //edit by mena
                    }
                    else if (((TextView)view.findViewById(R.id.tvQGRowQuestionName)).getText().toString().equalsIgnoreCase("Summary"))
                    {
                         //   Utils.showToast(getActivity(),((TextView)view.findViewById(R.id.tvQGRowQuestionName)).getText().toString());

                        Utils.setSharedPreString(getActivity(), Utils.PREFS_DRAWER,"Summary");
                        Intent intent = new Intent(((MainActivity)getActivity()), FillOutDetailsDrawerActivity.class);

                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        getActivity().finish();
                    }
                    else if (((TextView)view.findViewById(R.id.tvQGRowQuestionName)).getText().toString().equalsIgnoreCase("Support Photos"))
                    {

                        Utils.setSharedPreString(getActivity(), Utils.PREFS_DRAWER,"Support Photos");
                       // Utils.showToast(getActivity(),((TextView)view.findViewById(R.id.tvQGRowQuestionName)).getText().toString());
                        startActivity(new Intent(getActivity(), AttachedImagesActivity.class));
                        //startActivity(new Intent(getActivity(), PhotoActivity.class));
                        getActivity().finish();
                    }
                    else if (((TextView)view.findViewById(R.id.tvQGRowQuestionName)).getText().toString().equalsIgnoreCase("Complete & Sync"))
                    {

                        Utils.setSharedPreString(getActivity(), Utils.PREFS_DRAWER,"Complete & Sync");
                        // Utils.showToast(getActivity(),((TextView)view.findViewById(R.id.tvQGRowQuestionName)).getText().toString());
                        startActivity(new Intent(getActivity(), CompleteSurveyAndSync.class));
                        getActivity().finish();
                    }

                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));
        }
    }

    public void expandable_drawer()
    {
       /* Ingredient beef = new Ingredient("beef","1");
        Ingredient cheese = new Ingredient("cheese", "2");
        Ingredient salsa = new Ingredient("salsa", "3");
        Ingredient tortilla = new Ingredient("tortilla", "4");
        Ingredient ketchup = new Ingredient("ketchup", "5");
        Ingredient bun = new Ingredient("bun", "6");*/

        Recipe taco = new Recipe("Photos", "Support Photos", "Photos",  Arrays.<Ingredient>asList());
        Recipe quesadilla = new Recipe("Summary", "Summary", "Summary", Arrays.<Ingredient>asList());
        Recipe burgera = new Recipe("Complete & Sync", "Complete & Sync", "Complete & Sync", Arrays.<Ingredient>asList());
        String Userid = "", Surveyid = "",Profileid="";
        Userid = Utils.getSharedPreString(getActivity(), Utils.PREFS_USERID);
        Surveyid = Utils.getSharedPreString(getActivity(), Utils.PREFS_SURVEYID);
        Profileid= Utils.getSharedPreString(getActivity(), Utils.PREFS_PROFILEID);
         List<Recipe> recipes =new ArrayList<>();
        if (Userid != null && !Userid.equalsIgnoreCase("") && Surveyid != null && !Surveyid.equalsIgnoreCase("")&& !Profileid.equalsIgnoreCase("")) {
            //data = dbHelper.getQuestionGroupModelArrayListDrawer(Surveyid, Userid,Profileid);
            recipes =dbHelper.getQuestionGroupModelArrayListDrawernew(Surveyid, Userid,Profileid);
        }
        if (recipes!=null&&recipes.size()>0)
        {
            recipes.add(new Recipe("Photos", "Support Photos", "Photos",  Arrays.<Ingredient>asList()));
            recipes.add(new Recipe("Summary", "Summary", "Summary", Arrays.<Ingredient>asList()));
            recipes.add(new Recipe("Complete & Sync", "Complete & Sync", "Complete & Sync", Arrays.<Ingredient>asList()));
        }

        //final List<Recipe> recipes = Arrays.asList(taco, quesadilla, burger,burgera);
       // recipes.add(quesadilla);
      //  RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

       // recipes = Arrays.asList(taco, quesadilla, burgera);
        if (recipes==null)
        {
            recipes=new ArrayList<>();
        }
        mAdapter = new RecipeAdapter(getActivity(), recipes);
        final List<Recipe> finalRecipes = recipes;


        mAdapter.setExpandCollapseListener(new ExpandableRecyclerAdapter.ExpandCollapseListener() {
            @UiThread
            @Override
            public void onParentExpanded(int parentPosition) {
                Recipe expandedRecipe = finalRecipes.get(parentPosition);

                /*String toastMsg = "Expanded "+expandedRecipe.getName();
                Toast.makeText(getActivity(),
                        toastMsg,
                        Toast.LENGTH_SHORT)
                        .show();*/


                String SurveyId = Utils.getSharedPreString(getActivity(),Utils.PREFS_SURVEYID);
                if (!expandedRecipe.getName().toString().equalsIgnoreCase("Summary")&&!expandedRecipe.getName().toString().equalsIgnoreCase("Support Photos")&&!expandedRecipe.getName().toString().equalsIgnoreCase("Complete & Sync")) {
                    //comment by mena
                    /*((MainActivity)getActivity()).showprogressnew();
                    //comment by mena

                    Utils.setSharedPreString(getActivity(), Utils.PREFS_GROUPID, dbHelper.getGroupId(SurveyId, ((TextView) view.findViewById(R.id.tvQGRowQuestionName)).getText().toString()));
                    Utils.setSharedPreString(getActivity(), Utils.PREFS_GROUPNAME,((TextView) view.findViewById(R.id.tvQGRowQuestionName)).getText().toString());
                    Utils.setSharedPreString(getActivity(), Utils.PREFS_DRAWER,((TextView) view.findViewById(R.id.tvQGRowQuestionName)).getText().toString());

                    //comment by mena

                    Intent intent = new Intent(((MainActivity)getActivity()), QuestionAnswerActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    getActivity().finish();
                    //comment by mena

                    //edit by mena
                    // openQuestionNameDiaolg();
                    //edit by mena*/

                    String Groupname= Utils.getSharedPreString(getActivity(),Utils.PREFS_GROUPNAME);
                    if (Groupname.equalsIgnoreCase("")||Groupname.equalsIgnoreCase(expandedRecipe.getName().toString())) {

                        Utils.setSharedPreString(getActivity(),"DPosition",parentPosition+"");
                        Utils.setSharedPreString(getActivity(), Utils.PREFS_DRAWEREXPANDPOSITION, parentPosition + "");
                        Utils.setSharedPreString(getActivity(), Utils.PREFS_DRAWEREXPANDNAME, expandedRecipe.getName().toString());
                    }


                }
                else if (expandedRecipe.getName().toString().equalsIgnoreCase("Summary"))
                {
                    //   Utils.showToast(getActivity(),((TextView)view.findViewById(R.id.tvQGRowQuestionName)).getText().toString());

                    Utils.setSharedPreString(getActivity(), Utils.PREFS_DRAWER,"Summary");
                    Intent intent = new Intent(((MainActivity)getActivity()), FillOutDetailsDrawerActivity.class);

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    getActivity().finish();
                }
                else if (expandedRecipe.getName().toString().equalsIgnoreCase("Support Photos"))
                {

                    Utils.setSharedPreString(getActivity(), Utils.PREFS_DRAWER,"Support Photos");
                    // Utils.showToast(getActivity(),((TextView)view.findViewById(R.id.tvQGRowQuestionName)).getText().toString());
                    startActivity(new Intent(getActivity(), AttachedImagesActivity.class));
                    //startActivity(new Intent(getActivity(), PhotoActivity.class));
                    getActivity().finish();
                }
                else if (expandedRecipe.getName().toString().equalsIgnoreCase("Complete & Sync"))
                {

                    Utils.setSharedPreString(getActivity(), Utils.PREFS_DRAWER,"Complete & Sync");
                    // Utils.showToast(getActivity(),((TextView)view.findViewById(R.id.tvQGRowQuestionName)).getText().toString());
                    startActivity(new Intent(getActivity(), CompleteSurveyAndSync.class));
                    getActivity().finish();
                }

            }

            @UiThread
            @Override
            public void onParentCollapsed(int parentPosition) {
                Recipe collapsedRecipe = finalRecipes.get(parentPosition);

                /*String toastMsg = "collapsed "+collapsedRecipe.getName();
                Toast.makeText(getActivity(),
                        toastMsg,
                        Toast.LENGTH_SHORT)
                        .show();*/

                if (!collapsedRecipe.getName().toString().equalsIgnoreCase("Summary")&&!collapsedRecipe.getName().toString().equalsIgnoreCase("Support Photos")&&!collapsedRecipe.getName().toString().equalsIgnoreCase("Complete & Sync")) {
                    //comment by mena
                    /*((MainActivity)getActivity()).showprogressnew();
                    //comment by mena

                    Utils.setSharedPreString(getActivity(), Utils.PREFS_GROUPID, dbHelper.getGroupId(SurveyId, ((TextView) view.findViewById(R.id.tvQGRowQuestionName)).getText().toString()));
                    Utils.setSharedPreString(getActivity(), Utils.PREFS_GROUPNAME,((TextView) view.findViewById(R.id.tvQGRowQuestionName)).getText().toString());
                    Utils.setSharedPreString(getActivity(), Utils.PREFS_DRAWER,((TextView) view.findViewById(R.id.tvQGRowQuestionName)).getText().toString());

                    //comment by mena

                    Intent intent = new Intent(((MainActivity)getActivity()), QuestionAnswerActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    getActivity().finish();
                    //comment by mena

                    //edit by mena
                    // openQuestionNameDiaolg();
                    //edit by mena*/
                    String Groupname= Utils.getSharedPreString(getActivity(),Utils.PREFS_GROUPNAME);
                    if (Groupname.equalsIgnoreCase("")) {
                        Utils.setSharedPreString(getActivity(),"DPosition",parentPosition+"");
                        Utils.setSharedPreString(getActivity(), Utils.PREFS_DRAWEREXPANDPOSITION, parentPosition + "");
                        Utils.setSharedPreString(getActivity(), Utils.PREFS_DRAWEREXPANDNAME, collapsedRecipe.getName().toString());
                    }


                }
                else if (collapsedRecipe.getName().toString().equalsIgnoreCase("Summary"))
                {
                    //   Utils.showToast(getActivity(),((TextView)view.findViewById(R.id.tvQGRowQuestionName)).getText().toString());

                    Utils.setSharedPreString(getActivity(), Utils.PREFS_DRAWER,"Summary");
                    Intent intent = new Intent(((MainActivity)getActivity()), FillOutDetailsDrawerActivity.class);

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    getActivity().finish();
                }
                else if (collapsedRecipe.getName().toString().equalsIgnoreCase("Support Photos"))
                {

                    Utils.setSharedPreString(getActivity(), Utils.PREFS_DRAWER,"Support Photos");
                    // Utils.showToast(getActivity(),((TextView)view.findViewById(R.id.tvQGRowQuestionName)).getText().toString());
                    startActivity(new Intent(getActivity(), AttachedImagesActivity.class));
                    //startActivity(new Intent(getActivity(), PhotoActivity.class));
                    getActivity().finish();
                }
                else if (collapsedRecipe.getName().toString().equalsIgnoreCase("Complete & Sync"))
                {

                    Utils.setSharedPreString(getActivity(), Utils.PREFS_DRAWER,"Complete & Sync");
                    // Utils.showToast(getActivity(),((TextView)view.findViewById(R.id.tvQGRowQuestionName)).getText().toString());
                    startActivity(new Intent(getActivity(), CompleteSurveyAndSync.class));
                    getActivity().finish();
                }
            }
        });

        String DrawerGroupname,Groupname,PositionParent;

        DrawerGroupname = Utils.getSharedPreString(getActivity(),Utils.PREFS_DRAWEREXPANDNAME);
        Groupname= Utils.getSharedPreString(getActivity(),Utils.PREFS_GROUPNAME);
        PositionParent= Utils.getSharedPreString(getActivity(),Utils.PREFS_DRAWEREXPANDPOSITION);
        String npos= Utils.getSharedPreString(getActivity(),"DPosition");
        //Utils.setSharedPreString(getActivity(),"DPosition",parentPosition+"")

        if (mAdapter!=null&&mAdapter.getItemCount()>0) {
            if (DrawerGroupname.equalsIgnoreCase(Groupname)) {

                try {
                    int pos = Integer.parseInt(PositionParent);
                    try {
                        if (pos<=mAdapter.getItemCount()) {
                            mAdapter.expandParent(pos);
                        }
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }

                } catch (NumberFormatException ex) {

                }


            }
        }


        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
