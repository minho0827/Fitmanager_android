package com.fitmanager.app.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class CoachVO implements Serializable {
    private int coachId;
    private int memberId;
    private String intro;
    private String height;
    private String weight;
    private String job;
    private int exerciseType;
    private String name;
    private String company;
    private String facebookUrl;
    private String instagramUrl;
    private String profileImg;
    private String career;
    private String certificate;
    private int bookmarkCount;

}