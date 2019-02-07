package com.example.project2018;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.project2018.bmobutils.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.QueryListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class Us extends Fragment {

    private ImageView imageView;
    private String id;

    public Us() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_us, container, false);
        Button loadIcon = view.findViewById(R.id.loadIcon);
        imageView = view.findViewById(R.id.icon);
        loadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = BmobUser.getCurrentUser(User.class).getObjectId();
                String path = getActivity().getCacheDir() + "/icon" + id + ".jpg";
                File file = new File(path);
                if (file == null) {

                } else
                    setIcon(path);
            }
        });
        return view;
    }

    private void downLoadIcon(final BmobFile user_icon) {
        user_icon.download(new DownloadFileListener() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null)
                    setIcon(s);
                else
                    Log.i("加载头像失败", e.getMessage());
            }

            @Override
            public void onProgress(Integer integer, long l) {
                Log.i("下载进度：", "  " + integer);
            }
        });
    }

    private void setIcon(String s) {
        try {
            FileInputStream inputStream = new FileInputStream(new File(s));
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            imageView.setImageBitmap(bitmap);
            inputStream.close();
        } catch (FileNotFoundException e1) {
            Log.i("File:", "FileInputStreamOpenError");
            Log.i("File为空", "56565656565656");
            BmobQuery<User> query = new BmobQuery<>();
            query.getObject(id, new QueryListener<User>() {
                @Override
                public void done(User user, BmobException e) {
                    if (e == null) {
                        downLoadIcon(user.getUser_Icon());
                    } else
                        Log.i("查询错误：", e.getMessage());
                }
            });
        } catch (IOException e1) {
            Log.i("InputStream:", "Close Fault");
        }
    }

}
