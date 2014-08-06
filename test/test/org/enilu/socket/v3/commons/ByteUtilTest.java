package test.org.enilu.socket.v3.commons;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.enilu.socket.v3.commons.util.ByteUtil;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class ByteUtilTest {

	@Test
	public void testByteToBson() {
		fail("Not yet implemented");
	}

	@Test
	public void testStringTOBsonByte() {
		String bson = null;
		try {
			Map map = new HashMap();
			map.put("id", 1);
			map.put("name", "张三");
			byte[] b = ByteUtil.MapTOBsonByte(map);
			String str = ByteUtil.byteToJsonString(b);
			System.out.println(str);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testByteToJsonString() {
		byte[] b = { 13, 0, 0, 0, 16, 105 };
		try {
			String s = ByteUtil.byteToJsonString(b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testJsonStringTOBsonByte() {
		String str = "{id:1}";
		try {
			byte[] bytes = ByteUtil.JsonStringTOBsonByte(str);
			System.out.println(bytes.length);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
