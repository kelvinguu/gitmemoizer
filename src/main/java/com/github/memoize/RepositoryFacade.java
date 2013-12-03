package com.github.memoize;

import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathSuffixFilter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Kelvin
 * Date: 12/2/13
 * Time: 4:41 PM
 */
public class RepositoryFacade {

    Repository repo;
    RevTree tree;

    // this class only analyzes files in the HEAD commit!

    public RepositoryFacade(String repoPath) throws IOException {
        FileRepositoryBuilder repoBuilder = new FileRepositoryBuilder();
        repoBuilder.setGitDir(new File(repoPath));
        repo = repoBuilder.build();

        ObjectId commitId = repo.resolve(Constants.HEAD);
        tree = new RevWalk(repo).parseCommit(commitId).getTree();
    }

    private List<ObjectId> findFilesByName(String fileName) throws IOException {
        TreeWalk treeWalk = new TreeWalk(repo);
        treeWalk.addTree(tree);
        treeWalk.setRecursive(true);

        // TODO: find exact file name match filter
        // filters out all files without fileName as suffix
        // this is more efficient that filtering in the while loop
        treeWalk.setFilter(PathSuffixFilter.create(fileName));

        List<ObjectId> objectIds = new ArrayList<ObjectId>();
        while (treeWalk.next()) {
            // ensures that the file name is exactly equal to fileName, rather
            // than just having a matching suffix
            // TODO: why does ObjectId have an index argument?
            if (fileName.equals(treeWalk.getNameString())) {
                objectIds.add(treeWalk.getObjectId(0));
            }
        }
        return objectIds;
    }

    private String gitObjectToString(ObjectId objectId) throws IOException {
        // TODO: actually return a string
        ObjectLoader loader = repo.open(objectId);
        return new String(loader.getBytes());
    }

    // TODO: give more specific exception
    public String getFileAsString(String fileName) throws Exception {
        List<ObjectId> objectIds = findFilesByName(fileName);

        if (objectIds.size() == 0) {
            throw new Exception(fileName + " not found");
        }

        if (objectIds.size() != 1) {
            throw new Exception(fileName + " does not uniquely specify file");
        }
        return gitObjectToString(objectIds.get(0));
    }

}
