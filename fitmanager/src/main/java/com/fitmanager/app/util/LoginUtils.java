package com.fitmanager.app.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.fitmanager.app.activity.LoginActivity;
import com.fitmanager.app.model.MemberVO;

import org.apache.commons.lang3.StringUtils;

public class LoginUtils {
    private static MemberVO loginUserVO;

    public static MemberVO getLoginUserVO() {
        return loginUserVO;
    }


    public static boolean isLoggedIn() {

        return loginUserVO != null
                && StringUtils.isNotBlank(loginUserVO.getEmail());
    }

    public static void setLoginUserVO(MemberVO loginUserVO) {
        LoginUtils.loginUserVO = loginUserVO;
    }

    public static void showLoginDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("로그인이 필요한 서비스입니다.");
        builder.setMessage("로그인 하시겠습니까?");
        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
