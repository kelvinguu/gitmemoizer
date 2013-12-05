package com.github.memoize.git;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;

import java.io.IOException;
import java.util.Iterator;

/**
 * User: Kelvin
 * Date: 12/4/13
 * Time: 5:14 PM
 */
public class GitIterable implements Iterable {

    private GitIterator gitIterator;

    public GitIterable(Repository repo, ObjectId commitId, GitProcessor processor) throws IOException {
        gitIterator = new GitIterator(repo, commitId, processor);
    }

    public Iterator iterator() {
        return gitIterator;
    }

}
