package org.dreamcat.round.string.loader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import lombok.Getter;
import org.dreamcat.common.util.ObjectUtil;

/**
 * @author Jerry Will
 * @version 2022-04-13
 */
@Getter
public class ByteArrayTemplateSource implements TemplateSource {

    private final String name;
    private final byte[] templateContent;
    private final long lastModified;

    public ByteArrayTemplateSource(String name, byte[] templateContent, long lastModified) {
        this.name = ObjectUtil.requireNotNull(name, "name");
        this.templateContent = ObjectUtil.requireNotNull(templateContent, "templateContent");
        this.lastModified = ObjectUtil.requireGe(lastModified, -1L, "lastModified");
    }

    @Override
    public Reader getReader(String encoding) throws IOException {
        return new InputStreamReader(new ByteArrayInputStream(templateContent), encoding);
    }

    @Override
    public void close() throws IOException {
        // nop
    }
}
