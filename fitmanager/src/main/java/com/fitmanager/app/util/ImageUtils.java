package com.fitmanager.app.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ImageUtils {
    private static final String TAG = ImageUtils.class.getSimpleName();

    public static String getProfileImageUrl(int memberId) {
        return String.format("https://s3.ap-northeast-2.amazonaws.com/s3testymh/%d_profile.png", memberId);
    }

//   앱에서 프로필 이미지 업로드할경우
//    public static void getProfileImage(Context context, ImageView imageView, int memberId) {
//        String imgUrl = getProfileImageUrl(memberId);
//        getGlideImageUrl(context, imageView, imgUrl);
//    }

//    DB에서 프로필 이미지 URL이 있을경우
    public static void getProfileImage(Context context, ImageView imageView, String profileImageUrl) {
        getGlideImageUrl(context, imageView, profileImageUrl);
    }

    private static void getGlideImageUrl(Context context, ImageView imageView, String profileImageUrl) {
        Glide.with(context)
                .load(profileImageUrl)
                .centerCrop()
                .bitmapTransform(new CropCircleTransformation(context))
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView);
    }
}
