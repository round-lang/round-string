package org.dreamcat.round.string.loader;

import java.io.File;
import java.net.URL;
import org.dreamcat.common.util.ObjectUtil;

/**
 * @author Jerry Will
 * @version 2022-04-13
 */
public class ClassTemplateLoader extends URLTemplateLoader {

    private final Class<?> resourceLoaderClass;
    private final ClassLoader classLoader;
    private final String basePackagePath; // look like a/b/c/

    public ClassTemplateLoader(Class<?> resourceLoaderClass, String basePackagePath) {
        this(resourceLoaderClass, null, basePackagePath);
    }

    public ClassTemplateLoader(ClassLoader classLoader, String basePackagePath) {
        this(null, classLoader, basePackagePath);
    }

    private ClassTemplateLoader(Class<?> resourceLoaderClass, ClassLoader classLoader, String basePackagePath) {
        ObjectUtil.requireNotNull(basePackagePath, "basePackagePath");
        if (classLoader == null) {
            if (resourceLoaderClass == null) {
                resourceLoaderClass = this.getClass();
            }
        } else {
            resourceLoaderClass = null;
        }
        basePackagePath = basePackagePath.replace('\\', '/');
        if (basePackagePath.length() > 0 && !basePackagePath.endsWith("/")) {
            basePackagePath = basePackagePath + File.separator; //
        }
        if (classLoader != null && basePackagePath.startsWith("/")) {
            basePackagePath = basePackagePath.substring(1);
        }

        this.resourceLoaderClass = resourceLoaderClass;
        this.classLoader = classLoader;
        this.basePackagePath = basePackagePath;
    }


    @Override
    public URL getURL(String name) {
        String fullPath = basePackagePath + name;
        if (resourceLoaderClass != null) {
            return resourceLoaderClass.getResource(fullPath);
        } else {
            return classLoader.getResource(fullPath);
        }
    }
}
