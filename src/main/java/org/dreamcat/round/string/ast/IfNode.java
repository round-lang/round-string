package org.dreamcat.round.string.ast;

import org.dreamcat.round.el.ast.ElNode;

/**
 * if [cond]
 * elif [cond]
 * else
 * end
 *
 * @author Jerry Will
 * @version 2022-05-11
 */
class IfNode extends TreeNode {

    ElNode cond;
    IfNode elseIf;
    ElseNode elsePart;

    @Override
    public String toString() {
        String condStr = String.valueOf(cond);
        StringBuilder s = new StringBuilder(condStr.length() << 1);
        s.append("if ( ").append(condStr).append(" ) {...}");
        if (elseIf != null) {
            s.append(" ").append(elseIf);
        }
        if (elsePart !=  null) {
            s.append(" else {...}");
        }
        return s.toString();
    }
}
