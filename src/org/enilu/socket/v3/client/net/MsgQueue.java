package org.enilu.socket.v3.client.net;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.enilu.socket.v3.commons.model.Msg;
import org.enilu.socket.v3.commons.model.MsgReplay;
import org.enilu.socket.v3.commons.model.StringMsg;

/**
 * 消息队列控制类 每个客户端实际只需要一个消息队列，所以采用了单例模式
 */
public class MsgQueue {

	private static MsgQueue instance = null;
	/**
	 * 用LinkedList来模拟FIFO队列，减少删除头元素时移动元素位置的时间
	 */
	private List<Msg> msgs = new LinkedList<Msg>();
	private List<MsgReplay> results = new ArrayList<MsgReplay>();

	private MsgQueue() {
	}

	/**
	 * 由于队列由UI和通信线程共同访问，所以加了同步操作 下面的方法也一样
	 * 
	 * @return
	 */
	public synchronized static MsgQueue getInstance() {
		if (instance == null) {
			instance = new MsgQueue();
		}
		return instance;
	}

	public synchronized void add(Msg msg) {
		msgs.add(msg);
	}

	public synchronized void add(String msg) {

		msgs.add(new StringMsg(msg));
	}

	public synchronized Msg get() {
		Msg msg = null;
		if (msgs.size() > 0) {
			msg = msgs.get(0);
			msgs.remove(0);
		}

		return msg;
	}

	public void addReplay(MsgReplay msgReplay) {
		results.add(msgReplay);
	}

	public MsgReplay getReplay() {
		if (results != null && results.size() > 0) {
			return results.remove(0);
		}
		return null;
	}

}
