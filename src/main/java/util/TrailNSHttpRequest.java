package util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

public class TrailNSHttpRequest {

	public static StringBuilder post(String URL,String TestDataProperty) throws Exception{
		HttpClient httpClient = HttpClientBuilder.create().build();
		String Token = "NLAuth nlauth_account=TSTDRV1069573, nlauth_email=mazharuddin.md@celigo.com, nlauth_signature=\"idontknow@1\", nlauth_role=3";
		StringBuilder sb = new StringBuilder();
		HttpPost httpRequest = new HttpPost(URL);
		httpRequest.setHeader("Content-Type", "application/json");
		httpRequest.setHeader("Authorization",Token);
		StringEntity Entity = new StringEntity(TestDataProperty);
		httpRequest.setEntity(Entity );
		HttpResponse httpresponse = httpClient.execute(httpRequest);
		
		
		
		return sb;
		
	}
	
	public static void main(String[] args) throws Exception
	{
		String Url = "https://rest.na1.netsuite.com/app/site/hosting/restlet.nl?script=2168&deploy=1&recordType=contact&recordId=55493";
		String body = "{\"recordtype\":\"customer\",\"entityid\":\"John Doe\",\"companyname\":\"ABCTools Inc\",\"subsidiary\":\"1\",\"email\":\"jdoe@email.com\"}";	
		post(Url, body);

	}

}
