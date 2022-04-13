package org.dreamcat.round.string.loader;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.Setter;
import org.dreamcat.common.util.ObjectUtil;

/**
 * @author Jerry Will
 * @version 2022-04-13
 */
public class MultiTemplateLoader implements TemplateLoader {

    private final List<TemplateLoader> templateLoaders;
    private final Map<String, TemplateLoader> lastTemplateLoaderForName = new ConcurrentHashMap<>();
    @Getter
    @Setter
    private boolean sticky = true;

    public MultiTemplateLoader(List<TemplateLoader> templateLoaders) {
        this.templateLoaders = ObjectUtil.requireNotEmpty(templateLoaders, "templateLoaders");
    }

    @Override
    public TemplateSource find(String name) throws IOException {
        TemplateLoader lastTemplateLoader = null;
        if (sticky) {
            lastTemplateLoader = this.lastTemplateLoaderForName.get(name);
            if (lastTemplateLoader != null) {
                TemplateSource source = lastTemplateLoader.find(name);
                if (source != null) return source;
            }
        }
        for (TemplateLoader templateLoader : templateLoaders) {
            if (templateLoader != lastTemplateLoader) {
                TemplateSource source = templateLoader.find(name);
                if (source != null) {
                    if (sticky) {
                        this.lastTemplateLoaderForName.put(name, templateLoader);
                    }
                    return source;
                }
            }
        }
        if (this.sticky) {
            this.lastTemplateLoaderForName.remove(name);
        }
        return null;
    }

    public void clear() {
        this.lastTemplateLoaderForName.clear();
    }
}
