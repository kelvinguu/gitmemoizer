package com.github.memoize.core;

import com.kelvingu.giterable.Giterable;
import org.apache.commons.io.FilenameUtils;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * User: Kelvin
 * Date: 12/2/13
 * Time: 6:59 PM
 */
public class StaticAnalysisUtils {

    public static void main(String[] args) throws IOException {

        String repoPath = "/Users/Kelvin/Dropbox/projects/giterable/code/.git";
        File repoFile = new File(repoPath);
        Giterable gitFiles = new Giterable(repoFile, "HEAD");

        List<File> sourceFiles = new ArrayList<File>();
        for (File file : gitFiles) {
            String ext = FilenameUtils.getExtension(file.getName());
            if (ext.equals("java")) {
                sourceFiles.add(file);
            }
        }


    }
}
