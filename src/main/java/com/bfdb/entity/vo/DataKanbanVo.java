package com.bfdb.entity.vo;

public class DataKanbanVo {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "DataKanbanVo{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }

    private String name;
    private Integer value;
}
