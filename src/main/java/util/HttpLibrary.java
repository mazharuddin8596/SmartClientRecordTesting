package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

public class HttpLibrary {
	static StringBuilder sb = null;

	public static StringBuilder doGET(String recordtype, long id) throws IOException
	{
		String request = "https://rest.na1.netsuite.com/app/site/hosting/restlet.nl?script=2168&deploy=1&recordType="
				+ recordtype + "&recordId=" + id;
		String Token = "NLAuth nlauth_account=TSTDRV1069573, nlauth_email=mazharuddin.md@celigo.com, nlauth_signature=\"idontknow@1\", nlauth_role=3";

		URL obj = new URL(request);

		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setDoOutput(true);
		con.setRequestMethod("GET");

		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", Token);

		int responseCode = con.getResponseCode();
		System.out.println("response code" + responseCode);
		System.out.println("error stream \n\n " + con.getErrorStream());
		con.getInputStream();

		System.out.println("\nSending 'GET' request to URL : " + request);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuilder response = new StringBuilder();
		System.out.println("Response message" + con.getResponseMessage());
		System.out.println(con.getErrorStream());
		while ((inputLine = in.readLine()) != null)
		{
			response.append(inputLine);
		}
		in.close();
		con.disconnect();

		return response;
	}

	public static StringBuilder doPOST(String URL, String TestDataProperty, String Token) throws Exception
	{
		HttpClient httpClient = HttpClientBuilder.create().build();
		StringBuilder sb = new StringBuilder();

		HttpPost httpRequest = new HttpPost(URL);
		httpRequest.setHeader("Content-Type", "application/xml");
		StringEntity xmlEntity = new StringEntity(TestDataProperty);
		httpRequest.setEntity(xmlEntity);
		HttpResponse httpresponse = httpClient.execute(httpRequest);

		return sb;
	}

	public static void restDelete(AccessToken accessToken, String sheet) throws Exception
	{
		for (int i = 0; i < 3; i++)
		{
			try
			{
				HttpClient httpClient = HttpClientBuilder.create().build();
				HttpDelete getRequest = new HttpDelete(
						"https://graph.microsoft.com/v1.0/me/drive/items/01JNEAOJ47H5SYYKQIWRF3NLGJZJM2GQRE/workbook/worksheets/"
								+ sheet);
				getRequest.addHeader("Content-Type", "application/json");
				getRequest.addHeader("Authorization", "Bearer " + accessToken.getAccesstoken());

				httpClient.execute(getRequest);
				break;
			} catch (java.lang.IllegalArgumentException e)
			{
				if(i==2){
				System.out.println("unable to delete sheet");}
				
				Thread.sleep(1000);
			}
		}
	}

	public String sheetName() throws IOException, Exception
	{
		String URL = "https://graph.microsoft.com/v1.0/me/drive/items/01JNEAOJ47H5SYYKQIWRF3NLGJZJM2GQRE/workbook/worksheets/";
		org.json.JSONObject json = HttpLibrary.restGet(URL, CommonLibrary.getAccessToken());
		// parsing JSON Response
		Configuration conf = Configuration.defaultConfiguration();
		Object document = conf.jsonProvider().parse(json.toString());

		String sheet = CommonLibrary.remExtraCharacters(JsonPath.read(document, "$.value..name")
				.toString());
		return sheet;
	}

	public String addSheet() throws ClientProtocolException, IOException
	{
		HttpClient httpClient = HttpClientBuilder.create().build();
		Configuration conf = Configuration.defaultConfiguration();
		HttpPost httpRequest = new HttpPost(
				"https://graph.microsoft.com/v1.0/me/drive/items/01JNEAOJ47H5SYYKQIWRF3NLGJZJM2GQRE/workbook/worksheets/add");
		httpRequest.addHeader("Content-Type", "application/json");
		httpRequest.addHeader("Authorization", "Bearer "
				+ CommonLibrary.getAccessToken().getAccesstoken());
		HttpResponse response = httpClient.execute(httpRequest);
		// System.out.println("get status line : " + response.getStatusLine());
		// System.out.println("entity response" +
		// response.getEntity().getContent());

		if (response.getStatusLine().getStatusCode() != 200)
		{
			if (response.getStatusLine().getStatusCode() != 201)
			{
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			}
		}

		StringWriter writer = new StringWriter();
		IOUtils.copy(response.getEntity().getContent(), writer, "UTF-8");
		String theString = writer.toString();
		// handle response here...
		StringBuilder sb = new StringBuilder();
		sb.append(theString);
		JSONObject jsonObject = new JSONObject(sb.toString());
		Object doc = conf.jsonProvider().parse(jsonObject.toString());
		writer.close();
		// System.out.println(doc.toString());
		// System.out.println(JsonPath.read(doc, "$.name"));
		return JsonPath.read(doc, "$.name");
	}

