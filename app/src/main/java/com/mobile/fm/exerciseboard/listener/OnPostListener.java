package com.mobile.fm.exerciseboard.listener;

import com.mobile.fm.exerciseboard.PostInfo;

public interface OnPostListener {
    void onDelete(PostInfo postInfo);
    void onModify();
}
