package com.github.memoize.git;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.TreeFilter;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * User: Kelvin
 * Date: 12/4/13
 * Time: 5:06 PM
 */

public class GitIterator implements Iterator {

    private TreeWalk treeWalk;
    private GitProcessor processor;

    private boolean nextExists;
    private Object nextItem;

    public GitIterator(Repository repo, ObjectId commitId, GitProcessor processor) throws IOException {

        // obtain treeWalk
        RevTree commitTree = new RevWalk(repo).parseCommit(commitId).getTree();
        treeWalk = new TreeWalk(repo);
        treeWalk.addTree(commitTree);
        treeWalk.setRecursive(true);

        if (processor == null) {
            this.processor = new SimpleGitProcessor(repo);
        } else {
            this.processor = processor;
            treeWalk.setFilter(processor.getFilter());
        }

        // TODO: does this make us miss the first item?
        update();
    }

    public boolean hasNext() {
        return nextExists;
    }

    public Object next() {
        Object returnItem;
        if (nextExists) {
            returnItem = nextItem;
        } else {
            throw new NoSuchElementException();
        }

        update();
        return returnItem;
    }

    // TODO: we could get into messy state if update fails
    private void update() {
        // attempt to advance treeWalk
        try {
            nextExists = treeWalk.next();
            if (nextExists) {
                nextItem = processor.process(treeWalk);
            } else {
                nextItem = null;
            }
        } catch (IOException e) {
            nextExists = false;
            nextItem = null;
        }
    }

    public void remove() {
        // remove does not do anything
    }
}
