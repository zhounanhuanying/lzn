package com.bfdb.entity.websocket;

import java.io.Serializable;

public class Duration implements Serializable {

    private Integer duration;

    @Override
    public String toString() {
        return "Duration{" +
                "duration=" + duration +
                '}';
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Duration(Integer duration) {

        this.duration = duration;
    }

    public Duration() {

    }
}
