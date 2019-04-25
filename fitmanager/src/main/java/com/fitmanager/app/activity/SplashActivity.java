package com.fitmanager.app.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fitmanager.app.BaseApplication;
import com.fitmanager.app.R;
import com.fitmanager.app.model.MemberVO;
import com.fitmanager.app.model.VersionCheckVO;
import com.fitmanager.app.network.MemberRestService;
import com.fitmanager.app.util.AlertDialogUtil;
import com.fitmanager.app.util.Constant;
import com.fitmanager.app.util.FitProgressBar;
import com.fitmanager.app.util.LoginUtils;
import com.fitmanager.app.util.OnSingleClickListener;
import com.fitmanager.app.util.PrefsUtils;
import com.fitmanager.app.util.RequestCodeUtil;
import com.fitmanager.app.util.RetroUtil;
import com.fitmanager.app.util.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SplashActivity extends BaseActivity {
    private FitProgressBar mProgressBar = new FitProgressBar();

    /**
     * GlobalApplication의 함수와 매소드를 사용하기 위한 생성자
     ************************************************************************************************************************************************/
    private BaseApplication mBaseApplication = new BaseApplication();
    private MemberVO mMemberVO;

    /**
     * Static String variable
     ************************************************************************************************************************************************/
    private static final String strPATH = "path";                                                                   // 문자 path
    private String strVersionCheck = null;                                                                         // App Version Check


    private String mStrVersionCheck = null;                                                                         // App Version Check
//    Y:공지사항 보이기 N:공지사항 감추기
    private String strIsShow = "N";


    private String strPushData;

    private final static String TAG = "SplashActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiInit();
        setContentView(R.layout.activity_splash);
        settingSplashMethod();

        boolean isAutoLogin = PrefsUtils.getBoolean(this, Constant.AUTO_LOGIN, false);
        if (isAutoLogin) {
            String userId = PrefsUtils.getString(this, Constant.USER_ID);
            String userPwd = PrefsUtils.getString(this, Constant.USER_PWD);

            gotoLogin(userId, userPwd);
        } else {
            permissionCheck();
        }

    }


    private void gotoLogin(String userId, String userPwd) {
        Constant.email = userId;
        Constant.password = userPwd;

        Map<String, Object> map = new HashMap<>();
        map.put("email", Constant.email);
        map.put("accessToken", Constant.password);
        requestLogin(map);
    }

    /**
     * 로그인 요청
     * **********************************************************************************************************************************************
     *
     * @param param : email,accessToken
     */
    private void requestLogin(Map<String, Object> param) {

        Call<MemberVO> requestLoginCall = RetroUtil.createService
                (Constant.SERVER_ADDR, MemberRestService.class).getLoginMember(param);

        requestLoginCall.enqueue(new Callback<MemberVO>() {
            @Override
            public void onResponse(Call<MemberVO> call, Response<MemberVO> response) {
                if (response.isSuccessful()) {
                    final MemberVO memberVO = response.body();
                    if (memberVO != null) {
                        onLoginSuccess(memberVO);
                    }

                }

            }

            @Override
            public void onFailure(Call<MemberVO> call, Throwable t) {

            }
        });

    }


    public void onLoginSuccess(MemberVO memberVO) {
        LoginUtils.setLoginUserVO(memberVO);

        Log.d(TAG, "## onResponse: " + memberVO.getMemberId());
        Log.d(TAG, "## onResponse: " + memberVO.getName());

        com.fitmanager.app.util.Utils.hideProgress(mProgressBar);
        Toast.makeText(mContext, "로그인 되었습니다.", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void onLoginFailed(@Nullable String errorResult) {
        if (StringUtils.isNotEmpty(errorResult)) {
            final String message = "로그인에 실패하였습니다. msg=" + errorResult;
        }

    }

    private void uiInit() {

        isBackButtonNotice = false;
        mMemberVO = memberManager.getMemberData();

        if (this.getIntent() != null) {
            if (StringUtils.isNotEmpty(this.getIntent().getStringExtra(strPATH))) {
                strPushData = this.getIntent().getStringExtra(strPATH);

                if (StringUtils.equals("transfer", strPushData)) {
//                    noticeMessage = true;
//                    Log.d(TAG, noticeMessage + "기본적으로 팝업을 누른 경우 무조건 나오는 로그");
                    Log.d(TAG, "PushData ::" + strPushData);
                }
                Log.d(TAG, "");
            }

            if (StringUtils.equals("transfer", this.getIntent().getStringExtra(Constant.INTENT_PUSH_TRANSFER))) {
                strPushData = this.getIntent().getStringExtra(Constant.INTENT_PUSH_TRANSFER);

                if (StringUtils.equals(strPushData, "transfer")) {
//                    noticeMessage = true;
//                    Log.d(TAG, noticeMessage + "");

                }
                Log.d(TAG, "Splash에서 재시작으로 나오는 로그");
                Log.d(TAG, "PushData :: " + strPushData);
            }
        }

    }


    /**
     * (1) + (2) + (3) :: SplashActivity 작업 처리
     ************************************************************************************************************************************************/
    private void settingSplashMethod() {
        if (Utils.isNetworkConnected(mContext)) {
            if (Constant.DEBUG_MODE) {

                permissionCheck();

            } else {
                if (requestGooglePlayService()) {

                    // 버전 체크
                    getMainnoticeAppversionCheck();
                }

            }
        } else {
            // 인터넷이 연결되지 않은 경우 팝업창
            AlertDialogUtil.showSingleDialog(mContext, getString(R.string.msg_not_internet), new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    AlertDialogUtil.dismissDialog();
                    finish();
                }
            });
        }
    }

    private void permissionCheck() {
//        PermissionUtil.checkAndRequestPermission(this, PERMISSION)

        goMain();
    }

    private void goMain() {
        // 버전 체크 X
        Handler splash_handler = new Handler();
        splash_handler.postDelayed(() -> {
            openActivity(MainActivity.class);
            finish();
        }, 100);
    }

    private void getMainnoticeAppversionCheck() {

        Map<String, Object> param = new HashMap<>();
        param.put("appKind", Constant.APP_KIND);

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_ADDR)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        MemberRestService service = retrofit.create(MemberRestService.class);
        final Call<VersionCheckVO> versionCheckVOCall = service.getMainnoticeAppversionCheck(param);

        versionCheckVOCall.enqueue(new Callback<VersionCheckVO>() {
            @Override
            public void onResponse(Call<VersionCheckVO> call, Response<VersionCheckVO> response) {

                Log.d(TAG, "onResponse: " + response.body());

                final VersionCheckVO versionCheckVO = response.body();
                if (versionCheckVO != null) {
                    VersionCheckVO.ResultMessage resultMessage = versionCheckVO.getResultMessage();
                    Log.d(TAG, "resultMessage: " + resultMessage);

                    switch (resultMessage.getCode()) {
                        case Constant.RS_SUCCESS:
                            List<VersionCheckVO.Notice> noticeList = versionCheckVO.getNoticeList();
                            if (CollectionUtils.isNotEmpty(noticeList)) {
                                String noticeTitle = noticeList.get(0).getNoticeTitle();
                                String noticeMessage = noticeList.get(0).getNoticeMessage();
                                String appClose = noticeList.get(0).getAppClose();

                                // App 이 종료되어야 할 경우.(서버에서 컨트롤)
                                if (Constant.Y.equals(appClose)) {
                                    AlertDialogUtil.showSingleDialog(mContext, noticeMessage, new OnSingleClickListener() {

                                        @Override
                                        public void onSingleClick(View v) {
                                            finish();
                                        }
                                    });
                                    return;
                                } else {
                                    if (!isFinishing()) {
                                        AlertDialogUtil.showNoticeDialog(mContext, noticeTitle, noticeMessage, new OnSingleClickListener() {
                                            @Override
                                            public void onSingleClick(View v) {
                                                AlertDialogUtil.dismissDialog();
                                                permissionCheck();
                                            }
                                        }, dialogInterface -> {
                                            AlertDialogUtil.dismissDialog();
                                            permissionCheck();

                                        });
                                    }
                                }
                            }

                            if (versionCheckVO.getVersion() != null) {
                                strVersionCheck = versionCheckVO.getVersion().getLatestVersion();

                                // local version 과 비교로직 필요.
//                                if (localVersion < serverVersion) {
//                                    requestAlertSetting();
//                                    return;
//                                }
                            }
                            break;

                        case Constant.RS_ERRORS_FAIL:
                            AlertDialogUtil.showSingleDialog(mContext, getString(R.string.error_e00), new OnSingleClickListener() {
                                @Override
                                public void onSingleClick(View v) {
                                    AlertDialogUtil.dismissDialog();
                                    finish();
                                }
                            });
                            break;

                        default:
                            AlertDialogUtil.showSingleDialog(mContext, getString(R.string.error_e00), new OnSingleClickListener() {
                                @Override
                                public void onSingleClick(View v) {
                                    AlertDialogUtil.dismissDialog();
                                    finish();
                                }
                            });
                            break;
                    }
                } else

                {
                    AlertDialogUtil.showSingleDialog(mContext, getString(R.string.error_e00), new OnSingleClickListener() {
                        @Override
                        public void onSingleClick(View v) {
                            AlertDialogUtil.dismissDialog();
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<VersionCheckVO> call, final Throwable t) {

                Log.d(TAG, "onFailure: " + t.getMessage());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Alert Setting
     ************************************************************************************************************************************************/
    private void requestAlertSetting() {

        AlertDialogUtil.dismissDialog();
        /**
         * 현재버전이 서버에서 지정한 버전과 같은 경우 Boolean값을 설정하며 return합니다.
         * 현재버전이 서버에서 지정한 버전과 다른 경우 구글플레이로 이동할 수 있는 여부의 팝업창을 띄워줍니다.
         */
        if (Utils.getCurrentVersion(mContext).equals(mStrVersionCheck)) {
            Handler splash_handler = new Handler();
            splash_handler.postDelayed(() -> {
                openActivity(LoginActivity.class);
                finish();
            }, 1000);
        } else {
            AlertDialogUtil.showDoubleDialog(mContext, getString(R.string.msg_quest_patch), new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    AlertDialogUtil.dismissDialog();
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + mContext.getPackageName())));
                    } catch (android.content.ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + mContext.getPackageName())));
                    }
                    finish();
                }
            }, new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    AlertDialogUtil.dismissDialog();
                    finish();
                }
            });
        }
    }


    /**
     * 구글 플레이 서비스 체크 여부
     ************************************************************************************************************************************************/
    private boolean requestGooglePlayService() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(mContext);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result, RequestCodeUtil.GooglePlayServiceCheckReqCode.REQ_GOOGLE_CHECK, dialogInterface -> finish()).show();
            }
            return false;
        }
        return true;
    }

}
