package org.enilu.socket.v3.server.net;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.enilu.socket.v3.commons.model.MsgHeader;
import org.enilu.socket.v3.commons.model.MsgSender;
import org.enilu.socket.v3.commons.util.ByteUtil;
import org.enilu.socket.v3.commons.util.Constants;
import org.enilu.socket.v3.server.service.ServerService;
import org.enilu.socket.v3.server.threadpool.WorkerQueue;

/**
 * 类说明：数据接收和转发，Socket连接请求处理类
 */
public class Dealer {

	private static Logger logger = Logger.getLogger(Dealer.class.getName());

	public Dealer() {
		logger.setLevel(Constants.log_level);
	}

	public static SocketChannel accept(Selector selector,
			ServerSocketChannel serverChannel) {
		SocketChannel channel = null;
		try {
			channel = serverChannel.accept();
			channel.configureBlocking(false);
			channel.register(selector, SelectionKey.OP_READ);

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error while configure socket channel :"
					+ e);
			if (channel != null) {
				try {
					channel.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return channel;
	}

	public static Object read(Selector selector, SelectionKey selectionkey,
			List<SocketChannel> clientChannels, int receiveLength) {
		Object result = "";
		SocketChannel channel = (SocketChannel) selectionkey.channel();

		ByteBuffer buffer = ByteBuffer.allocateDirect(receiveLength);

		int num = 0;
		try {

			num = channel.read(buffer);
		} catch (IOException e) {
			/**
			 * 如果出现异常，则需要关闭连接。故把num设置为-1，用下面的关闭逻辑来关闭channel
			 */
			num = -1;
		}

		if (num < 0) {
			/**
			 * 对方关闭了SocketChannel 所以服务器这边也要关闭
			 */
			try {
				logger.log(Level.INFO, "客户端关闭了连接");
				channel.close();
				for (int index = 0, length = clientChannels.size(); index < length; index++) {
					if (clientChannels.get(index).equals(channel)) {
						clientChannels.remove(index);
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			/**
			 * 把数据下发下去
			 */
			buffer.flip();
			byte[] temp = new byte[num];
			buffer.get(temp);
			// 客户端关闭连接
			if (temp.length == 4) {
				try {
					String flag = new String(temp);
					if ("quit".equals(flag)) {
						channel.close();
						logger.log(Level.INFO, "client disconnect");
						return null;
					}
				} catch (Exception e) {
					logger.log(Level.SEVERE,
							"close socket error:" + e.getMessage());

				}
				return null;
			}
			// 客户端连接认证

			// 客户端正常发送数据请求
			if (receiveLength == 12) {
				// 接收msgsender头信息：msgheader
				MsgHeader header = new MsgHeader();
				byte[] byteInts = new byte[4];
				System.arraycopy(temp, 0, byteInts, 0, 4);
				header.setSequence(ByteUtil.byteToInt(byteInts));

				System.arraycopy(temp, 4, byteInts, 0, 4);
				header.setMessageLen(ByteUtil.byteToInt(byteInts));
				receiveLength = header.getMessageLen();

				System.arraycopy(temp, 8, byteInts, 0, 4);
				header.setOpCode(ByteUtil.byteToInt(byteInts));
				// 接收到头信息完毕，继续接收消息体

				ByteBuffer buffer2 = ByteBuffer.allocateDirect(receiveLength);

				int num2 = 0;
				try {
					MsgSender msgSender = new MsgSender();
					num2 = channel.read(buffer2);
					buffer2.flip();
					byte[] temp2 = new byte[num2];
					buffer2.get(temp2);
					msgSender.setHeader(header);
					msgSender.setData(ByteUtil.byteToJsonString(temp2));

					// 向客户端返回数据
					ServerService work = new ServerService(channel, msgSender);
					WorkerQueue.getInstance().push(work);

				} catch (IOException e) {
					/**
					 * 如果出现异常，则需要关闭连接。故把num设置为-1，用下面的关闭逻辑来关闭channel
					 */
					num = -1;
				}

			} else {
				result = new String(temp);
				if ("stop".equals(result)) {
					return result;
				}
			}

		}
		return result;
	}
}
