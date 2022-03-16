package com.passurvey.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.passurvey.R;


public class ThankYouActivity extends MainActivity implements View.OnClickListener{




        Button btnOK;


    @Override
    protected void onPause() {
        super.onPause();
       // handler.removeCallbacks(r);
    }



    @Override
    public  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.container_body); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.fragment_thankyou, contentFrameLayout);
        bindid();
    }

    private void bindid() {
        setHeaderTitle(getString(R.string.title_thankyou));

        btnOK =(Button)findViewById(R.id.btnOK);
        btnOK.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnOK:
                startActivity(new Intent(ThankYouActivity.this,SelectSurveyActivity.class));
                //startActivity(new Intent(ThankYouActivity.this,SplashActivity.class));
                finish();

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

}
