package com.mobile.fm.exerciseboard.activity;

import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mobile.fm.R;

public class BasicActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }

    public void setToolbarTitle(String title, String category) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            if (category != null) {
                if (category.equals("Music"))
                    actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Music)));
                else if (category.equals("Reading"))
                    actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Reading)));
                else if (category.equals("Travel"))
                    actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Travel)));
                else if (category.equals("Exercise"))
                    actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Exercise)));
                else if (category.equals("TV"))
                    actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.TV)));
                else if (category.equals("Movie"))
                    actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Movie)));
            }
        }
    }
}
