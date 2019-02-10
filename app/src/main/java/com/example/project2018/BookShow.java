package com.example.project2018;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.project2018.bmobutils.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookShow extends Fragment {


    private List<User> userData=new ArrayList<>();

    public BookShow() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_book_show, container, false);
        Button loginout=view.findViewById(R.id.loginout);
        loginout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser.logOut();
                startActivity(new Intent(getContext(),Login.class));
                getActivity().finish();

            }
        });
        RecyclerView review=view.findViewById(R.id.infolist);
        final SwipeRefreshLayout swipeRefreshLayout=view.findViewById(R.id.pullre);
        swipeRefreshLayout.setColorSchemeColors(Color.RED);

        final CardViewAdapter cdadt=new CardViewAdapter(getContext(),userData);
        review.setLayoutManager(new LinearLayoutManager(getContext()));
        review.setAdapter(cdadt);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //dosth
                BmobQuery<User> query=new BmobQuery<>();
                Date date=new Date(System.currentTimeMillis());
                Log.i("时间",date.toString());
                BmobDate bd=new BmobDate(date);
                Log.i("时间",bd.getDate().toString());
                query.addWhereLessThan("createdAt",bd);
                query.findObjects(new FindListener<User>() {
                    @Override
                    public void done(List<User> list, BmobException e) {
                        if (e==null){
                            userData.clear();
                            for (int i=0;i<list.size();i++){
                                userData.add(list.get(i));
                            }
                            Log.i("查询成",""+(list.size()));
                            swipeRefreshLayout.setRefreshing(false);
                            cdadt.notifyDataSetChanged();
                            Log.i("Tag","刷新了");
                        }else {
                            Toast.makeText(getContext(), "刷新失败", Toast.LENGTH_SHORT).show();                   swipeRefreshLayout.setRefreshing(false);

                        }
                    }
                });

            }
        });
        return view;
    }

}
