package org.dreamcat.round.string.ast;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.dreamcat.round.el.ElEngine;
import org.dreamcat.round.el.ast.ElNode;
import org.dreamcat.round.string.model.StreamString;
import org.dreamcat.round.string.model.TemplateModel;

/**
 * @author Jerry Will
 * @version 2022-05-11
 */
@RequiredArgsConstructor
class ExpressionNode extends RsNode {

    final ElNode astNode;
    final int lineNo;

    static final ElEngine elEngine = ElEngine.getEngine();

    @Override
    public String toString() {
        return astNode.toString();
    }

    @Override
    public void process(TemplateModel model, StreamString ss) throws IOException {
        astNode.evaluate(model, elEngine);
        super.process(model, ss);
    }
}
