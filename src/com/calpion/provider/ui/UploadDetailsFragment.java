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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.calpion.provider.R;
import com.calpion.provider.model.JsonParser;
import com.calpion.provider.model.PatientBean;
import android.app.Activity;
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

public class UploadDetailsFragment extends Fragment {
	private ArrayList<HashMap<String, String>> mPatients, orgPatientList;
	private ArrayList<HashMap<String, String>> mPatientsFilter;
	public static HashMap mMap, mapBackup;
	private Button btnSearch;
	public SimpleAdapter adapter;
	public EditText editsearch;
	String url = "http://202.83.17.167:8090/api/Upload/GetAllUploads";
	private ProgressDialog PD;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.patient_detail_fragment,
				container, false);

		mPatients = new ArrayList<HashMap<String, String>>();
		orgPatientList = new ArrayList<HashMap<String, String>>();
		final ListView lv = (ListView) view.findViewById(R.id.list);
		btnSearch = (Button) view.findViewById(R.id.btn_search);
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		adapter = new SimpleAdapter(getActivity(), mPatients,
				R.layout.list_item, new String[] { "id", "file_name",
						"file_size", "uploaded_by", "upload_date", "action" },
				new int[] { R.id.txt1, R.id.txt2, R.id.txt3, R.id.txt4,
						R.id.txt5, R.id.txt6 });
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mMap = mPatients.get(position);

			}
		});

		// Locate the EditText in listview_main.xml
		editsearch = (EditText) view.findViewById(R.id.etsearch);
		editsearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				performSearch();
				/*
				 * String text = editsearch.getText().toString()
				 * .toLowerCase(Locale.getDefault());
				 * 
				 * mPatients.clear(); ArrayList<HashMap<String, String>>
				 * mListPatient = new ArrayList<HashMap<String, String>>(); for
				 * (HashMap<String, String> m : orgPatientList) { if
				 * (m.get("upload_date").contains(
				 * editsearch.getText().toString())) { mPatients.add(m); } }
				 * 
				 * if (editsearch.getText().toString().trim().length() == 0)
				 * mPatients.addAll(orgPatientList); else
				 * mPatients.addAll(mListPatient);
				 * adapter.notifyDataSetChanged();
				 */

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

				// filter(editsearch.getText().toString());

			}
		});

		// new HttpGetTask().execute(url);
		PD = new ProgressDialog(getActivity());
		PD.setMessage("Loading.....");
		PD.setCancelable(false);
		MakeJsonArrayReq();

		return view;
	}

	private void MakeJsonArrayReq() {
		PD.show();

		JsonArrayRequest jreq = new JsonArrayRequest(url,
				new Response.Listener<JSONArray>() {

					@Override
					public void onResponse(JSONArray response) {

						for (int i = 0; i < response.length(); i++) {
							try {
								JSONObject jo = response.getJSONObject(i);
								String name = jo.getString("name");

								HashMap<String, String> map = new HashMap<String, String>();

								map.put("id", "1");
								map.put("file_name", jo.getString("id"));
								map.put("file_size", "8KB");
								map.put("uploaded_by", "Test");
								map.put("upload_date", "8/18/2014");
								map.put("action", "test action");
								mPatients.add(map);

								orgPatientList.addAll(mPatients);

							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

						PD.dismiss();
						adapter.notifyDataSetChanged();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
					}
				});

		MyApplication.getInstance().addToReqQueue(jreq, "jreq");
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
			if (m.get("upload_date").contains(editsearch.getText().toString())) {
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

		/*
		 * for (int i = 0; i < 5; i++) {
		 * 
		 * map.put("id", "1"); map.put("file_name", "abc"); map.put("file_size",
		 * "8KB"); map.put("uploaded_by", "Test"); map.put("upload_date",
		 * "8/18/2014"); map.put("action", "test action"); mPatients.add(map); }
		 */
		HashMap<String, String> map1 = new HashMap<String, String>();
		map1.put("id", "1");
		map1.put("file_name", "abc");
		map1.put("file_size", "8KB");
		map1.put("uploaded_by", "Test");
		map1.put("upload_date", "8/17/2014");
		map1.put("action", "test action");
		mPatients.add(map1);

		orgPatientList.addAll(mPatients);

	}

	public void setText(String item) {
	}

}
