package com.yunjian.server.tcp;

import com.yunjian.server.dto.ProtocolBody;
import com.yunjian.server.util.ByteUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class ProtocolDecoder extends LengthFieldBasedFrameDecoder {

	public ProtocolDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment,
			int initialBytesToStrip) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		ByteBuf buffer = (ByteBuf) super.decode(ctx, in);
		if (buffer == null) {
			return null;
		}
		ProtocolBody body = new ProtocolBody();
		body.setCardCode(buffer.readByte());  //起始码，卡板类型
		body.setType(buffer.readByte());      //贞类型
		byte[] length = new byte[2];           //贞长度
		buffer.readBytes(length);
		body.setLength(ByteUtils.getShort(length, 0));
		body.setCtrlCode(buffer.readByte());  //命令域
		int dataLength = body.getLength() - 7; 
		byte[] data = new byte[dataLength];
		buffer.readBytes(data);
		body.setData(data);                    //数据域
		byte[] end = new byte[2];
		buffer.readBytes(end);
		body.setEnd(ByteUtils.getShort(end, 0));        //结束码
		
		byte[] msgType = {body.getType(),body.getCtrlCode()};
		body.setMsgType(ByteUtils.getShort(msgType, 0));
		
		buffer.release();
		return body;
	}

}
