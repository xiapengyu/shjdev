package com.yunjian.server.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * TCP服务代理.
 */
@Component
public class TCPServerProxy {
  private static final Logger logger = Logger.getLogger(TCPServerProxy.class);

  private Channel channel;

  @Autowired
  private TCPServerInitializer serverInitializer;

  private EventLoopGroup bossGroup;

  private EventLoopGroup workerGroup;

  @Value("${tcp.server.port:38081}")
  int port;  
  
  /**
   * 启动Tcp服务 .
   * @throws InterruptedException void
   */
  @PostConstruct
  public void start() throws InterruptedException {
    // 配置NIO线程组
    bossGroup = new NioEventLoopGroup(2);
    workerGroup = new NioEventLoopGroup(8);
    ServerBootstrap b = new ServerBootstrap();
    b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
        .handler(new LoggingHandler(LogLevel.INFO)).childHandler(serverInitializer)//
        .option(ChannelOption.SO_BACKLOG, 10 * 1024) //
        .option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(64, 512, 2048))//
        .childOption(ChannelOption.SO_KEEPALIVE, true);
    channel = b.bind(port).sync().channel();
    logger.info("服务器开始监听TCP端口： " + port + " ，等待客户端（设备）连接...");
  }

  /**
   * 停止TCP服务 .
   */
  @PreDestroy
  public void stop() {
    logger.info("设备接入TCP服务正在停止，销毁TCP服务资源...");
    if (null == channel) {
      logger.error("server channel is null");
      bossGroup = null;
      workerGroup = null;
      return;
    }
    bossGroup.shutdownGracefully();
    workerGroup.shutdownGracefully();
    channel.closeFuture().syncUninterruptibly();
    bossGroup = null;
    workerGroup = null;
    channel = null;
  }

}
