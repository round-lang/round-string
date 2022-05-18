package org.dreamcat.round.string;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import org.dreamcat.common.io.IOUtil;
import org.dreamcat.round.string.ast.RsLexer;
import org.dreamcat.round.string.ast.RsNode;
import org.dreamcat.round.string.loader.TemplateSource;
import org.dreamcat.round.string.model.StreamString;
import org.dreamcat.round.string.model.TemplateModel;

/**
 * @author Jerry Will
 * @version 2022-03-18
 */
class RoundTemplateImpl implements RoundTemplate {

    private final TemplateSource source;
    private final RoundEngineImpl engine;
    private final String encoding;
    private final RsNode root;

    public RoundTemplateImpl(TemplateSource source, RoundEngineImpl engine, String encoding) throws IOException {
        this.source = source;
        this.engine = engine;
        this.encoding = encoding;

        try (Reader in = source.getReader(encoding)) {
            String string = IOUtil.readAsString(in);
            RsLexer rsLexer = new RsLexer();
            this.root = RsNode.compile(rsLexer.lex(string));
        }
    }

    @Override
    public void process(Object model, Writer out) throws IOException {
        TemplateModel templateModel = TemplateModel.valueOf(model);
        StreamString ss = StreamString.ofWriter(out);
        root.process(templateModel, ss);
    }
}
