package com.fitmanager.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;


@Data
public class NoticeVo {

    /**
     * Variable
     */
    @SerializedName("return_code") @Expose
    private String returnCode;

    @SerializedName("list") @Expose
    private java.util.List<List> list = null;



    /**
     * List
     */
    @Data
    public class List implements Serializable {
        @SerializedName("notice_message_id") @Expose
        private String noticeMessageId;

        @SerializedName("notice_language") @Expose
        private String noticeLanguage;

        @SerializedName("is_notice_all") @Expose
        private String isNoticeAll;

        @SerializedName("notice_title") @Expose
        private String noticeTitle;

        @SerializedName("write_datetime") @Expose
        private String writeDatetime;

        @SerializedName("notice_message") @Expose
        private String noticeMessage;

        @SerializedName("notice_kind") @Expose
        private String noticeKind;

        @SerializedName("is_show") @Expose
        private String isShow;

    }
}
