package com.calpion.provider.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.calpion.provider.R;
import com.calpion.provider.adapter.MultiChoiceListAdapter;
import com.calpion.provider.model.PatientBean;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PatientListFragment extends Fragment implements OnClickListener {
	public List<PatientBean> items = new ArrayList<PatientBean>();
	private MultiChoiceListAdapter objAdapter = null;
	private ArrayList<HashMap<String, String>> mPatients, orgPatientList;
	private ArrayList<HashMap<String, String>> mPatientsFilter;
	public static HashMap mMap, mapBackup;
	private Button btnSearch;
	Context c;
	public SimpleAdapter adapter;
	public EditText editsearch;
	String url = "http://202.83.17.167:8090/api/Upload/GetAllUploads";
	private ProgressDialog PD;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.list_of_patients, container,
				false);

		mPatients = new ArrayList<HashMap<String, String>>();
		orgPatientList = new ArrayList<HashMap<String, String>>();
		final ListView lv = (ListView) view.findViewById(R.id.list);
		
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		
		adapter = new SimpleAdapter(getActivity(), mPatients,
				R.layout.list_item2, new String[] { "fname", "lname",
						"dob", "age", "phone", "email" ,"policyid,effectivedate"},
				new int[] { R.id.txt1, R.id.txt2, R.id.txt3, R.id.txt4,
						R.id.txt5, R.id.txt6 ,R.id.txt7,R.id.txt8});
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			

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

}
