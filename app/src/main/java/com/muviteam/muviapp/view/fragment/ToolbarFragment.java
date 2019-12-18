package com.muviteam.muviapp.view.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.muviteam.muviapp.R;
import com.muviteam.muviapp.view.adapter.AdapterViewPagerToolBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class ToolbarFragment extends Fragment{

    ViewPager myViewPager;
    AdapterViewPagerToolBar myAdapter;
    TabLayout myTabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_toolbar, container, false);

        encotrarVariablesId(view);

        myAdapter = new AdapterViewPagerToolBar(getActivity().getSupportFragmentManager());

        myViewPager.setAdapter(myAdapter);
        myTabLayout.setupWithViewPager(myViewPager);
        myViewPager.setCurrentItem(1);
        return view;
    }

    private void encotrarVariablesId(View view){
        myViewPager = view.findViewById(R.id.FragmentToolbar_ViewPager_ContenedorDeFragments);
        myTabLayout = view.findViewById(R.id.FragmentToolbar_TabLayout);
    }


}
