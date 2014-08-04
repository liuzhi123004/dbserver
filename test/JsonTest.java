import net.sf.json.JSONArray;

public class JsonTest {

	public static void main(String[] args) {
		JSONArray json = new JSONArray().fromObject("[{id:1,name:234}]");
		System.out.println(json.getJSONObject(0).get("id"));
	}

}
