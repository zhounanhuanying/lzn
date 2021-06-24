package com.bfdb.entity.websocket;

import java.io.Serializable;

public class LibID implements Serializable {

    private Integer id;

    @Override
    public String toString() {
        return "LibID{" +
                "id=" + id +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LibID(Integer id) {

        this.id = id;
    }

    public LibID() {

    }
}
