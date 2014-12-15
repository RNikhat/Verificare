package com.calpion.provider.ui;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.calpion.provider.R;
import com.calpion.provider.model.JSONParser;
import com.calpion.provider.model.PatientBean;
import com.calpion.provider.ui.Login.HttpGetTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class PatientsFragment extends Fragment {
	private ArrayList<HashMap<String, String>> mPatients, orgPatientList;
	private ArrayList<HashMap<String, String>> mPatientsFilter;
	public static HashMap mMap, mapBackup;
	private Button btnSearch;
	Context c;
	public SimpleAdapter adapter;
	public EditText editsearch;
	String url = "http://202.83.17.167:8090/api/Patient/GetAllPatients";
	private ProgressDialog PD;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.patients_fragment, container,
				false);

		mPatients = new ArrayList<HashMap<String, String>>();
		orgPatientList = new ArrayList<HashMap<String, String>>();
		final ListView lv = (ListView) view.findViewById(R.id.list);
		btnSearch = (Button) view.findViewById(R.id.btn_search);
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		adapter = new SimpleAdapter(getActivity(), mPatients,
				R.layout.list_item, new String[] { "fname", "lname", "age",
						"phone", "email", "edate" }, new int[] { R.id.txt1,
						R.id.txt2, R.id.txt3, R.id.txt4, R.id.txt5, R.id.txt6 });
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mMap = mPatients.get(position);
				String msg = "SSN: " + mMap.get("ssn") + "\n" + "Gender: "
						+ mMap.get("gender") + "\n" + "Policy No: "
						+ mMap.get("PolicyNo") + "\n" + "Provider: "
						+ mMap.get("provider") + "\n" + "Policy Ending: "
						+ mMap.get("PolicyEnding") + "\n" + "City: "
						+ mMap.get("city") + "\n" + "State: "
						+ mMap.get("state");
				showDialog(msg, mMap.get("fname").toString());

			}
		});

		// Locate the EditText in listview_main.xml
		editsearch = (EditText) view.findViewById(R.id.etsearch);
		editsearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				performSearch();

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}
		});

		editsearch
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_SEARCH) {
							performSearch();
							return true;
						}
						return false;
					}
				});

		btnSearch.setFocusable(true);

		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});
		PD = new ProgressDialog(getActivity());
		PD.setMessage("Loading.....");
		PD.setCancelable(true);

		HttpGetTask mTask = new HttpGetTask();
		mTask.execute(url);

		// populateList();

		return view;
	}

	protected void performSearch() {
		String text = editsearch.getText().toString()
				.toLowerCase(Locale.getDefault());
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editsearch.getWindowToken(), 0);

		mPatients.clear();
		ArrayList<HashMap<String, String>> mListPatient = new ArrayList<HashMap<String, String>>();
		for (HashMap<String, String> m : orgPatientList) {
			if (m.get("fname").contains(editsearch.getText().toString())) {
				mPatients.add(m);
			}
		}

		if (editsearch.getText().toString().trim().length() == 0)
			mPatients.addAll(orgPatientList);
		else
			mPatients.addAll(mListPatient);
		adapter.notifyDataSetChanged();

	}

	public void populateList(JSONArray result) {

		HashMap<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < result.length(); i++) {
			JSONObject jObj;
			try {
				jObj = result.getJSONObject(i);

				HashMap<String, String> map1 = new HashMap<String, String>();
				map1.put("fname", jObj.getString("fname"));
				map1.put("lname", jObj.getString("lname"));
				map1.put("age", jObj.getString("age"));
				map1.put("phone", jObj.getString("phone"));
				map1.put("email", jObj.getString("email"));
				map1.put("edate", jObj.getString("effectivedt"));
				map1.put("gender", jObj.getString("gender"));
				map1.put("addressline1", jObj.getString("addressline1"));
				map1.put("addressline2", jObj.getString("addressline2"));
				map1.put("city", jObj.getString("city"));
				map1.put("state", jObj.getString("state"));
				map1.put("provider", jObj.getString("provider"));
				map1.put("ssn", jObj.getString("ssn"));
				map1.put("policyid", jObj.getString("policyid"));
				map1.put("PolicyNo", jObj.getString("PolicyNo"));
				map1.put("insurancecompany", jObj.getString("insurancecompany"));
				map1.put("physician", jObj.getString("physician"));

				mPatients.add(map1);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		orgPatientList.addAll(mPatients);
		adapter.notifyDataSetChanged();

	}

	public class HttpGetTask extends AsyncTask<String, String, JSONArray> {
		String mResult;

		// private ProgressDialog dialog;// = new
		// ProgressDialog(getBaseContext());

		protected void onPreExecute() {
			// dialog = new ProgressDialog(c);
			PD.setMessage("Processing");
			PD.setCancelable(false);
			PD.setCanceledOnTouchOutside(false);
			PD.show();
		}

		protected void onPostExecute(JSONArray result) {
			if (PD.isShowing()) {
				PD.dismiss();
			}
			if (result != null) {
				populateList(result);
			}

		}

		@Override
		protected JSONArray doInBackground(String... args) {
			JSONParser jParser = new JSONParser();
			JSONArray json;
			try {
				json = jParser.getJSONFromUrl(url);
				if (json != null)
					return json;
				// for (int i = 0; i < json.length(); i++) {

				// }

			} catch (Exception e) {
				Log.e("Error: ", " " + e.getMessage());
				return null;
			}
			return json;
		}
	}

	public void setText(String item) {
	}

	private void showDialog(String string, String fname) {
		AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
		ab.setTitle(fname + "'s " + "details");
		ab.setMessage(string);
		ab.setIcon(android.R.drawable.ic_dialog_alert);
		ab.setNeutralButton("OK", null);
		ab.show();

	}

}
