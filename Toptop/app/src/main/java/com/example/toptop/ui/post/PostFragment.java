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
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

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

import com.example.toptop.Funk;
import com.example.toptop.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

    String description;
    Uri uri;
    InputStream inputStream;
    boolean isPremium, is_posting = false;

    EditText descriptionEditText;
    CheckBox agreeCheckBox;
    ViewPager viewPager;
    public PostFragment(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            Toast.makeText(requireActivity(), "READ_EXTERNAL Permission Not Granted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireActivity(), "READ_EXTERNAL Already Granted", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireActivity(), "READ_EXTERNAL allowed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireActivity(), "READ_EXTERNAL Denied", Toast.LENGTH_SHORT).show();
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
                if (is_posting) {
                    return;
                }
                is_posting = true;
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

            File file = new File(requireActivity().getFilesDir(), "tmp.mp4");
            Log.d("QUAN", "pathhhhhhh: " + file.getAbsolutePath());
            if (!file.exists()) {
                file.createNewFile();
            }
            OutputStream os = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                os.write(buf, 0, len);
            }
            os.close();
            inputStream.close();
            inputStream = new FileInputStream(file);

            OkHttpClient client = new OkHttpClient();

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("video", "name.mp4", RequestBody.create(MediaType.parse("video/mp4"), file))
                    .addFormDataPart("description", description)
                    .addFormDataPart("is_premium", String.valueOf(isPremium))
                    .build();

            Request request = new Request.Builder()
                    .url("https://soc.q2k.dev/api/post-video/")
                    .post(requestBody)
                    .addHeader("Authorization", "Bearer "+ Funk.get_token(getContext()))
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    is_posting = false;
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    is_posting = false;
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            descriptionEditText.setText("");
                            agreeCheckBox.setChecked(false);
                            Toast.makeText(requireActivity(), "Upload success", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        } catch (Exception e) {
            is_posting = false;
            Toast.makeText(requireActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_VIDEO && resultCode == Activity.RESULT_OK) {
            uri = data.getData();
            try {
                inputStream = requireActivity().getContentResolver().openInputStream(uri);
                Log.d("QUAN", "size:  " + inputStream.available());
            } catch (FileNotFoundException e) {
                Log.d("QUAN", "File not found" + e.getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}