package test.org.enilu.socket.v3.commons;

import org.enilu.socket.v3.commons.model.MsgHeader;
import org.enilu.socket.v3.commons.model.MsgReplay;
import org.junit.Test;

public class MsgReplayTest {

	@Test
	public void testToString() {

		MsgHeader msgHeader = new MsgHeader();
		msgHeader.setOpCode(2);
		msgHeader.setSequence(1);
		MsgReplay msgReplay = new MsgReplay();
		msgReplay.setHeader(msgHeader);
		msgReplay.setData("发送数据了");
		msgReplay.setNumReturn(1);
		msgReplay.setReturnCode(1);

		System.out.println(msgReplay.toString());

	}

	@Test
	public void testGetBytes() {
		MsgHeader msgHeader = new MsgHeader();
		msgHeader.setOpCode(2);
		msgHeader.setSequence(1);
		MsgReplay msgReplay = new MsgReplay();
		msgReplay.setHeader(msgHeader);
		msgReplay.setData("服务器端返回数据了");
		msgReplay.setNumReturn(1);
		msgReplay.setReturnCode(1);

		byte[] bytes = msgReplay.getBytes();
		for (int i = 0; i < bytes.length; i++) {
			System.out.println(bytes[i]);
		}
	}

}
