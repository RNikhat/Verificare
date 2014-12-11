package com.calpion.provider.ui;

import com.calpion.provider.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.calpion.provider.adapter.NavDrawerListAdapter;
import com.calpion.provider.model.NavDrawerItem;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Home
		for (int i = 0; i < navMenuTitles.length; i++) {
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[i], navMenuIcons
					.getResourceId(i, -1)));
		}

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);
		LayoutInflater mInflater = LayoutInflater.from(this);

		View mCustomView = mInflater.inflate(R.layout.actionbar, null);
		TextView mTitleTextView = (TextView) mCustomView
				.findViewById(R.id.title_text);
		mTitleTextView.setSelected(true);
		mTitleTextView.setText("Welcome " + getIntent().getStringExtra("user"));

		ImageView imageButton = (ImageView) mCustomView
				.findViewById(R.id.imageButton);
		imageButton.setClickable(true);

		imageButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Toast.makeText(getApplicationContext(), "logout",
				// Toast.LENGTH_LONG).show();
				Intent i = new Intent(getApplicationContext(), Login.class);
				startActivity(i);
				finish();
			}
		});

		getSupportActionBar().setDisplayOptions(
				ActionBar.DISPLAY_USE_LOGO | ActionBar.DISPLAY_SHOW_HOME
				// | ActionBar.DISPLAY_SHOW_TITLE
						| ActionBar.DISPLAY_HOME_AS_UP);

		getSupportActionBar().setCustomView(mCustomView);
		getSupportActionBar().setDisplayShowCustomEnabled(true);

		getSupportActionBar().setLogo(R.drawable.logo);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle("HealthPay");
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};

		mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(999);
		}
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position) {

		case 999:
			fragment = new HomeFragment();
			break;
		case 0:
			fragment = new UploadDetailsFragment();
			break;
		case 1:
			fragment = new NewPatientFragment();

			break;
		case 2:
			fragment = new ReportListFragment();
			break;
		case 3:

			showCalendar();
			mDrawerLayout.closeDrawer(mDrawerList);
			break;
		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment)
					.addToBackStack(fragment.getClass().getName()).commit();
			// update selected item and title, then close the drawer
			if (position != 999) {
				mDrawerList.setItemChecked(position, true);
				mDrawerList.setSelection(position);
				setTitle(navMenuTitles[position]);
			}
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;

		getSupportActionBar().setTitle(mTitle);

	}

	@Override
	public void onBackPressed() {
		if (getFragmentManager().getBackStackEntryCount() == 1) {

			showDialog("Exit", "Are you sure you want to exit the app?");
		} else {
			super.onBackPressed();
		}
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	public void showCalendar() {
		Date date = new Date();
		Calendar startTime = Calendar.getInstance();
		startTime.set(2013, 2, 13, 11, 35);

		Uri uri = Uri.parse("content://com.android.calendar/time/"
				+ String.valueOf(startTime.getTimeInMillis()));
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		// Use the Calendar app to view the time.
		startActivity(intent);
	}

	public void showDialog(String title, String msg) {
		AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
				MainActivity.this);

		// Setting Dialog Title
		alertDialog2.setTitle(title + "...");

		// Setting Dialog Message
		alertDialog2.setMessage(msg);

		// Setting Icon to Dialog
		// alertDialog2.setIcon(R.drawable.delete);

		// Setting Positive "Yes" Btn
		alertDialog2.setPositiveButton("YES",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
		// Setting Negative "NO" Btn
		alertDialog2.setNegativeButton("NO",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						dialog.cancel();
					}
				});

		// Showing Alert Dialog
		alertDialog2.show();

	}

}
