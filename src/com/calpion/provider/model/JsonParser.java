package com.calpion.provider.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import org.json.simple.JSONArray;

import org.json.simple.JSONObject;

import org.json.simple.parser.JSONParser;

import org.json.simple.parser.ParseException;

public class JsonParser {
	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";
	private String response;
	JSONObject jsonObj;

	// constructor
	public JsonParser() {
	}

	public JSONArray getJSONFromUrl(String url) {

		JSONArray array_obj = null;
		JSONParser parser_obj = null;
		String response = httpGet(url);
		try {
			parser_obj = new JSONParser();

			array_obj = (JSONArray) parser_obj.parse(response);
		} catch (ParseException e) {
			
			e.printStackTrace();
		}

		return array_obj;

	}

	public String httpGet(String url) {
		HttpResponse httpResponse = null;
		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			response = EntityUtils.toString(httpEntity);

			// JSONObject jObj= (JSONObject) parser_obj.parse(response);

			// jsonObj = new JSONObject(response);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;

	}
}
