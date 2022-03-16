package com.passurvey.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.passurvey.R;
import com.passurvey.adapters.AttachedImageAdapter;
import com.passurvey.model.PhotoModel;
import com.passurvey.utility.Utils;

import java.util.ArrayList;

/**
 * Created by iadmin on 28/9/16.
 */

public class AttachedImagesActivity extends  MainActivity implements View.OnClickListener {
    private AttachedImageAdapter imageAdapter;
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions defaultOptions;
    private RecyclerView attachedImgRV;
    public static Button btnAddmore,btnNext;
    ArrayList<PhotoModel> imageList=new ArrayList<>();;
    String  ProfileId,UserID,SurveyID;
    TextView tvphotolistnodata;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.container_body); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.attached_image_layout, contentFrameLayout);
        bindid();
    }

    private  void bindid(){
        setHeaderTitle(getString(R.string.title_photo));
        attachedImgRV=(RecyclerView)findViewById(R.id.attachedImgRV);
        btnAddmore=(Button)findViewById(R.id.btnAddMore);
        tvphotolistnodata =(TextView)findViewById(R.id.tvphotolistnodata);
        btnAddmore.setOnClickListener(this);
        btnNext=(Button)findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);

        ProfileId = Utils.getSharedPreString(AttachedImagesActivity.this,Utils.PREFS_PROFILEID);
        UserID=Utils.getSharedPreString(AttachedImagesActivity.this,Utils.PREFS_USERID);
        SurveyID=Utils.getSharedPreString(AttachedImagesActivity.this,Utils.PREFS_SURVEYID);

        imageList =dbHelper.getimagepathAndcomment(SurveyID,ProfileId,UserID);
        if (imageList!=null&&imageList.size()>0) {
            tvphotolistnodata.setVisibility(View.GONE);
            attachedImgRV.setVisibility(View.VISIBLE);
            imageAdapter = new AttachedImageAdapter(imageList, AttachedImagesActivity.this);
            attachedImgRV.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
            attachedImgRV.setLayoutManager(layoutManager);
            attachedImgRV.setAdapter(imageAdapter);
        }
        else
        {
            tvphotolistnodata.setVisibility(View.VISIBLE);
            attachedImgRV.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnAddMore:
                startActivity(new Intent(AttachedImagesActivity.this, PhotoActivity.class));
                finish();
                break;

            case R.id.btnNext:
                startActivity(new Intent(AttachedImagesActivity.this, FillOutDetailsActivity.class));
                finish();
                break;
        }
    }
}
