package com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.ViewPagerData;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.NearbyRewardEvents;
import com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.ParticipationHistory;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> titlesList = new ArrayList<>();

    public ViewPagerAdapter(@NonNull @NotNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                Fragment fragment1 = new NearbyRewardEvents();
                return fragment1;

            case 1:
                Fragment fragment2 = new ParticipationHistory();
                return fragment2;

        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        titlesList.add(title);
    }
}
