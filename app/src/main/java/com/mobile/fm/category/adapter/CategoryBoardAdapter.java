package com.mobile.fm.category.adapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.fm.R;
import com.mobile.fm.exerciseboard.FirebaseHelper;
import com.mobile.fm.exerciseboard.PostInfo;
import com.mobile.fm.exerciseboard.activity.PostActivity;
import com.mobile.fm.exerciseboard.adapter.HomeAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class CategoryBoardAdapter extends RecyclerView.Adapter<CategoryBoardAdapter.CustomViewHolder>{
    private ArrayList<PostInfo> arrayList;
    private Context context;
    private Activity activity;
    private FirebaseHelper firebaseHelper;
    // 리스너 객체 참조를 저장하는 변수
    private CategoryBoardAdapter.OnItemClickListener mListener = null ;




    public CategoryBoardAdapter(Activity activity, ArrayList<PostInfo> arrayList) {
        this.arrayList = arrayList;
        this.activity = activity;

     //   firebaseHelper = new FirebaseHelper(activity);
    }

    @NonNull
    @Override
    //뷰 홀더를 처음 부를때 뷰 홀더를 객체화.
    public CategoryBoardAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_list_item, parent, false);
        CategoryBoardAdapter.CustomViewHolder holder = new CategoryBoardAdapter.CustomViewHolder(view);
        //새로 생성된 뷰홀더 객체를 리턴하는데 , 뷰 객체를 담아서 리턴한다.
        final CategoryBoardAdapter.CustomViewHolder customViewHolder = new CategoryBoardAdapter.CustomViewHolder(view);

        return customViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull CategoryBoardAdapter.CustomViewHolder holder, int position) {
        //만들어진 뷰 홀더에 데이터만 다르게 담음.
        //glide는 이미지 로딩에 빠름.
//        Glide.with(holder.itemView)
//                .load(arrayList.get(position).getCover())
//                .into(holder.cover);
        holder.title.setText(arrayList.get(position).getTitle());
        arrayList.get(position).getFormats().indexOf(0);
        if(arrayList.get(position).getFormats().size()==0){
            holder.content.setText("내용없음");
        }
        else if (arrayList.get(position).getFormats().get(0).equals("text")) {
            holder.content.setText(arrayList.get(position).getContents().get(0));

        }
        else {
            holder.content.setText("사진, 동영상 첨부");
        }
        holder.username.setText(arrayList.get(position).getNid());

        SimpleDateFormat dateFormat= new SimpleDateFormat("yyMMddHHmmss");
        Date ndate= new Date();
        Date ydate= arrayList.get(position).getCreatedAt();
        long diff=ndate.getTime()-ydate.getTime();
        diff=diff/60000;
        String time;
        if(ndate.getDay()!=ydate.getDay()) {
            time= new SimpleDateFormat("MM.dd", Locale.getDefault()).format(arrayList.get(position).getCreatedAt()).toString();
        }
        else if(diff<61){
            time= diff+"";
            time+="분 전";

        }else if(diff<3601) {
            time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(arrayList.get(position).getCreatedAt()).toString();
        }

        else{
            time= new SimpleDateFormat("MM.dd", Locale.getDefault()).format(arrayList.get(position).getCreatedAt()).toString();
        }

        holder.time.setText(time);
    }

    @Override
    public int getItemCount() {
        return (arrayList!=null ? arrayList.size(): 0);
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(CategoryBoardAdapter.OnItemClickListener listener) {
       this.mListener = listener ;
    }

    //뷰를 담아두는 곳.
    public class CustomViewHolder extends RecyclerView.ViewHolder {

//        private String title;
////        private ArrayList<String> contents;
////        private ArrayList<String> formats;
////        private String publisher;//유저 아이디
////        private Date createdAt;
////        private String nid;//유저 닉네임
////        private String id;//문서 이름(파이어베이스 문서 이름)
////        private String boardSelect;
////        private int numComments=0;
        TextView title;
        TextView content;
        TextView time;
        TextView username;
        TextView commentNumber;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.title = itemView.findViewById(R.id.tv_title);
            this.content = itemView.findViewById(R.id.tv_content);
            this.time=itemView.findViewById(R.id.tv_time);
            this.username=itemView.findViewById(R.id.tv_userName);
            this.commentNumber=itemView.findViewById(R.id.tv_commentNumber);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=getAdapterPosition(); //이부분이 안먹힘
                    if (pos != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(activity, PostActivity.class);
                        intent.putExtra("postInfo", arrayList.get(pos));
                        activity.startActivity(intent);

                    }
                }
            });

        }
    }
}

