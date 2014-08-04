package org.enilu.socket.demo01;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;

public class TcpClient {

	public static void main(String[] args) {
		Socket client = null;
		Writer  writer=null;
		Reader reader = null;
		try {
			client = new Socket("127.0.0.1", 9999);
			writer = new OutputStreamWriter(client.getOutputStream());
			String info1 = "救命啊，救命啊\n,我掉水裡了，我不會游泳\nover";
			writer.write(info1);
			System.out.println("to server:"+info1);
			writer.flush();
			
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
			System.out.println("from server:"+builder.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				reader.close();
				writer.close();
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
