package com.fitmanager.app.network;

import com.fitmanager.app.model.BookmarkVO;
import com.fitmanager.app.model.CoachVO;
import com.fitmanager.app.model.CoachViewHistoryVO;
import com.fitmanager.app.model.DefaultRetrunCodeListVO;
import com.fitmanager.app.model.MealVO;
import com.fitmanager.app.model.MemberVO;
import com.fitmanager.app.model.NoticeVo;
import com.fitmanager.app.model.VideoHistoryVO;
import com.fitmanager.app.model.VideoVO;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


public interface RestService {

    /* 코치 리스트 */
    @GET("api/getCoachList")
    Call<List<CoachVO>> getCoachList(@Query("start") int start,
                                     @Query("end") int end,
                                     @Query("gender") String gender,
                                     @Query("filter") String filter);

    /* 코치 전체 카운트 */
    @GET("api/getCoachCount")
    Call<Integer> getCoachCount(@Query("gender") String gender, @Query("filter") String filter);

    /* 코치 1명 상세정보 */
    @GET("api/getCoachInfo")
    Call<CoachVO> getCoachInfo(@Query("coachId") int coachId);

    /* 식단 리스트 */
    @GET("api/getMealList")
    Call<List<MealVO>> getMealList(@Query("coachId") int coachId);

    /* 비디오 리스트 */
    @GET("api/getVideoList")
    Call<List<VideoVO>> getVideoList(@Query("coachId") int coachId);

    /* 인기 비디오 리스트 */
    @GET("api/getPopularityVideoList")
    Call<List<VideoVO>> getPopulartyList(@Query("start") int start,
                                         @Query("end") int end,
                                         @Query("gender") String gender,
                                         @Query("exerciseType") String cexerciseType,
                                         @Query("bodypart") String bodypart,
                                         @Query("order") String order);

    /* 식단 1개의 상세정보 */
    @GET("api/getMealInfo")
    Call<MealVO> getMealInfo(@Query("mealId") int mMealId);

    /* 회원 가입 */
    @POST("/api/insertMember")
    Call<MemberVO> insertMember(@QueryMap Map<String, Object> param);

    /* 로그인 */
    @GET("/api/getLoginMember")
    Call<MemberVO> getLoginMember(@QueryMap Map<String, Object> param);

    /* 북마크 등록 */
    @POST("/api/insertBookmark")
    Call<Integer> insertBookmark(@QueryMap Map<String, Object> param);

    /* 해당 비디오 상세정보 */
    @GET("/api/getVideoInfo")
    Call<VideoVO> getVideoInfo(@QueryMap Map<String, Object> param);

    /* 북마크 등록,해제 여부 확인 */
    @POST("/api/getIsBookmark")
    Call<Integer> getIsBookmark(@QueryMap Map<String, Object> param);

    /* 코치 북마크 등록,해제 여부 확인*/
    @POST("/api/getIsCoachBookmark")
    Call<Integer> getIsCoachBookmark(@QueryMap Map<String, Object> param);

    /* 내가 본 비디오 리스트에 추가 */
    @POST("/api/insertVideoHistory")
    Call<VideoHistoryVO> insertVideoHistory(@QueryMap Map<String, Object> param);

    /* 내가 본 비디오 목록 리스트  */
    @GET("/api/getVideoHistoryList")
    Call<List<VideoVO>> getVideoHistoryList(@QueryMap Map<String, Object> param);

    /* 내가 북마크한 식단 리스트 */
    @GET("/api/getBookmarkMealList")
    Call<List<MealVO>> getBookmarkMealList(@QueryMap Map<String, Object> param);

    /* 북마크 또는 북마크 해제 */
    @POST("/api/insertOrDeleteBookmark")
    Call<BookmarkVO> insertOrDeleteBookmark(@QueryMap Map<String, Object> param);

    /* 내가 북마크한 비디오 리스트 */
    @GET("/api/getBookmarkVideoList")
    Call<List<VideoVO>> getBookmarkVideoList(@QueryMap Map<String, Object> param);

    /* 추천 비디오영상 리스트 */
    @GET("/api/selectRecommendVideoList")
    Call<List<VideoVO>> selectRecommendVideoList(@QueryMap Map<String, Object> param);

    /* 비디오 삭제 */
    @POST("/api/deleteVideoRequest")
    Call<Integer> deleteVideoRequest(@QueryMap Map<String, Object> param);

    /* 인기 비디오 리스트 */
    @GET("/api/getPopularityVideoCount")
    Call<Integer> getPopularityVideoCount(@Query("start") int start,
                                          @Query("end") int end,
                                          @Query("gender") String gender,
                                          @Query("exerciseType") String exerciseType,
                                          @Query("bodypart") String bodypart,
                                          @Query("order") String order);

    /* 식단 1개이상 멀티로 삭제 */
    @POST("/api/deleteMealBookmarkList")
    Call<List<MealVO>> deleteMealBookmarkList(@Body Map<String, Object> param);

    /* 내가 구독중인 코치 리스트 */
    @GET("/api/getBookmarkCoachList")
    Call<List<CoachViewHistoryVO>> getBookmarkCoachListRequestService(@QueryMap Map<String, Object> param);

    /* 이름으로 검색한 코리 리스트 */
    @GET("/api/getCoachSearchList")
    Call<List<CoachVO>> getCoachSearchList(@Query("name") String name);

    /* 로그인한 회원 정보 */
    @GET("/api/getLoginUserInfo")
    Call<MemberVO> getLoginUserInfoService(@QueryMap Map<String, Object> param);

    /* 회원 정보 업데이트 */
    @GET("/api/updateUserInfo")
    Call<MemberVO> updateUserInfo(@QueryMap Map<String, Object> param);


    /* 프로필 이미지 업데이트  */
    @PUT("/api/updateProfileImg")
    Call<Integer> updateProfileImg(@Body MemberVO memberVO);


    /**
     * Key값 불러오기
     ************************************************************************************************************************************************/
    @GET("/api/keyList")
    Call<List<DefaultRetrunCodeListVO>> requestKeyValue();


    /**
     * 공지사항 List 요청
     ************************************************************************************************************************************************/
    @GET("api/noticepushlist/{appKind}/{userId}/{nextPageNo}")
    Call<List<NoticeVo>> requestNoticeList(
            @Path("appKind") String appKind,
            @Path("userId") String userId,
            @Path("nextPageNo") int nextPageNo);
;

}