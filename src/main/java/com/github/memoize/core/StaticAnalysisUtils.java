package com.github.memoize.core;

import com.kelvingu.giterable.Giterable;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * User: Kelvin
 * Date: 12/2/13
 * Time: 6:59 PM
 */
public class StaticAnalysisUtils {

    public static void main(String[] args) throws IOException {

        String repoPath = "/Users/Kelvin/Dropbox/projects/giterable/code/.git";
        File repoFile = new File(repoPath);
        Giterable gitFiles = new Giterable(repoFile, "HEAD");

        List<File> sourceFiles = new ArrayList<File>();
        for (File file : gitFiles) {
            String ext = FilenameUtils.getExtension(file.getName());
            if (ext.equals("java")) {
                sourceFiles.add(file);
            }
        }

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.
                getStandardFileManager(null, null, null);

        Iterable<? extends JavaFileObject> compilationUnits1 =
                fileManager.getJavaFileObjectsFromFiles(sourceFiles);


        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null,
                null, null, compilationUnits1);


        @SupportedSourceVersion(SourceVersion.RELEASE_7)
        @SupportedAnnotationTypes("*")
        class CodeAnalyzerProcessor extends AbstractProcessor {
            @Override
            public boolean process(Set<? extends TypeElement> annotations,
                                   RoundEnvironment roundEnvironment) {
                for (Element e : roundEnvironment.getRootElements()) {
                    System.out.println("Element is "+ e.getSimpleName());
                    // Add code here to analyze each root element
                }
                return true;
            }
        }

        LinkedList<AbstractProcessor> processors = new LinkedList<AbstractProcessor>();
        processors.add(new CodeAnalyzerProcessor());
        task.setProcessors(processors);
        task.call();
    }
    //public static String extractMethodDefinition(Method targetMethod, String source) {
    //    String name = targetMethod.getName();
    //    // TODO: actually extract method source
    //    return source;
    //}
    //
    //public static String extractPackage(String source) {
    //    String[] lines = source.split("\n");
    //    for (String line : lines) {
    //        // TODO: this does not necessarily match all valid pkg statements
    //        if (line.matches("^package \\S+;$")) {
    //            return line.substring(0,line.length()-1).split(" ")[1];
    //        }
    //    }
    //    return null;
    //}
}
