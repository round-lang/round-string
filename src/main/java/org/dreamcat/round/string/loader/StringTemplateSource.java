package org.dreamcat.round.string.loader;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import lombok.Getter;
import org.dreamcat.common.util.ObjectUtil;

/**
 * @author Jerry Will
 * @version 2022-04-13
 */
@Getter
public class StringTemplateSource implements TemplateSource {

    private final String name;
    private final String templateContent;
    private final long lastModified;

    public StringTemplateSource(String name, String templateContent, long lastModified) {
        this.name = ObjectUtil.requireNotNull(name, "name");
        this.templateContent = ObjectUtil.requireNotNull(templateContent, "templateContent");
        this.lastModified = ObjectUtil.requireGe(lastModified, -1L, "lastModified");
    }

    @Override
    public Reader getReader(String encoding) throws IOException {
        return new StringReader(templateContent);
    }

    @Override
    public void close() throws IOException {
        // nop
    }
}
