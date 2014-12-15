package com.calpion.provider.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.calpion.provider.R;
import com.calpion.provider.model.ReportstBean;
import com.calpion.provider.ui.ReportListFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

public class MultiChoiceListAdapter extends ArrayAdapter<ReportstBean> {

	private List<ReportstBean> orginlist;
	private List<ReportstBean> filterlist;
	private LayoutInflater inflator;

	public MultiChoiceListAdapter(Activity context, List<ReportstBean> list) {
		super(context, R.layout.report_list_item, list);
		this.orginlist = list;
		this.filterlist = new ArrayList<ReportstBean>();
		filterlist.addAll(orginlist);

		inflator = context.getLayoutInflater();

	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflator.inflate(R.layout.report_list_item, null);
			holder = new ViewHolder();
			holder.txt1 = (TextView) convertView.findViewById(R.id.txt1);
			holder.txt2 = (TextView) convertView.findViewById(R.id.txt2);
			holder.txt3 = (TextView) convertView.findViewById(R.id.txt3);
			holder.txt4 = (TextView) convertView.findViewById(R.id.txt4);
			holder.txt5 = (TextView) convertView.findViewById(R.id.txt5);
			holder.txt6 = (ImageView) convertView.findViewById(R.id.txt6);
			holder.chk = (CheckBox) convertView.findViewById(R.id.checkbox);
			holder.chk
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton view,
								boolean isChecked) {
							int getPosition = (Integer) view.getTag();
							orginlist.get(getPosition).setSelected(
									view.isChecked());

						}
					});
			convertView.setTag(holder);
/*			convertView.setTag(R.id.txt1, holder.txt1);
			convertView.setTag(R.id.checkbox, holder.chk);*/
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.chk.setTag(position);

		holder.txt1.setText(orginlist.get(position).getProvider());
		holder.txt2.setText(orginlist.get(position).getPatientName());
		holder.txt3.setText(orginlist.get(position).getAptDate());
		holder.txt4.setText(orginlist.get(position).getInsurance());
		holder.txt5.setText(orginlist.get(position).getPriority());

		holder.chk.setChecked(orginlist.get(position).isSelected());

		return convertView;
	}

	static class ViewHolder {
		protected TextView txt1, txt2, txt3, txt4, txt5;
		protected ImageView txt6;
		protected CheckBox chk;
	}

	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		orginlist.clear();
		if (charText.length() == 0) {
			orginlist.addAll(filterlist);
		} else {
			for (ReportstBean wp : filterlist) {
				if (wp.getAptDate().toLowerCase().contains(charText)) {
					orginlist.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}

}