package com.github.memoize.core;

import com.kelvingu.giterable.Giterable;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * User: Kelvin
 * Date: 12/2/13
 * Time: 6:59 PM
 */
public class MethodSources {

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

        MethodSources ms = new MethodSources(sourceFiles);
    }

    private Map<String,String> sources;

    public MethodSources(List<File> files) throws IOException, ParseException {
        sources = new HashMap<String, String>();

        Method[] methods = MethodSources.class.getDeclaredMethods();
        System.out.println(new MethodKeyBuilder(method[0]).build());

        //for (File f : files) {
        //    FileInputStream in = new FileInputStream(f);
        //    CompilationUnit cu = JavaParser.parse(in);
        //    in.close();
        //    addMethodSources(cu);
        //}
        //
        //for (String key : sources.keySet()) {
        //    //System.out.println("-----");
        //    System.out.println(key);
        //    //System.out.println("-----");
        //    //System.out.println(sources.get(key));
        //}
    }

    private class MethodKeyBuilder {
        private String pkgName;
        private String className;
        private String methodName;
        private String returnType;
        private List<String> parameterTypes;

        public MethodKeyBuilder(MethodDeclaration methodDeclaration,
                                String className, String pkgName) {
            this.pkgName = pkgName;
            this.className = className;
            methodName = methodDeclaration.getName();
            // TODO: deal with void
            returnType = methodDeclaration.getType().toString();

            parameterTypes = new ArrayList<String>();
            List<Parameter> parameterList = methodDeclaration.getParameters();

            if (parameterList != null) {
                for (Parameter parameter : parameterList) {
                    parameterTypes.add(parameter.getType().toString());
                }
            }

        }

        public MethodKeyBuilder(Method method) {
            // TODO: deal with null and void
            pkgName = method.getDeclaringClass().getPackage().getName();
            className = method.getDeclaringClass().getName();
            methodName = method.getName();
            returnType = method.getGenericReturnType().toString();

            parameterTypes = new ArrayList<String>();
            Type[] paramTypeArray = method.getGenericParameterTypes();
            for (Type pType : paramTypeArray) {
                parameterTypes.add(pType.toString());
            }

        }

        public String build() {
            String paramsName = StringUtils.join(parameterTypes, " ");
            return StringUtils.join(Arrays.asList(pkgName,
                    className,
                    returnType,
                    methodName,
                    paramsName
            ), " ");
        }
    }

    private void addMethodSources(CompilationUnit cu) {
        List<TypeDeclaration> types = cu.getTypes();
        for (TypeDeclaration type : types) {
            String className = type.getName();

            List<BodyDeclaration> members = type.getMembers();
            for (BodyDeclaration member : members) {
                if (member instanceof MethodDeclaration) {

                    // TODO: only add if annotation is present
                    MethodDeclaration method = (MethodDeclaration) member;

                    PackageDeclaration pkg = cu.getPackage();
                    String pkgName;
                    if (pkg == null) {
                        pkgName = "";
                    } else {
                        pkgName = pkg.getName().toString();
                    }

                    String methodKey = new MethodKeyBuilder(method, className,
                            pkgName).build();
                    sources.put(methodKey, method.getBody().toString());
                }
            }
        }
    }

}
