package org.dreamcat.round.string.loader;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jerry Will
 * @version 2022-04-13
 */
public class ByteArrayTemplateLoader implements TemplateLoader {

    private final Map<String, ByteArrayTemplateSource> templates = new ConcurrentHashMap<>();

    @Override
    public TemplateSource find(String name) throws IOException {
        return this.templates.get(name);
    }

    public void putTemplate(String name, byte[] templateContent) {
        this.putTemplate(name, templateContent, System.currentTimeMillis());
    }

    public void putTemplate(String name, byte[] templateContent, long lastModified) {
        this.templates.put(name, new ByteArrayTemplateSource(name, templateContent, lastModified));
    }

    public boolean removeTemplate(String name) {
        return this.templates.remove(name) != null;
    }

}
