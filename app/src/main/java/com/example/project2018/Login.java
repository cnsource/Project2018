package com.example.project2018;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.project2018.bmobutils.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadFileListener;

public class Login extends AppCompatActivity {
    private List<Fragment> fragments;
    private TabLayout tabs;
    private ViewPager views;
    private String[] titles={"登录","注册"};
    private LPageAdapter lPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tabs=findViewById(R.id.tabs);
        views=findViewById(R.id.views);
        fragments=new ArrayList<>();
        fragments.add(new LoginPage());
        fragments.add(new RegisterPage());
        lPageAdapter =new LPageAdapter(getSupportFragmentManager(),titles,fragments);

        tabs.setupWithViewPager(views);
        views.setAdapter(lPageAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri=data.getData();
        Log.i("结果码","结果码：  " + resultCode);
        Log.i("Login_Uri",uri.toString());
        ContentResolver cr =getContentResolver();
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
        } catch (FileNotFoundException e) {
            Log.e("错误1",e.getMessage());
        }
        FileOutputStream fos=null;
        try {

            String path=getCacheDir()+"/icon.jpg";
            fos =new FileOutputStream(new File(path));
            bitmap.compress(Bitmap.CompressFormat.JPEG,40,fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("错误2",e.getMessage());
        } catch (IOException e) {
            Log.e("错误3",e.getMessage());
        }
    }


}
