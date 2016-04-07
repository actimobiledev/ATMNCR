package actiknow.com.atmncr.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import actiknow.com.atmncr.R;

public class LastFragment extends android.support.v4.app.Fragment {
    // Store instance variables
    private int page;


    // newInstance constructor for creating fragment with arguments
    public static LastFragment newInstance (int page) {
        LastFragment fragmentFirst = new LastFragment ();
        Bundle args = new Bundle ();
        args.putInt ("page_number", page);
        fragmentFirst.setArguments (args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        page = getArguments ().getInt ("page_number", 0);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.fragment_last, container, false);

        return view;
    }
}