package com.bfdb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@PropertySource({"classpath:token.properties"})
public class TokenTime  implements Serializable{
    //http
    @Value("${token.validity.time}")
    private String tokenValidityTime;

    public String getTokenValidityTime() {
        return tokenValidityTime;
    }

    public void setTokenValidityTime(String tokenValidityTime) {
        this.tokenValidityTime = tokenValidityTime;
    }

    @Override
    public String toString() {
        return "TokenTime{" +
                "tokenValidityTime='" + tokenValidityTime + '\'' +
                '}';
    }
}
