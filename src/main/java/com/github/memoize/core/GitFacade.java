package com.github.memoize.core;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;

public class GitFacade {
    private Repository repo;

    public GitFacade(File repoPath) throws IOException {
        FileRepositoryBuilder repoBuilder = new FileRepositoryBuilder();
        repoBuilder.setGitDir(repoPath);
        repoBuilder.readEnvironment().findGitDir();
        repo = repoBuilder.build();
    }

    public String getCommitSHA(String revisionStr) throws IOException {
        return repo.resolve(revisionStr).getName();
    }
}
