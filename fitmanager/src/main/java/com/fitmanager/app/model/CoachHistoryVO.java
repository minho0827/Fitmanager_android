package com.fitmanager.app.model;

import lombok.Data;

@Data
public class CoachHistoryVO {


    private int commentId;
    private int targetId;
    private int targetType;
    private String coachName;
    private String title;
    private String content;
    private String created;
    private String image;
    private String url;

}