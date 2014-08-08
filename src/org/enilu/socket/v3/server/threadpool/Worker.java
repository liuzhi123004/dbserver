package org.enilu.socket.v3.server.threadpool;

/**
 * 标准的工作类，使用线程池的worker必须集成该类
 * 
 * @author enilu
 * 
 */
public abstract class Worker {
	public abstract void work();
}
