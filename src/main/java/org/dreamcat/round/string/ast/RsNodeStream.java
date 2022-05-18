package org.dreamcat.round.string.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.dreamcat.round.el.exception.CompileException;

/**
 * @author Jerry Will
 * @version 2022-05-11
 */
public class RsNodeStream {

    List<RsNode> tokens = new ArrayList<>();
    int offset = -1;
    int size;

    void add(RsNode token) {
        tokens.add(token);
        size++;
    }

    void addAll(List<RsNode> tokens) {
        tokens.forEach(this::add);
    }

    public boolean hasNext() {
        return offset < size - 1;
    }

    public RsNode next() {
        if (hasNext()) {
            return tokens.get(++offset);
        }
        return throwWrongSyntax();
    }

    public RsNode previous() {
        if (offset > 0) {
            return tokens.get(--offset);
        }
        return throwWrongSyntax();
    }

    public void reset() {
        offset = -1;
    }

    public <T> T throwWrongSyntax() {
        if (offset >= size) offset = size - 1;
        else if (offset < 0) offset = 0;
        RsNode node = null;
        while (offset >= 0 && !((node = tokens.get(offset)) instanceof TextNode)) offset--;
        int lineNo = 1;
        if (node instanceof TextNode) {
            lineNo = ((TextNode)node).getLineNo();
        }
        throw new CompileException("You has wrong syntax in your string, near at line " + lineNo);
    }
}
