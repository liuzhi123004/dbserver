package org.enilu.socket.v3.client.driver;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.enilu.socket.v3.client.net.MsgQueue;
import org.enilu.socket.v3.client.util.SequenceUtil;
import org.enilu.socket.v3.commons.model.MsgHeader;
import org.enilu.socket.v3.commons.model.MsgReplay;
import org.enilu.socket.v3.commons.model.MsgSender;
import org.enilu.socket.v3.commons.util.Constants;

/**
 * 操作对象
 * 
 * @author enilu
 * 
 */
public class StatementImpl implements Statement {
	private static Logger logger = Logger.getLogger(StatementImpl.class
			.getName());

	private StatementImpl() {
		logger.setLevel(Constants.log_level);
	}

	public static StatementImpl getInstacne() {
		logger.setLevel(Constants.log_level);
		return new StatementImpl();
	}

	@Override
	public int execute(String sql) {
		int op_code = MsgHeader.OP_INSERT;
		String header = "";
		if (sql.startsWith("insert")) {
			header = "insert";
			op_code = MsgHeader.OP_INSERT;
		} else if (sql.startsWith("delete")) {
			header = "delete";
			op_code = MsgHeader.OP_DELETE;
		} else if (sql.startsWith("update")) {
			header = "update";
			op_code = MsgHeader.OP_UPDATE;
		}
		this.addMsg(sql.replace(header, "").trim(), op_code);
		try {
			return this.getResult().getReturnCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	private void addMsg(String sql, int OP_CODE) {
		MsgSender msgSender = new MsgSender();
		MsgHeader header = new MsgHeader();
		header.setSequence(SequenceUtil.getInstance().getSequence());
		header.setOpCode(OP_CODE);
		msgSender.setHeader(header);
		msgSender.setData(sql);
		MsgQueue.getInstance().add(msgSender);
	}

	@Override
	public List query(String sql) {
		this.addMsg(sql.replace("query", "").trim(), MsgHeader.OP_QUERY);
		List list = new ArrayList();
		try {
			list.add(this.getResult().getData());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	private MsgReplay getResult() throws Exception {
		int count = 0;
		MsgReplay result = null;
		while (count < 200) {
			result = MsgQueue.getInstance().getReplay();
			if (result != null) {
				break;
			}
			count++;
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (count == 200) {
			throw new Exception("connect time out");
		}
		return result;

	}

}
