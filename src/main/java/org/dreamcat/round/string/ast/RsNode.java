package org.dreamcat.round.string.ast;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import org.dreamcat.round.el.ElEngine;
import org.dreamcat.round.string.model.StreamString;
import org.dreamcat.round.string.model.TemplateModel;

/**
 * @author Jerry Will
 * @version 2022-05-11
 */
public abstract class RsNode {

    public RsNode parent;

    public static RsNode compile(RsNodeStream stream) throws IOException {
        return Analyzer.analyse(stream);
    }

    void addChild(RsNode child) {
        throw new UnsupportedOperationException();
    }

    public void process(TemplateModel model, StreamString ss) throws IOException {

        //
    }
}
