package com.inke.plugins.mvpmodel

/**
 * mvp入参
 */
class MvpModelInfo {
    //类全路径 .拼接
    String allPath = ""  //app.src.main.java.com.inke.inkebone
    //类的名称
    String className = ""//TextActivity
    //Manifest的路径
    String manifestPath = ""//app.src.main
    //是否需要创建新的文件夹 默认创建
    boolean isCreateNewPackage = true
    //是否需要创建Mvp框架 默认创建
    boolean isCreateMvp = true
}