	public static JSONObject restGet(String URL, AccessToken accessToken) throws Exception, IOException
	{
		HttpClient httpClient = HttpClientBuilder.create().build();
		StringBuilder sb = new StringBuilder();
		JSONObject jsonObject = new JSONObject();

		if (accessToken.getExpires_on() < System.currentTimeMillis())
		{
			System.out.println("inside expire check");
			// getAccessTokenRestApi();
		}
		HttpGet getRequest = new HttpGet(URL);
		getRequest.addHeader("Content-Type", "application/json");
		getRequest.addHeader("Authorization", "Bearer " + accessToken.getAccesstoken());

		HttpResponse response = httpClient.execute(getRequest);
		// System.out.println("get status line : " + response.getStatusLine());
		// System.out.println("entity response" +
		// response.getEntity().getContent());

		if (response.getStatusLine().getStatusCode() != 200)
		{
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatusLine().getStatusCode());
		}

		StringWriter writer = new StringWriter();
		IOUtils.copy(response.getEntity().getContent(), writer, "UTF-8");
		String theString = writer.toString();
		// handle response here...
		sb.append(theString);
		jsonObject = new JSONObject(sb.toString());
		writer.close();
		return jsonObject;

	}

	public static AccessToken getAccessTokenRestApi() throws Exception
	{

		HttpClient httpClient = HttpClientBuilder.create().build();
		AccessToken at = new AccessToken();;
		StringBuilder sb = new StringBuilder();
		JSONObject access = new JSONObject();
		try
		{

			HttpPost request = new HttpPost("https://login.microsoftonline.com/common/oauth2/token");
			StringEntity params = new StringEntity(
					"grant_type=\"refresh_token\"&redirect_uri=\"http://localhost:8080\"&client_id=\"94aaab54-f9e6-4032-a61f-88c07749a346\"&client_secret=\"Xo/MQ/YUE096WKeVumC6WzJyVpuAUJzJAd5/zWozoYo=\"&refresh_token=\"AQABAAAAAADRNYRQ3dhRSrm-4K-adpCJbcH1Owq_LQU_hUOePbPmOPLp6e3C-FxGkloSyo8pad_t8dAdvr9_wJ0Xpeba9GD93Kd1KLdyw5dSNiwvlKa09lE49eQ5pEJj_IaUSh6B9llsPznlnfPJLmcrW2vtBVHrD5z-qR3k6Z2L5qxd7cz9V0V65P_6gAgQ8sp2Zpr9oYAT-O4skIlS0ATEH7yQaE3njGw8R7wlckl6qF2azOQrEe0RAFNl8F49tuK9LZt-iwheoFtO_la26my5Jp4wtAoeDmAnlBJugckGtpuVPWe5tcWeHZalp89ISIRZe9mRLcm3SKx83K5ufVl1d0y-a3USrl6XFf4asf_wUD45w8bLw9CFXzQgoh1ZOKqYyMfkphhe78YgRorb2x9xMAGXOdtmCrHGW3qHUgz1UjzWxZHnMHHouWO1m6_L4Uw-86vac3IRq1Ux7LfBJ3o7BjKpxfJDUKUi32HqSAHVt8oA05q9FqTUw9vUnIUGRgG5Zj7vMfrE3xk43v7jrsb1ZuOmkGKI3Lm9Hgn4BASu_4CaRfGqKkygjIb7nhaG1F2ITM5hDoPUVcRMJOZclxNe-ALDw01RKwmbpxZkJVwbfTeO85pbFubLc_Osa3ULTCSo5wEk4I7kh67NLPUEyScusfnVVnYBF5smMqiP3aK5aIuKoe5YASAA\"&resource=\"https://graph.microsoft.com/\" ");
			request.addHeader("content-type", "application/x-www-form-urlencoded");
			request.setEntity(params);
			HttpResponse response = httpClient.execute(request);
			// System.out.println(response.getEntity().getContent());
			StringWriter writer = new StringWriter();
			IOUtils.copy(response.getEntity().getContent(), writer, "UTF-8");
			String theString = writer.toString();

			// handle response here...
			sb.append(theString);
			access = new JSONObject(sb.toString());
			at.setAccesstoken(access.getString("access_token"));
			at.setExpires_on(access.getLong("expires_on") * 1000);
			// System.out.println("access Token : "+at.getAccesstoken());
			// System.out.println("Expires in : "+ at.getExpires_on());
			return at;

		} catch (Exception ex)
		{
			System.out.println("unable to generate Access Token");
		}

		return at;

	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> parseHeaderRowData(JSONObject jo) throws Exception
	{
		// Extracting Header values from Response
		Gson googleJson = new Gson();

		ArrayList<String> headerList = googleJson.fromJson(jo.getJSONArray("text").getJSONArray(0)
				.toString(), ArrayList.class);
		// System.out.println(headerList);

		String recType = headerList.get(0).split("\\.")[0];
		System.out.println("Record Type " + recType);
		String str = new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir")
				+ "//src//test//resources//oneworld.fields.json")));
		// JSONObject metaData = Files.readAllBytes(Paths.get());
		org.json.simple.JSONObject metaData = (org.json.simple.JSONObject) new JSONParser()
				.parse(str);
		Object document = Configuration.defaultConfiguration().jsonProvider()
				.parse(metaData.toString());
		ArrayList<String> hformat = new ArrayList<String>();
		String query = "";

		for (int i = 0; i < headerList.size(); i++)
		{
			// System.out.println("checking");
			query = "$..[?(@.id==\"" + recType + headerList.get(i) + "\")].type";

			String value = JsonPath.read(document, query).toString();
			try
			{
				value = value.substring(2, value.length() - 2);
			} catch (java.lang.StringIndexOutOfBoundsException e)
			{

			}
			hformat.add(value);
			// System.out.println(value+" : "+headerList.get(i));
		}
		ArrayList<String> hf = new ArrayList<String>();
		for (Object s : hformat)
		{
			hf.add(s.toString());
		}
		// System.out.println(hf);

		ArrayList<String> arr = new ArrayList<String>();
		for (Object s : headerList)
		{
			arr.add(s.toString().toLowerCase());
		}
		// System.out.println("arr: "+arr.size()+"hf: "+hf.size());
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (int i = 0; i < arr.size(); i++)
		{
			map.put(arr.get(i), hf.get(i));
		}
		return map;
	}

	public static void printCurrentDataValues(Map<String, String> map)
	{
		for (Map.Entry<String, String> entry : map.entrySet())
		{
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
	}

	public static Map<String, String> mapHeaderWithRowData(
			ArrayList<String> head,
			ArrayList<String> rowData)
	{
		// mapping header and row values
		Map<String, String> map = new LinkedHashMap<String, String>();

		for (int i = 0; i < head.size(); i++)
		{
			map.put(head.get(i), rowData.get(i));
		}
		return map;
	}

	public static ArrayList<String> getRowAtIndex(JSONObject rows, int i) throws Exception
	{

		System.out.println("Fetching data from Sheet");
		JSONObject temp = (JSONObject) rows.getJSONArray("value").get(i);
		Gson googleJson = new Gson();
		@SuppressWarnings("rawtypes")
		ArrayList rowValues = googleJson.fromJson(temp.getJSONArray("values").getJSONArray(0)
				.toString(), ArrayList.class);
		ArrayList<String> arr = new ArrayList<String>();

		for (int j = 0; j < rowValues.size(); j++)
		{
			String text = rowValues.get(j).toString();
			try
			{
				if (text.substring(text.length() - 2).equals(".0"))
				{
					arr.add(rowValues.get(j).toString().replace(".0", ""));
				} else
					arr.add(rowValues.get(j).toString());
				// System.out.println(rowValues.get(j).toString());
			} catch (StringIndexOutOfBoundsException e)
			{
				arr.add(rowValues.get(j).toString());
			}
		}

		return arr;
	}

	public static String getTableId(String workbookId, AccessToken accessToken) throws Exception
	{
		String URL = "https://graph.microsoft.com/v1.0/me/drive/items/" + workbookId
				+ "/workbook/tables/";
		JSONObject json = restGet(URL, accessToken);
		// parsing JSON Response
		Configuration conf = Configuration.defaultConfiguration();
		// Configuration conf1 =
		// conf.addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);
		Object document = conf.jsonProvider().parse(json.toString());

		String value = JsonPath.read(document, "$.value[0].name");
		return value;

	}

}
