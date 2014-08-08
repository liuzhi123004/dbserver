/**
 * 
 */
package org.enilu.socket.v3.commons.model;


/**
 * @author burns
 * 
 */
public class StringMsg extends Msg {
	private String text;

	public StringMsg(String msg) {
		this.text = msg;
	}

	@Override
	public byte[] getBytes() {
		return this.text.getBytes();
	}

	@Override
	public String toString() {
		return this.text;
	}

}
