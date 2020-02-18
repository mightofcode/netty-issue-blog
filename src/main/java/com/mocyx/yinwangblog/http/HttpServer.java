package com.mocyx.yinwangblog.http;


import com.mocyx.yinwangblog.Global;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 */
@Component
@Slf4j
public class HttpServer implements Runnable {

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;


    @Override
    public void run() {
        bossGroup = new NioEventLoopGroup(3);
        workerGroup = new NioEventLoopGroup(1);

        ServerBootstrap b = new ServerBootstrap();
        try {
            b.group(bossGroup)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .channel(NioServerSocketChannel.class)
//				.handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new HttpServerCodec())
                                    //把多个消息转换为一个单一的FullHttpRequest或是FullHttpResponse
                                    .addLast(new HttpObjectAggregator(65536))
                                    //压缩Http消息
//						.addLast(new HttpChunkContentCompressor())
                                    //大文件支持
                                    .addLast(new ChunkedWriteHandler())
                                    .addLast(new ActionHandler());
                            ;


                        }
                    });
            Channel ch = b.bind(Global.config.getServerIp(), Global.config.getServerPort()).sync().channel();
            log.info("http server started {} {}", Global.config.getServerIp(), Global.config.getServerPort());
            ch.closeFuture().sync();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }
}
