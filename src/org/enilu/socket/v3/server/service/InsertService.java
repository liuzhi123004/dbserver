package org.enilu.socket.v3.server.service;

import java.util.HashMap;
import java.util.Map;

import org.enilu.socket.v3.commons.model.MsgReplay;
import org.enilu.socket.v3.commons.model.MsgSender;

/**
 * 插入数据服务类
 * 
 * @author enilu(82552623@qq.com)
 * 
 */
public class InsertService extends AbstractService {

	@Override
	public MsgReplay process(MsgSender sender) {
		MsgReplay msgReplay = new MsgReplay();
		msgReplay.setHeader(sender.getHeader());
		msgReplay.setReturnCode(0);
		msgReplay.setNumReturn(1);
		String sendmsg = "{id:'1',insert:1}";
		Map map = new HashMap();
		map.put("id", 1);
		map.put("name", "张三");
		msgReplay.setMapdata(map);
		return msgReplay;
	}

}
