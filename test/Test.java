import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class Test {

	public static void main(String[] args) throws JsonGenerationException,
			JsonMappingException, IOException {
		testSplit();
	}

	private static void testReg() {
		Pattern p = Pattern.compile("[a-z]+[ ]+\\{[a-z]+:.+\\}");
		Matcher m = p.matcher("query {id:1}");
		boolean b = m.matches();
		System.out.println(b);
	}

	private static void testSplit() {
		String str = "关键词1";
		String[] words = str.split("\\s+");
		for (String word : words) {
			System.out.println(word);
		}
	}

}
