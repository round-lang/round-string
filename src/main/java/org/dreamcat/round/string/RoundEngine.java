package org.dreamcat.round.string;

import java.io.IOException;

/**
 * @author Jerry Will
 * @version 2022-03-18
 */
public interface RoundEngine {

    static RoundEngine getEngine() {
        return new InternalRoundEngine();
    }

    RoundConfig getConfig();

    default RoundTemplate getTemplate(String name) throws IOException {
        return getTemplate(name, null);
    }

    RoundTemplate getTemplate(String name, String encoding) throws IOException;
}
