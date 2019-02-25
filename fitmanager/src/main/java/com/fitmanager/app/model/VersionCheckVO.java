package com.fitmanager.app.model;

import lombok.Data;

@Data
public class VersionCheckVO {

    /**
     * Variable
     */
    private ResultMessage resultMessage;
    private java.util.List<Notice> noticeList;
    private Version version;

    /**
     * 공지사항
     */
    @Data
    public static class Notice {
        private String noticeMessageId;
        private String isNoticeAll;
        private String noticeTitle;
        private String created;
        private String noticeMessage;
        private String appClose;
    }

    /**
     * Version
     */
    @Data
    public static class Version {
        private String vId;
        private String latestVersion;
        private String created;
    }

    @Data
    public class ResultMessage {
        private String code;
        private String message;
        private Object extra;
    }

}