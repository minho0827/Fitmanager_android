package com.fitmanager.app.model;


import lombok.Data;

@Data
public class VideoHistoryVO {

    private int historyId;
    private int memberId;
    private int videoId;
    private String viewed;


}
