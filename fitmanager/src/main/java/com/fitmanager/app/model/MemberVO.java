package com.fitmanager.app.model;

import java.util.List;

import lombok.Data;

@Data
public class MemberVO {
    private int memberId;
    private Boolean isAutoLogin;
    private String password;
    private String memberSaveEmail;                              // 유저 아이디 저장
    private String memberSavePassWord;                        // 유저 패스워드 저장
    private String name;
    private String snType;
    private String nickname;
    private String activeYn;
    private String accessToken;
    private String userAppVersion;                          // 유저 App Version
    private String phone;
    private String infomation;
    private int gender;
    private String loginBoolean;
    private int memberType;
    private List<VideoVO> bookmarkVideoList;
    private List<CoachVO> bookmarkCoachList;
    private List<MealVO> bookmarkMealList;
    private List<CommentVO> myCommentList;


    private String memZipcode;
    private String memAddress1;
    private String memAddress2;
    private String memAddress3;
    private String email;
    private String level;
    private int receivePush;
    private String profileImg;
    private Long created;
    private Long updated;
    private int joinResult;
    private String userFirebaseToken;                       // 유저 FCM Token
    private String userNotiChannel;                         // 유저 알람 채널 여부



}

