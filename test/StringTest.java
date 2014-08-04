public class StringTest {

	public static void main(String[] args) {
		byte[] bytes = { 123, 105, 100, 58, 49, 44, 110, 97, 109, 101, 58, 39,
				116, 101, 115, 116, 39, 44, 115, 101, 120, 58, 39, 109, 97,
				108, 101, 39, 125 };
		System.out.println(new String(bytes));

		System.out.println("张三".getBytes().length);
	}
}
