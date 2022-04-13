package org.dreamcat.round.string.process;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.dreamcat.common.MutableLong;
import org.dreamcat.round.el.ast.ElNode;
import org.dreamcat.round.el.exception.CompileException;
import org.dreamcat.round.el.lex.LexSettings;
import org.dreamcat.round.el.lex.Lexer;
import org.dreamcat.round.el.lex.TokenStream;
import org.dreamcat.round.string.RoundEngine;
import org.dreamcat.round.string.RoundTemplate;
import org.dreamcat.round.string.loader.TemplateSource;
import org.dreamcat.round.string.model.TemplateModel;

/**
 * @author Jerry Will
 * @version 2022-03-18
 */
@RequiredArgsConstructor
public class InternalRoundTemplate implements RoundTemplate {

    private final TemplateSource source;
    private final RoundEngine engine;
    private final String encoding;

    private final Map<Long, ElNode> nodeCache = new ConcurrentHashMap<>();
    private final Lexer lexer = new Lexer(new LexSettings());

    @Override
    public void process(Object model, Writer out) throws IOException {
        TemplateModel templateModel = TemplateModel.valueOf(model);
        try (Reader in = source.getReader(encoding)) {
            process(in, templateModel, out);
        }
    }

    // todo impl tree visit, refactor to fit the context control
    private void process(Reader in, TemplateModel model, Writer out) throws IOException {
        MutableLong counter = new MutableLong();
        int c;
        while ((c = in.read()) != -1) {
            counter.incrementAndGet();
            if (c != '$') {
                out.write(c);
            }

            int nc = in.read();
            if (nc == -1) { // hit EOF
                out.write(c);
                break;
            }
            counter.incrementAndGet();
            if (nc == '{') {
                ElNode node = readNode(in, counter);
                nodeCache.put(counter.get(), node);
            } else {
                out.write(c);
                out.write(nc);
            }
        }
    }

    private ElNode readNode(Reader in, MutableLong counter) throws IOException {
        String el = readNodeEl(in, counter);
        TokenStream stream = lexer.lex(el);
        return ElNode.compile(stream);
    }

    private String readNodeEl(Reader in, MutableLong counter) throws IOException {
        StringBuilder s = new StringBuilder();
        int c;
        while ((c = in.read()) != -1) {
            counter.incrementAndGet();
            if (c == '}') {
                break;
            } else if (c != '\\') {
                s.append(c);
            }
            int nc = in.read();
            if (nc == -1) { // hit EOF
                throw new CompileException("unclosed { at EOF");
            }
            s.append(c).append(nc); // append \}
        }
        return s.toString();
    }
}
