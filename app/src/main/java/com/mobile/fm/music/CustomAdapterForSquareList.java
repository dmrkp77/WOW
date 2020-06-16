package com.mobile.fm.music;



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

public class CustomAdapterForSquareList extends RecyclerView.Adapter<CustomAdapterForSquareList.CustomViewHolder>{
    private ArrayList<MusicListSquare> arrayList;
    private Context context;
    // 리스너 객체 참조를 저장하는 변수
    private CustomAdapterForSquareList.OnItemClickListener mListener = null ;



    public CustomAdapterForSquareList(ArrayList<MusicListSquare> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    //뷰 홀더를 처음 부를때 뷰 홀더를 객체화.
    public CustomAdapterForSquareList.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_square, parent, false);
        CustomAdapterForSquareList.CustomViewHolder holder = new CustomAdapterForSquareList.CustomViewHolder(view);
        //새로 생성된 뷰홀더 객체를 리턴하는데 , 뷰 객체를 담아서 리턴한다.
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        //만들어진 뷰 홀더에 데이터만 다르게 담음.
        //glide는 이미지 로딩에 빠름.
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getImage())
                .into(holder.listImage);
        holder.ms_name.setText(arrayList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return (arrayList!=null ? arrayList.size(): 0);
    }



    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(CustomAdapterForSquareList.OnItemClickListener listener) {
        this.mListener = listener ;
    }


    //뷰를 담아두는 곳.
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView listImage;
        TextView ms_name;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.listImage = itemView.findViewById(R.id.list_cover);
            this.ms_name = itemView.findViewById(R.id.list_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onItemClick(v, pos) ;
                        }
                    }
                }
            });
        }
    }
}
