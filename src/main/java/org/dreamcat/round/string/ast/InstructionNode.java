package org.dreamcat.round.string.ast;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dreamcat.round.el.lex.SimpleTokenStream;
import org.dreamcat.round.el.lex.TokenStream;

/**
 * @author Jerry Will
 * @version 2022-05-11
 */
@Getter
@RequiredArgsConstructor
class InstructionNode extends RsNode {

    final TokenStream tokenStream; // assign on lex
    boolean end;

    @Override
    public String toString() {
        return ((SimpleTokenStream)tokenStream).getExpression();
    }
}
