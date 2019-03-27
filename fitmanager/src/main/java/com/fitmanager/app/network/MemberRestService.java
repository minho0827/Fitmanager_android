package com.fitmanager.app.network;

import com.fitmanager.app.model.MemberVO;
import com.fitmanager.app.model.VersionCheckVO;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface MemberRestService {


    /**
     * email로 가입된 회원정보 가져오기
     ************************************************************************************************************************************************/
    @GET("/api/getLoginMember")
    Call<MemberVO> getLoginMember(@QueryMap Map<String, Object> param);


    /**
     * sns로 가입된 회원정보 가져오기
     ************************************************************************************************************************************************/
    @GET("/api/getSnsLoginMember")
    Call<MemberVO> getSnsLoginMember(@QueryMap Map<String, String> param);

    /**
     * 버전체크 및 공지사항
     ************************************************************************************************************************************************/
    @POST("/api/getMainnoticeAppversionCheck")
    Call<VersionCheckVO> getMainnoticeAppversionCheck(@QueryMap Map<String, Object> param);

    /**
     * 공지사항 상세정보 요청
     ************************************************************************************************************************************************/
    @GET("api/getNoticeRequest")
    Call<List<VersionCheckVO>> getNoticeRequest(@QueryMap Map<String, Object> param);


  /**
     * 식단등록 요청
     ************************************************************************************************************************************************/
    @GET("api/requestMealInsert")
    Call<Integer> requestMealInsert(@QueryMap Map<String, Object> param);



}
