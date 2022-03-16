package com.passurvey.fragments;

/**
 * Created by Ravi on 29/07/15.
 */

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.passurvey.R;
import com.passurvey.adapters.DefaultAnswerAdapter;
import com.passurvey.adapters.SpinnerPriorityAdapter;
import com.passurvey.model.DefaultAnswerModel;
import com.passurvey.utility.DividerItemDecoration;

import java.util.ArrayList;


public class QuestionAnswerFragment extends Fragment implements View.OnClickListener,AdapterView.OnItemSelectedListener {
    private RelativeLayout rLSurveyTop;
    private TextView tvQuestionName;
    private ImageView imganswerIcon;
 //   private Spinner SpinPriority;
    private LinearLayout lLSelectDefaultAns;
    private ImageView imgansSelIcon;

    Dialog dialog;
    String[] priority={"High","Medium","Low"};
    int icons[] = {R.drawable.green_circle, R.drawable.orange_circle, R.drawable.red_circle};

    /* Default ans*/
    private RecyclerView RVDialogDefaultAns;

    ArrayList<DefaultAnswerModel> DefaultAnsList;
    DefaultAnswerAdapter adapter;
    public QuestionAnswerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_question_answer, container, false);
        bindid(rootView);

        // Inflate the layout for this fragment
        return rootView;
    }

    private void bindid(View rootView) {

        rootView.setOnClickListener(this);
        //SpinPriority = (Spinner) rootView.findViewById(R.id.SpinPriority);
       // SpinPriority.setOnItemSelectedListener(this);

        SpinnerPriorityAdapter customAdapter=new SpinnerPriorityAdapter(getActivity(),icons,priority);
       // SpinPriority.setAdapter(customAdapter);

        rLSurveyTop = (RelativeLayout) rootView.findViewById(R.id.RLSurveyTop);
        tvQuestionName = (TextView) rootView.findViewById(R.id.tvQuestionName);
        imganswerIcon = (ImageView) rootView.findViewById(R.id.imganswer_icon);

        lLSelectDefaultAns = (LinearLayout) rootView.findViewById(R.id.LLSelectDefaultAns);
        imgansSelIcon = (ImageView) rootView.findViewById(R.id.imgans_sel_icon);
        rootView.findViewById(R.id.btnQABack).setOnClickListener(this);
        rootView.findViewById(R.id.btnQANext).setOnClickListener(this);
        lLSelectDefaultAns.setOnClickListener(this);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            /*case R.id.LLIncompleteMain:
                ((MainActivity)getActivity()).ReplaceFragment(new QuestionGroupFragment(),"QuestionGroup",null);
                break;*/
            case R.id.LLSelectDefaultAns:
                selectDefaultAns();
                break;

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), priority[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void selectDefaultAns()
    {

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_default_answer);
        RVDialogDefaultAns=(RecyclerView)dialog.findViewById(R.id.RVDialogDefaultAns);


       /* LLbtnFB=(LinearLayout)dialog.findViewById(R.id.LLbtnFB);
        LLbtnTwitter=(LinearLayout)dialog.findViewById(R.id.LLbtnTwitter);
        LLbtnGplus=(LinearLayout)dialog.findViewById(R.id.LLbtnGplus);
        LLbtnGplus.setOnClickListener(this);
        LLbtnFB.setOnClickListener(this);
        LLbtnTwitter.setOnClickListener(this);*/

        setdummydata();

        dialog.show();
    }
    private  void setdummydata() {
        DefaultAnsList = new ArrayList<>();
        DefaultAnsList.add(new DefaultAnswerModel("1", "Dummy TExt 1"));
        DefaultAnsList.add(new DefaultAnswerModel("2", "Dummy TExt 2"));
        DefaultAnsList.add(new DefaultAnswerModel("3", "Dummy TExt 1"));
        DefaultAnsList.add(new DefaultAnswerModel("4", "Dummy TExt 1"));
        DefaultAnsList.add(new DefaultAnswerModel("5", "Dummy TExt 1"));
        DefaultAnsList.add(new DefaultAnswerModel("6", "Dummy TExt 1"));
        DefaultAnsList.add(new DefaultAnswerModel("7", "Dummy TExt 1"));
        adapter = new DefaultAnswerAdapter(DefaultAnsList);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        RVDialogDefaultAns.setLayoutManager(llm);
        //  RVQuestionGroup.addItemDecoration(new DividerItemDecoration(getActivity()));
        /*or*/
        RVDialogDefaultAns.addItemDecoration(new DividerItemDecoration(getActivity(),R.drawable.divider));
        RVDialogDefaultAns.setAdapter(adapter);
    }
}
