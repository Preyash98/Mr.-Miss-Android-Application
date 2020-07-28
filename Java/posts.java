package com.MrAndMiss.admin.mrmrspu;

/**
 * Created by ADMIN on 3/14/2017.
 */

public class posts {


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLikers() {
        return likers;
    }

    public void setLikers(long likers) {
        this.likers = likers;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    private String image;
    private String name;
    private long likers;
    private String userid;

    public posts() {
    }

    public posts(String image, String name, long likers, String userid) {
        this.image = image;
        this.name = name;
        this.likers = likers;
        this.userid = userid;
    }
}

