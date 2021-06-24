package com.bfdb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class BaseOrganizition implements Serializable {

    private static final long serialVersionUID = 1L;

    //主键id
    private Integer id;

    //组织编码
    private String orgCode;

    //组织名称
    private String orgName;

    //组织类型
    private Integer orgType;

    //组织描述
    private String orgContent;

    //父编码
    private String pcode;

    //父ID
    private String pid;

    //园区ID
    private String parkId;

    //负责人
    private String personLiable;
    private String name;

    //所有父级ID集合(便于查询）
    private String parentIds;

    private List<BaseOrganizition> children = new ArrayList<BaseOrganizition>();

    //创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    //更新时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


}
