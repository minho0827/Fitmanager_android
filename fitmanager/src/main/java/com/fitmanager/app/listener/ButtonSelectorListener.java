package com.fitmanager.app.listener;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fitmanager.app.util.Utils;

/**
 * TextViwe의 터치 효과를 주기 위한 TouchListener, 내부적으로 OnClickListener를 호출할 수 있다.
 * ScrollView가 있는 경우 생성자에 추가하여 지정할 수 있다.
 * @author 정문기
 * @since 2018.03.30
 */
public class ButtonSelectorListener implements OnTouchListener {

	/**
	 * Listener & ViewGroup
     ************************************************************************************************************************************************/
	private OnClickListener clickListener;
	private ViewGroup scrollableView;

    /**
     * Static String variable
     ************************************************************************************************************************************************/
	private static final String setting_1 = "#80";
	private static final String setting_2 = "#FF";
	
	/**
	 * 생성자
	 * @param clickListener     :: onClick 이벤트를 핸들할 listener
	 * @param scrollableView    :: 터치 대상 뷰의 상위 뷰에 스크롤 가능한 뷰가 있는 경우 사용, 없으면 null로 입력할 것.
     ************************************************************************************************************************************************/
	public ButtonSelectorListener(OnClickListener clickListener, ViewGroup scrollableView) {
		this.clickListener = clickListener;
		this.scrollableView = scrollableView;
	}

    @Override
	public boolean onTouch(View v, MotionEvent event) {
        TextView textView = null;

        if(v instanceof TextView)
            textView = (TextView)v;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                /**
                 * 터치 Down시 text 색상 투명도 50% 적용
                 */

                if(scrollableView != null)
                    scrollableView.requestDisallowInterceptTouchEvent(true);

                if(textView != null) {
                    String textColor = Utils.getTextHexColor(textView.getCurrentTextColor());
                    if(!TextUtils.isEmpty(textColor))
                        textView.setTextColor(Color.parseColor(setting_1 + textColor));
                }

                break;

            case MotionEvent.ACTION_UP:

                /**
                 * 터치 Up시 text 색상 투명도 100% 적용
                 */

                if(textView != null) {
                    String textColor = Utils.getTextHexColor(textView.getCurrentTextColor());
                    if(!TextUtils.isEmpty(textColor))
                        textView.setTextColor(Color.parseColor(setting_2 + textColor));
                }

                if(Utils.isAvailableTouch(v, event)) {
                    if(clickListener != null)
                        clickListener.onClick(v);
                }

                break;

            default:
                v.performClick();
                break;
        }
        return true;
	}
}
