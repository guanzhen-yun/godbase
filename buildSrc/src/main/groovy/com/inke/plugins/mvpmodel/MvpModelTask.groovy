package com.inke.plugins.mvpmodel

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * 根据类名和路径 生成MVP框架的代码  一个文件夹 三个文件Contract Presenter Activity/Fragment
 * 输入 参数 类名和路径
 * 输出 MVP框架的代码  一个文件夹 三个文件 一个布局文件
 */
class MvpModelTask extends DefaultTask {
    MvpModelInfo info

    MvpModelTask() {
        info = new MvpModelInfo()
        group = 'selftask'
        description = 'mvp plugin task'
        String buildSrcBuildDir = project.projectDir.parent + File.separator + "buildSrc" + File.separator + "build"
        File buildFile = new File(buildSrcBuildDir)
        deleteFile(buildFile)
    }
    //相当于Task的执行入口
    @TaskAction
    void doAction() {
        boolean isCreateNewPackage = info.isCreateNewPackage //是否创建新的文件夹
        boolean isCreateMvp = info.isCreateMvp //是否创建mvp结构
        String classPath = info.classPath //类相对路径
        String className = info.className //类名称
        println("isCreateNewPackage: " + isCreateNewPackage)
        println("isCreateMvp: " + isCreateMvp)
        println("classPath: " + classPath)
        println("className: " + className)

        String srcPath = classPath.split(File.separator + "src")[0]
        String javaPath = classPath.split(File.separator + "java")[0]
        String extraPath = javaPath.replace(srcPath, "")
        String[] split = extraPath.split(File.separator)
        int extraLength = split.length
        String manifestName = "AndroidManifest.xml"
        File manifestFile = new File(srcPath, manifestName)
        if (!manifestFile.exists()) {
            String manifestPath = srcPath;
            for (int i = 0; i < extraLength; i++) {
                if ("".equals(split[i])) continue
                manifestPath = manifestPath + File.separator + split[i]
                manifestFile = new File(manifestPath, manifestName)
                if (manifestFile.exists()) {
                    break
                }
            }
        }
        String manifestFilePath = manifestFile.getParent()
        // 0.获取包名
        String packageName = getPackageName(manifestFilePath)
        println("packageName: " + packageName)
        println("manifestFilePath: " + manifestFilePath)
        //1.创建class文件
        createActivityFragment(classPath, className, packageName, isCreateMvp, isCreateNewPackage)
        if (isCreateMvp) {
            // 2.创建Contract文件
            createContract(classPath, className, isCreateNewPackage)
            // 3.创建Presenter文件
            createPresenter(classPath, className, isCreateNewPackage)
        }
        // 4.创建布局文件
        createLayout(javaPath, className)
        // 5.注册Activity
        if (className.endsWith("Activity")) {
            boolean isExistActivityInManifest = checkManifestActivity(manifestFilePath, classPath, className, packageName, isCreateNewPackage)
            if(!isExistActivityInManifest) {
                registManifestActivity(manifestFilePath, classPath, className, packageName, isCreateNewPackage)
            }
        }
        //6.原class文件 需要删除
        if (isCreateNewPackage) {
            File oldClassFile = new File(classPath, className + ".java")
            if (oldClassFile.exists()) {
                oldClassFile.delete()
            }
        }
        //7.重置mvpInfo
        resetMvpInfo(project.projectDir.parent)
        //8.删除build文件夹
        String buildSrcBuildDir = project.projectDir.parent + File.separator + "buildSrc" + File.separator + "build"
        File buildFile = new File(buildSrcBuildDir)
        deleteFile(buildFile)
    }

    /**
     * 重置mvpInfo
     */

