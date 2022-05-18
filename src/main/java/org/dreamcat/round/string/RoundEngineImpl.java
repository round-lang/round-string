package org.dreamcat.round.string;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import org.dreamcat.round.el.ElEngine;
import org.dreamcat.round.string.exception.TemplateNotFoundException;
import org.dreamcat.round.string.loader.TemplateLoader;
import org.dreamcat.round.string.loader.TemplateSource;

/**
 * @author Jerry Will
 * @version 2022-03-18
 */
@Getter
class RoundEngineImpl implements RoundEngine {

    private final RoundConfig config = new RoundConfig();
    private final Map<String, RoundTemplate> templateCache = new ConcurrentHashMap<>();

    @Override
    public RoundTemplate getTemplate(String name, String encoding) throws IOException {
        String templateKey = getTemplateKey(name, encoding);
        RoundTemplate template = templateCache.get(templateKey);
        if (template != null) return template;

        TemplateLoader templateLoader = config.getTemplateLoader();
        if (templateLoader == null) {
            throw new IllegalArgumentException("templateLoader is null");
        }
        TemplateSource source = templateLoader.find(name);
        if (source == null) {
            if (config.isIgnoreMissing()) return null; // ignore missing template
            throw new TemplateNotFoundException(name, "template not found for name " + name);
        }

        if (encoding == null) encoding = config.getDefaultEncoding();
        template = new RoundTemplateImpl(source, this, encoding);
        templateCache.put(templateKey, template);
        return template;
    }

    private String getTemplateKey(String name, String encoding) {
        return encoding + ":" + name;
    }

    public void clear() {
        templateCache.clear();
    }
}
