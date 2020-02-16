package com.mocyx.yinwangblog;


import io.netty.handler.codec.http.HttpResponseStatus;

public class HttpException extends RuntimeException {
    public HttpException(HttpResponseStatus code, String message) {
        super(message);
        this.code = code;
    }

    public final HttpResponseStatus code;
}
