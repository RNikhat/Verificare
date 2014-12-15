package com.calpion.provider.ui;

import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import com.calpion.provider.R;
import com.calpion.provider.model.JSONParser;
import android.media.audiofx.BassBoost.Settings;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class Login extends Activity implements OnClickListener {
	Button bLogin, btnCancel, btnBack, btnNext, btnLogin;
	EditText u_name, passw;
	CheckBox cbRememberMe;
	ViewFlipper flipper;
	Context c;
	private TextView register, sis, mSite, mUserId, mClientId, mUssa, battery;
	public static String sUserName = "";
	public static String sPassword = "";
	public static String mRem = "";
	static boolean isOkay = false;
	public static String userType = "2";
	String mradius;
	public String clientId;
	public String userId;
	public static boolean isLoggedIn = false;
	public SharedPreferences mPref;
	public SharedPreferences.Editor mEditor;
	String sName;
	// String url = "http://202.83.17.167:8090/api/User/IsValidUser";
	String url = "http://202.83.17.167:8090/api/user/isvaliduser/?username=manager&password=pass";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		setContentView(R.layout.login);

		// mSite = (TextView) findViewById(R.id.site);

		/*
		 * btnBack = (Button) findViewById(R.id.back); btnNext = (Button)
		 * findViewById(R.id.next); btnLogin = (Button)
		 * findViewById(R.id.submit); btnCancel = (Button)
		 * findViewById(R.id.cancel);
		 * 
		 * btnLogin.setText("Login"); btnNext.setText("Logout");
		 * 
		 * btnNext.setVisibility(View.GONE); btnBack.setVisibility(View.GONE);
		 */

		/*
		 * Intent hangouts = new Intent(Intent.ACTION_SEND);
		 * hangouts.setPackage("com.google.android.talk");
		 * hangouts.setType("text/plain");
		 * startActivity(hangouts);//Intent.createChooser(hangouts,
		 * "Hangouts is not installed."));
		 */
		mPref = getSharedPreferences("login", MODE_PRIVATE);
		mEditor = mPref.edit();
		bLogin = (Button) findViewById(R.id.blogin);
		bLogin.setOnClickListener(this);
		u_name = (EditText) findViewById(R.id.edit_user);
		passw = (EditText) findViewById(R.id.edit_passwd);
		cbRememberMe = (CheckBox) findViewById(R.id.cbRememberMe);
		mradius = mPref.getString("radius", "");
		c = this;
		if (mradius.length() == 0) {
			mradius = "100";
		}
		String PATH = Environment.getExternalStorageDirectory() + "/healthpay/";
		File file1 = new File(PATH);

		if (!file1.exists())
			file1.mkdirs();
		File loginFile = new File(PATH, "login");
		if (loginFile.exists()) {
			try {
				String str = readFromFile(PATH + "login");
				if (str.contains("~")) {
					sUserName = str.split("~")[0];
					sPassword = str.split("~")[1];

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			sUserName = "";
			sPassword = "";
			mradius = "100";
		}

		mRem = mPref.getString("remember", "junk");
		if (sUserName != "" && sPassword != "") {
			if (mRem.equalsIgnoreCase("yes")) {
				u_name.setText(sUserName);
				passw.setText(sPassword);
			} else {
				u_name.setText("");
				passw.setText("");
				mradius = "100";
			}
		} else {
			bLogin.setText("Register");
			cbRememberMe.setVisibility(View.INVISIBLE);

		}

		/*
		 * if(getIntent().getBooleanExtra("isComingBack", false) || isLoggedIn)
		 * { btnBack.setVisibility(View.VISIBLE);
		 * btnNext.setVisibility(View.VISIBLE); bLogin.setVisibility(View.GONE);
		 * btnCancel.setVisibility(View.GONE); //sis.setText("hereNow Login ");
		 * flipper.showNext(); }
		 */
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (request != null)
			request.abort();
		if (dialog1 != null)
			dialog1.dismiss();
	}

	private String readFromFile(String filePath) throws IOException {
		StringBuffer fileData = new StringBuffer();
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
		}
		reader.close();
		return fileData.toString();
	}

	private void writeToFile(String data, String filePath) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
		writer.write(data);
		writer.close();
	}

	static Dialog dialog1;
	static ProgressDialog progressDialog = null;
	static HttpGet request;
	static boolean loginLoopRunning = false;

	public void showToast(String msg) {
		Toast.makeText(Login.this, msg, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onBackPressed() {
		Intent intn = new Intent(Intent.ACTION_MAIN);
		intn.addCategory(Intent.CATEGORY_HOME);

		startActivity(intn);
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/*
		 * case R.id.back:
		 * 
		 * btnBack.setVisibility(View.GONE); btnNext.setVisibility(View.GONE);
		 * btnLogin.setVisibility(View.VISIBLE);
		 * btnCancel.setVisibility(View.VISIBLE); sis.setText("Login");
		 * flipper.showPrevious();
		 * 
		 * 
		 * finish(); break; case R.id.next: u_name.setText("");
		 * passw.setText(""); btnBack.setVisibility(View.GONE);
		 * btnNext.setVisibility(View.GONE); bLogin.setVisibility(View.VISIBLE);
		 * btnCancel.setVisibility(View.VISIBLE); // sis.setText("Login");
		 * flipper.showPrevious(); isLoggedIn = false; break; case R.id.cancel:
		 * isLoggedIn = false;
		 * 
		 * finish(); break;
		 */
		case R.id.blogin:
			Intent i = new Intent();
			String mRemember = "";
			sName = u_name.getText().toString().trim();
			String sPwd = passw.getText().toString().trim();
			if (sName.equals("") || sName.length() == 0) {
				showToast("Please Enter Username");
			} else if (sPwd.trim().length() == 0) {
				showToast("Please Enter Password");
			} else {
				HttpGetTask mTask = new HttpGetTask();
				mTask.execute(url);
				// startActivity(new Intent(getApplicationContext(),
				// MainActivity.class));
			}

			break;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("Settings");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		return super.onOptionsItemSelected(item);
	}

	public class HttpGetTask extends AsyncTask<String, String, Boolean> {
		String mResult;
		private ProgressDialog dialog;// = new ProgressDialog(getBaseContext());

		protected void onPreExecute() {
			dialog = new ProgressDialog(c);
			dialog.setMessage("Processing");
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		protected void onPostExecute(Boolean result) {
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			processResult(result);

		}

		@Override
		protected Boolean doInBackground(String... args) {
			String json = "kk";

			try {
				JSONParser jParser = new JSONParser();
				if (jParser.getResponseFromUrl(url))
					return true;

			} catch (Exception e) {
				Log.e("Error: ", "" + e.getMessage());
			}
			return false;
		}
	}

	private void processResult(boolean result) {
		if (result) {

			String mRemember = "";
			if (cbRememberMe.isChecked())
				mRemember = "yes";
			else
				mRemember = "no";
			mEditor.putString("remember", mRemember);
			mEditor.commit();

			try {

				String PATH = Environment.getExternalStorageDirectory()
						+ "/healthpay/";
				File loginFile = new File(PATH, "login");
				if (!loginFile.exists()) {
					loginFile.createNewFile();
				}
				String sName = u_name.getText().toString();
				String sPwd = passw.getText().toString();

				writeToFile(sName + "~" + sPwd, PATH + "login");
				Intent i = new Intent(getApplicationContext(),
						MainActivity.class);
				i.putExtra("user", sName);
				startActivity(i);
				finish();

			} catch (Exception ex) {
				ex.printStackTrace();
				showToast("Error");
			}

		} else {
			showToast("Error logging in");
		}

	}

}
