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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.toptop.MainActivity;
import com.example.toptop.R;
import com.example.toptop.SettingActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MeFragment extends Fragment {

    private LinearLayout layoutLogin, layoutProfile;
    private boolean isLoggedIn = true;
    private Button btnLogin;
    private ImageView settingImg;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        super.onViewCreated(view, savedInstanceState);

        // Find the layouts by ID
        layoutLogin = view.findViewById(R.id.layout_login);
        layoutProfile = view.findViewById(R.id.layout_profile);

        if(isLoggedIn){
            layoutProfile.setVisibility(View.VISIBLE);
            layoutLogin.setVisibility(View.GONE);
        }else {
            layoutProfile.setVisibility(View.GONE);
            layoutLogin.setVisibility(View.VISIBLE);
        }
        btnLogin = view.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });


        settingImg = view.findViewById(R.id.settings);
        settingImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SettingActivity.class));
            }
        });

        return view;
    }

}