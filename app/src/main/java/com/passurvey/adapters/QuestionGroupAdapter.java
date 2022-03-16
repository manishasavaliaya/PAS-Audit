package com.passurvey.adapters;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.passurvey.R;
import com.passurvey.activities.SelectQuestionGroupActivity;
import com.passurvey.database.DBHelper;
import com.passurvey.model.QuestionAnswerModel;
import com.passurvey.model.QuestionGroupModel;
import com.passurvey.utility.Utils;

import java.util.List;

/**
 * Created by iadmin on 20/9/16.
 */
public class QuestionGroupAdapter extends RecyclerView.Adapter<QuestionGroupAdapter.MyViewHolder> {
    private List<QuestionGroupModel> questionGroupModelArrayList;
    QuestionAnswerModel data = new QuestionAnswerModel();
    boolean check = false;
    DBHelper dbHelper;
    Context mContext;

    public QuestionGroupAdapter(List<QuestionGroupModel> questionGroupModelArrayList, Context context) {

        this.questionGroupModelArrayList = questionGroupModelArrayList;
        this.mContext = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.question_group_row, parent, false);
        return new MyViewHolder(itemView);
    }


    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // holder.recientImg.setImageResource(recientList.get(position).getImgId());
        holder.tvQGRowQuestionName.setText(questionGroupModelArrayList.get(position).getQuestionGroupName());


        if (questionGroupModelArrayList.get(position).getQuestionGroupName().equalsIgnoreCase(Utils.getSharedPreString(mContext, Utils.PREFS_GROUPNAME))) {
            holder.RLQGrow.setBackgroundColor(mContext.getResources().getColor(R.color.search_bg));
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.RLQGrow.setBackground(mContext.getResources().getDrawable(R.drawable.selector_listtext));
            }
        }

        if (questionGroupModelArrayList.get(position).getQuestionGroupFlag().equalsIgnoreCase("0")) {
            holder.lLSidebarRemain.setVisibility(View.GONE);
            holder.imgSidebarCheckIcon.setVisibility(View.VISIBLE);
        } else if (questionGroupModelArrayList.get(position).getQuestionGroupFlag().equalsIgnoreCase("Summary")) {
            holder.lLSidebarRemain.setVisibility(View.GONE);
            holder.imgSidebarCheckIcon.setVisibility(View.GONE);
        } else if (questionGroupModelArrayList.get(position).getQuestionGroupFlag().equalsIgnoreCase("Photos")) {
            holder.lLSidebarRemain.setVisibility(View.GONE);
            holder.imgSidebarCheckIcon.setVisibility(View.GONE);
        } else if (questionGroupModelArrayList.get(position).getQuestionGroupFlag().equalsIgnoreCase("Complete & Sync")) {
            holder.lLSidebarRemain.setVisibility(View.GONE);
            holder.imgSidebarCheckIcon.setVisibility(View.GONE);
        } else {
            holder.lLSidebarRemain.setVisibility(View.VISIBLE);
            holder.imgSidebarCheckIcon.setVisibility(View.GONE);
            holder.tvSidebarCount.setText(questionGroupModelArrayList.get(position).getQuestionGroupFlag());
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String Userid = "", Surveyid = "", Profileid = "", GroupId = "";
                Userid = Utils.getSharedPreString(mContext, Utils.PREFS_USERID);
                Surveyid = Utils.getSharedPreString(mContext, Utils.PREFS_SURVEYID);
                Profileid = Utils.getSharedPreString(mContext, Utils.PREFS_PROFILEID);
                //GroupId= Utils.getSharedPreString(mContext,questionGroupModelArrayList.get(position).getQuestionGroupId());
                GroupId = questionGroupModelArrayList.get(position).getQuestionGroupId();
                data.setUserId(Userid);
                /*21-10*/
                //  data.setUserAnswer(Utils.ReplaceAsciChar(edtAnswer.getHtml().toString()));
                data.setSurveyId(Surveyid);
                data.setGroupId(GroupId);

                data.setProfileId(Profileid);
                //change by mena 15-11-2016 for priorty
                data.setAnswerPriority("Not Applicable");
                Log.v("update>>", "onLongClick: " + Userid + "\n" + Surveyid + " \n" + GroupId + "\n" + Profileid);

                // data.setAnswerPriority(tvQApriority.getText().toString());
                //change by mena 15-11-2016 for priorty
                //  data.setCreateDateTime(Utils.getDateTime());
                data.setCreateDateTime(Utils.getDateTimeNew());
                data.setCompleteFlag("1");
                data.setCompleteSurveyFlag("0");
                data.setSyncFlag("0");
                dbHelper = new DBHelper(mContext);
                dialog();
                return true;
            }
        });
    }

    boolean callupdate() {
        long insert = dbHelper.groupPriorityNotApplicable(data);
        if (insert > 0) {
            //SelectQuestionGroupActivity.showProgress(mContext);
            Utils.showToast(mContext, "Data Updated");
            //((SelectQuestionGroupActivity)mContext).refresh();
        } else {
            Utils.showToast(mContext, "Data not updated");
        }
        ((SelectQuestionGroupActivity) mContext).refresh();
        return true;
    }

    void dialog() {
        final Dialog dialog = new Dialog(mContext, android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_not_applicable);

        dialog.getWindow().setDimAmount(0.6f);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        Button dialogButtonyes = (Button) dialog.findViewById(R.id.btnyes);
        // if button is clicked, close the custom dialog
        dialogButtonyes.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                callupdate();
                dialog.dismiss();
                check = true;
            }
        });
        Button dialogButtonno = (Button) dialog.findViewById(R.id.btnno);
        // if button is clicked, close the custom dialog
        dialogButtonno.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                dialog.dismiss();
                check = false;
            }
        });

        dialog.show();
    }

    @Override
    public int getItemCount() {

        return questionGroupModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgQGRowArrow;
        private TextView tvQGRowQuestionName;
        private ImageView imgIncompleteImage;

        private FrameLayout fLsidebarRemaing;
        private LinearLayout lLSidebarRemain;
        private TextView tvSidebarCount;
        private ImageView imgSidebarCheckIcon;
        private RelativeLayout RLdrawerrow, RLQGrow;


        public MyViewHolder(View view) {
            super(view);
            imgQGRowArrow = (ImageView) view.findViewById(R.id.imgQGRowArrow);
            tvQGRowQuestionName = (TextView) view.findViewById(R.id.tvQGRowQuestionName);
            imgIncompleteImage = (ImageView) view.findViewById(R.id.imgIncompleteImage);


            fLsidebarRemaing = (FrameLayout) view.findViewById(R.id.FLsidebarRemaing);
            lLSidebarRemain = (LinearLayout) view.findViewById(R.id.LLSidebarRemain);
            tvSidebarCount = (TextView) view.findViewById(R.id.tvSidebarCount);
            imgSidebarCheckIcon = (ImageView) view.findViewById(R.id.imgSidebarCheckIcon);
            RLdrawerrow = (RelativeLayout) view.findViewById(R.id.RLdrawerrow);
            RLQGrow = (RelativeLayout) view.findViewById(R.id.RLQGrow);
        }
    }

}