package com.github.memoize;

import com.github.memoize.core.GitMemoizer;
import com.github.memoize.core.StaticAnalysisUtils;
import com.github.memoize.git.GitFacade;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Tester {

    private static Logger logger = Logger.getLogger(Tester.class);

    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.DEBUG);

        //Method[] targetMethods = GitMemoizer.class.getMethods();
        //Method targetMethod = targetMethods[0];
        //StaticAnalysisUtils.extractMethodDefinition(targetMethod, "hello");



        //String repoPath = "/Users/Kelvin/Dropbox/projects/memoize/code/.git";
        //GitFacade git = new GitFacade(repoPath);
        //git.getSourceFiles();

        Calculator calc = new Calculator();

        // result will be calculated and stored in cache
        logger.debug("First result: " + calc.sum(1, 2));

        // result will be retrieved from cache
        logger.debug("Second result: " + calc.sum(1, 2));
    }
}