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

public class HomeFragment extends Fragment {

	TextView tvfileselect;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.home_fragment, container, false);
		TextView mTitle = (TextView) view.findViewById(R.id.ltitle);
		mTitle.setSelected(true);
		return view;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onActivityCreated(savedInstanceState);
	}

}
