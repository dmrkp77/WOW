package com.mobile.fm.main;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.GeneratedAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.fm.R;
import com.mobile.fm.adapter.GalleryAdapter;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        final int nuberOfColumns = 3;

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
       // layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this,nuberOfColumns));

        mAdapter = new GalleryAdapter(this,getImagesPath(this));
        recyclerView.setAdapter(mAdapter);
    }




    public  ArrayList<String> getImagesPath(Activity activity) {
        Uri uri;
        ArrayList<String> listOfAllImages = new ArrayList<>();
        Cursor cursor;
        int column_index_data,colum_index_folder_name;
        String PathOfImage=null;
        String[] projection;
        Intent intent= getIntent();
        if(intent.getStringExtra("media").equals("video")){
            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            projection =new String[]{MediaStore.MediaColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME};
        }else {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            projection = new String[] {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
        }

        cursor =activity.getContentResolver().query(uri,projection,null,
                null,null);
        column_index_data=cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        colum_index_folder_name=cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while(cursor.moveToNext()){
            PathOfImage=cursor.getString(column_index_data);

            listOfAllImages.add(PathOfImage);
        }
        return listOfAllImages;
    }
}

