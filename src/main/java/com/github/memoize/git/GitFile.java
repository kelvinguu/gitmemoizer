package com.github.memoize.git;

import java.io.File;

/**
 * User: Kelvin
 * Date: 12/4/13
 * Time: 6:24 PM
 */
public class GitFile {

    private String filePath;
    private String content;

    public GitFile(String filePath, String content) {
        this.filePath = filePath;
        this.content = content;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getContent() {
        return content;
    }

    public String getClassName() {
        String fileName = new File(filePath).getName();
        int extStart = fileName.lastIndexOf('.');
        return fileName.substring(0, extStart);
    }
}
