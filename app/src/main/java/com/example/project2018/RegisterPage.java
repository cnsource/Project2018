package com.example.project2018;


import android.Manifest;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.NonNull;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.project2018.bmobutils.User;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;


public class RegisterPage extends Fragment {
    private ProgressBar pb;
    private ImageView user_icon;
    private Button registe;
    private TextInputEditText name, new_pwd, pwd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_register_page, container, false);
        user_icon = view.findViewById(R.id.user_icon);
        user_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPermission();
            }
        });
        name = view.findViewById(R.id.user_name);
        new_pwd = view.findViewById(R.id.new_pwd);
        pb = view.findViewById(R.id.up_icon);
        pwd = view.findViewById(R.id.config_pwd);
        registe = view.findViewById(R.id.registe);
        registe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkUserInfo() == true) {
                    User user = new User();
                    user.setUsername(name.getText().toString());
                    user.setPassword(pwd.getText().toString());
                    user.signUp(new SaveListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            if (e == null) {
                                BmobUser.loginByAccount(name.getText().toString(), pwd.getText().toString(), new LogInListener<User>() {
                                    @Override
                                    public void done(User user, BmobException e) {
                                        if (e == null) {
                                            setUserIcon(user.getObjectId());
                                            startActivity(new Intent(getContext(), ContentActivity.class));
                                            getActivity().finish();
                                        }
                                    }
                                });
                            } else Log.e("errer:------:  ", e.getMessage());
                            Log.e("User_Info:", name.getText().toString() + pwd.getText().toString());
                        }
                    });
                }
            }
        });
        return view;

    }

    private boolean checkUserInfo() {
        if (new_pwd.getText().toString().equals(pwd.getText().toString())) {
            return true;
        } else
            return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions.length > 0 && grantResults.length > 0) {
            startIntent();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("结果码", "结果码：  " + resultCode);
        Uri uri = data.getData();
        Log.i("Fragment_Uri", uri.toString());
        ContentResolver cr = getContext().getContentResolver();
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
        } catch (FileNotFoundException e) {
            Log.e("错误1", e.getMessage());
        }
        user_icon.setImageBitmap(bitmap);

    }

    void startIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 1);
    }

    void initPermission() {
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (ActivityCompat.checkSelfPermission(getContext(), permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(permissions[0]) == false) ;
            else
                ActivityCompat.requestPermissions(getActivity(), permissions, 1);
        } else startIntent();
    }

    private void setUserIcon(String objectId) {
        File file=new File(getActivity().getCacheDir() + "/icon.jpg");
        String id=BmobUser.getCurrentUser(User.class).getObjectId();
        String path=getActivity().getCacheDir()+"/icon"+id+".jpg";
        file.renameTo(new File(path));
        final BmobFile icon = new BmobFile(new File(path));
        icon.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                userIconUpdate(icon);
            }

            @Override
            public void onProgress(final Integer value) {
                super.onProgress(value);
                Log.i("Value", "=" + value);
                pb.setVisibility(ProgressBar.VISIBLE);
                pb.setMax(100);
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        while (value != 100)
                            pb.setProgress(value);
                    }
                }.start();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    private void userIconUpdate(BmobFile icon) {
        User user = BmobUser.getCurrentUser(User.class);
        user.setUser_Icon(icon);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("SetUserIcon:", "Successeful");
                } else
                    Log.i("SetUserIcon:", "Error" + e.getMessage());
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
