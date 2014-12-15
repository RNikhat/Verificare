package com.calpion.provider.ui;

import org.json.JSONArray;

import com.calpion.provider.R;
import com.calpion.provider.model.JSONParser;

import com.calpion.provider.model.ReportstBean;
import com.calpion.provider.ui.ReportListFragment.HttpGetTask;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class NewPatientFragment extends Fragment implements OnClickListener {
	EditText etfirst_name, etlast_name, etxage, etpolicy_id, eteffective_date,
			etxpriority, etxssno, etpinfo_phone, etemail, etemployer,
			etoffice_phone, etxphysician, etxnetwork, etxcity, etxstate,
			etxpayorid, etxinscompny, etinsrep, etpolicyno, etxgroupno,
			etinsphone;
	ImageButton bSave;
	TextView tvdob, tvappntmnt, tvpolicyeff, tvpolicyend;
	private ProgressDialog PD;
	String url = "http://202.83.17.167:8090/api/Upload/GetAllUploads";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		// View view= inflater.inflate(R.layout.new_patient, container, false);
		View view = inflater.inflate(R.layout.patient_new, container, false);
		PD = new ProgressDialog(getActivity());
		PD.setMessage("Loading.....");
		PD.setCancelable(true);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		etfirst_name = (EditText) getActivity().findViewById(R.id.etfirstname);
		etlast_name = (EditText) getActivity().findViewById(R.id.etlastname);
		etxage = (EditText) getActivity().findViewById(R.id.etage);
		tvdob = (TextView) getActivity().findViewById(R.id.etdob);
		tvdob.setClickable(true);
		tvdob.setOnClickListener(this);
		etxssno = (EditText) getActivity().findViewById(R.id.etssno);
		etemail = (EditText) getActivity().findViewById(R.id.etemail);
		etinsphone = (EditText) getActivity().findViewById(R.id.etiphone);
		bSave = (ImageButton) getActivity().findViewById(R.id.bsave);
		bSave.setOnClickListener(this);
		/*
		 * etpolicy_id=(EditText)getActivity().findViewById(R.id.etpolicyid);
		 * eteffective_date
		 * =(EditText)getActivity().findViewById(R.id.eteffectivedate);
		 * etxpriority=(EditText)getActivity().findViewById(R.id.etpriority);
		 * etxssno=(EditText)getActivity().findViewById(R.id.etssno);
		 * etpinfo_phone=(EditText)getActivity().findViewById(R.id.etphone);
		 * etemail=(EditText)getActivity().findViewById(R.id.etemail);
		 * etemployer=(EditText)getActivity().findViewById(R.id.etemployer);
		 * etoffice_phone
		 * =(EditText)getActivity().findViewById(R.id.etofficephone);
		 * etxphysician=(EditText)getActivity().findViewById(R.id.etphysician);
		 * etxnetwork=(EditText)getActivity().findViewById(R.id.etnetwork);
		 * etxcity=(EditText)getActivity().findViewById(R.id.etcity);
		 * etxstate=(EditText)getActivity().findViewById(R.id.etstate);
		 * etxpayorid=(EditText)getActivity().findViewById(R.id.etpayorid);
		 * etxinscompny
		 * =(EditText)getActivity().findViewById(R.id.etinsurancecmpny);
		 * etinsrep=(EditText)getActivity().findViewById(R.id.etinsurancerep);
		 * etpolicyno=(EditText)getActivity().findViewById(R.id.etpolicyno);
		 * etxgroupno=(EditText)getActivity().findViewById(R.id.etgroupno);
		 * etinsphone=(EditText)getActivity().findViewById(R.id.etiphone);
		 * tvdob=(TextView)getActivity().findViewById(R.id.etdob);
		 * tvappntmnt=(TextView)getActivity().findViewById(R.id.etappointment);
		 * tvpolicyeff
		 * =(TextView)getActivity().findViewById(R.id.etpolicyeffective);
		 * tvpolicyend
		 * =(TextView)getActivity().findViewById(R.id.etpolicyending);
		 * tvdob.setOnClickListener(this); tvappntmnt.setOnClickListener(this);
		 * tvpolicyeff.setOnClickListener(this);
		 * tvpolicyend.setOnClickListener(this);
		 */
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.bsave) {

			if (valid()) {

				HttpGetTask mTask = new HttpGetTask();
				mTask.execute(url);
			}
		}
		if (v.getId() == R.id.tvdob) {

		}

	}

	private boolean valid() {
		// TODO Auto-generated method stub
		return false;
	}

	public class HttpGetTask extends AsyncTask<String, String, Boolean> {
		String mResult;

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
			showDialog("New Patient Added Successfuly");

		}

		private void showDialog(String string) {
			AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
			ab.setMessage(string);
			ab.show();

		}

		@Override
		protected Boolean doInBackground(String... args) {
			JSONParser jParser = new JSONParser();
			try {
				JSONArray json = jParser.getJSONFromUrl(url);
				for (int i = 0; i < json.length(); i++) {

				}

			} catch (Exception e) {
				Log.e("Error: ", " " + e.getMessage());
			}
			return false;
		}
	}

}
