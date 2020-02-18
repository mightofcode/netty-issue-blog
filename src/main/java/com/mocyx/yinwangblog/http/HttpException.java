package com.mocyx.yinwangblog.http;

import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * @author Administrator
 */
public class HttpException extends RuntimeException {
    public HttpException(HttpResponseStatus code, String message) {
        super(message);
        this.code = code;
    }
    public final HttpResponseStatus code;
}




