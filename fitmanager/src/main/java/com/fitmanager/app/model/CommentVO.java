package com.fitmanager.app.model;


import java.io.Serializable;

import lombok.Data;

@Data
public class CommentVO implements Serializable {

    private int commentId;
    private int parentId;
    private int targetId;
    private int targetType;
    private String content;
    private int memberId;
    private String nickname;
    private String created;
    private String updated;
    private String deleteYn;
    private String profileImg;
    private int commentCount;

}
