package com.github.memoize.git;

import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.TreeFilter;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Kelvin
 * Date: 12/4/13
 * Time: 7:16 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GitProcessor {

    public Object process(TreeWalk treeWalk) throws IOException;

    public void setFilter(TreeFilter filter);

    public TreeFilter getFilter();
}
