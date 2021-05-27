package com.creator.rewardsapp.Body.OfferWalls.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.ViewPagerData.ViewPagerAdapter;
import com.creator.rewardsapp.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class HomeFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ViewPagerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {

        viewPager2 = root.findViewById(R.id.tabview_pager);
        adapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(adapter);

        String [] tabTtiles={"Reward Events","Participation History"};
        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> tab.setText("OBJECT " + (position + 1))
        ).attach();
        tabLayout.getTabAt(0).setText(tabTtiles[0]);
        tabLayout.getTabAt(1).setText(tabTtiles[1]);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_round_emoji_events_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_round_history_24);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        tabLayout = root.findViewById(R.id.tab_bar);

        return root;
    }


}