package org.enilu.socket.v3.client.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 后台精灵进程，负责管理SocketChannel，对Dealer的操作进行调度
 * 
 * @author Administrator
 * 
 */
public class Deamon implements Runnable {

	/**
	 * 选择器，用于监听注册在上面的SocketChannel的状态
	 */
	private Selector selector = null;

	/**
	 * SocketChannel 用户发送和接受数据的信道
	 */
	private SocketChannel channel = null;

	/**
	 * 运行标识。在线程里此标识为false的时候会推出线程
	 * 该属性在ExitCommandListener里通过调用setFlag方法修改，用于通知线程用户要求退出的程序
	 */
	private boolean flag = true;

	public Deamon(String addr, int port) throws IOException {

		channel = SocketChannel.open(new InetSocketAddress(addr, port));
		channel.configureBlocking(false);
		selector = Selector.open();
		channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);

	}

	public void run() {
		while (this.flag) {
			/**
			 * 如果可以继续执行，则在循环体内循环执行监听选择操作
			 */
			int num = 0;
			try {
				/**
				 * 得到处于可读或者可写状态的SocketChannel对象的个数
				 */
				num = this.selector.select();
			} catch (IOException e) {
				/**
				 * 如果出现异常，则此处应该加上日志打印，然后跳出循环,执行循环体下面的释放资源操作
				 */
				break;
			}
			if (num > 0) {
				/**
				 * 如果有多个SocketChannel处于可读或者可写状态，则轮询注册在Selector上面的SelectionKey
				 */
				Iterator<SelectionKey> keys = selector.selectedKeys()
						.iterator();
				while (keys.hasNext()) {
					SelectionKey key = keys.next();
					/**
					 * 此步操作用于删除该SelectionKey的被选中状态
					 */
					keys.remove();
					if (key.isReadable()) {
						/**
						 * 如果是读操作，则调用读操作的处理逻辑
						 */
						Dealer.read((SocketChannel) key.channel());
					} else if (key.isWritable()) {
						/**
						 * 如果是写操作，则调用写操作的处理逻辑
						 */
						Dealer.write((SocketChannel) key.channel());
					}
				}
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (this.channel != null && this.channel.isOpen()) {
			/**
			 * 关闭SocketChannel
			 */
			try {
				this.channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (this.selector != null && this.selector.isOpen()) {
			/**
			 * 关闭Selector选择器
			 */
			try {
				this.selector.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

}
