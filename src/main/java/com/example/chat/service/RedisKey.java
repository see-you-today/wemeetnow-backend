package com.example.chat.service;

import lombok.Getter;

@Getter
public enum RedisKey {
    REGISTER("Register_"), EAUTH("EAuth_");
    private String key;
    RedisKey(String key){
        this.key = key;
    }
}
