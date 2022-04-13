package org.dreamcat.round.string.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import org.dreamcat.common.util.ObjectUtil;

/**
 * @author Jerry Will
 * @version 2022-04-13
 */
public class FileTemplateSource implements TemplateSource {

    private final String name;
    private final File file;

    public FileTemplateSource(String name, File file) {
        this.name = name;
        this.file = ObjectUtil.requireNotNull(file, "file");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getLastModified() {
        return file.lastModified();
    }

    @Override
    public Reader getReader(String encoding) throws IOException {
        return new InputStreamReader(new FileInputStream(file), encoding);
    }

    @Override
    public void close() throws IOException {
        // nop
    }
}
