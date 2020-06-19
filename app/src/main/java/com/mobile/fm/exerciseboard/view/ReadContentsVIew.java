package com.mobile.fm.exerciseboard.view;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;
import com.mobile.fm.R;
import com.mobile.fm.exerciseboard.PostInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ReadContentsVIew extends LinearLayout {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<SimpleExoPlayer> playerArrayList = new ArrayList<>();
    private int moreIndex = -1;

    public ReadContentsVIew(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public ReadContentsVIew(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        initView();
    }

    private void initView(){
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setOrientation(LinearLayout.VERTICAL);
        layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.view_post, this, true);
    }

    public void setMoreIndex(int moreIndex){
        this.moreIndex = moreIndex;
    }

    public void setPostInfo(PostInfo postInfo){
        TextView createdAtTextView = findViewById(R.id.createAtTextView);
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyMMddHHmmss");
        Date ndate= new Date();
        Date ydate= postInfo.getCreatedAt();
        long diff=ndate.getTime()-ydate.getTime();
        diff=diff/60000;
        String time;
        if(ndate.getDay()!=ydate.getDay()) {
            time= new SimpleDateFormat("MM.dd", Locale.getDefault()).format(postInfo.getCreatedAt()).toString();
        }
        else if(diff<61){
            time= diff+"";
            time+="분 전";

        }else if(diff<3601) {
            time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(postInfo.getCreatedAt()).toString();
        }

        else{
            time= new SimpleDateFormat("MM.dd", Locale.getDefault()).format(postInfo.getCreatedAt()).toString();
        }
        time += " | ";
        time += postInfo.getNid();
        createdAtTextView.setText(time);
        LinearLayout contentsLayout = findViewById(R.id.so_contentsLayout);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ArrayList<String> contentsList = postInfo.getContents();
        ArrayList<String> formatList = postInfo.getFormats();
        int media=0;
        for (int i = 0; i < contentsList.size(); i++) {
            String contents = contentsList.get(i);
            String formats = formatList.get(i);
            if(formats.equals("video")||formats.equals("image")){
                media=1;
                break;
            }
        }
        Log.d("NO123", "Here1");

        for (int i = 0; i < contentsList.size(); i++) {
            if (i == moreIndex) {
                if(media==2){
                    TextView textView = new TextView(context);
                    textView.setLayoutParams(layoutParams);
                    textView.setText("미디어 더보기...");
                    contentsLayout.addView(textView);
                }
                else {
                    TextView textView = new TextView(context);
                    textView.setLayoutParams(layoutParams);
                    textView.setText("더보기...");
                    contentsLayout.addView(textView);
                }
                break;
            }

            String contents = contentsList.get(i);
            String formats = formatList.get(i);

            if(formats.equals("image")){
                if(moreIndex==2){
                    TextView textView = new TextView(context);
                    textView.setLayoutParams(layoutParams);
                    textView.setText("사진");
                    contentsLayout.addView(textView);
                    break;
                }
                ImageView imageView = (ImageView)layoutInflater.inflate(R.layout.view_contents_image, this, false);
                contentsLayout.addView(imageView);
                Glide.with(this).load(contents).override(1000).thumbnail(0.1f).into(imageView);
            }else if(formats.equals("video")){
                if(moreIndex==2){
                    TextView textView = new TextView(context);
                    textView.setLayoutParams(layoutParams);
                    textView.setText("동영상");
                    contentsLayout.addView(textView);
                    break;
                }
                final PlayerView playerView = (PlayerView) layoutInflater.inflate(R.layout.view_contents_player, this, false);

                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                        Util.getUserAgent(context, getResources().getString(R.string.app_name)));
                MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(Uri.parse(contents));

                SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(context);

                player.prepare(videoSource);

                player.addVideoListener(new VideoListener() {
                    @Override
                    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
                        playerView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
                    }
                });

                playerArrayList.add(player);

                playerView.setPlayer(player);
                contentsLayout.addView(playerView);
            }else{
                if(moreIndex==2) {
                    TextView textView = (TextView) layoutInflater.inflate(R.layout.view_contents_text, this, false);
                    int length = contents.length();
                    if(length > 20){

                        String c = contents.substring(0, 20);
                        textView.setText(c+"...");
                    }
                    else{
                        textView.setText(contents);
                    }

                    contentsLayout.addView(textView);
                    break;
                }
                TextView textView = (TextView) layoutInflater.inflate(R.layout.view_contents_text, this, false);

                textView.setText(contents);
                contentsLayout.addView(textView);
            }
        }
    }

    public ArrayList<SimpleExoPlayer> getPlayerArrayList() {
        return playerArrayList;
    }
}
