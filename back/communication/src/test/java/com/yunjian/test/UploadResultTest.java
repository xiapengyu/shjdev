package com.yunjian.test;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import com.yunjian.server.CommandRouterApplication;
//import com.yunjian.server.common.CommandEnum;
//import com.yunjian.server.dto.ProtocolBody;
//import com.yunjian.server.service.UpService;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = CommandRouterApplication.class)
public class UploadResultTest {
//	
//	@Autowired
//	private UpService upService;
//	
//	@Test
//	public void testCtrlResultUpload() {
//		//02 06 01 b0 01 03 b7 
//		byte result = (byte)0x01;
//		CommandEnum commandEnum = CommandEnum.MECHINE_CTRL_RESULT;
//		ProtocolBody body = new ProtocolBody();
//		byte[] data = {result};
//		body.setData(data);
//		upService.uploadResult(body, commandEnum);
//	}
//	
//	@Test
//	public void testStatusCodeResult() {
//		//02 0b 01 b2 01 00 00 00 00 00 03 b8
//		byte result[] = {(byte)0x01,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};
//		CommandEnum commandEnum = CommandEnum.QUERY_STATUS_RESULT;
//		ProtocolBody body = new ProtocolBody();
//		body.setData(result);
//		upService.uploadResult(body, commandEnum);
//	}
//	
//	@Test
//	public void testReadVersionResult() {
//		byte result[] = {(byte)0x01,(byte)0x02,(byte)0x03,(byte)0x04,(byte)0x0a,(byte)0x05,(byte)0x00,(byte)0x00,(byte)0x00};
//		CommandEnum commandEnum = CommandEnum.READ_VERSION_RESULT;
//		ProtocolBody body = new ProtocolBody();
//		body.setData(result);
//		upService.uploadResult(body, commandEnum);
//	}
//	
//	@Test
//	public void test2() {
//		System.out.println("test");
//	}
//
}
