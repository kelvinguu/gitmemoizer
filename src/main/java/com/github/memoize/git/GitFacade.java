package com.github.memoize.git;

import com.github.memoize.core.StaticAnalysisUtils;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.filter.PathSuffixFilter;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * User: Kelvin
 * Date: 12/2/13
 * Time: 4:41 PM
 */
public class GitFacade {

    Repository repo;
    ObjectId commitId;

    // this class only analyzes files in the HEAD commit!

    public GitFacade(String repoPath) throws IOException {
        FileRepositoryBuilder repoBuilder = new FileRepositoryBuilder();
        repoBuilder.setGitDir(new File(repoPath));
        repo = repoBuilder.build();
        commitId = repo.resolve(Constants.HEAD);
    }

    @Override
    public void finalize() throws Throwable {
        // TODO: this is not strictly necessary
        repo.close();
        super.finalize();
    }

    public Map<String, String> getSourceFiles() throws IOException {

        GitProcessor processor = new SimpleGitProcessor(repo);
        processor.setFilter(PathSuffixFilter.create(".java"));

        GitIterable files = new GitIterable(repo, commitId, processor);

        Map<String, String> sourceFiles = new HashMap<String, String>();
        for (Object file : files) {

            GitFile gitFile = (GitFile) file;
            String className = gitFile.getClassName();
            String source = gitFile.getContent();
            String pkgName = StaticAnalysisUtils.extractPackage(source);

            // TODO: use conditional operator
            String fullName;
            if (pkgName != null) {
                fullName = pkgName + "." + className;
            } else {
                fullName = className;
            }
            sourceFiles.put(fullName, source);
        }
        return sourceFiles;
    }

}
