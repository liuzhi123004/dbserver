package org.enilu.socket.v3.client;

import java.util.List;
import java.util.Scanner;

import org.enilu.socket.v3.client.driver.Connection;
import org.enilu.socket.v3.client.driver.ConnectionFactory;
import org.enilu.socket.v3.client.driver.Statement;

/**
 * 
 * 客户端控制台
 * <p>
 * 
 * @author enilu(82552623@qq.com)
 * 
 */
public class Console {
	String prefix = "cnensql>";
	Connection conn = null;
	boolean quit = false;

	/**
	 * 启动客户端控制台
	 * 
	 * @param args
	 * @throws Exception
	 */
	public void start() throws Exception {
		Scanner input = new Scanner(System.in);
		System.out.println("#welcome to cnensql console#");
		System.out.print("#");
		String cmd = "";
		while (!quit) {
			System.out.print(prefix);
			cmd = input.nextLine();
			execute(cmd);
		}

		input.close();
	}

	/**
	 * 处理客户端命令
	 * 
	 * @param cmd
	 *            客户输入的命令
	 * @throws Exception
	 */
	private void execute(String cmd) throws Exception {
		String cmdheader = cmd.split("\\s+")[0];
		if (conn == null && !"quit".equals(cmdheader)
				&& !"connect".equals(cmdheader)) {
			// 未连接服务器端提醒
			System.out.println(prefix
					+ "please connect database,eg:connect hostname port");
			return;
		}
		if (!"quit".equals(cmdheader) && cmd.split("\\s+").length == 1) {
			// 非法命令
			System.out.println(prefix
					+ "your input characters is illegal,please input again:");
			return;
		}
		switch (cmdheader) {
		case "connect":
			// 连接命令处理
			if (conn == null) {
				String[] textArr = cmd.split(" ");
				conn = ConnectionFactory.getConnection(textArr[1].trim(),
						Integer.valueOf(textArr[2].trim()), null, null, null);
				if (conn != null) {
					System.out.println(prefix + "success");

				} else {
					System.out.println(prefix + "connected failed");
				}
			} else {
				System.out.println(prefix
						+ "you has connected,don't connect again");
			}
			break;
		case "quit":

			// 退出连接
			if (conn != null) {
				conn.close();
			}
			System.out.println(prefix + "bye");
			conn = null;
			quit = true;
			break;
		case "query":

			// 查询命令
			Statement st2 = conn.createStatement();
			System.out.println(prefix + "result:");
			List list = st2.query(cmd);
			for (int i = 0; i < list.size(); i++) {
				System.out.println(list.get(i));
			}

			break;
		case "update":

			// 更新命令
			Statement st3 = conn.createStatement();
			System.out.println(prefix + "result:");
			System.out.println(st3.execute(cmd));

			break;
		case "delete":

			// 删除命令
			Statement st4 = conn.createStatement();
			System.out.println(prefix + "result:");
			System.out.println(st4.execute(cmd));
			break;
		case "insert":

			// 插入命令
			Statement st5 = conn.createStatement();
			System.out.println(prefix + "result:");
			System.out.println(st5.execute(cmd));
			break;
		default:

			// 非法命令
			System.out.println(prefix
					+ "your input characters is illegal,please input again:");

			break;
		}
	}

}
