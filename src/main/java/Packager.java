




import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.jar.*;
import java.util.zip.ZipEntry;

/**
 * Created by admin on 2017/7/18.
 */
public class Packager {

    private final static int MAGIC = 0xCAFEBABE;
    /* 主窗体里面的若干元素 */
    private static JFrame mainForm = new JFrame("增量发布小工具"); // 主窗体
    private static JLabel label1 = new JLabel("请选择待替换Jar包：");
    private static JLabel label2 = new JLabel("请选择需要替换的class文件：");
    static JTextField sourcefile = new JTextField(); // 选择待替换Jar包
    static JTextField targetfile = new JTextField(); // 选择需要替换的class文件
    static JButton buttonBrowseSource = new JButton("浏览"); // 浏览按钮
    static JButton buttonBrowseTarget = new JButton("浏览"); // 浏览按钮
    static JButton button = new JButton("开始替换"); // 加密按钮


    public Packager(){
        Container container = mainForm.getContentPane();
    /* 设置主窗体属性 */
        mainForm.setSize(400, 270);// 设置主窗体大小
        mainForm.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);// 设置主窗体关闭按钮样式
        mainForm.setLocationRelativeTo(null);// 设置居于屏幕中央
        mainForm.setResizable(false);// 设置窗口不可缩放
        mainForm.setLayout(null);
        mainForm.setVisible(true);// 显示窗口
    /* 设置各元素位置布局 */
        label1.setBounds(30, 10, 300, 30);
        sourcefile.setBounds(50, 50, 200, 30);
        buttonBrowseSource.setBounds(270, 50, 60, 30);
        label2.setBounds(30, 90, 300, 30);
        targetfile.setBounds(50, 130, 200, 30);
        buttonBrowseTarget.setBounds(270, 130, 60, 30);
        button.setBounds(150, 180, 100, 30);
    /* 为各元素绑定事件监听器 */
        buttonBrowseSource.addActionListener(new BrowseAction()); // 为源文件浏览按钮绑定监听器，点击该按钮调用文件选择窗口
        buttonBrowseTarget.addActionListener(new BrowseAction()); // 为目标位置浏览按钮绑定监听器，点击该按钮调用文件选择窗口
        button.addActionListener(new PackageAction());
        sourcefile.setEditable(false);// 设置源文件文本域不可手动修改
        targetfile.setEditable(false);// 设置目标位置文本域不可手动修改
        container.add(label1);
        container.add(label2);
        container.add(sourcefile);
        container.add(targetfile);
        container.add(buttonBrowseSource);
        container.add(buttonBrowseTarget);
        container.add(button);
        mainForm.repaint();
    }







    public static void main(String[] args) throws Exception {
        new Packager();

    }




}
