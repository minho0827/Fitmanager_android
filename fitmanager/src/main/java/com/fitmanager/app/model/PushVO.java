package com.fitmanager.app.model;

import lombok.Data;


@Data
public class PushVO {

    private String pushId;
    private String noticeMessageId;
    private String pushTitle;
    private String pushMessage;
    private String appServerkey;
    private String pushResultFail;
    private String recipientIds;

}
