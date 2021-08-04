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
        String allPath = info.allPath //全路径
        String className = info.className //类名称
        String manifestPath = info.manifestPath //Manifest全路径
        println("isCreateNewPackage: " + isCreateNewPackage)
        println("isCreateMvp: " + isCreateMvp)
        println("allPath: " + allPath)
        println("className: " + className)
        println("manifestPath: " + manifestPath)

        String rootDir = project.projectDir
        rootDir = rootDir.replace(File.separator + "app", "")
        println("rootDir: " + rootDir)
        if(allPath == null || "".equals(allPath)) {
            return
        }

        String allDir = rootDir + File.separator + allPath.replace(".", "/") + File.separator
        println("allDir: " + allDir) //类全路径

        String manifestDir = rootDir + File.separator + manifestPath.replace(".", "/") + File.separator
        println("manifestDir: " + manifestDir) //manifest路径

        String javaDir = allDir.split(File.separator + "java")[0] + File.separator + "java" + File.separator //main的路径
        println("javaDir: " + javaDir)

        String layoutPath = allDir.split(File.separator + "java")[0] + File.separator + "res" + File.separator + "layout" + File.separator
        println("layoutPath: " + layoutPath) //布局的路径

        if (!className.endsWith("Activity") && !className.endsWith("Fragment")) return

        //类的别名Test 除去Activity 和 Fragment 的
        String classAliasName = ""
        if (className.endsWith("Activity")) {
            classAliasName = className.replace("Activity", "")
        } else if (className.endsWith("Fragment")) {
            classAliasName = className.replace("Fragment", "")
        }
        println("classAliasName: " + classAliasName)

        String mvpFilePackage = classAliasName.toLowerCase();
        println("mvpFilePackage: " + mvpFilePackage) //mvp的文件包名

        String presenterName = classAliasName + "Presenter"
        println("presenterName: " + presenterName)
        String contractName = classAliasName + "Contract"
        println("contractName: " + contractName)
        //创建MVP文件夹
        File fileDir = null
        if (isCreateNewPackage) {
            fileDir = new File(allDir, mvpFilePackage)
        } else {
            fileDir = new File(allDir)
        }
        if (!fileDir.exists()) {
            fileDir.mkdirs()
        }

        //在新文件夹下创建三个文件
        //新文件夹的路径
        String newFilePath = fileDir.getPath()
        println("newFilePath: " + newFilePath)
        // 0.获取包名
        String packageName = getPackageName(manifestDir)
        println("packageName: " + packageName)
        //1.创建class文件
        String classRelPath = allPath.split("java.")[1] //class相对路径 com.inke.inkebone
        if(isCreateNewPackage) {
            classRelPath = classRelPath + "." + mvpFilePackage
        }
        println("classRelPath: " + classRelPath)
        createActivityFragment(newFilePath, classRelPath, className, presenterName, contractName, classAliasName, packageName, isCreateMvp)
        if (isCreateMvp) {
            // 2.创建Contract文件
            createContract(newFilePath, classRelPath, contractName)
            // 3.创建Presenter文件
            createPresenter(newFilePath, classRelPath, contractName, presenterName)
        }
        // 4.创建布局文件
        createLayout(layoutPath, classAliasName, className)
        // 5.注册Activity
        if (className.endsWith("Activity")) {
            registManifestActivity(manifestDir, packageName, classRelPath, className)
        }
        //6.原class文件 需要删除
        if (isCreateNewPackage) {
            File oldClassFile = new File(allDir, className + ".java")
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
        List<String> listStart = new ArrayList<String>()
        List<String> listCommond = new ArrayList<String>()
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

            listCode.add("    String allPath = \"\"  //app.src.main.java.com.inke.inkebone")
            listCode.add("    String className = \"\"//TextActivity")
            listCode.add("    String manifestPath = \"\"//app.src.main")
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
     * 注册Activity
     */
    static void registManifestActivity(String manifestPath, String packageName, String classRelPath, String className) {
        RandomAccessFile raf = new RandomAccessFile(manifestPath + "AndroidManifest.xml", "rw")
        classRelPath = classRelPath.replace(packageName, "")
        String line = null
        long lastPoint = 0
        StringBuilder sb = new StringBuilder()
        sb.append("\n")
        sb.append("        <activity android:name=" + "\"" + classRelPath + "." + className + "\"")
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
    static void createLayout(String layoutPath, String classAliasName, String className) {
        String layoutType = ""
        if (className.endsWith("Activity")) {
            layoutType = "activity"
        } else if (className.endsWith("Fragment")) {
            layoutType = "fragment"
        }
        String layoutName = layoutType + humpToLine(classAliasName) + ".xml"
        File fileDir = new File(layoutPath, layoutName)
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
    static void createPresenter(String filePath, String classPath, String contractName, String presenterName) {
        File fileDir = new File(filePath, presenterName + ".java")
        if (fileDir.exists()) {
            fileDir.delete()
        }
        fileDir.createNewFile()
        FileWriter writer = new FileWriter(fileDir.getPath())
        writer.write("package " + classPath + ";")
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

    /**
     * 创建Contract文件
     */
    static void createContract(String filePath, String classPath, String contractName) {
        File fileDir = new File(filePath, contractName + ".java")
        if (fileDir.exists()) {
            fileDir.delete()
        }
        fileDir.createNewFile()
        FileWriter writer = new FileWriter(fileDir.getPath())
        writer.write("package " + classPath + ";")
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
    static void createActivityFragment(String filePath, String classRelPath, String className, String presenterName, String contractName, String classAliasName, String packageName, boolean isCreateMvp) {
        String layoutType = ""
        if (className.endsWith("Activity")) {
            layoutType = "activity"
        } else if (className.endsWith("Fragment")) {
            layoutType = "fragment"
        }
        String layoutName = layoutType + humpToLine(classAliasName)
        println("layoutName: " + layoutName) //布局文件

        File fileDir = new File(filePath, className + ".java")
        if (fileDir.exists()) {
            fileDir.delete()
        }
        fileDir.createNewFile()
        FileWriter writer = new FileWriter(fileDir.getPath())
        writer.write("package " + classRelPath + ";")
        writer.write("\n")
        writer.write("\n")
        //导包
        writer.write("import com.ziroom.base.")

        String parentName = ""

        if (className.endsWith("Activity")) {
            parentName = "BaseActivity"
        } else if (className.endsWith("Fragment")) {
            parentName = "BaseFragment"
        }
        writer.write(parentName + ";")
        writer.write("\n")
        writer.write("import " + packageName + ".R;")
        writer.write("\n")
        writer.write("\n")

        //class类
        writer.write("public class " + className + " extends ")
        writer.write(parentName)

        if (isCreateMvp) {
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