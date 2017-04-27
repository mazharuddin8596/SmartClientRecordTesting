package WebDriverTest;

import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;

public class Demo {

	public static void main(String[] args) throws JSONException {

		org.json.JSONObject obj = new org.json.JSONObject();
		org.json.JSONObject obj1 = new org.json.JSONObject();

		obj.put("name", "TRUE");

		obj.put("age", new Integer(100));

		org.json.JSONArray list = new org.json.JSONArray();
		list.put("msg 1 m");
		list.put("msg 2 m");
		list.put("msg 3 m");

		obj.put("messages", list);

		/*
		 * try (FileWriter file = new FileWriter("D:\\test.json")) {
		 * 
		 * file.write(obj.toJSONString()); file.flush();
		 * 
		 * } catch (IOException e) { e.printStackTrace(); }
		 */

		obj1.put("name", "true");
		obj1.put("age", new Integer(100));

		org.json.JSONArray list1 = new org.json.JSONArray();
		list1.put("msg 1 m");
		list1.put("msg 2 m");
		list1.put("msg 3 m");

		obj1.put("messages", list1);
		System.out.println(obj);
		System.out.println(obj1);

		JSONAssert.assertEquals(obj1, obj, true);

	}

}