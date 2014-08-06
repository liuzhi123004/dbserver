package org.enilu.socket.v3.commons.model;

import org.enilu.socket.v3.commons.util.ByteUtil;

public class MsgSender extends Msg {
	private MsgHeader header;// 消息头
	private int numInsert;// ?
	private String data;// 发送数据

	private final int index = 1, length = 5;

	public MsgSender() {
	}

	public MsgSender(String text) {

	}

	public MsgSender(byte[] bytes) {
		String text = new String(bytes);
		new MsgSender(text.substring(4)).setHeader(new MsgHeader(text
				.substring(0, 4)));
	}

	public MsgHeader getHeader() {
		return header;
	}

	public void setHeader(MsgHeader header) {
		this.header = header;
	}

	public int getNumInsert() {
		return numInsert;
	}

	public void setNumInsert(int numInsert) {
		this.numInsert = numInsert;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return new StringBuilder(this.getHeader().toString())
				.append("  numInsert:").append(this.numInsert)
				.append("  data:").append(this.data).toString();
	}

	public byte[] getBytes() {

		try {
			byte[] datas = ByteUtil.JsonStringTOBsonByte(this.data);
			this.getHeader().setMessageLen(datas.length);
			byte[] headers = this.getHeader().getBytes();
			byte[] bytes = new byte[headers.length + datas.length];
			System.arraycopy(headers, 0, bytes, 0, headers.length);
			System.arraycopy(datas, 0, bytes, headers.length, datas.length);
			return bytes;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
}
