import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Created by admin on 2017/7/26.
 */
public class JarFileFilter extends FileFilter{
    public boolean accept(File f) {
        if (f.isDirectory()) return  true;
        return f.getName().endsWith(".jar");
    }

    public String getDescription() {
        return ".jar";
    }
}
