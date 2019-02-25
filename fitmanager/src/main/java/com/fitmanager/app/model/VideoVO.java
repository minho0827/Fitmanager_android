package com.fitmanager.app.model;

import java.io.Serializable;

import lombok.Data;


@Data
public class VideoVO implements Serializable {

    private int videoId;
    private int coachId;
    private String title;
    private String content;
    private int level;
    private int exerciseType;
    private int bodypart01;
    private int bodypart02;
    private String created;
    private int bookmarkCount;
    private int coachBookmarkCount;
    private int commentCount;
    private int viewHistoryCount;
    private String profileImg;
    private String imageUrl;
    private String videoUrl;
    private String name;
    private int resultType;    // 1: bookmark ON, 2: bookmark OFF
    private int cellType;      // 0: normal, 1: Empty cell


    @Override
    public String toString() {
        return "VideoVO{" +
                "videoId=" + videoId +
                ", coachId=" + coachId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", level=" + level +
                ", exerciseType=" + exerciseType +
                ", bodypart01=" + bodypart01 +
                ", bodypart02=" + bodypart02 +
                ", created='" + created + '\'' +
                ", bookmarkCount=" + bookmarkCount +
                ", coachBookmarkCount=" + coachBookmarkCount +
                ", commentCount=" + commentCount +
                ", viewHistoryCount=" + viewHistoryCount +
                ", profileImg='" + profileImg + '\'' +
                ", name='" + name + '\'' +
                ", resultType=" + resultType +
                ", cellType=" + cellType +
                '}';
    }
}