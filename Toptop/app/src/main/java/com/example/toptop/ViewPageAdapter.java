package com.example.toptop;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.toptop.ui.discover.DiscoverFragment;
import com.example.toptop.ui.home.Comment;
import com.example.toptop.ui.home.ProfileDialogFragment;
import com.example.toptop.ui.home.Video;
import com.example.toptop.ui.home.VideoListFragment;
import com.example.toptop.ui.inbox.InboxFragment;
import com.example.toptop.ui.me.MeFragment;
import com.example.toptop.ui.post.PostFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewPageAdapter extends FragmentStatePagerAdapter {
    VideoListFragment videoListFragment ;
    private ArrayList<Fragment> fragments;
    static ProfileDialogFragment.IProfile iProfile;
    public ViewPageAdapter(@NonNull FragmentManager fm, int behavior, ProfileDialogFragment.IProfile iProfile) {
        super(fm, behavior);
        videoListFragment = new VideoListFragment(iProfile);
        fragments = new ArrayList<>();
        fragments.add(videoListFragment);
        fragments.add(null);
        fragments.add(null);
        fragments.add(null);
        fragments.add(null);
    }

    public void updateComments(List<Comment> comments){
        videoListFragment.updateComments(comments);
    }

    public void updateVideo(Video video){
        videoListFragment.updateVideo(video);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return fragments.get(0);
            case 1:
                if(fragments.get(1) == null){
                    fragments.set(1, new DiscoverFragment());
                }
                return fragments.get(1);
            case 2:
                return new PostFragment(this.viewPager);
            case 3:
                if(fragments.get(3) == null){
                    fragments.set(3, new InboxFragment());
                }
                return fragments.get(3);
            case 4:
                if(fragments.get(4) == null){
                    fragments.set(4, new MeFragment());
                }
                return fragments.get(4);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }

    public void addComments(List<Comment> comments) {
        videoListFragment.addComments(comments);
    }
}
