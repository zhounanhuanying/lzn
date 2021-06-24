package com.bfdb.entity.logBook;

/**
 * 类描述: 主要时用于返回错误码和错误信息
 * @description 返回码和msg
 * @date 2019/3/12
 * @return
 */
public enum ResultStatusCode {
    OK(200, "OK"),
    TIME_OUT(130, "访问超时"),
    BAD_REQUEST(407, "参数解析失败"),
    INVALID_TOKEN(401, "无效的授权码"),
    INVALID_CLIENTID(402, "无效的密钥"),
    REQUEST_NOT_FOUND(404, "访问地址不存在！"),
    METHOD_NOT_ALLOWED(405, "不支持当前请求方法"),
    REPEAT_REQUEST_NOT_ALLOWED(406, "请求重复提交"),
    SYSTEM_ERR(500, "服务器运行异常");

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    ResultStatusCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

