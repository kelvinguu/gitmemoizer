package com.github.memoize.git;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.TreeFilter;

import java.io.IOException;

/**
 * User: Kelvin
 * Date: 12/4/13
 * Time: 7:17 PM
 */
public class SimpleGitProcessor implements GitProcessor {

    private Repository repo;
    private TreeFilter filter;

    public SimpleGitProcessor(Repository repo) {
        this.repo = repo;
        // the null filter matches everything
        filter = null;
    }

    @Override
    public GitFile process(TreeWalk treeWalk) throws IOException {
        String gitFilePath = treeWalk.getPathString();
        // TODO: why does ObjectId have an index argument?
        ObjectId fileId = treeWalk.getObjectId(0);
        String content = getFileContent(fileId);
        return new GitFile(gitFilePath, content);
    }

    private String getFileContent(ObjectId fileId) throws IOException {
        ObjectLoader loader = repo.open(fileId);
        return new String(loader.getBytes());
    }

    @Override
    public void setFilter(TreeFilter filter) {
        this.filter = filter;
    }

    @Override
    public TreeFilter getFilter() {
        return filter;
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
