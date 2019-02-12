package com.example.project2018;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bmob.initialize(this,"89f3ffb5df046d33354765d13af06dd3");
        if (BmobUser.isLogin()){
            startActivity(new Intent(this,DrawerMenu.class));
            finish();
        }else
            startActivity(new Intent(getApplicationContext(),DrawerMenu.class));
        finish();
    }
}
