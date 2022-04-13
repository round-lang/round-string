package org.dreamcat.round.string.loader;

import java.io.IOException;
import java.net.URL;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Jerry Will
 * @version 2022-04-13
 */
public abstract class URLTemplateLoader implements TemplateLoader {

    @Getter
    @Setter
    private Boolean urlConnectionUsesCaches;

    @Override
    public TemplateSource find(String name) throws IOException {
        URL url = this.getURL(name);
        if (url != null) {
            return new URLTemplateSource(name, url, urlConnectionUsesCaches);
        }
        return null;
    }

    public abstract URL getURL(String name);
}
