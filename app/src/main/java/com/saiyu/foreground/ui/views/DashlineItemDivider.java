package com.saiyu.foreground.ui.views;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by jiushubu on 2017/6/26.
 */

public class DashlineItemDivider extends RecyclerView.ItemDecoration {
    private int space;
    public DashlineItemDivider(int space){
        this.space = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.bottom = space;
        outRect.top = space;
    }

}
