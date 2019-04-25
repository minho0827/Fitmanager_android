package com.fitmanager.app.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.fitmanager.app.R;
import com.fitmanager.app.model.MemberVO;
import com.fitmanager.app.network.RestService;
import com.fitmanager.app.util.Constant;
import com.fitmanager.app.util.FitProgressBar;
import com.fitmanager.app.util.ImageUtils;
import com.fitmanager.app.util.LoginUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ProfileEditActivity extends AppCompatActivity {
    private static final String TAG = "ProfileEditActivity";
    //사진으로 전송시 되돌려 받을 번호
    static int REQUEST_PICTURE = 1;
    //앨범으로 전송시 돌려받을 번호
    static int REQUEST_PHOTO_ALBUM = 2;
    //첫번째 이미지 아이콘 샘플 이다.
    static String SAMPLEIMG = "ic_launcher.png";
    // Amazon S3
    CognitoCachingCredentialsProvider credentialsProvider;
    AmazonS3 s3;
    TransferUtility transferUtility;
    private static FitProgressBar mProgressBar = new FitProgressBar();

    private Toolbar mToolbar;
    private MemberVO mMemberVO;
    @BindView(R.id.edit_nickname)
    AppCompatEditText editNickname;
    @BindView(R.id.img_profile)
    ImageView imgProfile;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.tv_gender)
    TextView tvGender;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.frame_layout_profile_img)
    FrameLayout frameLayoutProfileImg;
    Dialog dialog;
    ImageView mIv;
