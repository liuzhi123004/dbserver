/**
 * 
 */
package org.enilu.socket.v3.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.enilu.socket.v3.commons.Constants;
import org.enilu.socket.v3.commons.MsgReplay;
import org.enilu.socket.v3.commons.MsgSender;
import org.enilu.socket.v3.threadpool.Worker;

/**
 * 服务器段服务类
 * 
 * @author enilu
 * 
 */
public class ServerService extends Worker {
	private static Logger logger = Logger.getLogger(ServerService.class
			.getName());
	private final MsgSender sender;
	private final SocketChannel socket;

	public ServerService(SocketChannel socket, MsgSender sender) {
		logger.setLevel(Constants.log_level);
		this.sender = sender;
		this.socket = socket;
	}

	@Override
	public void work() {
		logger.log(Level.INFO, "accept from client：" + sender.toString());
		MsgReplay msgReplay = new MsgReplay();
		msgReplay.setHeader(sender.getHeader());
		msgReplay.setReturnCode(0);
		msgReplay.setNumReturn(1);
		String sendmsg = "{id:'1',name:'张三'}";
		Map map = new HashMap();
		map.put("id", 1);
		map.put("name", "张三");
		msgReplay.setMapdata(map);
		ByteBuffer bufferSender = ByteBuffer.wrap(msgReplay.getBytes());
		try {
			logger.log(Level.INFO,
					"send message to client：" + msgReplay.toString());
			socket.write(bufferSender);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
