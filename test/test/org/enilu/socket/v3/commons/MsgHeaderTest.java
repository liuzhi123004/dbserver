package test.org.enilu.socket.v3.commons;

import org.enilu.socket.v3.commons.model.MsgHeader;
import org.junit.Test;

public class MsgHeaderTest {

	@Test
	public void testToString() {
		MsgHeader msgHeader = new MsgHeader();
		msgHeader.setMessageLen(20);
		msgHeader.setOpCode(2);
		msgHeader.setSequence(1);
		System.out.println(msgHeader.toString());
	}

	@Test
	public void testGetBytes() {
		MsgHeader msgHeader = new MsgHeader();
		msgHeader.setMessageLen(20);
		msgHeader.setOpCode(2);
		msgHeader.setSequence(1);
		byte[] bytes = msgHeader.getBytes();
		for (int i = 0; i < bytes.length; i++) {
			System.out.println(bytes[i]);
		}
	}

	@Test
	public void testMsgHeader() {
		MsgHeader msgHeader = new MsgHeader("1231");
		System.out.println(msgHeader.toString());

	}

}
