package com.softup.utkarsh.feedhigh.headadapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.softup.utkarsh.feedhigh.R;
import com.softup.utkarsh.feedhigh.headDepartmentReviewmodel.HeadNote;

import java.util.List;


public class HeadNoteAdapter extends RecyclerView.Adapter<HeadNoteAdapter.CustomViewHolder> {
    String test = "Emp";
    private List<HeadNote> dataList;
    private Context mContext;

    public HeadNoteAdapter(Context context, List<HeadNote> data) {
        this.dataList = data;
        this.mContext = context;
    }
        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(mContext).inflate(R.layout.headcard_note, parent, false);

                return new CustomViewHolder(view);

    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.bind(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

class CustomViewHolder extends RecyclerView.ViewHolder {

    private TextView title;
    private TextView body;
    private TextView body2;
    private TextView body3;
    private TextView body4;
    private TextView body5;
    private TextView priority;


    CustomViewHolder(View itemView) {
        super(itemView);
        title = (TextView)itemView.findViewById(R.id.title);
        body = (TextView) itemView.findViewById(R.id.body);
        body2 = (TextView) itemView.findViewById(R.id.body2);
        body3 = (TextView) itemView.findViewById(R.id.body3);
        body4 = (TextView) itemView.findViewById(R.id.body4);
        body5 = (TextView) itemView.findViewById(R.id.body5);
        priority = (TextView) itemView.findViewById(R.id.priority);
    }

    void bind(final HeadNote note) {

                title.setText(note.getTitle());
                body.setText(note.getBody());
                body2.setText(note.getBody2());
                body3.setText(note.getBody3());
                body4.setText(note.getBody4());
                body5.setText(note.getBody5());
                int prior = note.getPriority();
                GradientDrawable priorityCircle = (GradientDrawable) priority.getBackground();
                priorityCircle.setColor(getPriorityColor(prior));
                priority.setText(String.valueOf(prior));

    }

    private int getPriorityColor(int priority) {
        int priorityColor = 0;

        switch(priority) {
            case 1: priorityColor = ContextCompat.getColor(mContext, R.color.materialRed);
                break;
            case 2: priorityColor = ContextCompat.getColor(mContext, R.color.materialOrange);
                break;
            case 3: priorityColor = ContextCompat.getColor(mContext, R.color.materialYellow);
                break;
            default: break;
        }
        return priorityColor;
    }
}
}

