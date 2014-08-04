package org.enilu.socket.demo01;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {
	public static void main(String[] args) {

		ServerSocket server = null;
		Socket client = null;
		Reader reader = null;
		Writer writer = null;
		try {
			server = new ServerSocket(9999);
			System.out.println("server监听中...");
			client = server.accept();
			reader = new InputStreamReader(client.getInputStream());
			char[] chars = new char[1024];
			int len;
			StringBuilder builder = new StringBuilder();
			while ((len = reader.read(chars)) != -1) {
				String line = new String(chars);
				builder.append(line);
				if (line.indexOf("over") > -1) {
					break;
				}
			}
			System.out.println("from client:" + builder.toString());
			//回复信息
			writer = new OutputStreamWriter(client.getOutputStream());
			writer.write("我来救你了，hold住啊over");
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
				reader.close();
				client.close();
				server.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
