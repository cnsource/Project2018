package com.example.project2018;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.project2018.bmobutils.City;
import com.example.project2018.bmobutils.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


public class Rele_Book extends Fragment {
private EditText addressInfo;
private Button add;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_rele__book, container, false);
        // Inflate the layout for this fragment
        addressInfo=view.findViewById(R.id.addressInfo);
        add=view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final City city=new City();
                city.setAddress("山东");
                city.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        Log.i("ObjectId",s);
                        if (e==null) {
                            User user = BmobUser.getCurrentUser(User.class);
                            city.setObjectId(s);
                            user.setCity(city);
                            user.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        Snackbar.make(view, "添加成功", Snackbar.LENGTH_LONG).show();
                                    } else
                                        Log.i("Address:", e.getMessage());
                                }
                            });
                        }else
                            Log.i("City:",e.getMessage());
                    }
                });
            }
        });
        return view;
    }
}
