package com.bfdb.untils;

public class AJAXResult {

        private boolean success;
        private Object data;
        private String errorMsg;

        public Object getData() {
            return data;
        }
        public void setData(Object data) {
            this.data = data;
        }
        public boolean getSuccess() {
            return success;
        }
        public void setSuccess(boolean success) {
            this.success = success;
        }
        public String getErrorMsg() {
            return errorMsg;
        }
        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }

}
