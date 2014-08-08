package org.enilu.socket.v3.server.threadpool;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.enilu.socket.v3.commons.util.Constants;

/**
 * 自定义的线程类，用作线程池李的标准线程对象
 * 
 * @author enilu
 * 
 */
public class WorkerThread extends Thread {
	private static Logger logger = Logger.getLogger(WorkerThread.class
			.getName());
	public static final int IDLE = 0;
	public static final int BUSY = 1;
	private int id;// 线程id
	private int status;// 0:idle,1:busy

	public WorkerThread(int id) {
		super();
		this.id = id;
		status = 0;
		logger.setLevel(Constants.log_level);
	}

	private Worker worker;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Worker getWorker() {
		return worker;
	}

	public void setWorker(Worker worker) {
		this.worker = worker;
	}

	@Override
	public void run() {
		while (true) {
			if (this.status == WorkerThread.IDLE && this.worker != null) {
				this.status = WorkerThread.BUSY;
				logger.log(Level.INFO, "Thread " + id + "start work");
				this.worker.work();
				this.status = WorkerThread.IDLE;
				this.worker = null;
				ThreadPool.getTheadPool().returnThread();
			} else {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
