package com.github.memoize.git;

import java.io.File;

/**
 * User: Kelvin
 * Date: 12/4/13
 * Time: 6:24 PM
 */
public class GitFile {

    private String gitFilePath;
    private String content;

    public GitFile(String gitFilePath, String content) {
        this.gitFilePath = gitFilePath;
        this.content = content;
    }

    public String getGitFilePath() {
        return gitFilePath;
    }

    public String getContent() {
        return content;
    }

    public String getClassName() {
        String fileName = new File(gitFilePath).getName();
        int extStart = fileName.lastIndexOf('.');
        return fileName.substring(0, extStart);
    }
}
