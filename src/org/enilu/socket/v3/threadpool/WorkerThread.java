package org.enilu.socket.v3.threadpool;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.enilu.socket.v3.commons.Constants;

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

	public WorkerThread() {
		super();
		logger.setLevel(Constants.log_level);
	}

	private Worker worker;
	private int status;// 0:idle,1:busy

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
		this.status = WorkerThread.BUSY;
		logger.log(Level.INFO, "start work");
		this.worker.work();
		this.status = WorkerThread.IDLE;
		ThreadPool.getTheadPool().returnThread(this);

	}

}
