package com.yunjian.server.tcp;

import com.yunjian.server.dto.ProtocolBody;

import com.yunjian.server.util.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ProtocolEncoder extends MessageToByteEncoder<ProtocolBody> {

	@Override
	protected void encode(ChannelHandlerContext ctx, ProtocolBody body, ByteBuf out) throws Exception {
		if (body != null) {
			out.writeByte(body.getCardCode());
			out.writeByte(body.getType());
			out.writeShort(body.getLength());
			out.writeByte(body.getCtrlCode());
			if(body.getData()!=null) out.writeBytes(body.getData());
			out.writeShort(body.getEnd());
			
//			byte[] msgByte = new byte[out.readableBytes()];
//			out.readBytes(msgByte);
//			System.out.println("打印消息体：" + ByteUtils.toHexString(msgByte));
//			out.writeBytes(msgByte);
		}
	}

}
