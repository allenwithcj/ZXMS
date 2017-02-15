package com.zxms.model;

import java.util.List;

/**
 * Created by hp on 2017/1/15.
 * 模块
 */
public class Module {
    private String title;
    private List<ModuleContent> moduleContents;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setModuleContents(List<ModuleContent> moduleContents) {
        this.moduleContents = moduleContents;
    }

    public String getTitle() {
        return title;
    }

    public List<ModuleContent> getModuleContents() {
        return moduleContents;
    }
}
