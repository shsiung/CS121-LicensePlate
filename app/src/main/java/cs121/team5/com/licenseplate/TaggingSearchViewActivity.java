package cs121.team5.com.licenseplate;

import android.os.Environment;
import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

/**
 * Created by Huameng on 2014/10/14.
 */
public class TaggingSearchViewActivity {

    public File[] Search(String keyword) {
        String myDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();
        File f = new File(myDirectory);
        if (f.exists() && f.isDirectory()) {
            // the regex has to be written
            final Pattern p = Pattern.compile("*" + keyword + "*");

            File[] flists = f.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return p.matcher(file.getName()).matches();
                }
            });
            return flists;
        } else {
            return null;
        }
    }
}
