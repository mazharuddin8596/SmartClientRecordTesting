package util;

import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

public class TrailNSHttpRequest {

	public static void main(String[] args) throws Exception
	{

		HttpLibrary http = new HttpLibrary();
		CommonLibrary lib = new CommonLibrary();

		AccessToken at = HttpLibrary.getAccessTokenRestApi();
		CommonLibrary.setAccessToken(at);

		String URL = "https://graph.microsoft.com/v1.0/me/drive/items/01JNEAOJ47H5SYYKQIWRF3NLGJZJM2GQRE/workbook/worksheets/";
		org.json.JSONObject json = HttpLibrary.restGet(URL, CommonLibrary.getAccessToken());
		// parsing JSON Response
		Configuration conf = Configuration.defaultConfiguration();
		Object document = conf.jsonProvider().parse(json.toString());

		String sheet = CommonLibrary.remExtraCharacters(JsonPath.read(document, "$.value..name")
				.toString());
		System.out.println(sheet);
		System.out.println(http.addSheet());
		HttpLibrary.restDelete(at,sheet);

	}

}
