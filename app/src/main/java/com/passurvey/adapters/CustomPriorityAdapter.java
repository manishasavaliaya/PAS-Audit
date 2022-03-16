package com.passurvey.adapters;

/**
 * Created by Ravi on 29/07/15.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.passurvey.R;


public class CustomPriorityAdapter extends RecyclerView.Adapter<CustomPriorityAdapter.MyViewHolder> {

    int flags[];
    String[] countryNames;
    private LayoutInflater inflater;
    private Context context;

    public CustomPriorityAdapter(Context context, int[] flags, String[] countryNames) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.flags = flags;
        this.countryNames = countryNames;
    }

    public void delete(int position) {
       // flags.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.spinner_priority_row  , parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
       holder.imageView.setImageResource(flags[position]);
        holder.textView.setText(countryNames[position]);

    }

    @Override
    public int getItemCount() {
        return countryNames.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textView = (TextView) itemView.findViewById(R.id.textView);
        }
    }
}
