package com.fitmanager.app.model;

import lombok.Data;

@Data
public class CoachViewHistoryVO {
    private int coachId;
    private String viewDate;
    private String coachName;
    private String profileImg;
    private int meal_updatedCount;
    private int video_updatedCount;
}

