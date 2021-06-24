package com.bfdb.config;


import com.bfdb.untils.AJAXResult;

public abstract class BaseController {

    private ThreadLocal<AJAXResult> resultLocal = new ThreadLocal<AJAXResult>();
    protected void start() {
        resultLocal.set(new AJAXResult());
    }

    protected Object end() {
        Object obj = resultLocal.get();
        resultLocal.remove();
        return obj;
    }

    protected void success( boolean flg ) {
        flg = true;
        resultLocal.get().setSuccess(flg);
    }

    protected void data( Object obj ) {
        resultLocal.get().setData(obj);
    }

    protected void success() {
        success(true);
    }

    protected void fail() {
        boolean flg = false;
        success(flg);
    }
}
