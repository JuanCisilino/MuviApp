package com.muviteam.muviapp.view.adapter;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.List;

public class AdapterViewPager extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;

    public List<Fragment> getFragmentList() {
        return fragmentList;
    }

    public AdapterViewPager(@NonNull FragmentManager fm, List<Fragment> fragmentAMostrar) {
        super(fm);
        fragmentList = fragmentAMostrar;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void actualizarListFragment(List<Fragment> fragmentList) {
        this.fragmentList = fragmentList;
        notifyDataSetChanged();
    }

}
