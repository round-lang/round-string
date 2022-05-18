package org.dreamcat.round.string.model;

import java.util.Set;
import lombok.RequiredArgsConstructor;

/**
 * @author Jerry Will
 * @version 2022-05-14
 */
@RequiredArgsConstructor
class TemplateModelObjectImpl implements TemplateModel {

    final Object dataModel;

    @Override
    public Object get(String s) {
        return null;
    }

    @Override
    public void set(String s, Object o) {

    }

    @Override
    public Set<String> names() {
        return null;
    }
}
