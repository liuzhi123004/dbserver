package org.enilu.socket.v3.commons.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.undercouch.bson4jackson.BsonFactory;
import de.undercouch.bson4jackson.BsonModule;

public class ByteUtil {
	public static byte[] intToByteArray(int i) {
		byte[] result = new byte[4];
		result[0] = (byte) ((i >> 24) & 0xFF);
		result[1] = (byte) ((i >> 16) & 0xFF);
		result[2] = (byte) ((i >> 8) & 0xFF);
		result[3] = (byte) (i & 0xFF);
		return result;
	}

	public static int byteToInt(byte[] b) {
		return ((b[0] & 0x000000ff) << 24) | ((b[1] & 0x000000ff) << 16)
				| ((b[2] & 0x000000ff) << 8) | (b[3] & 0x000000ff);
	}

	public static int hBytesToInt(byte[] b) {
		int s = 0;
		for (int i = 0; i < 3; i++) {
			if (b[i] >= 0) {
				s = s + b[i];
			} else {
				s = s + 256 + b[i];
			}
			s = s * 256;
		}
		if (b[3] >= 0) {
			s = s + b[3];
		} else {
			s = s + 256 + b[3];
		}
		return s;
	}

	public static int lBytesToInt(byte[] b) {
		int s = 0;
		for (int i = 0; i < 3; i++) {
			if (b[3 - i] >= 0) {
				s = s + b[3 - i];
			} else {
				s = s + 256 + b[3 - i];
			}
			s = s * 256;
		}
		if (b[0] >= 0) {
			s = s + b[0];
		} else {
			s = s + 256 + b[0];
		}
		return s;
	}

	// public static String bsonByteTOString(byte[] b) throws IOException {
	//
	// ByteArrayInputStream bais = new ByteArrayInputStream(b);
	// BSONDecoder decoder = new BasicBSONDecoder();
	// BSONObject obj = decoder.readObject(bais);
	// byte[] o = (byte[]) obj.get("data");
	// CharBuffer buf = ByteBuffer.wrap(o).order(ByteOrder.LITTLE_ENDIAN)
	// .asCharBuffer();
	// return buf.toString();
	//
	// }
	//
	// public static String bsonByteTOMap(byte[] b) throws IOException {
	//
	// ByteArrayInputStream bais = new ByteArrayInputStream(b);
	// BSONDecoder decoder = new BasicBSONDecoder();
	// BSONObject obj = decoder.readObject(bais);
	// byte[] o = (byte[]) obj.get("data");
	// CharBuffer buf = ByteBuffer.wrap(o).order(ByteOrder.LITTLE_ENDIAN)
	// .asCharBuffer();
	// return buf.toString();
	//
	// }

	// public static byte[] StringTOBsonByte(String data)
	// throws JsonGenerationException, JsonMappingException, IOException {
	// ByteArrayOutputStream baos = new ByteArrayOutputStream();
	// BsonGenerator gen = new BsonGenerator(
	// JsonGenerator.Feature.collectDefaults(), 0, baos);
	// gen.writeStartObject();
	// gen.writeFieldName("data");
	// gen.writeRaw(data);
	// gen.writeEndObject();
	// gen.close();
	// return baos.toByteArray();
	// }
	/**
	 * 将map转换为bson数组
	 * 
	 * @param data
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static byte[] MapTOBsonByte(Map data)
			throws JsonGenerationException, JsonMappingException, IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BsonFactory bsonFactory = new BsonFactory();
		ObjectMapper om = new ObjectMapper(bsonFactory);
		om.registerModule(new BsonModule());
		om.writeValue(baos, data);

		return baos.toByteArray();
	}

	public static byte[] JsonStringTOBsonByte(String data)
			throws JsonGenerationException, JsonMappingException, IOException {
		JSONArray jsonArray = null;
		if (data.startsWith("{")) {
			data = "[" + data + "]";

			jsonArray = new JSONArray().fromObject(data);
		} else if (data.startsWith("[")) {
			jsonArray = new JSONArray().fromObject(data);
		}
		Map map = Json2Map(jsonArray.getJSONObject(0));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BsonFactory bsonFactory = new BsonFactory();
		ObjectMapper om = new ObjectMapper(bsonFactory);
		om.registerModule(new BsonModule());
		om.writeValue(baos, map);

		return baos.toByteArray();
	}

	private static Map Json2Map(JSONObject json) {
		Iterator iter = json.keys();
		Map map = new HashMap();
		while (iter.hasNext()) {
			String key = iter.next().toString();
			map.put(key, json.get(key));
		}
		return map;
	}

	// 将bson数组转换微字符串
	public static String byteToJsonString(byte[] b) throws IOException {

		ByteArrayInputStream bais = new ByteArrayInputStream(b);
		BsonFactory fac = new BsonFactory();
		ObjectMapper mapper = new ObjectMapper(fac);

		fac.setCodec(mapper);
		Map map = mapper.readValue(bais, Map.class);
		JSONArray json = JSONArray.fromObject(map);

		return json.toString();
	}
}
