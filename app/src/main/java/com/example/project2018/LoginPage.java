package com.example.project2018;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project2018.bmobutils.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;


public class LoginPage extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_login_page, container, false);
        Button btn_lofin=view.findViewById(R.id.logined);
        final TextInputEditText name=view.findViewById(R.id.username);
        final TextInputEditText pwd=view.findViewById(R.id.pwd);
        btn_lofin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user=new User();
                user.setUsername(name.getText().toString());
                user.setPassword(pwd.getText().toString());
                BmobUser.loginByAccount(name.getText().toString(), pwd.getText().toString(), new LogInListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        if (e==null) {
                            Log.i("登录成功", ".......................");
                            startActivity(new Intent(getContext(),ContentActivity.class));
                            getActivity().finish();
                        }else
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return view;
    }

}
