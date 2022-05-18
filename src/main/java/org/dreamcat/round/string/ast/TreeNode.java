package org.dreamcat.round.string.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.dreamcat.round.string.model.TemplateModel;

/**
 * @author Jerry Will
 * @version 2022-05-11
 */
class TreeNode extends RsNode {

    public List<RsNode> children = new ArrayList<>();

    @Override
    public void addChild(RsNode child) {
        children.add(child);
        child.parent = this;
    }

    public void process(TemplateModel model, List<StringBuilder> out) {
        System.out.println();
        //
    }

    @Override
    public String toString() {
        return children.stream().map(Objects::toString)
                .collect(Collectors.joining("\n"));
    }
}
