package com.fitmanager.app.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoginStatusCallback;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.fitmanager.app.R;
import com.fitmanager.app.listener.ButtonSelectorListener;
import com.fitmanager.app.model.MemberVO;
import com.fitmanager.app.network.MemberRestService;
import com.fitmanager.app.util.Constant;
import com.fitmanager.app.util.FitProgressBar;
import com.fitmanager.app.util.LoginUtils;
import com.fitmanager.app.util.MemberManager;
import com.fitmanager.app.util.PrefsUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import mehdi.sakout.fancybuttons.FancyButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private MemberVO mMemberVO;
    protected MemberManager memberManager;                                                                      // 유저 데이터

    private static final int REQUEST_SIGNUP = 0;
    CallbackManager mCallbackManager;
    private LoginStatusCallback mLoginCallback;
    FitProgressBar mProgressBar = new FitProgressBar();
    private CheckBox cbAutoLogin;                                        // 이메일 저장, 패스워드 저장

    @BindView(R.id.et_emailID)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPasswordText;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_signup)
    AppCompatTextView _signupLink;
    @BindView(R.id.btn_facebook)
    LoginButton btnFacebookLogin;
    @BindView(R.id.btn_kakao)
    com.kakao.usermgmt.LoginButton btnKakaoLogin;

    @BindView(R.id.fake_kakao)
    ImageView fakeKakao;
    @BindView(R.id.fake_facebook)
    ImageView fakeFacebook;


    Context mContext;
    private SessionCallback callback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        uiInit();
    }

    private void uiInit() {
        mCallbackManager = CallbackManager.Factory.create();
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();
        facebookBtnLayoutInit();
        fakeFacebook = findViewById(R.id.fake_facebook);
        fakeKakao = findViewById(R.id.fake_kakao);
        cbAutoLogin = findViewById(R.id.cb_auto_login);

        fakeFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFacebookLogin.performClick();
            }
        });


        ButtonSelectorListener buttonSelector = new ButtonSelectorListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        }, null);


        fakeKakao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnKakaoLogin.performClick();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });


        btnKakaoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

            }
        });


        btnFacebookLogin.setReadPermissions("email");
        // Other app specific specialization

        /* 페이스북 회원가입 */
        // Callback registration
        btnFacebookLogin.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                processLoginByFacebookId(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        Log.d(TAG, "Key Hash: " + getKeyHash(this));


    }

    private void facebookBtnLayoutInit() {
        FancyButton facebookLoginBtn = new FancyButton(this);
        facebookLoginBtn.setText("");
        facebookLoginBtn.setBackgroundColor(Color.parseColor("#3b5998"));
        facebookLoginBtn.setFocusBackgroundColor(Color.parseColor("#5474b8"));
        facebookLoginBtn.setTextSize(17);
        facebookLoginBtn.setRadius(5);
        facebookLoginBtn.setIconResource("\uf082");
        facebookLoginBtn.setIconPosition(FancyButton.POSITION_LEFT);
        facebookLoginBtn.setFontIconSize(30);
    }

    public static String getKeyHash(final Context context) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return android.util.Base64.encodeToString(md.digest(), android.util.Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.w(TAG, "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }

    private void processLoginByFacebookId(AccessToken accessToken) {
        // App code
        GraphRequest request = GraphRequest.newMeRequest(accessToken,

                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Application code
                        try { // Application code
                            String id = object.getString("id");
                            String name = object.getString("name");
                            String email = object.getString("email");
                            String gender = object.getString("gender");

                            Log.d(TAG, "newMeRequest");
                            Log.d(TAG, "id: " + id);
                            Log.d(TAG, "name: " + name);
                            Log.d(TAG, "email: " + email);
                            Log.d(TAG, "gender: " + gender);

                            Map<String, String> map = new HashMap<>();
                            map.put("snsType", Constant.SnsType.FACEBOOK.value);
                            map.put("snsId", id);
                            map.put("email", email);
                            map.put("gender", gender);
                            map.put("userName", name);

                            snsLogin(map);
                        } catch (JSONException E) {
                            E.printStackTrace();
                        }
                    }
                }
        );
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed(null);
            return;
        }

        btnLogin.setEnabled(false);

        Constant.email = etEmail.getText().toString();
        Constant.password = etPasswordText.getText().toString();
        //암호화
