package org.enilu.socket.v3.commons.model;

import java.io.IOException;
import java.util.Map;

import org.enilu.socket.v3.commons.util.ByteUtil;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class MsgReplay {
	private MsgHeader header;// 消息头
	private int returnCode;// 返回编码
	private int numReturn; // 返回数据记录数
	private String data;// 消息体map的字符串形式
	private Map mapdata;// 消息体map

	public MsgHeader getHeader() {
		return header;
	}

	public void setHeader(MsgHeader header) {
		this.header = header;
	}

	public int getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}

	public int getNumReturn() {
		return numReturn;
	}

	public void setNumReturn(int numReturn) {
		this.numReturn = numReturn;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Map getMapdata() {
		return mapdata;
	}

	public void setMapdata(Map mapdata) {
		this.mapdata = mapdata;
	}

	@Override
	public String toString() {
		return new StringBuilder("  head:" + this.getHeader().toString())
				.append("  returnCode:").append(this.returnCode)
				.append("  numReturn").append(this.numReturn)
				.append("  result:").append(this.data).toString();
	}

	public byte[] getBytes() {

		// 将data信息转换微bson的二进制数组形式

		byte[] datas = null;
		try {
			datas = ByteUtil.MapTOBsonByte(mapdata);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.getHeader().setMessageLen(datas.length + 8);
		byte[] headers = this.getHeader().getBytes();
		byte[] returnCode = ByteUtil.intToByteArray(this.returnCode);
		byte[] numberReturn = ByteUtil.intToByteArray(this.numReturn);
		byte[] bytes = new byte[headers.length + returnCode.length
				+ numberReturn.length + datas.length];
		System.arraycopy(headers, 0, bytes, 0, headers.length);
		System.arraycopy(returnCode, 0, bytes, headers.length,
				returnCode.length);
		System.arraycopy(numberReturn, 0, bytes, headers.length
				+ returnCode.length, numberReturn.length);
		System.arraycopy(datas, 0, bytes, headers.length + returnCode.length
				+ numberReturn.length, datas.length);
		return bytes;
	}

}
