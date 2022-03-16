package com.passurvey.fragments;

/**
 * Created by Ravi on 29/07/15.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.passurvey.R;
import com.passurvey.activities.MainActivity;


public class SelectSurveyFragment extends Fragment implements OnClickListener{

    private Spinner spinner;
    private Button btnStartSurvey;
    public SelectSurveyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_select_survey, container, false);
        bindid(rootView);

        // Inflate the layout for this fragment
        return rootView;
    }

    private void bindid(View rootView) {
        rootView.setOnClickListener(this);
        ((MainActivity)getActivity()).setHeaderTitle(getString(R.string.title_survey));
       // spinner = (Spinner)rootView. findViewById(R.id.SpinSelectSurvey);
        btnStartSurvey =(Button)rootView.findViewById(R.id.btnStartSurvey);
        btnStartSurvey.setOnClickListener(this);

      //  String[] years = {"1996"};


       // ArrayAdapter<CharSequence> langAdapter = new ArrayAdapter<CharSequence>(getActivity(),R.layout.spinner_survey_row, years);
        ArrayAdapter<CharSequence> langAdapter = new ArrayAdapter<CharSequence>(getActivity(),R.layout.spinner_survey_row, MainActivity.dbHelper.getSurveyList() );

        langAdapter.setDropDownViewResource(R.layout.spinner_survey_row_white);
        spinner.setAdapter(langAdapter);
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
            case R.id.btnStartSurvey:
               // ((MainActivity)getActivity()).ReplaceFragment(new IncompleteSurveyFragment(),"incompleteSurvey",null);
                startActivity(new Intent());
            break;
        }
    }
}
