package com.fitmanager.app;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.bumptech.glide.Glide;
import com.fitmanager.app.model.MemberVO;
import com.fitmanager.app.util.Utils;
import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;

/**
 * Created by Home on 2018. 3. 21..
 */

public class BaseApplication extends MultiDexApplication {

    private final static String TAG = "BaseApplication";
    private static volatile BaseApplication instance = null;
    /**
     * 현재 사용되는 Class Name
     ************************************************************************************************************************************************/
    public static String CLASS_NAME;                                                                                // App 현재 Class Name
    public final static String APP_NAME = "Fitmanager";

    /**
     * Application Context
     ************************************************************************************************************************************************/
    private static BaseApplication mContext;                                                                      // Application Context

    /**
     * Application User Local DataBase
     ************************************************************************************************************************************************/
    private MemberVO mMemberVO;                                                                                      // 유저 정보

    public static String APP_VERSION;


    /**
     * Application Status
     ************************************************************************************************************************************************/
    private AppStatus appStatus = AppStatus.FOREGROUND;                                                             // App 현재 화면 상태

    /**
     * 예외처리 ExceptionHandler
     ************************************************************************************************************************************************/
    private Thread.UncaughtExceptionHandler androidDefaultUEH;                                                      // 기본 Exception Handler



    /**
     * 현재 사용되는 Class Name
     ************************************************************************************************************************************************/
    public static void setClassName(String className) {
        CLASS_NAME = className;
    }



    /**
     * Application onConfigurationChanged()
     * Android는 모든 변화에 대해 Activity를 재시작하도록 되어 있습니다.
     * Manifest에 Activity의 configchanges를 기록해두면 해당 변화에 Activity를 재시작 하지 않습니다.
     * Activity에 대한 변화가 있는 경우 작업할 내용을 onConfigurationChanged에 작성합니다.
     ************************************************************************************************************************************************/
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }






    private static class KakaoSDKAdapter extends KakaoAdapter {
        /**
         * Session Config에 대해서는 default값들이 존재한다.
         * 필요한 상황에서만 override해서 사용하면 됨.
         *
         * @return Session의 설정값.
         */
        @Override
        public ISessionConfig getSessionConfig() {
            return new ISessionConfig() {
                @Override
                public AuthType[] getAuthTypes() {
                    return new AuthType[]{AuthType.KAKAO_ACCOUNT};
                }

                @Override
                public boolean isUsingWebviewTimer() {
                    return false;
                }

                @Override
                public boolean isSecureMode() {
                    return false;
                }

                @Override
                public ApprovalType getApprovalType() {
                    return ApprovalType.INDIVIDUAL;
                }

                @Override
                public boolean isSaveFormData() {
                    return true;
                }
            };
        }

        @Override
        public IApplicationConfig getApplicationConfig() {
            return new IApplicationConfig() {
                @Override
                public Context getApplicationContext() {
                    return BaseApplication.getGlobalApplicationContext();
                }
            };
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        KakaoSDK.init(new BaseApplication.KakaoSDKAdapter());
        APP_VERSION = Utils.getCurrentVersion(getContext());

    }


    /**
     * Enum Class :: App Status
     ************************************************************************************************************************************************/
    public enum AppStatus {
        BACKGROUND, RETURNED_TO_FOREGROUND, FOREGROUND
    }

    /**
     * MultiDexApplication Get Context
     ************************************************************************************************************************************************/
    public BaseApplication getMyApplcationContext(Context context) {
        return (BaseApplication) context.getApplicationContext();
    }

    /**
     * Application Get Context
     ************************************************************************************************************************************************/
    public static Context getContext() {
        return mContext;
    }

    /**
     * Get AppStatus
     ************************************************************************************************************************************************/
    public AppStatus getAppStatus() {
        return appStatus;
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Glide.get(this).trimMemory(level);
    }


    /**
     * /**
     * singleton 애플리케이션 객체를 얻는다.
     *
     * @return singleton 애플리케이션 객체
     */
    public static BaseApplication getGlobalApplicationContext() {
        if (instance == null)
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        return instance;
    }

    /**
     * 애플리케이션 종료시 singleton 어플리케이션 객체 초기화한다.
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * ActivityLifecycleCallbacks
     ************************************************************************************************************************************************/
    private class MyActivityLifeCycleCallbacks implements ActivityLifecycleCallbacks {

        private int runningCount = 0;

        @Override
        public void onActivityStarted(Activity activity) {
            if(++runningCount == 1) {
                appStatus = AppStatus.RETURNED_TO_FOREGROUND;
            } else if(runningCount > 1) {
                appStatus = AppStatus.FOREGROUND;
            }
        }

        @Override
        public void onActivityStopped(Activity activity) {
            if(--runningCount == 0) {
                appStatus = AppStatus.BACKGROUND;
            }
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}

        @Override
        public void onActivityResumed(Activity activity) {}

        @Override
        public void onActivityPaused(Activity activity) {}

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

        @Override
        public void onActivityDestroyed(Activity activity) {}
    }
}
