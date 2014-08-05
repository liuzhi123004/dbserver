package org.enilu.socket.v3.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.enilu.socket.v3.commons.Constants;

public class Deamon implements Runnable {
	private static Logger logger = Logger.getLogger(Deamon.class.getName());
	private boolean flag = true;

	private ServerSocketChannel serverChannel = null;

	private Selector selector = null;

	private List<SocketChannel> clientChannels = null;

	public Deamon(int port) {

		logger.setLevel(Constants.log_level);
		try {
			logger.log(Level.INFO, "server listening...");
			serverChannel = ServerSocketChannel.open();
			serverChannel.socket().bind(new InetSocketAddress(port));
			selector = Selector.open();
			serverChannel.configureBlocking(false);
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
			this.clientChannels = new ArrayList<SocketChannel>();
			ServiceEngine.getInstance().bootstrap();
			// ThreadPool.getTheadPool();
		} catch (IOException e) {
			logger.log(Level.SEVERE,
					"Error while building server : " + e.getMessage());
		}
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	@Override
	public void run() {
		while (this.flag) {
			int num = 0;
			try {

				num = selector.select();
			} catch (IOException e) {
				logger.log(Level.SEVERE,
						"Error while select channel :" + e.getMessage());
				break;
			}
			if (num > 0) {
				logger.log(Level.INFO, "client connected");
				Iterator<SelectionKey> it = selector.selectedKeys().iterator();
				while (it.hasNext()) {
					SelectionKey key = it.next();
					it.remove();
					if (key.isAcceptable()) {
						this.clientChannels.add(Dealer.accept(selector,
								serverChannel));
					} else if (key.isReadable()) {

						Object result = Dealer.read(selector, key,
								this.clientChannels, 12);

						if ("stop".equals(result)) {
							logger.log(Level.INFO, "socket服务停止中...");
							this.flag = false;
						}
					}
				}
			}

		}

		if (this.serverChannel != null && this.serverChannel.isOpen()) {
			try {
				this.serverChannel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (this.selector != null && this.selector.isOpen()) {
			try {
				this.selector.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		logger.log(Level.INFO, "socket服务已停止");

	}

}
