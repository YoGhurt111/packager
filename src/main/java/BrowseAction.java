import javax.swing.*;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by admin on 2017/7/26.
 */
public class BrowseAction implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(Packager.buttonBrowseSource)){
            JFileChooser fcDlg = new JFileChooser();
            fcDlg.setDialogTitle("请选择.jar文件");
            int returnVal = fcDlg.showOpenDialog(Packager.buttonBrowseSource);
            if (returnVal == JFileChooser.APPROVE_OPTION){
                String filepath = fcDlg.getSelectedFile().getPath();
                Packager.sourcefile.setText(filepath);
            }
        }else if (e.getSource().equals(Packager.buttonBrowseTarget)){
            JFileChooser fcDlg = new JFileChooser();
            fcDlg.setDialogTitle("请选择.class文件");
            int returnVal = fcDlg.showOpenDialog(Packager.buttonBrowseTarget);
            if (returnVal == JFileChooser.APPROVE_OPTION){
                String filepath = fcDlg.getSelectedFile().getPath();
                Packager.targetfile.setText(filepath);
            }
        }

    }
}
