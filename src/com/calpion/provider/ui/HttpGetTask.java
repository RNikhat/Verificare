package com.calpion.provider.ui;

import org.json.JSONArray;
import org.json.JSONObject;

import com.calpion.provider.model.JSONParser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

public class HttpGetTask extends AsyncTask<String, String, JSONArray> {
	String mResult;
	ProgressDialog dialog = new ProgressDialog(null);
	private UploadDetailsFragment uFragment;

	
	protected void onPreExecute() {
		dialog.setMessage("Processing");
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	protected void onPostExecute(JSONArray result) {
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
		
		
		//uFragment.populateList(result);
	}

	@Override
	protected JSONArray doInBackground(String... args) {
		org.json.JSONArray json = null;

		try {
			JSONParser jParser = new JSONParser();
		   json = jParser.getJSONFromUrl(args[0] );
			 
           
         } catch (Exception e) {
             Log.e("Error: ", e.getMessage());
         }
         return json;
     }

	@Override
	protected void onProgressUpdate(String... values) {
	
	}

}


