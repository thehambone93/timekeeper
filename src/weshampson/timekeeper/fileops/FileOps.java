
package weshampson.timekeeper.fileops;

import java.io.File;

/**
 *
 * @author  Wes Hampson
 * @version 0.3.0 (Nov 6, 2014)
 * @since   0.3.0 (Nov 6, 2014)
 */
public class FileOps {
    private static final String fileSeparator = System.getProperty("file.separator");
    public static String getRelativePath(String absolutePath, String relativeTo) {
        String regex = fileSeparator;
        if (fileSeparator.equals("\\")) {
            regex = "\\\\";
        }
        File absoluteFile = new File(absolutePath);
        File relativeFile = new File(relativeTo);
        if (absoluteFile.getAbsolutePath().equals(relativeFile.getAbsolutePath())) {
            return(".");
        }
        String[] absoluteDirs = absoluteFile.getAbsolutePath().split(regex);
        String[] relativeDirs = relativeFile.getAbsolutePath().split(regex);
        int length = absoluteDirs.length < relativeDirs.length ? absoluteDirs.length : relativeDirs.length;
        int lastCommonRoot = -1;
        int index;
        for (index = 0; index < length; index++) {
            if (absoluteDirs[index].equals(relativeDirs[index])) {
                lastCommonRoot = index;
            } else {
                break;
            }
        }
        if (lastCommonRoot == -1) {
            return(relativeFile.getAbsolutePath());
        }
        StringBuilder relativePath = new StringBuilder();
        if (lastCommonRoot + 1 == absoluteDirs.length) {
            relativePath.append(".").append(fileSeparator);
        } else {
            for (index = lastCommonRoot + 1; index < absoluteDirs.length; index++) {
                if (absoluteDirs[index].length() > 0) {
                    relativePath.append("..").append(fileSeparator);
                }
            }
        }
        for (index = lastCommonRoot + 1; index < relativeDirs.length - 1; index++) {
            relativePath.append(relativeDirs[index]).append(fileSeparator);
        }
        relativePath.append(relativeDirs[relativeDirs.length - 1]);
        return(relativePath.toString());
    }
}
