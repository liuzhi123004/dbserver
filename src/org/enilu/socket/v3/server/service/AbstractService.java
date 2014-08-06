package org.enilu.socket.v3.server.service;

import org.enilu.socket.v3.commons.model.MsgReplay;
import org.enilu.socket.v3.commons.model.MsgSender;

/**
 * 抽象的服务类，负责处理用户请求，并返回相关数据
 * <p>
 * 2014年8月6日
 * <p>
 * 
 * @author enilu(82552623@qq.com)
 * 
 */
public abstract class AbstractService {
	/**
	 * 根据传入的参数，从数据库文件系统中获取相应的数据并返回
	 * 
	 * @param msgSender
	 * @return
	 */
	public abstract MsgReplay process(MsgSender sender);
}
