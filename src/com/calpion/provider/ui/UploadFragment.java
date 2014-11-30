package com.calpion.provider.ui;

import java.io.File;
import java.net.URISyntaxException;

import com.calpion.provider.R;

import android.app.Fragment;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UploadFragment extends Fragment implements
		OnClickListener {
	Button browse, upload;
	int PICK_REQUEST_CODE = 0;
	TextView tvfileselect;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.upload, container, false);
		return view;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onActivityCreated(savedInstanceState);
		browse = (Button) getActivity().findViewById(R.id.btnchoosefile);
		tvfileselect = (TextView) getActivity().findViewById(
				R.id.tvfileselected);
		upload = (Button) getActivity().findViewById(R.id.btnupload);
		browse.setOnClickListener(this);
		upload.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnchoosefile:

			/*
			 * Intent i = new Intent(); i.setAction(Intent.ACTION_GET_CONTENT);
			 * i.setType("* / *"); Uri startDir = Uri.fromFile(new
			 * File("/sdcard")); startActivityForResult(i, PICK_REQUEST_CODE);
			 */

			Intent intent = new Intent(getActivity(), FilePicker.class);
			startActivityForResult(intent, 0);

			break;
		case R.id.btnupload:
			break;
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			if (resultCode == getActivity().RESULT_OK && requestCode == 0) {
				Uri uri = data.getData();
				Log.i("FILE", "File Uri: " + uri.toString());
				// String path = getRealPathFromURI(uri);
				String path = data.getStringExtra("file_path");
				Log.i("FILE", "File Path: " + path);

				if (path == null || !path.contains("/")) {
					Toast.makeText(getActivity().getApplicationContext(),
							"Chose a different file", Toast.LENGTH_LONG).show();
				} else /*if (!path.split("/")[path.split("/").length - 1]
						.contains(".")) {
					Toast.makeText(
							getActivity().getApplicationContext(),
							"You cannot select a file without any file type. Try Again...",
							Toast.LENGTH_LONG).show();
				} else if (!path.endsWith(".txt") && !path.endsWith(".pdf")
						&& !path.endsWith(".doc") && !path.endsWith(".docx")
						&& !path.endsWith(".xls") && !path.endsWith(".xlsx")
						&& !path.endsWith(".jpg")) {
					if (path.length() > 4)
						Toast.makeText(
								getActivity().getApplicationContext(),
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
						Toast.makeText(getActivity().getApplicationContext(),
								"You cannot select this type of file",
								Toast.LENGTH_LONG).show();
				} else*/ {

					Log.d("the selected file", path);
					String fileName = path.split("/")[path.split("/").length-1];
					tvfileselect.setText(fileName);
				}
			}
		} catch (Exception e) {
			Log.d("HP", "" + e.getMessage());
		}

	}

	private String getStringNameFromRealPath(final String bucketName) {
		return bucketName.lastIndexOf('/') > 0 ? bucketName
				.substring(bucketName.lastIndexOf('/') + 1) : bucketName;
	}

	public String getRealPathFromURI(Context context, Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		CursorLoader loader = new CursorLoader(context, contentUri, proj, null,
				null, null);
		Cursor cursor = loader.loadInBackground();
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	public String getImagePath(Uri uri) {
		Cursor cursor = getActivity().getContentResolver().query(uri, null,
				null, null, null);
		cursor.moveToFirst();
		String document_id = cursor.getString(0);
		document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
		cursor.close();

		cursor = getActivity().getContentResolver().query(
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				null, MediaStore.Images.Media._ID + " = ? ",
				new String[] { document_id }, null);
		cursor.moveToFirst();
		String path = cursor.getString(cursor
				.getColumnIndex(MediaStore.Images.Media.DATA));
		cursor.close();

		return path;
	}

}
