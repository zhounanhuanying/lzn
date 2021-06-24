package com.bfdb.entity.IMOS;

import lombok.Data;

import java.util.List;

@Data
public class JsonRootBean {
    private String ErrMsg;
    private int ErrCode;
    private List<Result> Result;
    @Data
    public class Result {
        private String Msg;
        private Integer Order;
        private Integer Type;
        private Integer Quality;
        private Integer Code;
        private String Data;
    }
}
