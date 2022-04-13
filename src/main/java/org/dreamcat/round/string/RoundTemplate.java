package org.dreamcat.round.string;

import java.io.IOException;
import java.io.Writer;

/**
 * @author Jerry Will
 * @version 2022-03-18
 */
public interface RoundTemplate {

    void process(Object model, Writer out) throws IOException;
}
