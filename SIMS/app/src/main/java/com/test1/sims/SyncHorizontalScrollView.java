package com.test1.sims;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class SyncHorizontalScrollView extends HorizontalScrollView {
    //另一个HorizontalScrollView
    SyncHorizontalScrollView otherHsv;

    public SyncHorizontalScrollView(Context context) {
        this(context, null);
    }

    public SyncHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    //滑动时调用
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        //不为空，实例不是自己是调用
        if (otherHsv != null && otherHsv != this) {
            otherHsv.scrollTo(l, t);
        }

    }

    public void setOtherHsv(SyncHorizontalScrollView otherHsv) {
        this.otherHsv = otherHsv;
    }
}