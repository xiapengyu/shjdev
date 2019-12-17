package com.yunjian.server.tcp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * tcp服务初始化类，添加协议拆包和封包对象.
 */
@Component
public class TCPServerInitializer extends ChannelInitializer<SocketChannel> {

  @Autowired
  private TCPServerHandler serverHandler;


  @Override
  protected void initChannel(SocketChannel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();
    pipeline.addLast("idleStateHandler", new IdleStateHandler(60, 0, 0));  //空闲检测周期60s
    pipeline.addLast("decoder", new ProtocolDecoder(512, 2, 2, -4, 0));
    pipeline.addLast("encoder", new ProtocolEncoder());
    pipeline.addLast("handler", serverHandler);
  }

}
