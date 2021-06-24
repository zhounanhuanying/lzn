package com.bfdb.entity.vo;

import java.io.Serializable;

public class PersonCodeVo implements Serializable {

    private  String personCode;
    private String personName;

    private String identityNo;

    public String getIdentityNo() {
        return identityNo;
    }

    public void setIdentityNo(String identityNo) {
        this.identityNo = identityNo;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonCode() {
        return personCode;
    }

    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }
    @Override
    public String toString() {
        return "PersonCodeVo{" +
                "personCode='" + personCode + '\'' +
                ", personName='" + personName + '\'' +
                ", identityNo='" + identityNo + '\'' +
                '}';
    }
}
