package org.enilu.socket.v3.client.driver;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.enilu.socket.v3.client.net.Deamon;
import org.enilu.socket.v3.client.net.MsgQueue;
import org.enilu.socket.v3.commons.util.Constants;

public class Connection {
	private static Logger logger = Logger.getLogger(Connection.class.getName());
	private final Deamon deamon;

	public Connection(Deamon deamon) {
		logger.setLevel(Constants.log_level);
		this.deamon = deamon;
	}

	public Statement createStatement() {

		return StatementImpl.getInstacne();
	}

	public void close() {
		logger.log(Level.INFO, "connection closing...");
		MsgQueue.getInstance().add("quit");
		if (deamon != null) {
			deamon.setFlag(false);
		}
	}
}
