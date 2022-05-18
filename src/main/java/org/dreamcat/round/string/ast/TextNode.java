package org.dreamcat.round.string.ast;

import java.io.IOException;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dreamcat.round.string.model.StreamString;
import org.dreamcat.round.string.model.TemplateModel;

/**
 * @author Jerry Will
 * @version 2022-05-11
 */
@Getter
@RequiredArgsConstructor
class TextNode extends RsNode {

    public final char[] raw;
    private volatile String rawCache;
    public final int lineNo;

    @Override
    public String toString() {
        if (rawCache != null) return rawCache;
        return rawCache = new String(raw);
    }

    @Override
    public void process(TemplateModel model, StreamString ss) throws IOException {
        ss.write(toString());
    }
}
