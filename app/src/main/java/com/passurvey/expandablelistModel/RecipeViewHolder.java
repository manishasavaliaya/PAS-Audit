package com.passurvey.expandablelistModel;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.passurvey.R;
import com.passurvey.utility.Utils;


public class RecipeViewHolder extends ParentViewHolder {

    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 90f;//180
    Context mContext;
    @NonNull
  //  private final ImageView mArrowExpandImageView;
//    private TextView mRecipeTextView;


    private ImageView imgQGRowArrow;
    private TextView tvQGRowQuestionName;
    private FrameLayout fLsidebarRemaing;
    private LinearLayout lLSidebarRemain;
    private TextView tvSidebarCount;
    private ImageView imgSidebarCheckIcon;
    private RelativeLayout RLdrawerrow;

    public RecipeViewHolder(@NonNull View itemView,Context context) {
        super(itemView);
        mContext=context;
       // mRecipeTextView = (TextView) itemView.findViewById(R.id.recipe_textview);

        //mArrowExpandImageView = (ImageView) itemView.findViewById(R.id.arrow_expand_imageview);

        imgQGRowArrow = (ImageView) itemView.findViewById(R.id.imgQGRowArrow);
        tvQGRowQuestionName = (TextView) itemView.findViewById(R.id.tvQGRowQuestionName);
        fLsidebarRemaing = (FrameLayout) itemView.findViewById(R.id.FLsidebarRemaing);
        lLSidebarRemain = (LinearLayout) itemView.findViewById(R.id.LLSidebarRemain);
        tvSidebarCount = (TextView) itemView.findViewById(R.id.tvSidebarCount);
        imgSidebarCheckIcon = (ImageView) itemView.findViewById(R.id.imgSidebarCheckIcon);
        RLdrawerrow=(RelativeLayout)itemView.findViewById(R.id.RLdrawerrow);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void bind(@NonNull Recipe recipe) {
        tvQGRowQuestionName.setText(recipe.getName());

        tvQGRowQuestionName.setText(recipe.getName());
        if (recipe.getName().equalsIgnoreCase(Utils.getSharedPreString(mContext,Utils.PREFS_DRAWER)))

        {
            RLdrawerrow.setBackgroundColor(mContext.getResources().getColor(R.color.currentgroupcolor));
        }
        else
        {
            RLdrawerrow.setBackground(mContext.getResources().getDrawable(R.drawable.selector_drawerlist));
        }
        if (recipe.getQuestionGroupFlag().equalsIgnoreCase("0")) {
           lLSidebarRemain.setVisibility(View.GONE);
            imgSidebarCheckIcon.setVisibility(View.VISIBLE);
        }
        else if (recipe.getQuestionGroupFlag().equalsIgnoreCase("Summary")) {
            lLSidebarRemain.setVisibility(View.GONE);
            imgSidebarCheckIcon.setVisibility(View.GONE);
        }
        else if (recipe.getQuestionGroupFlag().equalsIgnoreCase("Photos")) {
            lLSidebarRemain.setVisibility(View.GONE);
            imgSidebarCheckIcon.setVisibility(View.GONE);
        }

        else if (recipe.getQuestionGroupFlag().equalsIgnoreCase("Complete & Sync")) {
            lLSidebarRemain.setVisibility(View.GONE);
            imgSidebarCheckIcon.setVisibility(View.GONE);
        }
        else
        {
            lLSidebarRemain.setVisibility(View.VISIBLE);
            imgSidebarCheckIcon.setVisibility(View.GONE);
            tvSidebarCount.setText(recipe.getQuestionGroupFlag());
        }

    }

    @SuppressLint("NewApi")
    @Override
    public void setExpanded(boolean expanded) {
        super.setExpanded(expanded);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (expanded) {
                imgQGRowArrow.setRotation(ROTATED_POSITION);
            } else {
                imgQGRowArrow.setRotation(INITIAL_POSITION);
            }
        }*/
    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        super.onExpansionToggled(expanded);
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            RotateAnimation rotateAnimation;
            if (expanded) { // rotate clockwise
                 rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                        INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            } else { // rotate counterclockwise
                rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION,
                        INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            }

            rotateAnimation.setDuration(200);
            rotateAnimation.setFillAfter(true);
            imgQGRowArrow.startAnimation(rotateAnimation);
        }*/
    }
}
