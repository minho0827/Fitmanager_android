package com.fitmanager.app.model;


import lombok.Data;

@Data
public class CommentHistoryVO {

    private int commentId;
    private int quizId;
    private int regId;
    private String content;
    private String regDt;
    private String quizImage;
    private String title;
    private String creatorName;


}