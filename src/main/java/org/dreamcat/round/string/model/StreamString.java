package org.dreamcat.round.string.model;

import java.io.IOException;
import java.io.Writer;

/**
 * @author Jerry Will
 * @version 2022-05-13
 */
public interface StreamString {

    static StreamString ofWriter(Writer out) {
        return new StreamStringWriterImpl(out);
    }

    void write(char c) throws IOException;

    void write(String line) throws IOException;

    void newLine();
}
