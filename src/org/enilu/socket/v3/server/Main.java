package org.enilu.socket.v3.server;

import org.enilu.socket.v3.server.net.Deamon;

/**
 * 
 * 启动服务器端，使用指定端口启动服务器端
 * <p>
 * 
 * @author enilu(82552623@qq.com)
 * 
 */
public class Main {

	/**
	 * 启动服务器端
	 * 
	 * @param args
	 *            默认第一个参数为监听端口，如果不输入，则使用9999作为默认端口
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		int port = 9999;
		if (args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (NumberFormatException e) {
				throw new Exception("端口必需为数字");
			}
		}
		Deamon deamon = new Deamon(port);
		new Thread(deamon).start();
	}

}
