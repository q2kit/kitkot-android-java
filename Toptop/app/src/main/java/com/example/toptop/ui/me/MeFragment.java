package com.example.toptop.ui.me;

import static android.widget.Toast.LENGTH_SHORT;
import static androidx.databinding.DataBindingUtil.setContentView;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.toptop.MainActivity;
import com.example.toptop.R;
import com.example.toptop.SettingActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_me, container, false);

        // Get a reference to the Toolbar and ImageView
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ImageView settingsImageView = toolbar.findViewById(R.id.settings);

        // Add a click listener to the ImageView
        settingsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the SettingsActivity

                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }



}