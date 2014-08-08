package org.enilu.socket.v3.server.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.enilu.socket.v3.commons.util.Constants;

/**
 * 线程池
 * 
 * @author enilu
 * 
 */
public class ThreadPool {

	private static Logger logger = Logger.getLogger(ThreadPool.class.getName());
	private static ThreadPool instance;
	private static List<WorkerThread> idleThreads = new ArrayList<WorkerThread>();// 空闲线程队列
	private static List<WorkerThread> busyThreads = new ArrayList<WorkerThread>();// 工作状态线程队列
	private static int threadNum = 10;

	private ThreadPool() {
		logger.setLevel(Constants.log_level);
	}

	/**
	 * 根据制定的线程数量构造线程池
	 * 
	 * @param threadNum
	 * @return
	 */
	public static ThreadPool getThreadPool(int num) {
		if (instance == null) {
			logger.log(Level.INFO, "create " + num
					+ " threads add to threadPool");
			instance = new ThreadPool();
			threadNum = num;
			for (int i = 0; i < num; i++) {
				idleThreads.add(new WorkerThread(i));
			}

		}
		int xiangchaNum = num > threadNum ? (num - threadNum) : 0;
		for (int i = 0; i < xiangchaNum; i++) {
			idleThreads.add(new WorkerThread(i));
		}
		return instance;

	}

	/**
	 * 使用默认的线程数目构造线程池
	 * 
	 * @return
	 */
	public static ThreadPool getTheadPool() {
		return getThreadPool(50);// 默认生成50个线程
	}

	/**
	 * 返还使用的thread
	 * 
	 * @param thread
	 */
	public void returnThread(WorkerThread thread) {
		logger.log(Level.INFO, "return thread to idleThreads");
		logger.log(Level.INFO,
				"the size of idleThreads is:" + idleThreads.size()
						+ " size of busyThreads is:" + busyThreads.size());
		idleThreads.add(thread);// 返还到空闲进程列表中
		busyThreads.remove(thread);// 从繁忙进程列表中移除当前线程
		logger.log(Level.INFO,
				"the size of idleThreads is:" + idleThreads.size()
						+ " size of busyThreads is:" + busyThreads.size());
	}

	public WorkerThread get() {
		WorkerThread thread = idleThreads.remove(0);
		if (thread != null) {
			busyThreads.add(thread);
		}
		return thread;
	}
}
