package org.enilu.socket.v3.commons.model;

import org.enilu.socket.v3.commons.util.ByteUtil;

/**
 * 定义socket传输时的消息的头部内容
 * 
 * @author enilu
 * 
 */
public class MsgHeader {
	public static final int OP_REPLY = 1;// 回復編碼
	public static final int OP_INSERT = 2; // 插入編碼
	public static final int OP_DELETE = 3;// 刪除編碼
	public static final int OP_QUERY = 4;// 查詢編碼
	public static final int OP_COMMAND = 5;// 命令編碼
	public static final int OP_DISCONNECT = 6; // 斷開編碼
	public static final int OP_CONNECT = 7;// 連接編碼
	public static final int OP_SNAPSHOT = 8;// 快照編碼
	public static final int OP_UPDATE = 9;// 更改編碼
	private int sequence;// 消息id
	private int messageLen;// 消息長度
	private int opCode;// 操作編碼

	public MsgHeader() {

	}

	public MsgHeader(String text) {
		this.setSequence(Integer.valueOf(text.substring(0, 1)));
		this.setMessageLen(Integer.valueOf(text.substring(1, 3)));
		this.setOpCode(Integer.valueOf(text.substring(3)));
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public int getMessageLen() {
		return messageLen;
	}

	public void setMessageLen(int messageLen) {
		this.messageLen = messageLen;
	}

	public int getOpCode() {
		return opCode;
	}

	public void setOpCode(int opCode) {
		this.opCode = opCode;
	}

	/**
	 * 将消息头转换为字符串形式<br>
	 * sequence（占1位）messageLen（占2位）opode（占1位）
	 */
	@Override
	public String toString() {
		return new StringBuilder("  sequence:").append(this.sequence)
				.append("  messageLen:").append(this.messageLen)
				.append("  opCode:").append(this.opCode).toString();
	}

	public byte[] getBytes() {
		byte[] sequences = ByteUtil.intToByteArray(this.sequence);
		byte[] opcodes = ByteUtil.intToByteArray(this.opCode);
		byte[] msgLens = ByteUtil.intToByteArray(this.messageLen);
		byte[] bytes = new byte[sequences.length + opcodes.length
				+ msgLens.length];
		System.arraycopy(sequences, 0, bytes, 0, sequences.length);
		System.arraycopy(msgLens, 0, bytes, sequences.length, msgLens.length);
		System.arraycopy(opcodes, 0, bytes, sequences.length + msgLens.length,
				opcodes.length);

		return bytes;

	}
}
