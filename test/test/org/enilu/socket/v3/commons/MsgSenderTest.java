package test.org.enilu.socket.v3.commons;

import org.enilu.socket.v3.commons.model.MsgHeader;
import org.enilu.socket.v3.commons.model.MsgSender;
import org.junit.Test;

public class MsgSenderTest {

	@Test
	public void testToString() {

		MsgHeader msgHeader = new MsgHeader();
		msgHeader.setOpCode(2);
		msgHeader.setSequence(1);
		MsgSender msgSender = new MsgSender();
		msgSender.setHeader(msgHeader);
		msgSender.setData("发送数据了");
		msgSender.setNumInsert(1);

		System.out.println(msgSender.toString());

	}

	@Test
	public void testGetBytes() {
		MsgHeader msgHeader = new MsgHeader();
		msgHeader.setOpCode(2);
		msgHeader.setSequence(1);
		MsgSender msgSender = new MsgSender();
		msgSender.setHeader(msgHeader);
		msgSender.setData("发送数据了");
		msgSender.setNumInsert(1);

		byte[] bytes = msgSender.getBytes();
		for (int i = 0; i < bytes.length; i++) {
			System.out.println(bytes[i]);
		}
	}

}
