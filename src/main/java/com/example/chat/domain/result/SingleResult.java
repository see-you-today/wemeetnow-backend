package com.example.chat.domain.result;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleResult <T> extends Result{
    private T data;
}
