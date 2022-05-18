package org.dreamcat.round.string.ast;

import java.io.IOException;
import org.dreamcat.common.io.ClassPathUtil;
import org.junit.jupiter.api.Test;

/**
 * @author Jerry Will
 * @version 2022-05-13
 */
class RsLexerTest {

    @Test
    void test() throws IOException {
        String xml = ClassPathUtil.getResourceAsString("bench/round-string.xml");
        RsLexer rsLexer = new RsLexer();
        RsNodeStream stream = rsLexer.lex(xml);
        while (stream.hasNext()) {
            RsNode node = stream.next();
            System.out.println(node.getClass().getSimpleName() + "=" + node);
        }
    }
}
