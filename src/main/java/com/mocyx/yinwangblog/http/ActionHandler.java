package com.mocyx.yinwangblog.http;

import com.mocyx.yinwangblog.Global;
import com.mocyx.yinwangblog.Util;
import com.mocyx.yinwangblog.http.HttpException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Administrator
 */
@Slf4j
public class ActionHandler extends SimpleChannelInboundHandler<FullHttpRequest> {


    private String fileToContentType(String path) {

        if (path.endsWith(".txt")) {
            return "text/plain; charset=utf-8";
        } else if (path.endsWith(".html")) {
            return "text/html; charset=utf-8";
        } else if (path.endsWith(".png")) {
            return "image/png";
        } else if (path.endsWith(".jpg")) {
            return "image/jpeg";
        } else if (path.endsWith(".css")) {
            return "text/css; charset=utf-8";
        } else {
            return "text/html; charset=utf-8";
        }
    }


    private byte[] tryReadFromResource(String path) {
        try {
            String filePath = Global.resourceWebRoot + path;
            Path p = Paths.get(filePath).normalize();
            Path pRoot = Paths.get(Global.resourceWebRoot).normalize();
            if (!p.startsWith(pRoot)) {
                return null;
            }
            byte[] data = Util.readResouceAsBytes(filePath);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }

    }

    private byte[] readFile(String path) {
        String filePath = Global.webRoot + path;
        Path p = Paths.get(filePath).normalize();
        Path pRoot = Paths.get(Global.webRoot).normalize();
        if (!p.startsWith(pRoot)) {
            return null;
        }
        File f = p.toFile();
        if (!f.exists()) {
            return null;
        }
        if (f.isDirectory()) {
            return null;
        }
        try {
            byte[] bytes = FileUtils.readFileToByteArray(f);
            return bytes;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest req) throws Exception {
        QueryStringDecoder queryString = new QueryStringDecoder(req.uri());

        String path = queryString.path();

        if (path.endsWith("/")) {
            path = "/index.html";
        }

        try {
            log.info("ActionHandler {}", queryString);

            byte[] data = null;
            data = readFile(path);
            if (data == null) {
                data = tryReadFromResource(path);
            }
            if (data == null) {
                throw new HttpException(HttpResponseStatus.NOT_FOUND, path);
            }

            ByteBuf buf = channelHandlerContext.alloc().buffer();
            buf.writeBytes(data);

            FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                    (buf));
            fullHttpResponse.headers().add(HttpHeaderNames.CONTENT_TYPE, fileToContentType(path));
            fullHttpResponse.headers().add(HttpHeaderNames.CONTENT_LENGTH, data.length);

            channelHandlerContext.writeAndFlush(fullHttpResponse);
        } catch (HttpException e) {
            HttpResponseStatus status = e.code;

            byte[] data = null;
            if (status == HttpResponseStatus.NOT_FOUND) {
                data = tryReadFromResource("./404.html");
            } else {
                String html = String.format("<h1>%d</h1><p>%s</p>", status.code(), e.getMessage());
                data = html.getBytes();
            }

            ByteBuf buf = channelHandlerContext.alloc().buffer();
            buf.writeBytes(data);
            FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
                    buf);
            fullHttpResponse.headers().add(HttpHeaderNames.CONTENT_TYPE, "text/html");
            fullHttpResponse.headers().add(HttpHeaderNames.CONTENT_LENGTH, data.length);
            channelHandlerContext.writeAndFlush(fullHttpResponse);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof IOException) {
            log.warn("{}", cause.getMessage());
        } else {
            super.exceptionCaught(ctx, cause);
        }
    }
}
