package com.passurvey.fragments;

/**
 * Created by Ravi on 29/07/15.
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.passurvey.R;
import com.passurvey.activities.MainActivity;

public class IncompleteSurveyFragment extends Fragment implements View.OnClickListener {

    private RelativeLayout rLSurveyTop;
    private ImageView imgCalendar;
    private TextView tvIncompleteDate;
    private ImageView imgWatch;
    private TextView tvIncompleteTime;
    private ImageView imgIncompleteImage;
    private LinearLayout LLIncompleteMain;
    public IncompleteSurveyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_incomplete_survey, container, false);
        bindid(rootView);

        // Inflate the layout for this fragment
        return rootView;
    }

    private void bindid(View rootView) {

        rootView.setOnClickListener(this);
        rLSurveyTop = (RelativeLayout) rootView.findViewById(R.id.RLSurveyTop);
        imgCalendar = (ImageView) rootView.findViewById(R.id.imgCalendar);
        tvIncompleteDate = (TextView) rootView.findViewById(R.id.tvIncompleteDate);
        imgWatch = (ImageView) rootView.findViewById(R.id.imgWatch);
        tvIncompleteTime = (TextView) rootView.findViewById(R.id.tvIncompleteTime);
        imgIncompleteImage = (ImageView) rootView.findViewById(R.id.imgIncompleteImage);
        LLIncompleteMain=(LinearLayout)rootView.findViewById(R.id.LLIncompleteMain);
        LLIncompleteMain.setOnClickListener(this);
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
            case R.id.LLIncompleteMain:
                //((MainActivity)getActivity()).ReplaceFragment(new QuestionGroupFragment(),"QuestionGroup",null);
                ((MainActivity)getActivity()).ReplaceFragment(new QuestionAnswerFragment(),"QuestionAnswer",null);
                break;
        }
    }
}
