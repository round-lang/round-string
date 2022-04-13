package org.dreamcat.round.string.loader;

import java.io.IOException;

/**
 * @author Jerry Will
 * @version 2022-04-13
 */
public interface TemplateLoader {

    TemplateSource find(String name) throws IOException;
}