//        Constant.password = Utils.encryptPassword(etPasswordText.getText().toString());

        if (cbAutoLogin.isChecked()) {
            PrefsUtils.setString(this, Constant.USER_ID, Constant.email);
            PrefsUtils.setString(this, Constant.USER_PWD, Constant.password);
            PrefsUtils.setBoolean(this, Constant.AUTO_LOGIN, true);
        } else {
            PrefsUtils.setString(this, Constant.USER_ID, "");
            PrefsUtils.setString(this, Constant.USER_PWD, "");
            PrefsUtils.setBoolean(this, Constant.AUTO_LOGIN, false);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("email", Constant.email);
        map.put("pwd", Constant.password);

        getRestService(map);
    }

    public void snsLogin(Map<String, String> map) {
        btnLogin.setEnabled(false);
        btnKakaoLogin.setEnabled(false);


        showProgress();
        getSnsRestService(map);
    }

    private void getSnsRestService(Map<String, String> param) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_ADDR)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        MemberRestService service = retrofit.create(MemberRestService.class);
        final Call<MemberVO> memberVO = service.getSnsLoginMember(param);

        memberVO.enqueue(new Callback<MemberVO>() {
            @Override
            public void onResponse(Call<MemberVO> call, Response<MemberVO> response) {

                final MemberVO memberVO = response.body();
                if (memberVO != null) {
                    onLoginSuccess(memberVO);
                }
            }

            @Override
            public void onFailure(Call<MemberVO> call, final Throwable t) {

                Log.d(TAG, "onFailure: " + t.getMessage());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                onLoginFailed(null);
            }
        });
    }

    private void getRestService(Map<String, Object> param) {
        showProgress();

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_ADDR)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        MemberRestService service = retrofit.create(MemberRestService.class);
        final Call<MemberVO> memberVO = service.getLoginMember(param);

        memberVO.enqueue(new Callback<MemberVO>() {
            @Override
            public void onResponse(Call<MemberVO> call, Response<MemberVO> response) {

                final MemberVO memberVO = response.body();
                if (memberVO != null) {
                    onLoginSuccess(memberVO);
                }
            }

            @Override
            public void onFailure(Call<MemberVO> call, Throwable t) {

                Log.d(TAG, "onFailure: ");

                onLoginFailed(null);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult requestCode: " + requestCode + ", resultCode: " + resultCode);

        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    private void showProgress() {
        if (mProgressBar != null && !isFinishing()) {
            Log.d(TAG, "showProgress()!!!");
            mProgressBar.show(mContext);
        }
    }


    public void onLoginSuccess(MemberVO memberVO) {
        LoginUtils.setLoginUserVO(memberVO);

        Log.d(TAG, "## onResponse: " + memberVO.getMemberId());
        Log.d(TAG, "## onResponse: " + memberVO.getName());

        com.fitmanager.app.util.Utils.hideProgress(mProgressBar);

        Toast.makeText(mContext, "로그인 되었습니다.", Toast.LENGTH_SHORT).show();
        finish();
        btnLogin.setEnabled(true);
        btnKakaoLogin.setEnabled(true);
        finish();
    }

    public boolean validate() {
        boolean valid = true;

        String email = etEmail.getText().toString();
        String password = etPasswordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("이메일을 입력해주세요");
            valid = false;
        } else {
            etEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            etPasswordText.setError("패스워드를 4글자 이상 10글자 이하로 입력해주세요");
            valid = false;
        } else {
            etPasswordText.setError(null);
        }

        return valid;
    }




    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            Log.d(TAG, "onSessionOpened");
            requestMe();
//            redirectSignupActivity();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Logger.e(exception);
            }
        }
    }

    private void onLoginFailed(@Nullable String errorResult) {
        if (StringUtils.isNotEmpty(errorResult)) {
            final String message = "로그인에 실패하였습니다. msg=" + errorResult;
            Log.e(TAG, "onLoginFailed: " + message);
        }

        com.fitmanager.app.util.Utils.hideProgress(mProgressBar);

        Toast.makeText(getBaseContext(), "로그인에 실패 하였습니다", Toast.LENGTH_SHORT).show();
        btnLogin.setEnabled(true);
        btnKakaoLogin.setEnabled(true);
    }

    private void requestMe() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                onLoginFailed(errorResult.getErrorMessage());
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                onLoginFailed(errorResult.getErrorMessage());
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                Logger.d("UserProfile : " + userProfile);

                // Application code
                String id = String.valueOf(userProfile.getId());
                String name = userProfile.getNickname();
                String email = userProfile.getEmail();

                Log.d(TAG, "onSuccess");
                Log.d(TAG, "id: " + id);
                Log.d(TAG, "name: " + name);
                Log.d(TAG, "email: " + email);

                Map<String, String> map = new HashMap<>();
                map.put("snsType", Constant.SnsType.KAKAOTALK.value);
                map.put("snsId", id);
                map.put("email", email);
                map.put("userName", name);

                snsLogin(map);
            }

            @Override
            public void onNotSignedUp() {
                onLoginFailed("KakaoTalk 에 가입되지 않았습니다.");
            }
        });
    }


}
