package com.calpion.provider.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.calpion.provider.R;
import com.calpion.provider.adapter.MultiChoiceListAdapter;
import com.calpion.provider.model.JsonParser;
import com.calpion.provider.model.PatientBean;
import com.calpion.provider.ui.UploadDetailsFragment.HttpGetTask;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ReportListFragment extends Fragment implements OnClickListener {
	public List<PatientBean> items = new ArrayList<PatientBean>();
	private MultiChoiceListAdapter objAdapter = null;
	String url = "http://202.83.17.167:8090/api/Upload/GetAllUploads";
	private ProgressDialog PD;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.report_list_fragment, container,
				false);

		// mPatients = new ArrayList<HashMap<String, String>>();
		final ListView lv = (ListView) view.findViewById(R.id.list);
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		// lv.setOnItemClickListener(listener);
		// items = new com.calpion.provider.ui.model.NamesParser().getData(url);
		
		PD = new ProgressDialog(getActivity());
		PD.setMessage("Loading.....");
		PD.setCancelable(true);
		HttpGetTask mTask = new HttpGetTask();
		mTask.execute(url);

		// =========================
		objAdapter = new MultiChoiceListAdapter(getActivity(), items);

		lv.setAdapter(objAdapter);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				CheckBox chk = (CheckBox) view.findViewById(R.id.checkbox);
				PatientBean bean = items.get(position);
				if (bean.isSelected()) {
					bean.setSelected(false);
					chk.setChecked(false);
				} else {
					bean.setSelected(true);
					chk.setChecked(true);
				}

			}
		});

		final EditText editsearch = (EditText) view.findViewById(R.id.etsearch);
		editsearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String text = editsearch.getText().toString()
						.toLowerCase(Locale.getDefault());
				lv.setFilterText(text);
				objAdapter.filter(text);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// objAdapter.notifyDataSetChanged();
			}
		});

		return view;
	}

	@Override
	public void onClick(View v) {
		StringBuffer sb = new StringBuffer();

		// Retrive Data from list
		for (PatientBean bean : items) {

			if (bean.isSelected()) {
				sb.append(bean.getPatientName());
				sb.append(",");
			}
		}

	}

	public class HttpGetTask extends AsyncTask<String, String, Boolean> {
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

		protected void onPostExecute(Boolean result) {
			if (PD.isShowing()) {
				PD.dismiss();
			}
			populateList();

		}

		private void populateList() {
			for (int i = 0; i < 10; i++) {

				PatientBean pb = new PatientBean();
				pb.setAptDate("8/24/2014");
				pb.setInsurance("Insurance" + i);
				pb.setPriority("" + i);
				pb.setPatientName("Patient" + i);
				pb.setProvider("Provider" + i);
				pb.setReportStatus("Completed");
				items.add(i, pb);
			}
			// ==============to be removed
			PatientBean pb = new PatientBean();
			pb.setAptDate("8/25/2014");
			pb.setInsurance("Insurance");
			pb.setPriority("");
			pb.setPatientName("Patient");
			pb.setProvider("Provider");
			pb.setReportStatus("Completed");
			items.add(pb);

			pb.setAptDate("8/55/2014");
			items.add(pb);
			pb.setAptDate("8/66/2014");
			items.add(pb);
			objAdapter.notifyDataSetChanged();

		}

		@Override
		protected Boolean doInBackground(String... args) {
			String json = null;

			try {
				JsonParser jParser = new JsonParser();
				//
				// Getting JSON from URL
				json = jParser.httpGet(args[0]);
				if (json.contains("success"))
					return true;

			} catch (Exception e) {
				Log.e("Error: ", " "+e.getMessage());
			}
			return false;
		}
	}

	public void setText(String item) {
	}

}
