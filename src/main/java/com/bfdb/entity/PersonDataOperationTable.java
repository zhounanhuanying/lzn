package com.bfdb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * person_data_operation_table
 *
 * @author
 */
public class PersonDataOperationTable implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 人员code
     */
    private String personcode;

    /**
     * 操作类型
     */
    private String operationaction;

    /**
     * 操作时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    protected Date operationtime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPersoncode() {
        return personcode;
    }

    public void setPersoncode(String personcode) {
        this.personcode = personcode;
    }

    public String getOperationaction() {
        return operationaction;
    }

    public void setOperationaction(String operationaction) {
        this.operationaction = operationaction;
    }

    public Date getOperationtime() {
        return operationtime;
    }

    public void setOperationtime(Date operationtime) {
        this.operationtime = operationtime;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        PersonDataOperationTable other = (PersonDataOperationTable) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals( other.getId() ))
                && (this.getPersoncode() == null ? other.getPersoncode() == null : this.getPersoncode().equals( other.getPersoncode() ))
                && (this.getOperationaction() == null ? other.getOperationaction() == null : this.getOperationaction().equals( other.getOperationaction() ))
                && (this.getOperationtime() == null ? other.getOperationtime() == null : this.getOperationtime().equals( other.getOperationtime() ));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getPersoncode() == null) ? 0 : getPersoncode().hashCode());
        result = prime * result + ((getOperationaction() == null) ? 0 : getOperationaction().hashCode());
        result = prime * result + ((getOperationtime() == null) ? 0 : getOperationtime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "PersonDataOperationTable{" +
                "id=" + id +
                ", personcode='" + personcode + '\'' +
                ", operationaction='" + operationaction + '\'' +
                ", operationtime=" + operationtime +
                '}';
    }
}