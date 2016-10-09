package com.cyou.runaway.inhouse.Core.Annotation.Doc;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Gang on 2016/10/9.
 */


public class AnnotationParser
{

    protected Map<String, ClassMethods> mDocMap;

    protected String mClassPostfix = "class";
    protected int mDotClassPostfixCount = 0;
    public AnnotationParser()
    {
        mDocMap = new HashMap<>();
        mDotClassPostfixCount = mClassPostfix.length() + 1;
    }

    public void ParsePackage(String path, String packageName)
    {
        String packageDirName = packageName.replace('.', File.separatorChar);
        List<Class> mClasses = getClassesInPackage(path + File.separator + packageDirName);

        if (null == mClasses) return;

        for(Class cls : mClasses)
        {
            ParseClass(cls);
        }

        GenerateDoc();
    }

    protected void GenerateDoc()
    {

    }

    protected List<Class> getClassesInPackage(String packageDirName)
    {
        List<Class> classes = new ArrayList<>();

        File packageDir = new File(packageDirName);

        if (!packageDir.exists())
            return null;
        else
        {
            File[] files = packageDir.listFiles(new FileFilter()
            {
                @Override
                public boolean accept(File pathname)
                {
                    boolean acceptClass = pathname.getName().endsWith(mClassPostfix);
                    return acceptClass;
                }
            });

            for (File file : files)
            {
                String className = file.getName().substring(0, file.getName().length() - mDotClassPostfixCount);
                try
                {
                    classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageDirName + "." + className));
                }
                catch (ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
            }
        }

        Enumeration<URL> dirs;

        try
        {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);

            while (dirs.hasMoreElements())
            {
                URL url = dirs.nextElement();

                String protocol = url.getProtocol();

                if ("file".equals(protocol))
                {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    findClassesInPackageByFile(packageDirName, filePath, true, classes);
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }

        return classes;
    }

    protected void findClassesInPackageByFile(String packageName, String filePath, final boolean recursive, List<Class> classes)
    {
        File dir = new File(filePath);

        if (!dir.exists() || !dir.isDirectory()) return;


        File[] dirFiles = dir.listFiles(new FileFilter()
        {
            @Override
            public boolean accept(File pathname)
            {
                boolean acceptDir = recursive && pathname.isDirectory();// 接受dir目录
                boolean acceptClass = pathname.getName().endsWith(mClassPostfix);// 接受class文件
                return acceptDir || acceptClass;
            }
        });

        for (File file : dirFiles)
        {
            if (file.isDirectory())
            {
                findClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
            } else
            {
                String className = file.getName().substring(0, file.getName().length() - 6);
                try
                {
                    classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + "." + className));
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void ParseClass(Class targetClass)
    {
        if (null == targetClass.getAnnotation(CommandAnnotation.class))
            return;

        String className = targetClass.getName();
        mDocMap.put(className, new ClassMethods());

        for (Method method : targetClass.getMethods())
        {
            MethodAnnotation methodInfo = method.getAnnotation(MethodAnnotation.class);
            if (null == methodInfo) continue;

            mDocMap.get(className).addMethodAnnotation(methodInfo);

        }
    }

    class ClassMethods
    {
        public ClassMethods()
        {
            mMethodAnnotations = new ArrayList<>();
        }

        public void addMethodAnnotation(MethodAnnotation annotation)
        {
            mMethodAnnotations.add(annotation);
        }

        protected List<MethodAnnotation> mMethodAnnotations;
    }
}
