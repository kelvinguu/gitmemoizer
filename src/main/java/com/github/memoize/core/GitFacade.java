package com.github.memoize.core;

import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;

/**
 * User: Kelvin
 * Date: 12/14/13
 * Time: 11:10 PM
 */
public class GitFacade {
    private Repository repo;

    public GitFacade(File repoPath) throws IOException {
        FileRepositoryBuilder repoBuilder = new FileRepositoryBuilder();
        repoBuilder.setGitDir(repoPath);
        repoBuilder.readEnvironment().findGitDir();
        Repository repo = repoBuilder.build();
    }

    public String getCommitSHA(String revisionStr) throws IOException {
        return repo.resolve(revisionStr).toString();
    }
}
