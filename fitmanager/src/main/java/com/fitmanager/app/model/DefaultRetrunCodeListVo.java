package com.fitmanager.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;



@Data
public class DefaultRetrunCodeListVo {
    /**
     * Variable
     */
    @SerializedName("return_code") @Expose
    private String return_code;

}
