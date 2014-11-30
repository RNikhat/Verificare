package com.calpion.provider.ui;

import java.io.File;
import java.util.ArrayList;

import com.calpion.provider.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;



public class FilePicker extends Activity {

	ArrayList<FileThumbnailSet> itemsList;
	int mHeight = 0;
	ListView listView;
	EditText etSearch;
	String currentFolder = "";
	
	@Override
	public void onCreate(Bundle icicle) {
		try{
			super.onCreate(icicle);
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			if(!init())
			{
				setResult(RESULT_CANCELED, getIntent());
				finish();
			}
		}catch(Exception ex)
		{
			ex.printStackTrace();
			setResult(RESULT_CANCELED, getIntent());
			finish();
		}
	}
	
	@SuppressWarnings("deprecation")
	private boolean init() {
		try{
			itemsList = new ArrayList<FileThumbnailSet>();
			mHeight = getWindowManager().getDefaultDisplay().getHeight();
			this.setContentView(generateLayout());
			
			return true;
		}catch(Exception ex)
		{
			ex.printStackTrace();
			return false;
		}catch(Error er)
		{
			er.printStackTrace();
			return false;
		}
	}
	
	LinearLayout generateLayout()
	{
		LinearLayout llList = new LinearLayout(this);
		llList.setOrientation(LinearLayout.VERTICAL);

		llList.setGravity(Gravity.CENTER);
		
		llList.setPadding(10, 10, 10, 10);
		
		llList.setBackgroundColor(Color.parseColor("#ffffff"));

		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llp.setMargins(0, 5, 0, 15);
		
		LinearLayout.LayoutParams llpupper = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llpupper.weight = 1;
		
		etSearch = new EditText(this);
		llp.gravity = Gravity.TOP|Gravity.LEFT;
		etSearch.setHint("Type to Search");
		etSearch.setTextSize(getTextSize(mHeight));
		etSearch.setLayoutParams(llp);
		etSearch.setSingleLine(true);
		etSearch.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
		etSearch.setTextColor(Color.parseColor("#000000"));
				etSearch.setImeOptions(EditorInfo.IME_ACTION_DONE);
		etSearch.setInputType(InputType.TYPE_CLASS_TEXT);
		etSearch.clearFocus();

		LinearLayout.LayoutParams llplist = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		listView = new ListView(this);
		listView.setLayoutParams(llplist);
		
		listView.setCacheColorHint(Color.WHITE);
		
		currentFolder = Environment.getExternalStorageDirectory().getAbsolutePath();
		FillList(Environment.getExternalStorageDirectory().getAbsolutePath());
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Log.i("isDirectory",itemsList.get(position).file.isDirectory()+"");
				Log.i("file_path",itemsList.get(position).file.getAbsolutePath()+"");
				
				if(itemsList.get(position).file.isDirectory())
				{
					currentFolder = itemsList.get(position).file.getAbsolutePath();
					FillList(itemsList.get(position).file.getAbsolutePath());
				}
				else
				{
					getIntent().putExtra("file_path", itemsList.get(position).file.getAbsolutePath());
					getIntent().setData(Uri.fromFile(itemsList.get(position).file));
					setResult(RESULT_OK, getIntent());
					finish();
				}
			}
		});
		etSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(etSearch.getText().toString().length() >= 3)
				{
					if(itemsList != null)
						itemsList.clear();
					itemsList = new ArrayList<FileThumbnailSet>();
					//
					DoSearch(currentFolder);
					//
					listView.setAdapter(new FilesAdapter(FilePicker.this));
				}
				else
					FillList(currentFolder);
			}
		});

		llList.addView(etSearch);
		llList.addView(listView);
		
		return llList;
	}
	
	@SuppressLint("DefaultLocale")
	private boolean DoSearch(String folderPath)
	{
		try{
			File f = new File(folderPath);
			File[] files = f.listFiles();
			if(files == null)
			{
				return false;
			}
			//
			for(File file : files)
			{
				if(file.getAbsolutePath().trim().toLowerCase().contains(etSearch.getText().toString().toLowerCase().trim()))
				{
					FileThumbnailSet set = new FileThumbnailSet();
					set.file = file;
					itemsList.add(set);
				}
				if(file.isDirectory())
				{
					DoSearch(file.getAbsolutePath());
				}
			}
			//
			return true;
		} catch(Exception ex)
		{
			ex.printStackTrace();
			return false;
		} catch(Error er)
		{
			er.printStackTrace();
			return false;
		}
	}
	
	
	@SuppressLint("DefaultLocale")
	private boolean FillList(String folerPath)
	{
		File f = new File(folerPath);
		//String[] files = f.list();
		File[] files = f.listFiles();
		if(files == null)
		{
			return false;
		}
		else if(files.length == 0)
		{
			Toast.makeText(FilePicker.this, "The folder is empty", Toast.LENGTH_LONG).show();
			return false;
		}
		
		if(itemsList != null)
			itemsList.clear();
		
		itemsList = new ArrayList<FileThumbnailSet>();
		
		for(File file : files)
		{
			if(file.getAbsolutePath().trim().toLowerCase().contains(etSearch.getText().toString().toLowerCase().trim()))
			{
				FileThumbnailSet set = new FileThumbnailSet();
				set.file = file;
				itemsList.add(set);
			}
		}
		
		listView.setAdapter(new FilesAdapter(this));
		
		return true;
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			etSearch.setText("");
			//
			File f = new File(currentFolder);
			String parent = f.getParent();
			Log.i("currentFolder : "+currentFolder,"parent : "+parent);
			if(parent == null || parent.equalsIgnoreCase(Environment.getExternalStorageDirectory().getParent()))
			{
				setResult(RESULT_CANCELED, getIntent());
				finish();
			}
			else
			{
				currentFolder = parent;
				FillList(parent);
			}
		}
		return true;
	}
	
	class FilesAdapter implements ListAdapter {

		Activity callingActivity;
		OnClickListener onClickListener;
		
		public FilesAdapter(Activity act)
		{
			this.callingActivity = act;
		}
		
		public void SetOnRemoveListener(OnClickListener listener) {
			onClickListener = listener;
		}
		
		@Override
		public void registerDataSetObserver(DataSetObserver observer) {
		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver observer) {
		}

		@Override
		public int getCount() {
			if(itemsList != null)
				return itemsList.size();
			else
				return 0;
		}

		@Override
		public Object getItem(int position) {
			if(itemsList != null)
				return itemsList.get(position);
			else
				return 0;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
			
			int value = getTextSize(mHeight);
			
			LinearLayout ll = new LinearLayout(callingActivity);
			ll.setLayoutParams(params);
			ll.setOrientation(LinearLayout.HORIZONTAL);
			ll.setPadding(20, value/2, 20, value/2);
			ll.setGravity(Gravity.CENTER_VERTICAL);
			
			try{
				double percent = 7;
				int size = (int)(mHeight * (percent / (double)100));
				LinearLayout.LayoutParams ivparams = new LinearLayout.LayoutParams(size,size);
				ImageView iv1 = new ImageView(callingActivity);
				iv1.setLayoutParams(ivparams);
				iv1.setScaleType(ScaleType.FIT_CENTER);
				iv1.setAdjustViewBounds(true);
				ll.addView(iv1);
				if(itemsList.get(position).thumbnail == null)
					new PutThumbnail(new ImageViewSet(iv1, itemsList.get(position), size, callingActivity.getResources())).execute();
				else
					iv1.setImageBitmap(itemsList.get(position).thumbnail);
			} catch(Exception ex)
			{
				ex.printStackTrace();
			} catch(Error er)
			{
				er.printStackTrace();
			}
			
			LinearLayout.LayoutParams tvparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
			tvparams.setMargins(20, 0, 0, 0);
			tvparams.gravity = Gravity.LEFT;
			TextView tv1 = new TextView(callingActivity);
			tv1.setTextColor(Color.BLACK);
			tv1.setText(itemsList.get(position).file.getName());
			tv1.setTextSize(value);
			tv1.setLayoutParams(tvparams);

			ll.addView(tv1);
			
			return ll;
		}

		@Override
		public int getItemViewType(int position) {
			return 0;
		}

		@Override
		public int getViewTypeCount() {
			return 1;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public boolean areAllItemsEnabled() {
			return true;
		}

		@Override
		public boolean isEnabled(int position) {
			return true;
		}
		
	}
	
	class FileThumbnailSet
	{
		public File file = null;
		public Bitmap thumbnail = null;
	}
	
	class ImageViewSet
	{
		public int size = 0;
		public ImageView iv = null;
		public FileThumbnailSet set = null;
		public Resources res;
		
		public ImageViewSet(ImageView iv, FileThumbnailSet s, int size,Resources res)
		{
			this.iv = iv;
			this.set = s;
			this.res = res;
			this.size = size;
		}
	}
	
	class PutThumbnail extends AsyncTask<Void,Void,Bitmap> {

		ImageViewSet obj;
		
		public PutThumbnail(ImageViewSet obj)
		{
			this.obj = obj;
		}
		
		@Override
		protected Bitmap doInBackground(Void... params) {
			try{
				if(obj.set.file.isDirectory())
				{
					return BitmapFactory.decodeResource(obj.res, R.drawable.folder_icon);
				}
				else if(obj.set.file.getAbsolutePath().endsWith(".jpg"))
				{
					Options opt = new Options();
					Bitmap bmp1 = BitmapFactory.decodeFile(obj.set.file.getAbsolutePath(), opt);
					Bitmap bmp2 = Bitmap.createScaledBitmap(bmp1, obj.size, obj.size, false);
					bmp1.recycle();
					return bmp2;
				}
				else if(obj.set.file.getAbsolutePath().endsWith(".pdf"))
				{
					return BitmapFactory.decodeResource(obj.res, R.drawable.pdfimg);
				}
				else
				{
					return BitmapFactory.decodeResource(obj.res, R.drawable.document);
				}
			}catch(Exception ex)
			{
				ex.printStackTrace();
			}catch(Error er)
			{
				er.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			obj.set.thumbnail = result;
			obj.iv.setImageBitmap(result);
		}
	}
	
	public int getTextSize(int deviceHeight) {
		if (deviceHeight < 500)
			return 18;
		if (deviceHeight > 1000)
			return 22;
		if (deviceHeight > 500 && deviceHeight < 1000)
			return 35;
		return 14;

	}

}
