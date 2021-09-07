package com.inke.plugins.mvpmodel

/**
 * mvp入参
 */
class MvpModelInfo {
    //类全路径 .拼接 Copy Absolute Path
    String classPath = ""  // /Users/guanzhen/godbase/app/src/main/java/com/ziroom/godbase/ui
    //类的名称
    String className = ""//TestActivity
    //是否需要创建新的文件夹 默认创建
    boolean isCreateNewPackage = true
    //是否需要创建Mvp框架 默认创建
    boolean isCreateMvp = true
}
