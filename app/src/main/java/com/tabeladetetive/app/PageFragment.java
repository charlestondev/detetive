package com.tabeladetetive.app;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tabeladetetive.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by charleston on 06/06/14.
 */
public class PageFragment extends Fragment {
    public List<Crime> crimeList;
    public CrimeAdapter crimeAdapter;

    public ListView crimeListView;
    public PageFragment(){}
    public PageFragment(List<Crime> crimeList) {
        this.crimeList = crimeList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.crime_list, container, false);

        crimeListView = (ListView) rootView.findViewById(R.id.crime_list);
        crimeAdapter = new CrimeAdapter(getActivity(), this.crimeList);
        crimeListView.setAdapter(crimeAdapter);

        return rootView;
    }
}
