package org.enilu.socket.v3.client.net;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.enilu.socket.v3.commons.model.Msg;
import org.enilu.socket.v3.commons.model.MsgHeader;
import org.enilu.socket.v3.commons.model.MsgReplay;
import org.enilu.socket.v3.commons.util.ByteUtil;
import org.enilu.socket.v3.commons.util.Constants;

/**
 * 类说明：SocketChannel信息处理类。完成数据的接收与发送
 */
public class Dealer {

	private static Logger logger = Logger.getLogger(Dealer.class.getName());

	/**
	 * 从SocketChannel中读取信息
	 * 
	 * @param channel
	 */
	public static void read(SocketChannel channel) {
		logger.setLevel(Constants.log_level);
		int receiveLength = 12;// 头消息长度为12
		/**
		 * 初始化缓冲区
		 */
		ByteBuffer buffer = ByteBuffer.allocateDirect(receiveLength);
		/**
		 * 读到的字节数
		 */
		int num = 0;
		try {
			/**
			 * 读取数据到缓冲区中
			 */
			num = channel.read(buffer);
		} catch (IOException e) {
			/**
			 * 如果出现异常，则把num设置为-1，这样下面的逻辑会对SocketChannel进行关闭
			 */
			num = -1;
			e.printStackTrace();
		}

		if (num > 0) {
			/**
			 * 如果读到了内容，则需要初始化一个字节数组
			 */
			byte[] temp = new byte[num];
			/**
			 * 重置缓冲区指针，并把数据放到字节数组中
			 */
			buffer.flip();
			buffer.get(temp);
			try {
				if (receiveLength == 12) {

					// 接收msgsender头信息：msgheader
					MsgHeader header = new MsgHeader();

					byte[] byteInts = new byte[4];
					System.arraycopy(temp, 0, byteInts, 0, 4);
					header.setSequence(ByteUtil.byteToInt(byteInts));
					System.arraycopy(temp, 4, byteInts, 0, 4);

					header.setMessageLen(ByteUtil.byteToInt(byteInts));

					System.arraycopy(temp, 8, byteInts, 0, 4);
					header.setOpCode(ByteUtil.byteToInt(byteInts));
					receiveLength = header.getMessageLen();
					// 接收到头信息后，继续接收消息体

					int num2 = 0;
					try {
						ByteBuffer buffer2 = null;
						byte[] totalBytes = new byte[receiveLength];
						if (receiveLength < 1024) {
							buffer2 = ByteBuffer.allocateDirect(receiveLength);
							channel.read(buffer2);
							buffer2.flip();
							buffer2.get(totalBytes);
						} else {
							logger.log(Level.INFO, "需要接收的总字节数receiveLen="
									+ receiveLength);

							int receivedLength = 0;
							while (receiveLength > 0) {
								buffer2 = ByteBuffer
										.allocateDirect(receiveLength >= 1024 ? 1024
												: receiveLength);
								num2 = channel.read(buffer2);
								buffer2.flip();
								byte[] temp2 = new byte[num2];
								buffer2.get(temp2);

								System.arraycopy(temp2, 0, totalBytes,
										receivedLength, temp2.length);

								receivedLength += temp2.length;
								receiveLength -= 1024;
							}
						}
						byte[] returnCode = new byte[4];
						byte[] numberReturn = new byte[4];
						byte[] text = new byte[totalBytes.length - 8];
						System.arraycopy(totalBytes, 0, returnCode, 0, 4);
						System.arraycopy(totalBytes, 4, numberReturn, 0, 4);
						System.arraycopy(totalBytes, 8, text, 0, text.length);

						MsgReplay msgReplay = new MsgReplay();
						msgReplay.setHeader(header);
						msgReplay.setReturnCode(ByteUtil.byteToInt(returnCode));
						msgReplay
								.setNumReturn(ByteUtil.byteToInt(numberReturn));

						msgReplay.setData(ByteUtil.byteToJsonString(text));
						// msgReplay.setData(new String(text));
						MsgQueue.getInstance().addReplay(msgReplay);

					} catch (IOException e) {
						logger.log(Level.SEVERE, "接收服务器返回值异常", e);
						/**
						 * 如果出现异常，则需要关闭连接。故把num设置为-1，用下面的关闭逻辑来关闭channel
						 */
						num = -1;
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (num < 0) {
			/**
			 * 如果读到的数据长度小于0，说明对方再进行连接断开，所以关闭SocketChannel
			 */
			try {
				channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 想SocketChannel中写入数据
	 * 
	 * @param channel
	 */
	public static void write(SocketChannel channel) {

		/**
		 * 从消息队列中获取要发送的消息
		 */
		Msg msg = MsgQueue.getInstance().get();

		if (msg == null) {
			/**
			 * 如果消息队列中没有要发送的消息，则返回。
			 */
			return;
		}
		/**
		 * 初始化缓冲区
		 */
		ByteBuffer buffer = ByteBuffer.allocateDirect(msg.getBytes().length);

		/**
		 * 把消息放到缓冲区中
		 */
		buffer.put(msg.getBytes());

		/**
		 * 重置缓冲区指针
		 */
		buffer.flip();
		try {
			/**
			 * 把缓冲区中的数据写到SocketChannel里
			 */
			channel.write(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
