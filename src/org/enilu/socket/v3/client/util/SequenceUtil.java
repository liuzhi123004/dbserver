package org.enilu.socket.v3.client.util;

public class SequenceUtil {
	private static int sequence;
	private static SequenceUtil instance;

	private SequenceUtil() {

	}

	public static SequenceUtil getInstance() {
		if (instance == null) {
			instance = new SequenceUtil();
		}
		return instance;
	}

	public synchronized int getSequence() {
		if (sequence == Integer.MAX_VALUE) {
			sequence = 0;
		}
		return sequence++;
	}
}
