package com.bfdb.entity.vo;

import java.io.Serializable;

public class ShowInfoList implements Serializable {
    private String Key;
    private String Value ;

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

}
