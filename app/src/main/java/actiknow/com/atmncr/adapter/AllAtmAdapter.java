package actiknow.com.atmncr.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import actiknow.com.atmncr.R;
import actiknow.com.atmncr.activity.AtmDetailsActivity;
import actiknow.com.atmncr.model.Atms;
import actiknow.com.atmncr.utils.Constants;

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

		ImageView atm_image = (ImageView) convertView.findViewById (R.id.ivAtmImage);
		TextView atm_name = (TextView) convertView.findViewById (R.id.tvAtmName);
		TextView atm_bank = (TextView) convertView.findViewById (R.id.tvBankName);
		TextView atm_location = (TextView) convertView.findViewById (R.id.tvAtmLocation);

		// getting movie data for the row
		final Atms atm = atms.get(position);

		// thumbnail image
//		service_image.setImageUrl (s.getService_image (), imageLoader);
		Picasso.with (activity).load (atm.getAtm_image ()).into (atm_image);
		
		// title
		atm_name.setText(atm.getAtm_name ());
		atm_bank.setText (atm.getAtm_bank ());
		atm_location.setText (atm.getAtm_location ());



		convertView.setOnClickListener (new View.OnClickListener () {
			private void die () {
			}

			@Override
			public void onClick (View arg0) {
				Constants.atm_id = atm.getAtm_id ();
				Intent intent = new Intent (activity, AtmDetailsActivity.class);
				activity.startActivity (intent);
				activity.overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
		return convertView;
	}

}