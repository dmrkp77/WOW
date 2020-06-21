package com.mobile.fm.exerciseboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.fm.R;
import com.mobile.fm.exerciseboard.CommentListItem;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CustomViewHolder> {
    private ArrayList<CommentListItem> arrayList;
    private Context context;
    public CommentAdapter(ArrayList<CommentListItem> arrayList, Context context){
        this.arrayList=arrayList;
        this.context=context;
    }
    @Override
    public CommentAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_comment, parent, false);
        CommentAdapter.CustomViewHolder holder = new CommentAdapter.CustomViewHolder(view);
        //새로 생성된 뷰홀더 객체를 리턴하는데 , 뷰 객체를 담아서 리턴한다.
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.commentAuthor.setText(arrayList.get(position).getUsername());
        holder.commentBody.setText(arrayList.get(position).getBody());
        holder.commentTime.setText(arrayList.get(position).getCreatedAt());
    }

    @Override
    public int getItemCount() {return (arrayList!=null ? arrayList.size(): 0); }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView commentAuthor;
        TextView commentBody;
        TextView commentTime;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.commentAuthor = itemView.findViewById(R.id.commentAuthor);
            this.commentBody = itemView.findViewById(R.id.commentBody);
            this.commentTime = itemView.findViewById(R.id.commentTime);
        }
    }
}
