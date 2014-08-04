package org.enilu.socket.v3.server;

public class StartServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Deamon deamon = new Deamon(9999);
		new Thread(deamon).start();

		// deamon.setFlag(false);
	}

}
