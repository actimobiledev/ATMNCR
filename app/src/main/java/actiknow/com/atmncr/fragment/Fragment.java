package actiknow.com.atmncr.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import actiknow.com.atmncr.R;

public class Fragment extends android.support.v4.app.Fragment {
    // Store instance variables
    private String question;
    private int page;

    // newInstance constructor for creating fragment with arguments
    public static Fragment newInstance (int page, String question) {
        Fragment fragmentFirst = new Fragment ();
        Bundle args = new Bundle ();
        args.putInt ("someInt", page);
        args.putString ("someTitle", question);
        fragmentFirst.setArguments (args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        page = getArguments ().getInt ("someInt", 0);
        question = getArguments ().getString ("someTitle");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.fragment_first, container, false);
        TextView tvQuestion = (TextView) view.findViewById (R.id.tvQuestion);
        tvQuestion.setText (question);
        return view;
    }
}