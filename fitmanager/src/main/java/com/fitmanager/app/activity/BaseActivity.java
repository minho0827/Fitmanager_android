package com.fitmanager.app.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fitmanager.app.R;
import com.fitmanager.app.util.Constant;
import com.fitmanager.app.util.MemberManager;

import java.util.Locale;



public class BaseActivity extends Activity{

    /**
     * Application 전역 정보 인터페이스
     ************************************************************************************************************************************************/
    protected Context mContext;                                                                                     // Context

    /**
     * Application User Local DataBase
     ************************************************************************************************************************************************/
    protected MemberManager memberManager;                                                                      // 유저 데이터


    /**
     * Application Back Key 관련
     ************************************************************************************************************************************************/
    protected boolean isBackButtonNotice = true;                                                                    // Back Key 눌렀을 때 App 종료 문구 출력 여부
    private boolean isBackPressed = false;                                                                          // Back Key 눌렀는지 여부


    /**
     * Top Area Layout
     ************************************************************************************************************************************************/
    protected AppCompatTextView txt_centerTitle;                                                                    // Top Area CenterTitle TextView
    protected AppCompatImageView iv_back, iv_refresh, iv_completion;                                                // Top Area Image Back, Refresh, Completion
    protected RelativeLayout layout_back, layout_refresh;                                                           // Top Area Layout Common Left, Right

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        // Application 전역 정보 인터페이스
        mContext = this;

        // Application User Local DataBase
        memberManager = new MemberManager(mContext);

        // Application StatusBar Color
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(mContext, R.color.C_C90000));
        }

        if(Constant.DEBUG_MODE)
            Toast.makeText(mContext, "현재 디버그 모드입니다.",Toast.LENGTH_SHORT).show();
    }

    /**
     * Configuration Get Locale :: High version
     ************************************************************************************************************************************************/
    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getSystemLocale(Configuration config) {
        return config.getLocales().get(0);
    }

    /**
     * Back Key 동작.
     ************************************************************************************************************************************************/
    @Override
    public void onBackPressed() {
        if (!isBackButtonNotice || isBackPressed) {
            finish();
            return;
        }

        isBackPressed = true;

    }


    /**
     * 현재 Activity를 종료하지 않고 새로운 Activity를 start할 때 사용.
     ************************************************************************************************************************************************/
    protected void openActivity(Class<?> cls) {
        Intent intent = new Intent(mContext, cls);
        startActivity(intent);
    }

}
