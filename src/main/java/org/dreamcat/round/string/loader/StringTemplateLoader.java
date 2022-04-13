package org.dreamcat.round.string.loader;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jerry Will
 * @version 2022-04-13
 */
public class StringTemplateLoader implements TemplateLoader {

    private final Map<String, StringTemplateSource> templates = new ConcurrentHashMap<>();

    @Override
    public TemplateSource find(String name) throws IOException {
        return this.templates.get(name);
    }

    public void putTemplate(String name, String templateContent) {
        this.putTemplate(name, templateContent, System.currentTimeMillis());
    }

    public void putTemplate(String name, String templateContent, long lastModified) {
        this.templates.put(name, new StringTemplateSource(name, templateContent, lastModified));
    }

    public boolean removeTemplate(String name) {
        return this.templates.remove(name) != null;
    }

}
