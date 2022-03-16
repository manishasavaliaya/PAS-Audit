package com.passurvey.adapters;

/**
 * Created by Ravi on 29/07/15.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.passurvey.R;
import com.passurvey.model.QuestionGroupModel;
import com.passurvey.utility.Utils;

import java.util.Collections;
import java.util.List;


public class NavigationDrawerAdapterQG extends RecyclerView.Adapter<NavigationDrawerAdapterQG.MyViewHolder> {
    List<QuestionGroupModel> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public NavigationDrawerAdapterQG(Context context, List<QuestionGroupModel> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.drawer_question_group_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        QuestionGroupModel current = data.get(position);
        holder.tvQGRowQuestionName.setText(current.getQuestionGroupName());
        if (current.getQuestionGroupName().equalsIgnoreCase(Utils.getSharedPreString(context,Utils.PREFS_DRAWER)))

        {
            holder.RLdrawerrow.setBackgroundColor(context.getResources().getColor(R.color.currentgroupcolor));
        }
        else
        {
            holder.RLdrawerrow.setBackground(context.getResources().getDrawable(R.drawable.selector_drawerlist));
        }
        if (current.getQuestionGroupFlag().equalsIgnoreCase("0")) {
            holder.lLSidebarRemain.setVisibility(View.GONE);
            holder.imgSidebarCheckIcon.setVisibility(View.VISIBLE);
        }
        else if (current.getQuestionGroupFlag().equalsIgnoreCase("Summary")) {
            holder.lLSidebarRemain.setVisibility(View.GONE);
            holder.imgSidebarCheckIcon.setVisibility(View.GONE);
        }
        else if (current.getQuestionGroupFlag().equalsIgnoreCase("Photos")) {
            holder.lLSidebarRemain.setVisibility(View.GONE);
            holder.imgSidebarCheckIcon.setVisibility(View.GONE);
        }

        else if (current.getQuestionGroupFlag().equalsIgnoreCase("Complete & Sync")) {
            holder.lLSidebarRemain.setVisibility(View.GONE);
            holder.imgSidebarCheckIcon.setVisibility(View.GONE);
        }
        else
        {
            holder.lLSidebarRemain.setVisibility(View.VISIBLE);
            holder.imgSidebarCheckIcon.setVisibility(View.GONE);
            holder.tvSidebarCount.setText(current.getQuestionGroupFlag());
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgQGRowArrow;
        private TextView tvQGRowQuestionName;
        private FrameLayout fLsidebarRemaing;
        private LinearLayout lLSidebarRemain;
        private TextView tvSidebarCount;
        private ImageView imgSidebarCheckIcon;
        private RelativeLayout RLdrawerrow;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgQGRowArrow = (ImageView) itemView.findViewById(R.id.imgQGRowArrow);
            tvQGRowQuestionName = (TextView) itemView.findViewById(R.id.tvQGRowQuestionName);
            fLsidebarRemaing = (FrameLayout) itemView.findViewById(R.id.FLsidebarRemaing);
            lLSidebarRemain = (LinearLayout) itemView.findViewById(R.id.LLSidebarRemain);
            tvSidebarCount = (TextView) itemView.findViewById(R.id.tvSidebarCount);
            imgSidebarCheckIcon = (ImageView) itemView.findViewById(R.id.imgSidebarCheckIcon);
            RLdrawerrow=(RelativeLayout)itemView.findViewById(R.id.RLdrawerrow);
        }
    }
}
