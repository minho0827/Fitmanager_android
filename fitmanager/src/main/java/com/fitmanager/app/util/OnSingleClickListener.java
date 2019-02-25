package com.fitmanager.app.util;

import android.os.SystemClock;
import android.view.View;

/**
 * 중복 클릭 방지 OnClickListener
 *
 */
public abstract class OnSingleClickListener implements View.OnClickListener {

    /**
     * 중복 클릭 방지 시간
     ************************************************************************************************************************************************/
    private static final long MIN_CLICK_INTERVAL = 1000;

    /**
     * 시간 계산 variable
     ************************************************************************************************************************************************/
    private long mLastClickTime;

    /**
     * 추상화 Class 생성자
     ************************************************************************************************************************************************/
    public abstract void onSingleClick(View v);

    @Override
    public final void onClick(View v) {
        long currentClickTime = SystemClock.uptimeMillis();
        long elapsedTime = SystemClock.uptimeMillis() - mLastClickTime;
        mLastClickTime = currentClickTime;

        /**
         * 중복 클릭    :: 정해진 시간안에 클릭하는 경우
         */
        if(elapsedTime <= MIN_CLICK_INTERVAL) {
            return;
        }

        /**
         * 정상 클릭    :: 시간 외에 클릭하는 경우
         */
        onSingleClick(v);
    }
}
