import javax.swing.*;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by admin on 2017/7/26.
 */
public class BrowseAction implements ActionListener {

    static JFileChooser jarFileChooser = new JFileChooser();
    static JFileChooser classFileChooser = new JFileChooser();

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(Packager.buttonBrowseSource)){
            JarFileFilter jarFileFilter = new JarFileFilter();
            jarFileChooser.setFileFilter(jarFileFilter);
            jarFileChooser.setDialogTitle("请选择.jar文件");
            int returnVal = jarFileChooser.showOpenDialog(Packager.buttonBrowseSource);
            if (returnVal == JFileChooser.APPROVE_OPTION){
                String filepath = jarFileChooser.getSelectedFile().getPath();
                Packager.sourcefile.setText(filepath);
            }
        }else if (e.getSource().equals(Packager.buttonBrowseTarget)){
            ClassFileFilter classFileFilter = new ClassFileFilter();
            classFileChooser.setMultiSelectionEnabled(true);
            classFileChooser.setFileFilter(classFileFilter);
            classFileChooser.setDialogTitle("请选择.class文件");
            int returnVal = classFileChooser.showOpenDialog(Packager.buttonBrowseTarget);
            if (returnVal == JFileChooser.APPROVE_OPTION){
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < classFileChooser.getSelectedFiles().length; i++){
                    stringBuffer.append(classFileChooser.getSelectedFiles()[i].getPath());
                    if(i != classFileChooser.getSelectedFiles().length-1){
                        stringBuffer.append(";");
                    }
                }
                Packager.targetfile.setText(stringBuffer.toString());
            }
        }

    }
}
