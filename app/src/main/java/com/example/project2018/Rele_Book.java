package com.example.project2018;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.project2018.bmobutils.City;
import com.example.project2018.bmobutils.User;

import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


public class Rele_Book extends Fragment {
    private TextView addressInfo;
    private Button add;
    private Button get;
    private Button getnew;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_rele__book, container, false);
        // Inflate the layout for this fragment
        addressInfo = view.findViewById(R.id.addressInfo);
        get = view.findViewById(R.id.get);
        getnew = view.findViewById(R.id.getnew);
        add = view.findViewById(R.id.add);
        final User user = BmobUser.getCurrentUser(User.class);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getCity().getAddress() == null) {
                    final City city = new City();
                    city.setAddress("山东");
                    city.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            Log.i("ObjectId", s);
                            if (e == null) {

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
                            } else
                                Log.i("City:", e.getMessage());
                        }
                    });
                }else {
                    Toast.makeText(getContext(), "地址已存在", Toast.LENGTH_SHORT).show();
                }
            }

        });
        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobQuery query = new BmobQuery();
                query.include("city");
                query.getObject(user.getObjectId(), new QueryListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        if (e == null) {
                            String addss = user.getCity().getAddress();
                            Toast.makeText(getContext(), addss, Toast.LENGTH_SHORT).show();
                        } else {
                            Log.i("获取地址出错", e.getMessage());
                        }
                    }
                });
            }
        });
        getnew.setOnClickListener(new View.OnClickListener() {
            boolean tag=false;
            @Override
            public void onClick(View v) {
                Date date=new Date(System.currentTimeMillis());
                Log.i("时间",date.toString());
                BmobDate bd=new BmobDate(date);
                Log.i("时间",date.toString());
              final  BmobQuery<User> query=new BmobQuery<>();
                query.addWhereLessThan("createdAt",bd);
               final ProgressDialog pb=new ProgressDialog(getContext());
                pb.setCancelable(true);

                pb.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        pb.cancel();
                    }
                });

                pb.setMessage("查询中...");
                pb.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pb.show();
                query.findObjects(new FindListener<User>() {
                    @Override
                    public void done(List<User> list, BmobException e) {
                        if (e==null){
                            Log.i("查询","成功"+list.size());
                            tag=true;
                            pb.cancel();
                            Snackbar.make(view,"查询失败",Snackbar.LENGTH_INDEFINITE).setAction("重试", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //dosth
                                }
                            }).setAction("退出", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //dosth
                                }
                            });
                        }else {
                            Log.i("查询","失败--"+e.getMessage());
                            Snackbar.make(view,"查询失败",Snackbar.LENGTH_INDEFINITE).setAction("重试", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //dosth
                                }
                            }).setAction("退出", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //dosth
                                }
                            });
                        }
                    }
                });
                if(tag==true){
                    //查询成功
                    //Do somthing
                }else {
                    //others
                }
            }
        });
        return view;
    }
}
