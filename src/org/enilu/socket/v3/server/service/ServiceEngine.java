package org.enilu.socket.v3.server.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.enilu.socket.v3.server.threadpool.ThreadPool;
import org.enilu.socket.v3.server.threadpool.Worker;
import org.enilu.socket.v3.server.threadpool.WorkerQueue;
import org.enilu.socket.v3.server.threadpool.WorkerThread;

/**
 * 后台服务类，从worker队列中获取任务，并从线程池中获取线程进行处理
 * <p>
 * 
 * @author enilu(82552623@qq.com)
 * 
 */
public class ServiceEngine {
	private static Logger logger = Logger.getLogger(ServiceEngine.class
			.getName());
	static ServiceEngine instance;
	static ThreadPool threadPool;

	private ServiceEngine() {

	}

	public static ServiceEngine bootstrap() {
		if (instance == null) {
			instance = new ServiceEngine();

			logger.log(Level.INFO, "bootstrap serviceEngine");
			threadPool = ThreadPool.getThreadPool(50);
			Task task = new Task();
			new Thread(task).start();

		}
		return instance;
	}

	/**
	 * 持续工作的线程，不停的从任务队列中获取任务，并从idle线程李表中获取空闲线程，使用idle线程来跑任务
	 * 
	 * @author enilu
	 * 
	 */
	static class Task implements Runnable {
		public Task() {
			super();
		}

		@Override
		public void run() {

			while (true) {
				logger.log(Level.INFO, "begin get worker");
				Worker worker = WorkerQueue.getInstance().take();
				WorkerThread thread = threadPool.get();
				logger.log(Level.INFO, "geted worker");
				thread.setWorker(worker);
				thread.start();
			}

		}

	}

}
