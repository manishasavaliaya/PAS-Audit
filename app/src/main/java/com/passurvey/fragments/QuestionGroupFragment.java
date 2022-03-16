package com.passurvey.fragments;

/**
 * Created by Ravi on 29/07/15.
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.passurvey.R;
import com.passurvey.adapters.QuestionGroupAdapter;
import com.passurvey.model.QuestionGroupModel;
import com.passurvey.utility.DividerItemDecoration;

import java.util.ArrayList;


public class QuestionGroupFragment extends Fragment implements View.OnClickListener{

    private TextView tvQGNodata;
    private RecyclerView RVQuestionGroup;

    ArrayList<QuestionGroupModel> QuestionGroupList;
   QuestionGroupAdapter adapter;

    public QuestionGroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_select_question_group, container, false);
        bindid(rootView);

        // Inflate the layout for this fragment
        return rootView;
    }

    private void bindid(View rootView) {
        rootView.setOnClickListener(this);
        RVQuestionGroup=(RecyclerView)rootView.findViewById(R.id.RVQuestionGroup);
        tvQGNodata=(TextView)rootView.findViewById(R.id.tvQGNodata);
        setdummydata();
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
        adapter = new QuestionGroupAdapter(QuestionGroupList,getActivity());

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        RVQuestionGroup.setLayoutManager(llm);
      //  RVQuestionGroup.addItemDecoration(new DividerItemDecoration(getActivity()));
        /*or*/
        RVQuestionGroup.addItemDecoration(new DividerItemDecoration(getActivity(),R.drawable.divider));
        RVQuestionGroup.setAdapter(adapter);
    }

}
