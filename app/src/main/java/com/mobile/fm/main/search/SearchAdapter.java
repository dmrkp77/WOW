package com.mobile.fm.main.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobile.fm.R;
import com.mobile.fm.exerciseboard.FirebaseHelper;
import com.mobile.fm.exerciseboard.PostInfo;
import com.mobile.fm.exerciseboard.activity.PostActivity;
import com.mobile.fm.exerciseboard.activity.WritePostActivity;
import com.mobile.fm.exerciseboard.adapter.HomeAdapter;
import com.mobile.fm.exerciseboard.listener.OnPostListener;
import com.mobile.fm.exerciseboard.view.ReadContentsVIew;

import java.util.ArrayList;
import java.util.List;
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MainViewHolder> {

    private ArrayList<PostInfo> mDataset;
    private Activity activity;
    private FirebaseHelper firebaseHelper;
    private ArrayList<ArrayList<SimpleExoPlayer>> playerArrayListArrayList = new ArrayList<>();
    private final int MORE_INDEX = 2;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    static class MainViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        MainViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }
    public SearchAdapter(Activity activity, ArrayList<PostInfo> myDataset) {
        this.mDataset = myDataset;
        this.activity = activity;
        firebaseHelper = new FirebaseHelper(activity);
    }

    public void setOnPostListener(OnPostListener onPostListener){
        firebaseHelper.setOnPostListener(onPostListener);
    }
    @Override
    public int getItemViewType(int position){
        return position;
    }

    @NonNull
    @Override
    public SearchAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.row_listview, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, PostActivity.class);
                intent.putExtra("postInfo", mDataset.get(mainViewHolder.getAdapterPosition()));
                intent.putExtra("postId", mDataset.get(mainViewHolder.getAdapterPosition()).getId());
                activity.startActivity(intent);
            }
        });

        cardView.findViewById(R.id.ssearch_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v, mainViewHolder.getAdapterPosition());
            }
        });

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MainViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        TextView titleTextView = cardView.findViewById(R.id.ssearch_titleTextView);

        PostInfo postInfo = mDataset.get(position);
        titleTextView.setText(postInfo.getTitle());

        ReadContentsVIew readContentsVIew = cardView.findViewById(R.id.ssearch_readContentsView);
        LinearLayout contentsLayout = cardView.findViewById(R.id.so_contentsLayout);

        if (contentsLayout.getTag() == null || !contentsLayout.getTag().equals(postInfo)) {
            contentsLayout.setTag(postInfo);
            contentsLayout.removeAllViews();

            readContentsVIew.setMoreIndex(MORE_INDEX);
            readContentsVIew.setPostInfo(postInfo);

            ArrayList<SimpleExoPlayer> playerArrayList = readContentsVIew.getPlayerArrayList();
            if(playerArrayList != null){
                playerArrayListArrayList.add(playerArrayList);
            }
        }
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private void showPopup(View v, final int position) {
        PopupMenu popup = new PopupMenu(activity, v);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.modify:
                        if(mDataset.get(position).getPublisher().equals(firebaseUser.getUid())) {
                            myStartActivity(WritePostActivity.class, mDataset.get(position));
                        }else{
                            Toast.makeText(activity, "수정 권한이 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    case R.id.delete:
                        if(mDataset.get(position).getPublisher().equals(firebaseUser.getUid())) {
                            firebaseHelper.storageDelete(mDataset.get(position));
                        }else{
                            Toast.makeText(activity, "삭제 권한이 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.post, popup.getMenu());
        popup.show();
    }

    private void myStartActivity(Class c, PostInfo postInfo) {
        Intent intent = new Intent(activity, c);
        intent.putExtra("postInfo", postInfo);
        intent.putExtra("postId", postInfo.getId());
        activity.startActivity(intent);
    }

    public void playerStop(){
        for(int i = 0; i < playerArrayListArrayList.size(); i++){
            ArrayList<SimpleExoPlayer> playerArrayList = playerArrayListArrayList.get(i);
            for(int ii = 0; ii < playerArrayList.size(); ii++){
                SimpleExoPlayer player = playerArrayList.get(ii);
                if(player.getPlayWhenReady()){
                    player.setPlayWhenReady(false);
                }
            }
        }
    }
//    public SearchAdapter(List<String> list, Context context){
//        this.list = list;
//        this.context = context;
//        this.inflate = LayoutInflater.from(context);
//    }
//    @Override
//    public int getCount() {
//        return list.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup viewGroup) {
//        if(convertView == null){
//            convertView = inflate.inflate(R.layout.row_listview,null);
//
//            viewHolder = new ViewHolder();
//            viewHolder.label = (TextView) convertView.findViewById(R.id.label);
//
//            convertView.setTag(viewHolder);
//        }else{
//            viewHolder = (ViewHolder)convertView.getTag();
//        }
//
//        // 리스트에 있는 데이터를 리스트뷰 셀에 뿌린다.
//        viewHolder.label.setText(list.get(position));
//
//        return convertView;
//    }
//
//
//
//
//    class ViewHolder{
//        public TextView label;
//    }


}
