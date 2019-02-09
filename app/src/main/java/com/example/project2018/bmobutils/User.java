package com.example.project2018.bmobutils;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class User extends BmobUser {

    private City city;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    private BmobFile user_Icon;

    public BmobFile getUser_Icon() {
        return user_Icon;
    }

    public void setUser_Icon(BmobFile user_Icon) {
        this.user_Icon = user_Icon;
    }

    @Override
    public void setUsername(String username) {
        super.setUsername(username);
    }

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
    }
}
