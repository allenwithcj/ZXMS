package com.zxms.model;

/**
 * Created by hp on 2017/1/15.
 * 模块内容
 */
public class ModuleContent {
    private String name;
    private int img;

    public void setName(String name) {
        this.name = name;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getImg() {
        return img;
    }

    public String getName() {
        return name;
    }

    public ModuleContent(String name, int img) {
        this.name = name;
        this.img = img;
    }
}
