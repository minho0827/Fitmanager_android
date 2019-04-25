package com.fitmanager.app.model;

import lombok.Data;


@Data
public class ResultMessageVO {
    private String code;
    private String message;
    private Object extra;
}