    static void resetMvpInfo(String parentPath) {
        String buildSrcPath = File.separator + "buildSrc" + File.separator + "src" + File.separator + "main" + File.separator + "groovy" + File.separator + "com" + File.separator + "inke" + File.separator + "plugins" + File.separator + "mvpmodel"
        String buildSrcFilePath = parentPath + buildSrcPath
        File mvpModelInfoFile = new File(buildSrcFilePath, "MvpModelInfo.groovy")
        List<String> listStart = new ArrayList<String>() //上面的注释行(一共6行)
        List<String> listCommond = new ArrayList<String>() //注释代码
        List<String> listCode = new ArrayList<String>()
        List<String> listReset = new ArrayList<String>()
        if (mvpModelInfoFile.exists()) {
            RandomAccessFile raf = new RandomAccessFile(buildSrcFilePath + File.separator + "MvpModelInfo.groovy", "rw")
            String mvpmodelLine = null
            int i = 0
            while ((mvpmodelLine = raf.readLine()) != null) {
                if (i < 6) {
                    listStart.add(new String(mvpmodelLine.getBytes("ISO8859-1"), "UTF-8"))
                } else {
                    if (i % 2 == 0) {
                        listCommond.add(new String(mvpmodelLine.getBytes("ISO8859-1"), "UTF-8"))
                    }
                }
                i++
            }

            listCode.add("    String classPath = \"\"  // /Users/guanzhen/godbase/app/src/main/java/com/ziroom/godbase/ui")
            listCode.add("    String className = \"\"//TestActivity")
            listCode.add("    boolean isCreateNewPackage = true")
            listCode.add("    boolean isCreateMvp = true")
            listReset.addAll(listStart)
            for (int index = 0; index < listCode.size(); index++) {
                listReset.add(listCommond.get(index))
                listReset.add(listCode.get(index))
            }
            listReset.add("}")

            File newMvpModelFile = new File(buildSrcFilePath, "MvpModelInfo.groovy")
            if (newMvpModelFile.exists()) {
                newMvpModelFile.delete()
            }
            newMvpModelFile.createNewFile()
            FileWriter newMvpModelWriter = new FileWriter(newMvpModelFile.getPath())
            for (int index = 0; index < listReset.size(); index++) {
                newMvpModelWriter.write(listReset.get(index))
                newMvpModelWriter.write("\n")
            }
            newMvpModelWriter.close()
        }
    }

    /**
     * 获取包名
     */

    static String getPackageName(String manifestPath) {
        RandomAccessFile raf = new RandomAccessFile(manifestPath + File.separator + "AndroidManifest.xml", "rw")
        String line2 = null
        while ((line2 = raf.readLine()) != null) {
            if (line2.contains("package=\"")) {
                break
            }
        }

        String pName = line2.replace(" ", "").replace("package=\"", "").replace("\"", "").replace(">", "")
        raf.close()
        return pName
    }

    /**
     * 校验Manifest是否存在该Activity 如果存在则不注册了
     */
    static boolean checkManifestActivity(String manifestFilePath, String classPath, String className, String packageName, boolean isCreateNewPackage) {
        String classAliasName = getClassAliasName(className) //除去Activity/Fragment的字符串
        String packagePath = getPackagePath(classPath, classAliasName, isCreateNewPackage)
        RandomAccessFile raf = new RandomAccessFile(manifestFilePath + File.separator + "AndroidManifest.xml", "rw")
        packagePath = packagePath.replace(packageName, "")
        String line = null
        boolean isExist = false
        while ((line = raf.readLine()) != null) {
            if (line.contains(packagePath)) {
                isExist = true
                break
            }
        }
        return isExist
    }


    /**
     * 注册Activity
     */
    static void registManifestActivity(String manifestFilePath, String classPath, String className, String packageName, boolean isCreateNewPackage) {
        String classAliasName = getClassAliasName(className) //除去Activity/Fragment的字符串
        String packagePath = getPackagePath(classPath, classAliasName, isCreateNewPackage)

        RandomAccessFile raf = new RandomAccessFile(manifestFilePath + File.separator + "AndroidManifest.xml", "rw")
        packagePath = packagePath.replace(packageName, "")
        String line = null
        long lastPoint = 0
        StringBuilder sb = new StringBuilder()
        sb.append("\n")
        sb.append("        <activity android:name=" + "\"" + packagePath + "." + className + "\"")
        sb.append("\n")
        sb.append("            android:screenOrientation=\"portrait\"")
        sb.append("\n")
        sb.append("            />")
        sb.append("\n")
        sb.append("    </application>")
        sb.append("\n")
        sb.append("\n")
        sb.append("</manifest>")
        while ((line = raf.readLine()) != null) {
            final long point = raf.getFilePointer()
            if (line.contains("</application>")) {
                String str = line.replace("</application>", sb.toString())
                raf.seek(lastPoint)
                raf.writeBytes(str)
            }
            lastPoint = point
        }
        raf.close()
    }

