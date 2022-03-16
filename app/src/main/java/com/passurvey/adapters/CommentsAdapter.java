package com.passurvey.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.passurvey.R;
import com.passurvey.model.CommentsModel;

import java.util.ArrayList;

/**
 * Created by iadmin on 20/9/16.
 */
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> {
    private ArrayList<CommentsModel> defaultAnsArrayList;

    public CommentsAdapter(ArrayList<CommentsModel> questionGroupModelArrayList) {

        this.defaultAnsArrayList = questionGroupModelArrayList;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.default_answer_row, parent, false);
        return new MyViewHolder(itemView);
    }


    public void onBindViewHolder(MyViewHolder holder, int position) {
       // holder.recientImg.setImageResource(recientList.get(position).getImgId());
        holder.tvDefaultAnswer.setText(defaultAnsArrayList.get(position).getComments());
    }

    @Override
    public int getItemCount() {

        return defaultAnsArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDefaultAnswer;

        public MyViewHolder(View view) {
            super(view);

            tvDefaultAnswer = (TextView) view.findViewById(R.id.tvDefaultAnswer);

        }
    }

}