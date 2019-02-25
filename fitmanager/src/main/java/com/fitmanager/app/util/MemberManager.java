package com.fitmanager.app.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.fitmanager.app.model.MemberVO;

public class MemberManager {

    private Context mContext;

    public MemberManager(Context context) {
        this.mContext = context;
    }


    /**
     * SharedPreferences Getter
     **************************************************************************************************************************************/
    public MemberVO getMemberData() {
        SharedPreferences preference = mContext.getSharedPreferences(Constant.PREF_NAME_KEY, Context.MODE_PRIVATE);
        Integer userId = preference.getInt(Constant.PREF_KEY_USER_PRIKEY, 0);
        String userPW = preference.getString(Constant.PREF_KEY_USER_PASSWORD, Constant.COMMON_NULL);
        String userName = preference.getString(Constant.PREF_KEY_USER_NAME, Constant.COMMON_NULL);
        String userNickname = preference.getString(Constant.PREF_KEY_USER_NICKNAME, Constant.COMMON_NULL);
        String snsType = preference.getString(Constant.PREF_KEY_USER_SNS_TYPE, Constant.COMMON_NULL);
        String accessToken = preference.getString(Constant.PREF_KEY_USER_ACCESS_TOKEN, Constant.COMMON_NULL);
        String userLevel = preference.getString(Constant.PREF_KEY_USER_LEVEL, Constant.COMMON_NULL);
        String userInfomation = preference.getString(Constant.PREF_KEY_USER_INFOMATION, Constant.COMMON_NULL);
        String userActiveYn = preference.getString(Constant.PREF_KEY_USER_ACTIVE_YN, Constant.COMMON_NULL);
        String userEmail = preference.getString(Constant.PREF_KEY_USER_EMAIL, Constant.COMMON_NULL);
        String userLoginBoolean = preference.getString(Constant.PREF_KEY_USER_LOGIN_BOOLEAN,Constant.COMMON_NULL);
        String userPhone = preference.getString(Constant.PREF_KEY_USER_PHONE, Constant.COMMON_NULL);
        Integer userGender = preference.getInt(Constant.PREF_KEY_USER_GENDER, 0);
        Integer userMemberType = preference.getInt(Constant.PREF_MEMBER_TYPE, 1);
        String userMemberAddress1 = preference.getString(Constant.PREF_MEMBER_ADDRESS1, Constant.COMMON_NULL);
        String userMemberAddress2 = preference.getString(Constant.PREF_MEMBER_ADDRESS2, Constant.COMMON_NULL);
        String userMemberAddress3 = preference.getString(Constant.PREF_MEMBER_ADDRESS3, Constant.COMMON_NULL);
        Long userCreated = preference.getLong(Constant.PREF_KEY_USER_CREATED, 0);



        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(userId);
        memberVO.setPassword(userPW);
        memberVO.setName(userName);
        memberVO.setNickname(userNickname);
        memberVO.setSnType(snsType);
        memberVO.setAccessToken(accessToken);
        memberVO.setPassword(accessToken);
        memberVO.setLevel(userLevel);
        memberVO.setInfomation(userInfomation);
        memberVO.setActiveYn(userActiveYn);
        memberVO.setEmail(userEmail);
        memberVO.setLoginBoolean(userLoginBoolean);
        memberVO.setPhone(userPhone);
        memberVO.setGender(userGender);
        memberVO.setMemberType(userMemberType);
        memberVO.setMemAddress1(userMemberAddress1);
        memberVO.setMemAddress2(userMemberAddress2);
        memberVO.setMemAddress3(userMemberAddress3);
        memberVO.setCreated(userCreated);


        return memberVO;
    }

    /**
     * SharedPreferences Setter
     **************************************************************************************************************************************/
    public void setMemberData(MemberVO memberVO) {
        SharedPreferences preference = mContext.getSharedPreferences(Constant.PREF_NAME_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();

        editor.putInt(Constant.PREF_KEY_USER_PRIKEY, LoginUtils.getLoginUserVO().getMemberId());
        editor.putString(Constant.PREF_KEY_USER_PASSWORD, LoginUtils.getLoginUserVO().getPassword());
        editor.putString(Constant.PREF_KEY_USER_NAME, LoginUtils.getLoginUserVO().getName());
        editor.putString(Constant.PREF_KEY_USER_NICKNAME,LoginUtils.getLoginUserVO().getNickname());
        editor.putString(Constant.PREF_KEY_USER_SNS_TYPE,LoginUtils.getLoginUserVO().getSnType());
        editor.putString(Constant.PREF_KEY_USER_ACCESS_TOKEN,LoginUtils.getLoginUserVO().getAccessToken());
        editor.putString(Constant.PREF_KEY_USER_LEVEL,LoginUtils.getLoginUserVO().getLevel());
        editor.putString(Constant.PREF_KEY_USER_INFOMATION,LoginUtils.getLoginUserVO().getInfomation());
        editor.putString(Constant.PREF_KEY_USER_ACTIVE_YN,LoginUtils.getLoginUserVO().getActiveYn());
        editor.putString(Constant.PREF_KEY_USER_EMAIL,LoginUtils.getLoginUserVO().getEmail());
        editor.putBoolean(Constant.PREF_KEY_USER_LOGIN_BOOLEAN, LoginUtils.isLoggedIn());
        editor.putString(Constant.PREF_KEY_USER_PHONE,LoginUtils.getLoginUserVO().getPhone());
        editor.putInt(Constant.PREF_KEY_USER_GENDER,LoginUtils.getLoginUserVO().getGender());
        editor.putInt(Constant.PREF_MEMBER_TYPE,LoginUtils.getLoginUserVO().getMemberType());
        editor.putString(Constant.PREF_MEMBER_ADDRESS1,LoginUtils.getLoginUserVO().getMemAddress1());
        editor.putString(Constant.PREF_MEMBER_ADDRESS2,LoginUtils.getLoginUserVO().getMemAddress2());
        editor.putString(Constant.PREF_MEMBER_ADDRESS3,LoginUtils.getLoginUserVO().getMemAddress3());
        editor.putLong(Constant.PREF_KEY_USER_CREATED, LoginUtils.getLoginUserVO().getCreated());

        editor.apply();
        editor.commit();
    }

}
