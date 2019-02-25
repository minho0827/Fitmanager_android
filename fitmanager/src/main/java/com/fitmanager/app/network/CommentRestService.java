package com.fitmanager.app.network;

import com.fitmanager.app.model.CommentHistoryVO;
import com.fitmanager.app.model.CommentVO;
import com.fitmanager.app.model.NotiVO;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;


public interface CommentRestService {

    /* 댓글 리스트 */
    @GET("/api/getCommentList")
    Call<List<CommentVO>> getCommentList(@QueryMap Map<String, Object> param);

    /* 대댓글 리스트 */
    @GET("/api/getCommentReplyList")
    Call<List<CommentVO>> getCommentReplyList(@QueryMap Map<String, Object> param);
    /* 댓글 1개 */
    @GET("/api/getComment")
    Call<CommentVO> getComment(@QueryMap Map<String, Object> param);
    /* 댓글 작성 */
    @POST("/api/insertComment")
    Call<Integer> insertComment(@QueryMap Map<String, Object> param);
    /* 댓글 삭제 */
    @POST("/api/deleteComment")
    Call<Integer> deleteComment(@QueryMap Map<String, Object> param);
    /* 대댓글 작성 */
    @POST("/api/insertReplyComment")
    Call<Integer> insertReplyComment(@QueryMap Map<String, Object> param);
    /*내가 작성한 댓글 리스트*/
    @GET("/api/getMyCommentHistoryList")
    Call<List<CommentHistoryVO>> getMyCommentHistoryList(@QueryMap Map<String, Object> param);
    /* 내가 작성한 댓글에 대댓글이 달리면 알림 */
    @GET("/api/selectNoti")
    Call<List<NotiVO>> selectNotiService(@QueryMap Map<String, Object> param);

    @GET("/api/getMyCommentContent")
    Call<NotiVO> getMyCommentContent(@QueryMap Map<String, Object> param);
}
