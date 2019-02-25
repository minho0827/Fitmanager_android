package com.fitmanager.app.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.fitmanager.app.R;

/**
 * Created by Home on 16. 8. 10..
 */
public class MyViewPager extends ViewPager {

    private boolean mSwipable = true;


    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a =  context.obtainStyledAttributes(attrs,R.styleable.MyViewPager);
        try {
            mSwipable = a.getBoolean(R.styleable.MyViewPager_swipeable,true);
        }finally {
            a.recycle();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mSwipable ? super.onTouchEvent(event) : false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mSwipable ? super.onInterceptTouchEvent(event) : false;
    }
    public boolean isSwipable() {
        return mSwipable;
    }

    public void setSwipable(boolean swipable) {
        mSwipable = swipable;
    }
}

