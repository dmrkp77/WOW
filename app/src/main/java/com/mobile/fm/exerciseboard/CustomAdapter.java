package com.mobile.fm.exerciseboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mobile.fm.R;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
    private ArrayList<User> arrayList;
    private Context context;

    public CustomAdapter(ArrayList<User> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_list_item,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.tv_title.setText(arrayList.get(position).getTitle());
        holder.tv_content.setText(String.valueOf((arrayList.get(position).getContent())));
        holder.tv_time.setText(arrayList.get(position).getTime());
        holder.tv_userName.setText(arrayList.get(position).getUserName());
        holder.tv_commentNumber.setText(String.valueOf((arrayList.get(position).getCommentNumber())));
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size():0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_content;
        TextView tv_time;
        TextView tv_userName;
        TextView tv_commentNumber;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_title=itemView.findViewById(R.id.tv_title);
            this.tv_content=itemView.findViewById(R.id.tv_content);
            this.tv_time=itemView.findViewById(R.id.tv_time);
            this.tv_userName=itemView.findViewById(R.id.tv_userName);
            this.tv_commentNumber=itemView.findViewById(R.id.tv_commentNumber);
        }
    }
}
