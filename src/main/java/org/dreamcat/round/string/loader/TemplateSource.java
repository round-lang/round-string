package org.dreamcat.round.string.loader;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;

/**
 * @author Jerry Will
 * @version 2022-04-13
 */
public interface TemplateSource extends Closeable {

    String getName();

    long getLastModified();

    Reader getReader(String encoding) throws IOException;
}
