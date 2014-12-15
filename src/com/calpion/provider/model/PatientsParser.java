package com.calpion.provider.model;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


import org.json.simple.JSONArray;

import org.json.simple.JSONObject;

import org.json.simple.parser.JSONParser;

import org.json.simple.parser.ParseException;

public class PatientsParser {

	ReportstBean objItem;
	List<PatientBean> listPatients;

	public List<PatientBean> getData(String response) {

		try {
			
			JSONArray array_obj = null;
			JSONParser parser_obj = null;
			parser_obj = new JSONParser();
            
			JSONArray jArrya= (JSONArray) parser_obj.parse(response);
			
			
			//JSONObject jObj= (JSONObject) parser_obj.parse(response);
			

		} catch (Exception e) {
			e.printStackTrace();
		}

		return listPatients;
	}


}
