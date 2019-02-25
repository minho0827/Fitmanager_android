package com.fitmanager.app.util;

import android.os.Handler;
import android.os.SystemClock;
import android.view.View;

/**
 * 중복 클릭 방지 및 딜레이 OnClickListener
 *
 * @since 2018.03.30
 */
public abstract class OnSingleClickHandleListener implements View.OnClickListener {

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
    public final void onClick(final View v) {
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
        new Handler().postDelayed(() -> onSingleClick(v), 300);
    }
}
