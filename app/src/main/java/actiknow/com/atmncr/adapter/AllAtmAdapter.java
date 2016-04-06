package actiknow.com.atmncr.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import actiknow.com.atmncr.R;
import actiknow.com.atmncr.activity.ViewPagerActivity;
import actiknow.com.atmncr.model.Atms;

public class AllAtmAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Atms> atms;

	public AllAtmAdapter (Activity activity, List<Atms> atms) {
		this.activity = activity;
		this.atms = atms;
	}

	@Override
	public int getCount() {
		return atms.size();
	}

	@Override
	public Object getItem(int location) {
		return atms.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate (R.layout.listview_item_atm, null);

		TextView atm_last_audit_date = (TextView) convertView.findViewById (R.id.tvLastAuditDate);
		TextView atm_atm_unique_id = (TextView) convertView.findViewById (R.id.tvAtmUniqueId);
		TextView atm_bank_name = (TextView) convertView.findViewById (R.id.tvBankName);
		TextView atm_address = (TextView) convertView.findViewById (R.id.tvAddress);
		TextView atm_city = (TextView) convertView.findViewById (R.id.tvCity);
		TextView atm_pincode = (TextView) convertView.findViewById (R.id.tvPincode);

		final Atms atm = atms.get(position);

		atm_last_audit_date.setText (atm.getAtm_last_audit_date ());
		atm_atm_unique_id.setText (atm.getAtm_unique_id ().toUpperCase ());
		atm_bank_name.setText (atm.getAtm_bank_name ().toUpperCase ());
		atm_address.setText (atm.getAtm_address ().toUpperCase ());
		atm_city.setText (atm.getAtm_city ().toUpperCase ());
		atm_pincode.setText (atm.getAtm_pincode ().toUpperCase ());


		convertView.setOnClickListener (new View.OnClickListener () {
			private void die () {
			}

			@Override
			public void onClick (View arg0) {
				Intent intent = new Intent (activity, ViewPagerActivity.class);
				activity.startActivity (intent);
				activity.overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
		return convertView;
	}

}