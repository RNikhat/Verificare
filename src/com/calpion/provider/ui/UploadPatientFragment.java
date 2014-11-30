package com.calpion.provider.ui;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import com.calpion.provider.R;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UploadPatientFragment extends Fragment implements OnClickListener {

	Button upload_btn, browse_btn;
	String path;
	TextView tvPath;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.upload_patient_fragment,
				container, false);

		// Initialize Views
		upload_btn = (Button) view.findViewById(R.id.u_btn_id);
		browse_btn = (Button) view.findViewById(R.id.btn_browse);
		tvPath = (TextView) view.findViewById(R.id.tvpath);
		upload_btn.setOnClickListener(this);
		browse_btn.setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.up_btn_id:
			

			break;
		case R.id.btn_browse:
			showFileChooser();
/*			
			Intent intent = new Intent(getActivity(),
					com.calpion.provider.ui.adapter.FilePicker.class);
			startActivityForResult(intent, 101);*/
			
			break;

		}

	}
	private static final int FILE_SELECT_CODE = 0;

	private void showFileChooser() {
	    Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
	    intent.setType("*/*"); 
	    intent.addCategory(Intent.CATEGORY_OPENABLE);

	    try {
	        startActivityForResult(
	                Intent.createChooser(intent, "Select a File to Upload"),
	                FILE_SELECT_CODE);
	    } catch (android.content.ActivityNotFoundException ex) {
	        // Potentially direct the user to the Market with a Dialog
	        Toast.makeText(getActivity(), "Please install a File Manager.", 
	                Toast.LENGTH_SHORT).show();
	    }
	}

/*	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		boolean errorOccured = false;
		try {
			if (resultCode == getActivity().RESULT_OK && requestCode == 101) {
				Uri uri = data.getData();
				Log.i("FILE", "File Uri: " + uri.toString());
				// String path = getRealPathFromURI(uri);
				path = data.getStringExtra("file_path");
				Log.i("FILE", "File Path: " + path);

				if (path == null || !path.contains("/")) {
					Toast.makeText(getActivity(), "Chose a different file",
							Toast.LENGTH_LONG).show();
				} else if (!path.split("/")[path.split("/").length - 1]
						.contains(".")) {
					Toast.makeText(
							getActivity(),
							"You cannot select a file without any file type. Try Again...",
							Toast.LENGTH_LONG).show();
				} else if (!path.endsWith(".txt") && !path.endsWith(".pdf")
						&& !path.endsWith(".doc") && !path.endsWith(".docx")
						&& !path.endsWith(".xls") && !path.endsWith(".xlsx")
						&& !path.endsWith(".jpg")) {
					if (path.length() > 4)
						Toast.makeText(
								getActivity(),
								"You cannot select a "
										+ path.split("/")[path.split("/").length - 1].subSequence(
												path.split("/")[path.split("/").length - 1]
														.lastIndexOf('.'), path
														.split("/")[path
														.split("/").length - 1]
														.length())
										+ " file. Try Again...",
								Toast.LENGTH_LONG).show();
					else
						Toast.makeText(getActivity(),
								"You cannot select this type of file",
								Toast.LENGTH_LONG).show();
				} else {
					boolean verified = true;

				}
			}
		} catch (Exception e) {
			Log.d("Upload", " " + e.getMessage());
		}

	}*/
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    switch (requestCode) {
	        case FILE_SELECT_CODE:
	        if (resultCode == getActivity().RESULT_OK) {
	            // Get the Uri of the selected file 
	            Uri uri = data.getData();
	            String path = uri.toString(); // "/mnt/sdcard/FileName.mp3"
	            		try {
							File file = new File(new URI(path));
						} catch (URISyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	            path = data.getStringExtra("file_path");
	            tvPath.setText(path);
	        }
	        break;
	    }
	    super.onActivityResult(requestCode, resultCode, data);
	}
}
