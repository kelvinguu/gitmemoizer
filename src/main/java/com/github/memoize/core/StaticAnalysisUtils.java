package com.github.memoize.core;

import com.kelvingu.giterable.Giterable;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;
import org.apache.commons.io.FilenameUtils;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * User: Kelvin
 * Date: 12/2/13
 * Time: 6:59 PM
 */
public class StaticAnalysisUtils {

    public static void main(String[] args) throws IOException, ParseException {

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

        FileInputStream in = new FileInputStream(sourceFiles.get(1));

        // TODO: make it take a string, not file
        CompilationUnit cu = JavaParser.parse(in);
        in.close();

        Map<String, String> methodSources = getMethodSources(cu);

        for (String key : methodSources.keySet()) {
            System.out.println(methodSources.get(key));
        }
    }

    private static Map<String, String> getMethodSources(CompilationUnit cu) {
        Map<String, String> methodSources = new HashMap<String, String>();

        List<TypeDeclaration> types = cu.getTypes();
        for (TypeDeclaration type : types) {
            List<BodyDeclaration> members = type.getMembers();
            for (BodyDeclaration member : members) {
                if (member instanceof MethodDeclaration) {
                    MethodDeclaration method = (MethodDeclaration) member;
                    methodSources.put(method.getName(), method.getBody().toString());
                }
            }
        }
        return methodSources;
    }

}