//    @Bind(R.id.imgView);


    @Override
    protected void onResume() {
        getLoginUserInfoService();
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        ButterKnife.bind(this);
        setLayout();


        frameLayoutProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileEditActivity.this, "편집 클릭", Toast.LENGTH_SHORT).show();
                if (v.getId() == R.id.frame_layout_profile_img) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper
                            (ProfileEditActivity.this, R.style.myDialog));
                    View customLayout = View.inflate(ProfileEditActivity.this, R.layout.custom_button, null);
                    builder.setView(customLayout);

                    customLayout.findViewById(R.id.camera).setOnClickListener(this);
                    customLayout.findViewById(R.id.photoAlbum).setOnClickListener(this);

                    dialog = builder.create();
                    dialog.show();
                } else if (v.getId() == R.id.camera) {
                    //카메라버튼인 경우 일단 다이어로그를 끄고 사진을 찍는 함수를 불러오자
                    dialog.dismiss();
                    doTakePhotoAction();

                } else if (v.getId() == R.id.photoAlbum) {
                    //이경우역시 다이어로그를 끄고 앨범을 불러오는 함수를 불러오자!!
                    dialog.dismiss();
                    doTakeAlbumAction();
                }


            }
        });

        //Amazon S3 세팅
        initAmazonS3();
    }

    private void initAmazonS3() {
        credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "ap-northeast-2:793fc46e-c014-47ac-baa7-0eb1c1a6fb94", // Identity Pool ID
                Regions.AP_NORTHEAST_2 // Region
        );
        s3 = new AmazonS3Client(credentialsProvider);
        transferUtility = new TransferUtility(s3, getApplicationContext());
    }


    private void setSaveBtnEnabled(boolean isEnabled) {
        tvSave.setEnabled(isEnabled);
    }

    private void updateProfileRestService() {
        Log.d(TAG, "updateProfileRestService: ");
        showProgress();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_ADDR)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        Map<String, Object> param = new HashMap<>();
        param.put("nickname", editNickname.getText().toString());
        param.put("memberId", LoginUtils.getLoginUserVO().getMemberId());


        RestService service = retrofit.create(RestService.class);
        final Call<MemberVO> memberVO = service.updateUserInfo(param);

        memberVO.enqueue(new Callback<MemberVO>() {
            @Override
            public void onResponse(Call<MemberVO> call, Response<MemberVO> response) {

                if (memberVO != null) {
                    final MemberVO memberVO = response.body();
                    if (memberVO.getJoinResult() == 2) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("email", Constant.email);
//        map.put("pwd", Utils.encryptPassword(_passwordText.getText().toString()));
                        map.put("pwd", Constant.password);
                        /* 업데이트된 ImageUrl 을 DB에서 다시 가져옴*/
                        Toast.makeText(getApplicationContext(), "저장 되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "업데이트에 실패하였습니다.", Toast.LENGTH_SHORT).show();

                    }
                    Log.d(TAG, "## memberVO 가 null 입니다.");
                }
            }

            @Override
            public void onFailure(Call<MemberVO> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void saveProfileImage() {
        imgProfile.setDrawingCacheEnabled(true);
        imgProfile.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        imgProfile.layout(0, 0, imgProfile.getMeasuredWidth(), imgProfile.getMeasuredHeight());
        Bitmap logoPhoto = Bitmap.createBitmap(imgProfile.getDrawingCache());

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        logoPhoto.compress(Bitmap.CompressFormat.PNG, 100, stream);

        String fileName = LoginUtils.getLoginUserVO().getMemberId() + "_profile.png";
        Uri tempUri = getImageUri(getApplicationContext(), logoPhoto);
        File file = new File(getRealPathFromURI(tempUri));

        TransferObserver observer = transferUtility.upload(
                Constant.BUCKET_NAME,   /* 업로드 할 버킷 이름 */
                fileName,               /* 버킷에 저장할 파일의 이름 */
                file                    /* 버킷에 저장할 파일  */
        );

        setTransferObserverListener(observer);

    }

    private void setLayout() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("프로필");
        mToolbar.setTitleTextColor(Color.WHITE);


        editNickname.addTextChangedListener(new TextWatcher() {
            String beforeTxt;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "beforeTextChanged: s: " + s);

                beforeTxt = s.toString();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "afterTextChanged: s: " + s);
                if (StringUtils.isEmpty(beforeTxt)) {
                    setSaveBtnEnabled(false);
                    return;
                }
                if (!StringUtils.equals(beforeTxt, s.toString())) {
                    // 넥네임 Text 변경되었으므로, 저장버튼 활성화 시킴.
                    setSaveBtnEnabled(true);
                }

            }
        });
    }


    @Override
    public void onBackPressed() {
        if (isValueChanged(editNickname)) {
            Toast.makeText(this, "값이 변경되었습니다..", Toast.LENGTH_SHORT).show();
            return;
        }
        super.onBackPressed();

    }

    private boolean isValueChanged(AppCompatEditText editText) {
        if (editText.getTag() != null &&
                !StringUtils.equals(editText.getText().toString(), editText.getTag().toString())) {
            return true;
        }
        return false;

    }


    private void getLoginUserInfoService() {
        showProgress();
        Log.d(TAG, "## getLoginUserInfoService: ");

        Map<String, Object> param = new HashMap<>();
        param.put("memberId", LoginUtils.getLoginUserVO().getMemberId());
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_ADDR)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RestService service = retrofit.create(RestService.class);
        final Call<MemberVO> memberVOCall = service.getLoginUserInfoService(param);

        memberVOCall.enqueue(new Callback<MemberVO>() {
            @Override
            public void onResponse(Call<MemberVO> call, Response<MemberVO> response) {
                final MemberVO memberVO = response.body();
                if (memberVO != null) {
                    mMemberVO = memberVO;
                    editNickname.setText(memberVO.getNickname());
                    tvEmail.setText(memberVO.getEmail());

                    ImageUtils.getProfileImage(getApplicationContext(),
                            imgProfile,
                            LoginUtils.getLoginUserVO().getProfileImg()
                    );

                    if (memberVO.getGender() == 1) {
                        tvGender.setText("남자");
                    } else {
                        tvGender.setText("여자");

                    }
                }
                hideProgress();
            }

            @Override
            public void onFailure(Call<MemberVO> call, Throwable t) {
                hideProgress();
                Log.e(TAG, "onFailure: 데이터 가져오는데 실패..");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    /**
     * 카메라에서 사진촬영
     */
    public void doTakePhotoAction() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        //임시로 사용 파일의 경로를 생성
//        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
//        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
//
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
//        startActivityForResult(intent, REQUEST_PICTURE);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_PICTURE);
    }


    /**
     * 앨범에서 이미지 가져오기
     */
    public void doTakeAlbumAction() {
//        앨범호출
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//        startActivityForResult(intent, REQUEST_PHOTO_ALBUM);

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("outputX", 256);
        intent.putExtra("outputY", 256);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUEST_PHOTO_ALBUM);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PICTURE) {
                //사진을 찍은경우 그사진을 로드해온다.
                final Uri uri = data.getData();
//                imgProfile.setImageURI(uri);


                Glide.with(getApplicationContext())
                        .load(uri)
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.placeholder)
                                .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .centerCrop())
                        .transition(withCrossFade())
                        .into(imgProfile);

