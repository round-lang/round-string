package org.dreamcat.round.string.loader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import lombok.Getter;
import org.dreamcat.common.util.ObjectUtil;

/**
 * @author Jerry Will
 * @version 2022-04-13
 */
@Getter
public class FileTemplateLoader implements TemplateLoader {

    private final File baseDir;
    private final String canonicalBasePath; // look like: /a/b/c/

    public FileTemplateLoader(File baseDir) throws IOException {
        ObjectUtil.requireNotNull(baseDir, "baseDir");
        if (!baseDir.exists()) {
            throw new FileNotFoundException(baseDir + " does not exist.");
        } else if (!baseDir.isDirectory()) {
            throw new IOException(baseDir + " is not a directory.");
        }
        this.baseDir = baseDir.getCanonicalFile();
        String basePath = this.baseDir.getPath();
        if (!basePath.endsWith(File.separator)) {
            basePath = basePath + File.separatorChar;
        }
        this.canonicalBasePath = basePath;
    }

    @Override
    public TemplateSource find(String name) throws IOException {
        File source = new File(baseDir, sep_is_slash ? name :
                name.replace('/', File.separatorChar));
        if (!source.isFile()) return null;
        String normalized = source.getCanonicalPath();
        if (!normalized.startsWith(canonicalBasePath)) {
            throw new SecurityException(
                    source.getAbsolutePath() + " resolves to " + normalized + " which doesn't start with "
                            + canonicalBasePath);
        }
        return new FileTemplateSource(name, source);
    }

    private static final boolean sep_is_slash;

    static {
        sep_is_slash = File.separatorChar == '/';
    }
}
