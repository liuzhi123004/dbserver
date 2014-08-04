package org.enilu.socket.v3.client;

import java.util.List;

/**
 * 数据库操作接口
 * 
 * @author enilu
 * 
 */
public interface Statement {
	public int execute(String sql);

	public List query(String sql);
}
