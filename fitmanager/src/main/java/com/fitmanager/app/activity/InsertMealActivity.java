package com.fitmanager.app.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.erikagtierrez.multiple_media_picker.Gallery;
import com.fitmanager.app.R;
import com.fitmanager.app.network.MemberRestService;
import com.fitmanager.app.util.Constant;
import com.fitmanager.app.util.RetroUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 코치 식단등록 페이지
 */

public class InsertMealActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    static final int OPEN_MEDIA_PICKER = 1;

    private static final String TAG = "InsertMealActivity";
    Context mContext;
    private int mCoachId;

    @BindView(R.id.img_camera)
    ImageView imgCamera;
    @BindView(R.id.tv_gallery_count)
    AppCompatTextView tvGalleryCount;                                                               // 갤러리 선택된 이미지 갯수

    @BindView(R.id.togg_btn_breakfast)
    ToggleButton toggBtnBreakfast;

    @BindView(R.id.togg_btn_lunch)
    ToggleButton toggBtnLunch;

    @BindView(R.id.togg_btn_dinner)
    ToggleButton toggBtnDinner;

    @BindView(R.id.togg_btn_dessert)
    ToggleButton toggBtnDessert;

    @BindView(R.id.edit_title)
    AppCompatEditText editTitle;

    @BindView(R.id.btn_insert)
    AppCompatButton btnInsert;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        mContext = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_insert);
        ButterKnife.bind(this);

        /* 식단올릴 사진 갤러리 열기  */
        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionREAD_EXTERNAL_STORAGE(mContext);

                Intent intent;
                intent = new Intent(mContext, Gallery.class);
                // Set the title
                intent.putExtra("title", "Select media");
                // Mode 1 for both images and videos selection, 2 for images only and 3 for videos!
                intent.putExtra("mode", 1);
                intent.putExtra("maxSelection", 3); // Optional
                startActivityForResult(intent, OPEN_MEDIA_PICKER);


            }
        });

        /* 아침 클릭 */
        toggBtnBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setButtonState(toggBtnBreakfast, true);
                setButtonState(toggBtnLunch, false);
                setButtonState(toggBtnDinner, false);
                setButtonState(toggBtnDessert, false);

                if (toggBtnBreakfast.isChecked()) {
                    toggBtnBreakfast.setBackgroundResource(R.color.maincolor);
                } else {
                    toggBtnBreakfast.setBackgroundResource(R.color.gray);
                }

            }
        });

         /* 점심 클릭 */
        toggBtnLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonState(toggBtnBreakfast, false);
                setButtonState(toggBtnLunch, true);
                setButtonState(toggBtnDinner, false);
                setButtonState(toggBtnDessert, false);
                if (toggBtnLunch.isChecked()) {
                    toggBtnLunch.setBackgroundResource(R.color.maincolor);
                } else {
                    toggBtnLunch.setBackgroundResource(R.color.gray);
                }

            }
        });


         /* 저녁 클릭 */
        toggBtnDinner.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setButtonState(toggBtnBreakfast, false);
                setButtonState(toggBtnLunch, false);
                setButtonState(toggBtnDinner, true);
                setButtonState(toggBtnDessert, false);

                if (toggBtnDinner.isChecked()) {
                    toggBtnDinner.setBackgroundResource(R.color.maincolor);
                } else {
                    toggBtnDinner.setBackgroundResource(R.color.gray);
                }

            }
        });

        /* 간식 클릭 */
        toggBtnDessert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setButtonState(toggBtnBreakfast, false);
                setButtonState(toggBtnLunch, false);
                setButtonState(toggBtnDinner, false);
                setButtonState(toggBtnDessert, true);

                if (toggBtnDessert.isChecked()) {
                    toggBtnDessert.setBackgroundResource(R.color.maincolor);
                } else {
                    toggBtnDessert.setBackgroundResource(R.color.gray);
                }
            }
        });

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap params = new HashMap();
                params.put("title", editTitle.toString());
                params.put("coachId", mCoachId);
                params.put("content", editTitle.toString());
                requestMealInsert(params);
            }
        });

    }


    private void setButtonState(ToggleButton btn, boolean isChecked) {
        btn.setChecked(isChecked);
        if (isChecked) {
            btn.setBackgroundResource(R.color.maincolor);
        } else {
            btn.setBackgroundResource(R.color.gray);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Check which request we're responding to
        if (requestCode == OPEN_MEDIA_PICKER) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> selectionResult = data.getStringArrayListExtra("result");
            }
        }
    }


    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }


    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{permission},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    /**
     * 식단 등록 요청
     * **********************************************************************************************************************************************
     *
     * @param param : email,accessToken
     */
    private void requestMealInsert(Map<String, Object> param) {


        Call<Integer> requestLoginCall = RetroUtil.createService(Constant.SERVER_ADDR, MemberRestService.class).requestMealInsert(param);

        requestLoginCall.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int result = response.body();
                    if (result > 0) {
                    }

                    Toast.makeText(mContext, "식단이 등록 되었습니다", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });

    }


}
