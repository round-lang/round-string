package org.dreamcat.round.string.ast;

/**
 * for item in list
 * for (item, index) in list
 * for (key, value) in map
 * end
 * <p>
 * list is a {@link Iterable} or array
 *
 * @author Jerry Will
 * @version 2022-05-11
 */
class ForNode extends TreeNode {

    String item;
    String index;
    String list;

    @Override
    public String toString() {
        if (index == null) {
            return "for " + item + " in " + list;
        } else {
            return "for (" + item + ", " + index + ") in " + list;
        }
    }
}
