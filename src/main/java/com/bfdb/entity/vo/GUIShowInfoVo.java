package com.bfdb.entity.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 此实体类主要用于请求宇视的人证设备
 */
public class GUIShowInfoVo implements Serializable {
    private String resultCode;
    /// 识别成功
    private String resultMsg;
    private String passTime;
    private String showInfoNum;
    private List<ShowInfoList> ShowInfoList;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getPassTime() {
        return passTime;
    }

    public void setPassTime(String passTime) {
        this.passTime = passTime;
    }

    public String getShowInfoNum() {
        return showInfoNum;
    }

    public void setShowInfoNum(String showInfoNum) {
        this.showInfoNum = showInfoNum;
    }

    public List<com.bfdb.entity.vo.ShowInfoList> getShowInfoList() {
        return ShowInfoList;
    }

    public void setShowInfoList(List<com.bfdb.entity.vo.ShowInfoList> showInfoList) {
        ShowInfoList = showInfoList;
    }

    @Override
    public String toString() {
        return "GUIShowInfoVo{" +
                "resultCode='" + resultCode + '\'' +
                ", resultMsg='" + resultMsg + '\'' +
                ", passTime='" + passTime + '\'' +
                ", showInfoNum=" + showInfoNum +
                ", ShowInfoList=" + ShowInfoList +
                '}';
    }
}
