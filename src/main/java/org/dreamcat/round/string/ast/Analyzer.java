package org.dreamcat.round.string.ast;

import java.io.IOException;
import org.dreamcat.round.el.lex.OperatorToken;
import org.dreamcat.round.el.lex.Token;
import org.dreamcat.round.el.lex.TokenStream;

/**
 * @author Jerry Will
 * @version 2022-05-11
 */
interface Analyzer {

    static TreeNode analyse(RsNodeStream stream) throws IOException {
        TreeNode root = new TreeNode();
        RsNode node;
        while (stream.hasNext()) {
            node = stream.next();
            if (node instanceof TextNode || node instanceof ExpressionNode) {
                root.addChild(node);
            } else {
                RsNode child = InstructionAnalyzer.analyse(
                        stream, ((InstructionNode) node).getTokenStream());
                root.addChild(child);
            }
        }
        return root;
    }
}