//                imgProfile.setImageBitmap(loadPicture());
            } else if (requestCode == REQUEST_PHOTO_ALBUM) {
                //앨범에서 호출한경우 data는 이전인텐트(사진갤러리)에서 선택한 영역을 가져오게된다.
                final Bundle extras = data.getExtras();
                Bitmap logoPhoto = extras.getParcelable("data");
                imgProfile.setImageBitmap(logoPhoto);
                saveProfileImage();
            }

            // 프로파일 이미지 변경되었으므로, 저장버튼 활성화 시킴.
            setSaveBtnEnabled(true);
        }
    }


    private void setTransferObserverListener(TransferObserver transferObserver) {

        transferObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.e(TAG, state + "");
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                int percentage = (int) (bytesCurrent / bytesTotal * 100);
                Log.d(TAG, "onProgressChanged > percentage: " + percentage);

                if (percentage == 100) {

                    restUpdateProfileImgService();
                }
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e(TAG, "error");
            }

        });
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    Bitmap loadPicture() {
        //사진찍은 것을 로드 해오는데 사이즈를 조절하도록하자!!일단 파일을 가져오고
        File file = new File(Environment.getExternalStorageDirectory(), SAMPLEIMG);
        //현재사진찍은 것을 조절하구이해서 조절하는 클래스를 만들자.
        BitmapFactory.Options options = new BitmapFactory.Options();
        //이제 사이즈를 설정한다.
        options.inSampleSize = 4;
        //그후에 사진 조정한것을 다시 돌려보낸다.
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }

    //  S3에 업로드한 ImageURL을 MySql DB에 업데이트
    private void restUpdateProfileImgService() {
        showProgress();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_ADDR)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RestService service = retrofit.create(RestService.class);

        int memberId = LoginUtils.getLoginUserVO().getMemberId();

        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(memberId);
        memberVO.setProfileImg(ImageUtils.getProfileImageUrl(memberId));

        final Call<Integer> coachList = service.updateProfileImg(memberVO);

        coachList.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Integer result = response.body();

                Map<String, Object> map = new HashMap<>();
                map.put("email", Constant.email);
//        map.put("pwd", Utils.encryptPassword(_passwordText.getText().toString()));
                map.put("pwd", Constant.password);
                /* 업데이트된 ImageUrl 을 DB에서 다시 가져옴*/

                MemberVO memberImageVO = LoginUtils.getLoginUserVO();
                memberImageVO.setProfileImg(ImageUtils.getProfileImageUrl(memberId));
                LoginUtils.setLoginUserVO(memberImageVO);
//                getRestService(map);
                Toast.makeText(getApplicationContext(), "저장 되었습니다.", Toast.LENGTH_SHORT).show();

                Log.d(TAG, "onResponse: updated count: " + result);
                hideProgress();
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                hideProgress();
                Log.e(TAG, "## onFailure: ");
            }
        });
    }

    private void getRestService(Map<String, Object> param) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_ADDR)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RestService service = retrofit.create(RestService.class);
        final Call<MemberVO> memberVO = service.getLoginMember(param);

        memberVO.enqueue(new Callback<MemberVO>() {
            @Override
            public void onResponse(Call<MemberVO> call, Response<MemberVO> response) {

                final MemberVO memberVO = response.body();
                if (memberVO != null) {
                    LoginUtils.setLoginUserVO(memberVO);

                    ImageUtils.getProfileImage(getApplicationContext(),
                            imgProfile,
                            LoginUtils.getLoginUserVO().getProfileImg()
                    );
                }
                hideProgress();
            }

            @Override
            public void onFailure(Call<MemberVO> call, Throwable t) {
                hideProgress();
                Log.d(TAG, "onFailure: ");
                hideProgress();

            }
        });
    }


    private void showProgress() {
        if (mProgressBar != null) {
            mProgressBar.show(this);
        }
    }


    private void hideProgress() {
        if (mProgressBar != null) {
            mProgressBar.hide();
        }
    }


    @OnClick(R.id.tv_save)
    void tvSaveOnClick() {
        Log.d(TAG, "tvSaveOnClick: ");
        updateProfileRestService();
    }
}
