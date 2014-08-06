package org.enilu.socket.v3.server.threadpool;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 消息队列工具类,包含对worker队列的简单操作
 * <p>
 * 
 * @author enilu(82552623@qq.com)
 * 
 */
public class WorkerQueue {
	private static BlockingDeque<Worker> workers = null;
	static WorkerQueue instance;

	private WorkerQueue() {

	}

	public static WorkerQueue getInstance() {
		if (instance == null) {
			instance = new WorkerQueue();
			workers = new LinkedBlockingDeque<Worker>(50);
		}
		return instance;
	}

	public void push(Worker work) {
		workers.offer(work);
	}

	public Worker take() {
		try {
			return workers.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
