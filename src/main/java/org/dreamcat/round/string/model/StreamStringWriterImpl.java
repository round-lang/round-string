package org.dreamcat.round.string.model;

import java.io.IOException;
import java.io.Writer;
import lombok.RequiredArgsConstructor;

/**
 * @author Jerry Will
 * @version 2022-05-13
 */
@RequiredArgsConstructor
class StreamStringWriterImpl implements StreamString {

    final Writer out;

    @Override
    public void write(char c) throws IOException {
        out.write(c);
    }

    @Override
    public void write(String line) {

    }

    @Override
    public void newLine() {

    }
}
