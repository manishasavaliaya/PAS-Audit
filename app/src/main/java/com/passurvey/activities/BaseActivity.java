package com.passurvey.activities;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.passurvey.R;
import com.passurvey.SurveyApp;
import com.passurvey.customviews.CustomBoldTextView;
import com.passurvey.database.DBHelper;

public abstract class BaseActivity extends AppCompatActivity {
    public static DBHelper dbHelper;
    public RelativeLayout lnrTopBar;
    public LinearLayout lnrLeft;
    public ImageView imgBack;
    public CustomBoldTextView txtTitle;
    public LinearLayout lnrTopBarRight;
    public ImageView imgTopFilter;
    public LinearLayout lnrMain;
    public LinearLayout lnrContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.transparent));
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorTextGray));
        }

        // firebase crashanalytics
        ((SurveyApp)getApplicationContext()).mFirebaseAnalytics.getAppInstanceId();
        FirebaseCrashlytics.getInstance();
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);

        lnrMain = (LinearLayout) findViewById(R.id.lnrMain);
        lnrContainer = (LinearLayout) findViewById(R.id.lnrContainer);
        dbHelper = DBHelper.getInstance(BaseActivity.this);
        lnrTopBar = (RelativeLayout) findViewById(R.id.lnrTopBar);
        lnrLeft = (LinearLayout) findViewById(R.id.lnrLeft);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        txtTitle = (CustomBoldTextView) findViewById(R.id.txtTitle);
        lnrTopBarRight = (LinearLayout) findViewById(R.id.lnrTopBarRight);
        imgTopFilter = (ImageView) findViewById(R.id.imgTopFilter);

        setLayoutView();
        initialization();
        setupData();
        setListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void hideTopBar() {
        lnrTopBar.setVisibility(View.GONE);
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    public abstract void setLayoutView();

    public abstract void initialization();

    public abstract void setupData();

    public abstract void setListeners();

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInputFromWindow(view.getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
    }
    //
//    @Override
//    public void onGetSentenceSuggestions(SentenceSuggestionsInfo[] results) {
//        final StringBuffer sb = new StringBuffer("");
//        for(SentenceSuggestionsInfo result:results){
//            int n = result.getSuggestionsCount();
//            for(int i=0; i < n; i++){
//                int m = result.getSuggestionsInfoAt(i).getSuggestionsCount();
//
//                if((result.getSuggestionsInfoAt(i).getSuggestionsAttributes() &
//                        SuggestionsInfo.RESULT_ATTR_LOOKS_LIKE_TYPO) != SuggestionsInfo.RESULT_ATTR_LOOKS_LIKE_TYPO )
//                    continue;
//
//                for(int k=0; k < m; k++) {
//                    sb.append(result.getSuggestionsInfoAt(i).getSuggestionAt(k))
//                            .append("\n");
//                }
//                sb.append("\n");
//            }
//        }
//    @Override
//    public void onGetSuggestions(SuggestionsInfo[] results) {
//        // Unused
//    }

//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                suggestions.setText(sb.toString());
//            }
//        });
//    }

//    private final int NUMBER_OF_SUGGESTIONS=8;
//
//    public void fetchSuggestionsFor(String input){
//        TextServicesManager tsm = (TextServicesManager) getSystemService(TEXT_SERVICES_MANAGER_SERVICE);
//        SpellCheckerSession session = tsm.newSpellCheckerSession(null, Locale.ENGLISH, this, true);
//
//        session.getSentenceSuggestions(new TextInfo[]{ new TextInfo(input) }, NUMBER_OF_SUGGESTIONS);
//    }
}