    /**
     * 创建布局文件
     */
    static void createLayout(String javaPath, String className) {
        String layoutName = getLayoutName(className) + ".xml"
        String resFilePath = javaPath + File.separator + "res" + File.separator + "layout"
        println("resFilePath: " + resFilePath)
        File fileDir = new File(resFilePath, layoutName)
        if (fileDir.exists()) {
            fileDir.delete()
        }
        fileDir.createNewFile()
        FileWriter writer = new FileWriter(fileDir.getPath())
        writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
        writer.write("\n")
        writer.write("<androidx.constraintlayout.widget.ConstraintLayout")
        writer.write("\n")
        writer.write("    xmlns:android=\"http://schemas.android.com/apk/res/android\"")
        writer.write("\n")
        writer.write("    android:layout_width=\"match_parent\"")
        writer.write("\n")
        writer.write("    android:layout_height=\"match_parent\">")
        writer.write("\n")
        writer.write("\n")
        writer.write("</androidx.constraintlayout.widget.ConstraintLayout>")
        writer.close()
    }

    /**
     * 驼峰转下划线
     */
    static String humpToLine(String str) {
        Pattern humpPattern = Pattern.compile("[A-Z]")
        Matcher matcher = humpPattern.matcher(str)
        StringBuffer sb = new StringBuffer()
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase())
        }
        matcher.appendTail(sb)
        return sb.toString()
    }

    /**
     * 创建Contract文件
     */
    static void createPresenter(String classPath, String className, boolean isCreateNewPackage) {
        String classAliasName = getClassAliasName(className) //除去Activity/Fragment的字符串
        String presenterName = getPresenterName(classAliasName)
        String contractName = getContractName(classAliasName)
        String allFilePath = getAllFilePath(classPath, classAliasName, isCreateNewPackage)
        String packagePath = getPackagePath(classPath, classAliasName, isCreateNewPackage)

        File fileDir = new File(allFilePath, presenterName + ".java")
        if (fileDir.exists()) {
            fileDir.delete()
        }
        fileDir.createNewFile()
        FileWriter writer = new FileWriter(fileDir.getPath())
        writer.write("package " + packagePath + ";")
        writer.write("\n")
        writer.write("\n")
        //导包
        writer.write("import com.ziroom.mvp.base.BaseMvpPresenter;")
        writer.write("\n")
        writer.write("\n")
        //class类
        writer.write("public class " + presenterName + " extends BaseMvpPresenter<" + contractName + ".IView> implements " + contractName + ".IPresenter {")
        writer.write("\n")
        writer.write("    public " + presenterName + "(" + contractName + ".IView view) {")
        writer.write("\n")
        writer.write("        super(view);")
        writer.write("\n")
        writer.write("    }")
        writer.write("\n")
        writer.write("}")

        writer.close()
    }

    private static String getClassAliasName(String className) {
        String classAliasName = "" //除去Activity/Fragment的字符串
        if (className.endsWith("Activity")) {
            classAliasName = className.replace("Activity", "")
        } else if (className.endsWith("Fragment")) {
            classAliasName = className.replace("Fragment", "")
        }
        return classAliasName
    }

    private static String getContractName(String classAliasName) {
        return classAliasName + "Contract"
    }

    private static String getPresenterName(String classAliasName) {
        return classAliasName + "Presenter"
    }

    private static String getPackagePath(String classPath, String classAliasName, boolean isCreateNewPackage) {
        String str = classPath.replace(File.separator, ".")
        String packagePath = "com." + str.split("com.")[1]
        if (isCreateNewPackage) {
            packagePath += "." +  classAliasName.toLowerCase()
        }
        return packagePath
    }

    private static String getLayoutName(String className) {
        String classAliasName = getClassAliasName(className) //除去Activity/Fragment的字符串
        String layoutType = ""
        if (className.endsWith("Activity")) {
            layoutType = "activity"
        } else if (className.endsWith("Fragment")) {
            layoutType = "fragment"
        }
        String layoutName = layoutType + humpToLine(classAliasName)
        return layoutName
    }

    private static String getAllFilePath(String classPath, String classAliasName, boolean isCreateNewPackage) {
        String allFilePath = classPath
        if (isCreateNewPackage) {
            String newPackageName = classAliasName.toLowerCase();
            allFilePath += File.separator + newPackageName
        }
        return allFilePath
    }

    /**
     * 创建Contract文件
     */
    static void createContract(String classPath, String className, boolean isCreateNewPackage) {
        String classAliasName = getClassAliasName(className) //除去Activity/Fragment的字符串
        String contractName = getContractName(classAliasName)
        String allFilePath = getAllFilePath(classPath, classAliasName, isCreateNewPackage)
        String packagePath = getPackagePath(classPath, classAliasName, isCreateNewPackage)

        File fileDir = new File(allFilePath, contractName + ".java")
        if (fileDir.exists()) {
            fileDir.delete()
        }
        fileDir.createNewFile()
        FileWriter writer = new FileWriter(fileDir.getPath())
        writer.write("package " + packagePath + ";")
        writer.write("\n")
        writer.write("\n")
        //导包
        writer.write("import com.ziroom.mvp.ILifeCircle;")
        writer.write("\n")
        writer.write("import com.ziroom.mvp.IMvpView;")
        writer.write("\n")
        writer.write("\n")
        //class类
        writer.write("public interface " + contractName + " {")
        writer.write("\n")
        writer.write("    interface IView extends IMvpView {")
        writer.write("\n")
        writer.write("    }")
        writer.write("\n")
        writer.write("\n")
        writer.write("    interface IPresenter extends ILifeCircle {")
        writer.write("\n")
        writer.write("    }")
        writer.write("\n")
        writer.write("}")

        writer.close()
    }


    /**
     * 创建Activity或Fragment
     * filePath 文件路径
     * classRelPath 类相对路径
     * className 类名 TestActivity
     */
    static void createActivityFragment(String classPath, String className, String packageName, boolean isCreateMvp, boolean isCreateNewPackage) {
        String classAliasName = getClassAliasName(className) //除去Activity/Fragment的字符串
        String packagePath = getPackagePath(classPath, classAliasName, isCreateNewPackage)
        String parentName = "Base"
        if (className.endsWith("Activity")) {
            parentName += "Activity"
        } else if (className.endsWith("Fragment")) {
            parentName += "Fragment"
        }
        String layoutName = getLayoutName(className)

        String allFilePath = getAllFilePath(classPath, classAliasName, isCreateNewPackage)

        File f = new File(allFilePath)
        if(!f.exists()) {
            f.mkdirs()
        }

        File fileDir = new File(allFilePath, className + ".java")
        if (fileDir.exists()) {
            fileDir.delete()
        }

        fileDir.createNewFile()

        FileWriter writer = new FileWriter(fileDir.getPath())
        writer.write("package " + packagePath + ";")
        writer.write("\n")
        writer.write("\n")
        //导包
        writer.write("import com.ziroom.base.")
        writer.write(parentName + ";")
        writer.write("\n")
        writer.write("import " + packageName + ".R;")
        writer.write("\n")
        writer.write("\n")

        //class类
        writer.write("public class " + className + " extends ")
        writer.write(parentName)

        if (isCreateMvp) {
            String presenterName = getPresenterName(classAliasName)
            String contractName = getContractName(classAliasName)
            writer.write("<" + presenterName + "> implements " + contractName + ".IView {")
        } else {
            writer.write(" {")
        }

        writer.write("\n")
        writer.write("\n")
        writer.write("    @Override")
        writer.write("\n")
        writer.write("    public int getLayoutId() {")
        writer.write("\n")
        writer.write("        return R.layout." + layoutName + ";")
        writer.write("\n")
        writer.write("    }")
        writer.write("\n")
        writer.write("\n")
        if (isCreateMvp) {
            String presenterName = getPresenterName(classAliasName)
            writer.write("    @Override")
            writer.write("\n")
            writer.write("    public " + presenterName + " getPresenter() {")
            writer.write("\n")
            writer.write("        return new " + presenterName + "(this);")
            writer.write("\n")
            writer.write("    }")
            writer.write("\n")
            writer.write("\n")
        }

        writer.write("}")

        writer.close()
    }

    void deleteFile(File file) {
        //判断文件不为null或文件目录存在
        if (file == null || !file.exists()) {
            System.out.println("文件删除失败,请检查文件路径是否正确");
            return;
        }
        //取得这个目录下的所有子文件对象
        File[] files = file.listFiles();
        //遍历该目录下的文件对象
        for (File f : files) {
            //打印文件名
            String name = file.getName();
            //判断子目录是否存在子目录,如果是文件则删除
            if (f.isDirectory()) {
                deleteFile(f);
            } else {
                f.delete();
            }
        }
        //删除空文件夹  for循环已经把上一层节点的目录清空。
        file.delete();
    }
}