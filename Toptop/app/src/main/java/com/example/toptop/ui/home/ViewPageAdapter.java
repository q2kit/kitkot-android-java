package com.example.toptop.ui.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.toptop.ui.discover.DiscoverFragment;
import com.example.toptop.ui.inbox.InboxFragment;
import com.example.toptop.ui.me.MeFragment;
import com.example.toptop.ui.post.PostFragment;

public class ViewPageAdapter extends FragmentStatePagerAdapter {

    public ViewPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:return new HomeFragment();
            case 1:return new DiscoverFragment();
            case 2:return new PostFragment();
            case 3:return new InboxFragment();
            case 4:return new MeFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }
}
