package com.bfdb.entity;


import com.bfdb.entity.vo.PersonTableVo;

import java.io.Serializable;
import java.util.List;

/**
 * 接收前端的信息 统一放在这个实体类
 */
public class Datas implements Serializable {
    private List<User> users;
    private List<Role> roles;
    private List<Integer> ids;
    private List<Integer> webpermissionId;
    private List<Integer> datapermissionId;
    private  List<PersonFaceInfomationTable> faceAddressList;

    //园区管理
    private BasePark basePark;

    //组织机构
    private BaseOrganizition baseOrganizition;
    //园区logo
    private String baseParkImg;

    public String getBaseParkImg() {
        return baseParkImg;
    }

    public void setBaseParkImg(String baseParkImg) {
        this.baseParkImg = baseParkImg;
    }

    public BasePark getBasePark() {
        return basePark;
    }

    public void setBasePark(BasePark basePark) {
        this.basePark = basePark;
    }

    public WechatPublic getWechatPublic() {
        return wechatPublic;
    }

    public void setWechatPublic(WechatPublic wechatPublic) {
        this.wechatPublic = wechatPublic;
    }

    private WechatPublic wechatPublic;

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    private List<String> imgs;
    private  List<PersonTable> personTableList;

    public List<PersonTableVo> getPersonTableVoList() {
        return personTableVoList;
    }

    public void setPersonTableVoList(List<PersonTableVo> personTableVoList) {
        this.personTableVoList = personTableVoList;
    }

    private List<PersonTableVo> personTableVoList;

    private PersonTable personNewTable;

    public List<Integer> getWebpermissionId() {
        return webpermissionId;
    }

    public void setWebpermissionId(List<Integer> webpermissionId) {
        this.webpermissionId = webpermissionId;
    }

    public List<Integer> getDatapermissionId() {
        return datapermissionId;
    }

    public void setDatapermissionId(List<Integer> datapermissionId) {
        this.datapermissionId = datapermissionId;
    }

    public PersonTable getPersonNewTable() {
        return personNewTable;
    }

    public void setPersonNewTable(PersonTable personNewTable) {
        this.personNewTable = personNewTable;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<PersonFaceInfomationTable> getFaceAddressList() {
        return faceAddressList;
    }

    public void setFaceAddressList(List<PersonFaceInfomationTable> faceAddressList) {
        this.faceAddressList = faceAddressList;
    }

    public BaseOrganizition getBaseOrganizition() {
        return baseOrganizition;
    }

    public void setBaseOrganizition(BaseOrganizition baseOrganizition) {
        this.baseOrganizition = baseOrganizition;
    }

    public List<PersonTable> getPersonTableList() {
        return personTableList;
    }

    public void setPersonTableList(List<PersonTable> personTableList) {
        this.personTableList = personTableList;
    }
}
