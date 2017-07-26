import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * Created by admin on 2017/7/26.
 */
public class PackageAction implements ActionListener {

    private final static int MAGIC = 0xCAFEBABE;

    /**
     * 通过.class文件的路径来获取包名
     *
     * @param fileName
     * @return
     */
    public  String getPackage(String fileName) {
        DataInputStream in = null;

        try {
            in = new DataInputStream(new BufferedInputStream(new FileInputStream(fileName)));
            if (in.readInt() != MAGIC) {
                throw new IOException(fileName + "非Java class文件!");
            }

            in.readUnsignedShort();// 次版本号
            in.readUnsignedShort();// 主版本号
            int i = in.readUnsignedShort();// 常量池长度
            Map<Integer, Integer> classInfoMap = new HashMap<Integer, Integer>();
            Map<Integer, String> classNameMap = new HashMap<Integer, String>();

            //常量池
            for (int j = 1; j < i; ++j) {
                int k = in.readUnsignedByte();
                switch (k) {
                    case 7:
                        classInfoMap.put(j, in.readUnsignedShort());
                        break;
                    case 6:
                        in.readDouble();
                        ++j;
                        break;
                    case 9:
                        in.readUnsignedShort();
                        in.readUnsignedShort();
                        break;
                    case 4:
                        in.readFloat();
                        break;
                    case 3:
                        in.readInt();
                        break;
                    case 11:
                        in.readUnsignedShort();
                        in.readUnsignedShort();
                        break;
                    case 18:
                        in.readUnsignedShort();
                        in.readUnsignedShort();
                        break;
                    case 5:
                        in.readLong();
                        ++j;
                        break;
                    case 15:
                        in.readUnsignedByte();
                        in.readUnsignedShort();
                        break;
                    case 16:
                        in.readUnsignedShort();
                        break;
                    case 10:
                        in.readUnsignedShort();
                        in.readUnsignedShort();
                        break;
                    case 12:
                        in.readUnsignedShort();
                        in.readUnsignedShort();
                        break;
                    case 8:
                        in.readUnsignedShort();
                        break;
                    case 1:
                        classNameMap.put(j, in.readUTF());
                        break;
                    case 2:
                    case 13:
                    case 14:
                    case 17:
                    default:
                        throw new RuntimeException("class文件错误！");
                }
            }

            //access flag
            in.readUnsignedShort();

            //this class
            int thisClazz = in.readUnsignedShort();

            return classNameMap.get(classInfoMap.get(thisClazz));

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 获取jar包的name（不含后缀）
     *
     * @param path
     * @return
     */
    public  String getJarName(String path) {
        int leftIndex = path.lastIndexOf("\\");
        int rightIndex = path.lastIndexOf(".");
        return path.substring(leftIndex + 1, rightIndex);
    }

    /**
     * 解压Jar包
     *
     * @param src
     * @param desDir
     * @throws FileNotFoundException
     * @throws IOException
     */
    public  void unJar(File src, File desDir) throws FileNotFoundException, IOException {
        JarFile jarFile = new JarFile(src);
        Enumeration<JarEntry> jarEntrys = jarFile.entries();
        if (!desDir.exists()) desDir.mkdirs(); //建立用户指定存放的目录
        byte[] bytes = new byte[1024];

        while (jarEntrys.hasMoreElements()) {
            ZipEntry entryTemp = jarEntrys.nextElement();
            File desTemp = new File(desDir.getAbsoluteFile() + File.separator + entryTemp.getName());

            if (entryTemp.isDirectory()) {    //jar条目是空目录
                if (!desTemp.exists()) desTemp.mkdirs();
            } else {    //jar条目是文件
                //因为manifest的Entry是"META-INF/MANIFEST.MF",写出会报"FileNotFoundException"
                File desTempParent = desTemp.getParentFile();
                if (!desTempParent.exists()) desTempParent.mkdirs();

                BufferedInputStream in = new BufferedInputStream(jarFile.getInputStream(entryTemp));
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(desTemp));

                int len = in.read(bytes, 0, bytes.length);
                while (len != -1) {
                    out.write(bytes, 0, len);
                    len = in.read(bytes, 0, bytes.length);
                }
                in.close();
                out.flush();
                out.close();
            }
        }
        jarFile.close();
    }

    /**
     * 通过文件路径复制文件到指定目录下
     *
     * @param oldPath
     * @param desDir
     */
    public  boolean copy(String oldPath, String desDir) {
        try {
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(desDir);
                byte[] buffer = new byte[1024];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
            return true;
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除文件
     *
     * @param targetFile
     */
    public  void deleteFile(File targetFile) {
        // 路径为文件且不为空则进行删除
        if (targetFile.isFile() && targetFile.exists()) {
            targetFile.delete();
        }
    }

    /**
     * 删除目录
     *
     * @param targetFile
     */
    public  void deleteDirectory(File targetFile) {
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (targetFile.exists() && targetFile.isDirectory()) {
            //删除文件夹下的所有文件(包括子目录)
            File[] files = targetFile.listFiles();
            for (int i = 0; i < files.length; i++) {
                //删除子文件
                if (files[i].isFile()) {
                    deleteFile(files[i]);
                }
                //删除子目录
                else {
                    deleteDirectory(files[i]);
                }
            }
            targetFile.delete();
        }
    }

    public void actionPerformed(ActionEvent e) {
        String jarPath = Packager.sourcefile.getText();
        String classPath = Packager.targetfile.getText();
//        String classPath = "G:\\test\\\\SettlementDiscountUploadListener.class";
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(jarPath);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        String jarName = getJarName(jarFile.getName());

//        System.out.println(jarName);

        // 解压jar包到项目当前工作路径
        String workDir = new File("").getAbsolutePath(); // 当前工作路径
        File src = new File(jarPath);
        File desDir = new File(workDir + File.separator + jarName);
//        System.out.println(desDir.getAbsolutePath());
        try {
            unJar(src, desDir);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        String className = getPackage(classPath);
//        System.out.println(className);
//        System.out.println(className.substring(0,className.lastIndexOf("/")));
        Enumeration<JarEntry> entrys = jarFile.entries();
        while (entrys.hasMoreElements()) {
            JarEntry jarEntry = entrys.nextElement();
//            System.out.println(jarEntry.getName());
            if (jarEntry.getName().equals("BOOT-INF/classes/" + className + ".class")){
//                System.out.println(jarEntry.getName());
//                System.out.println("BOOT-INF/classes/" + className + ".class");
//                System.out.println(desDir.getAbsolutePath() + "\\BOOT-INF\\classes\\"
//                        + className.replace("/", "\\") + ".class");
                if (copy(classPath, desDir.getAbsolutePath() + "\\BOOT-INF\\classes\\"
                        + className.replace("/", "\\") + ".class")){
                    System.out.println("复制成功！");
                    break;
                }
            }
            else if (jarEntry.getName().endsWith(".jar")) {
                JarFile nextJarFile = null;
                try {
                    nextJarFile = new JarFile(workDir + File.separator + jarName + File.separator
                            + jarEntry.getName());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
//                System.out.println(workDir + File.separator + jarName + File.separator
//                        + jarEntry.getName());
//                System.out.println(getJarName(nextJarFile.getName()));
                Enumeration<JarEntry> nextEntrys = nextJarFile.entries();
                while (nextEntrys.hasMoreElements()) {
                    JarEntry temp = nextEntrys.nextElement();
//                    System.out.println(temp.getName());
                    if (temp.getName().equals(className + ".class")){
                        File nextFile = new File(workDir + File.separator + jarName + File.separator
                                + jarEntry.getName());
//                        System.out.println(temp.getName());
                        String tempName = getJarName(nextJarFile.getName());
//                        System.out.println(tempName);
                        File tempDir = new File(nextFile.getParent() + File.separator + tempName);
                        try {
                            unJar(nextFile, tempDir);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
//                        System.out.println(tempDir.getAbsolutePath());
//                        System.out.println(tempDir.getAbsolutePath() + File.separator
//                                + className.replace("/", "\\") + ".class");
                        if (copy(classPath, tempDir.getAbsolutePath() + File.separator
                                + className.replace("/", "\\") + ".class")){
                            System.out.println("复制成功！");
                        }
//                        jar(tempDir, new File(tempDir.getAbsolutePath() + ".jar"));

                        Runtime rt = Runtime.getRuntime();
                        System.out.println("cmd /c jar cvfm0 " + tempDir.getAbsolutePath() + ".jar " + tempDir.getAbsolutePath()
                                + "/META-INF/MANIFEST.MF -C " + tempDir.getAbsolutePath() + "/ .");

                        Process p = null;
                        try {
                            p = rt.exec("cmd /c jar cvfm0 " + tempDir.getAbsolutePath() + ".jar " + tempDir.getAbsolutePath()
                                    + "/META-INF/MANIFEST.MF -C " + tempDir.getAbsolutePath() + "/ .");
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        InputStream inputStream1 = p.getInputStream();
                        InputStream inputStream2 = p.getErrorStream();
                        cleanThread(inputStream1);
                        cleanThread(inputStream2);
                        try {
                            p.waitFor();
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }

                        System.out.println("打包完成");
                        while (tempDir.exists()){
                            deleteDirectory(tempDir);
                        }
                        break;
                    }
                }
            }
        }
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            process = runtime.exec("cmd /c jar cvfm0 " + jarName
                    + ".jar " + jarName + "/META-INF/MANIFEST.MF -C " + jarName + "/ .");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        final InputStream is1 = process.getInputStream();
        final InputStream is2 = process.getErrorStream();
        cleanThread(is1);
        cleanThread(is2);
        try {
            process.waitFor();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        while (desDir.exists()){
            deleteDirectory(desDir);
        }

    }

    /**
     * 清除开启子线程调用jar命令的输入流和错误流，防止阻塞
     * @param inputStream
     */
    public void cleanThread(final InputStream inputStream) {
        new Thread() {
            @Override
            public void run() {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = null;
                try {
                    while ((line = bufferedReader.readLine()) != null) {
//                        System.out.println(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
