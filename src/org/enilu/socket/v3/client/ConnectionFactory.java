package org.enilu.socket.v3.client;

import java.io.IOException;

public class ConnectionFactory {
	public static Connection getConnection(String ip, int port, String dbname,
			String user, String password) throws IOException {
		Deamon deamon = new Deamon(ip, port);
		new Thread(deamon).start();

		return new Connection(deamon);
	}

}
