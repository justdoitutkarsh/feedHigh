package com.softup.utkarsh.feedhigh.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.softup.utkarsh.feedhigh.Interface.ItemClickListener;
import com.softup.utkarsh.feedhigh.R;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView DepartmentName;
    public ImageView imageView;
    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public MenuViewHolder(View itemView) {
        super(itemView);

        DepartmentName =(TextView)itemView.findViewById(R.id.menu_name);
        imageView =(ImageView)itemView.findViewById(R.id.menu_image);

        itemView.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        itemClickListener.onClick(view,getAdapterPosition(),false);
    }
}
