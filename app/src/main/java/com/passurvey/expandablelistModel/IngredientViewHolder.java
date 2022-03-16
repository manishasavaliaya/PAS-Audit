package com.passurvey.expandablelistModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.passurvey.R;
import com.passurvey.activities.MainActivity;
import com.passurvey.activities.QuestionAnswerActivity;
import com.passurvey.utility.Utils;

public class IngredientViewHolder extends ChildViewHolder {

    private TextView mIngredientTextView;
    private String questionid,questionsrno,questionflag;
    private ImageView imgSidebarCrossIcon,imgSidebarCheckIcon;

    public IngredientViewHolder(final Context context, @NonNull View itemView) {
        super(itemView);
        mIngredientTextView = (TextView) itemView.findViewById(R.id.ingredient_textview);

        imgSidebarCrossIcon=(ImageView)itemView.findViewById(R.id.imgSidebarCrossIcon);
        imgSidebarCheckIcon=(ImageView)itemView.findViewById(R.id.imgSidebarCheckIcon);

        mIngredientTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Toast.makeText(context, mIngredientTextView.getText()+" question id :"+questionid,
                        Toast.LENGTH_SHORT).show();*/
                String Userid = "", Surveyid = "",Profileid="",GroupId="";
                Userid = Utils.getSharedPreString(context, Utils.PREFS_USERID);
                Surveyid = Utils.getSharedPreString(context, Utils.PREFS_SURVEYID);
                Profileid= Utils.getSharedPreString(context, Utils.PREFS_PROFILEID);
                GroupId=Utils.getSharedPreString(context,Utils.PREFS_GROUPID);
                int count =-1;
                if (GroupId=="")
                {
                    count=0;
                }

                ((MainActivity)context).showprogressnew();
                Utils.setSharedPreString(context, Utils.PREFS_GROUPID, MainActivity.dbHelper.getGroupIdfromquestionid(Surveyid, questionid));
                Utils.setSharedPreString(context, Utils.PREFS_GROUPNAME,MainActivity.dbHelper.getGroupNamefromquestionid(Surveyid, questionid));
                Utils.setSharedPreString(context, Utils.PREFS_DRAWER,MainActivity.dbHelper.getGroupNamefromquestionid(Surveyid, questionid));

                //comment by mena

                Intent intent = new Intent(((MainActivity)context), QuestionAnswerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.putExtra("QuestionSrNo",questionsrno);
                context.startActivity(intent);
                ((Activity)context).finish();

               /* count = MainActivity.dbHelper.getQuestionGroupCount(Surveyid,Userid,Profileid,GroupId);
                if (MainActivity.dbHelper.getGroupIdfromquestionid(Surveyid, questionid).equalsIgnoreCase(GroupId.toString())||count==0)
                {
                    ((MainActivity)context).showprogressnew();
                    Utils.setSharedPreString(context, Utils.PREFS_GROUPID, MainActivity.dbHelper.getGroupIdfromquestionid(Surveyid, questionid));
                    Utils.setSharedPreString(context, Utils.PREFS_GROUPNAME,MainActivity.dbHelper.getGroupNamefromquestionid(Surveyid, questionid));
                    Utils.setSharedPreString(context, Utils.PREFS_DRAWER,MainActivity.dbHelper.getGroupNamefromquestionid(Surveyid, questionid));

                    //comment by mena

                    Intent intent = new Intent(((MainActivity)context), QuestionAnswerActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.putExtra("QuestionSrNo",questionsrno);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
                else if(count==-1||count>0)
                {
                    Toast.makeText(context, context.getString(R.string.toast_completequstiongroupfirst)+" "+MainActivity.dbHelper.getGroupNamefromGroupid(Surveyid, GroupId),
                            Toast.LENGTH_SHORT).show();
                }*/
            }
        });



    }

    public void bind(@NonNull Ingredient ingredient) {
        mIngredientTextView.setText(ingredient.getName());

        questionid=ingredient.getId();
        questionsrno=ingredient.getQuestionSrNo();
        questionflag=ingredient.getQuestionFlag();
        if (questionflag.equalsIgnoreCase("1"))
        {
            imgSidebarCrossIcon.setVisibility(View.GONE);
            imgSidebarCheckIcon.setVisibility(View.VISIBLE);
        }
        else
        {
            imgSidebarCrossIcon.setVisibility(View.VISIBLE);
            imgSidebarCheckIcon.setVisibility(View.GONE);
        }
    }


}
