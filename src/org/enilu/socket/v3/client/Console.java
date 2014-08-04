/**
 * 
 */
package org.enilu.socket.v3.client;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 客户端控制台
 * 
 * @author enilu
 * 
 */
public class Console {
	static String prefix = "cnensql>";
	static Connection conn = null;
	static boolean quit = false;

	/**
	 * @param args
	 * @throws IOException
	 * @throws
	 */
	public static void main(String[] args) throws Exception {
		Scanner input = new Scanner(System.in);
		System.out.println("#welcome to cnensql console#");
		System.out.print("#");
		String text = "";
		while (!quit) {
			System.out.print(prefix);
			text = input.nextLine();
			execute(text);
		}

		input.close();
	}

	private static void execute(String text) throws Exception {
		if (text.startsWith("connect")) {

			String[] textArr = text.split(" ");
			conn = ConnectionFactory.getConnection(textArr[1].trim(),
					Integer.valueOf(textArr[2].trim()), null, null, null);
			if (conn != null) {
				System.out.println(prefix + "success");

			} else {
				System.out.println(prefix + "connected failed");
			}
			return;

		} else if (conn == null && !"quit".equals(text)) {
			System.out.println(prefix
					+ "please connect database,eg:connect hostname port");
			return;
		} else if (text.equals("quit")) {
			if (conn != null) {
				conn.close();
			}
			System.out.println(prefix + "bye");
			conn = null;
			quit = true;
			return;
		} else {
			Pattern p = Pattern.compile("[a-z]+[ ]+\\{[a-z]+:.+\\}");
			Matcher m = p.matcher(text);
			if (!m.matches()) {
				System.out
						.println(prefix
								+ "please input legal characters. ,eg：query {id:1,name:'张三'}");
				return;
			}

		}
		if (text.startsWith("query")) {

			Statement st2 = conn.createStatement();
			System.out.println(prefix + "result:");
			List list = st2.query(text);
			for (int i = 0; i < list.size(); i++) {
				System.out.println(list.get(i));
			}
		} else if (text.startsWith("update")) {

			Statement st2 = conn.createStatement();
			System.out.println(prefix + "result:");
			System.out.println(st2.execute(text));

		} else if (text.startsWith("delete")) {

			Statement st2 = conn.createStatement();
			System.out.println(prefix + "result:");
			System.out.println(st2.execute(text));
		} else if (text.startsWith("insert")) {

			Statement st2 = conn.createStatement();
			System.out.println(prefix + "result:");
			System.out.println(st2.execute(text));
		} else {
			System.out.println(prefix
					+ "your input characters is illegal,please input again:");
		}

	}

}
