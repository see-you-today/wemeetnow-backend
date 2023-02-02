package com.example.chat.domain.result;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {
    private String resultCode;
    private T result;

    public static <T> Response<T> success(T result) {
        return Response.<T>builder()
                .resultCode("SUCCESS")
                .result(result)
                .build();
    }

    public static <T> Response<T> error(T result) {
        return Response.<T>builder()
                .resultCode("ERROR")
                .result(result)
                .build();
    }

}
