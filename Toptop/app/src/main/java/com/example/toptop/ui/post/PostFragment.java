package com.example.toptop.ui.post;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.toptop.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class PostFragment extends Fragment {private static final int REQUEST_VIDEO = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 37;

    String description, videoPath;
    boolean isPremium;

    EditText descriptionEditText;
    CheckBox agreeCheckBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            Toast.makeText(requireActivity(), "Permission Denied1", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireActivity(), "Permission Granted1", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        Button chooseVideoButton = view.findViewById(R.id.btn_select);
        chooseVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseVideo();
            }
        });

        descriptionEditText = view.findViewById(R.id.description);
        agreeCheckBox = view.findViewById(R.id.is_premium);

        Button uploadButton = view.findViewById(R.id.btn_upload);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadVideo();
            }
        });

        return view;
    }

    private void chooseVideo() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        startActivityForResult(intent, REQUEST_VIDEO);
    }

    private void uploadVideo() {
        try {

            description = descriptionEditText.getText().toString();
            if (description.isEmpty()) {
                descriptionEditText.setError("Description required");
                descriptionEditText.requestFocus();
                return;
            }
            isPremium = agreeCheckBox.isChecked();

            if (videoPath == null) {
                return;
            }
            Log.d("QUAN", "videoPath" + videoPath);
            Log.d("QUAN", "description" + description);
            Log.d("QUAN", "isPremium" + isPremium);
            File file = new File(videoPath);
            Log.d("QUAN", "file" + file);
            Log.d("QUAN", "size" + file.length());
            FileInputStream fileInputStream = new FileInputStream(file);
            Log.d("QUAN", "fileInputStream" + fileInputStream);
            Log.d("QUAN", "size" + fileInputStream.available());


//            OkHttpClient client = new OkHttpClient();
//
//            RequestBody requestBody = new MultipartBody.Builder()
//                    .setType(MultipartBody.FORM)
//                    .addFormDataPart("video", file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file))
//                    .addFormDataPart("description", description)
//                    .addFormDataPart("is_premium", String.valueOf(isPremium))
//                    .build();
//
//            Request request = new Request.Builder()
//                    .url("https://soc.q2k.dev/api/post-video/")
//                    .post(requestBody)
//                    .build();
//
//            client.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    e.printStackTrace();
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    if (response.isSuccessful()) {
//                        // TODO: handle successful response
//                    } else {
//                        // TODO: handle unsuccessful response
//                    }
//                }
//            });
        } catch (Exception e) {
            Toast.makeText(requireActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_VIDEO && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            videoPath = uri.getPath();
            Log.d("QUAN","videoPath" + videoPath);
        }
    }
}