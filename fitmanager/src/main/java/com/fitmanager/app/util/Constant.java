package com.fitmanager.app.util;


public class Constant {
    /**
     * DEBUG_MODE :: 통신 URL을 확실히 확인하세요.
     ************************************************************************************************************************************************/
    public static final boolean DEBUG_MODE = false;

    // preference keys
    public static final String USER_ID = "USER_ID";
    public static final String USER_PWD = "USER_PWD";
    public static final String AUTO_LOGIN = "AUTO_LOGIN";
    /**
     * App Kind & App Swpe
     ************************************************************************************************************************************************/
    public static final String APP_KIND = "fitmanager";                                  // Applcation Kind

    public static final String SERVER_ADDR = "http://172.20.10.5:8080/";
//    public static final String SERVER_ADDR = "http://192.168.0.186:8080/";
    public static final String BUCKET_NAME = "s3testymh";
//    public static final String PROFILE_UPLOAD_DIR = "profile";

    public static String email = "";
    public static String password = "";

    public enum SnsType {
        FACEBOOK("Facebook"),
        KAKAOTALK("KakaoTalk");
        public String value;

        SnsType(String value) {
            this.value = value;
        }
    }


    public enum TargetType {
        COACH(1),
        VIDEO(2),
        MEAL(3);

        public int value;

        TargetType(int value) {
            this.value = value;
        }
    }


    public enum VideoFileType {
        THUMBNAIL(1),
        VIDEO_URL(2);

        public int value;

        VideoFileType(int value) {
            this.value = value;
        }
    }

    /**
     * 자주 사용되는 String
     ************************************************************************************************************************************************/
    public static final String COMMON_NULL = "";                                               // null
    public static final String COMMON_NULL2 = "null";                                           // null [String]
    public static final String COMMON_DIVISION = "::";                                             // division
    public static final String PAY_NOT_REQUEST = "n/a";                                            // n/a
    public static final String ONE_1 = "1";                                              // [1]
    public static final String ZERO_1 = "0";                                              // [0]
    public static final String ZERO_2 = "00";                                             // [00]
    public static final String ARROW_RIGHT = " -> ";                                           // [ -> }
    public static final String SPACE = " ";                                              // [ ]
    public static final String ZUM = ".";                                              // [.]
    public static final String ZUM_SPLIT = "\\.";                                            // 특수문자 Split
    public static final String PERCENT_2F = "%.2f";                                           // 소수점 2자리 이하
    public static final String SLASH = "/";                                              // [/]
    public static final String OPEN = "open";                                           // [open]
    public static final String SMS = "SMS";                                            // [SMS]


    /**
     * ReturnCode - Param Key
     ************************************************************************************************************************************************/
    public static final String RS_SUCCESS = "SUCCESS";                                            // 정상 Return
    public static final String RS_ERRORS_FAIL = "FAIL";                                            //

    public static final String Y = "Y";                                                              // YES
    public static final String N = "N";                                                              // No
    public static final Integer MEMBER_TYPE_COACH = 2;                                               // 코치
    public static final Integer MEMBER_TYPE_MEM = 1;                                                 // 일반회원


    /**
     * Intent
     ************************************************************************************************************************************************/
    public static final String INTENT_NOTICE                    = "Notice";                                         // 공지사항

    /**
     * PREFERENCE KEY
     ************************************************************************************************************************************************/
    public static final String PREF_NAME_KEY = "FitManager";                                         // Preference Key
    public static final String PREF_KEY_USER_PRIKEY = "memberId";                                         // 아이디
    public static final String PREF_KEY_USER_PASSWORD = "password";
    public static final String PREF_KEY_USER_NAME = "name";                                         // 이름
    public static final String PREF_KEY_USER_NICKNAME = "nickname";                                 // 닉네임
    public static final String PREF_KEY_USER_SNS_TYPE = "snsType";                                  // sns타입
    public static final String PREF_KEY_USER_ACCESS_TOKEN = "accessToken";                          // 패스워드
    public static final String PREF_KEY_USER_LEVEL = "level";                                       // 회원레
    public static final String PREF_KEY_USER_INFOMATION = "infomation";                                       // 회원레
    public static final String PREF_KEY_USER_GENDER = "gender";                                     // 성별
    public static final String PREF_KEY_USER_ACTIVE_YN = "activeYn";                                              // 회원 활성상태
    public static final String PREF_KEY_USER_EMAIL = "email";                                                // 이메일
    public static final String PREF_KEY_USER_PHONE = "phone";                                                // 휴대폰 번호
    public static final String PREF_MEMBER_ADDRESS1 = "memAddress1";                                // 주소
    public static final String PREF_MEMBER_ADDRESS2 = "memAddress2";                                // 주소2
    public static final String PREF_MEMBER_ADDRESS3 = "memAddress3";                                // 주소3
    public static final String PREF_MEMBER_TYPE = "memberType";                                     // 1:회원 2:코치
    public static final String PREF_KEY_USER_LOGIN_BOOLEAN = "loginBoolean";                        // 유저 로그인 여부
    public static final String PREF_KEY_USER_RECIVE_PUSH = "receivePush";                           // 푸쉬 알림 설정여부
    public static final String PREF_KEY_USER_PROFILE_IMG = "profileImg";                            // 유저 프로필 이미지
    public static final String PREF_KEY_USER_CREATED = "created";                                   // 회원가입 시간
    public static final String PREF_KEY_USER_UPDATED = "updated";                                   // 회원수정 시간

    public static final String INTENT_PUSH_TRANSFER             = "Push_Transfer";                                  // Push Transfer




}