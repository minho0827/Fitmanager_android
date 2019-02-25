package com.fitmanager.app.model;


import lombok.Data;

@Data
public class NotiVO {


    private int notiId;
    private int memberId;
    private int notiMemberId;
    private int targetId;
    private int targetType;
    private String notiType;
    private String title;
    private String created;
    private String name;
    private String thumbnail;

}