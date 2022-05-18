package org.dreamcat.round.string.model;

import org.dreamcat.round.el.ElContext;

/**
 * @author Jerry Will
 * @version 2022-03-18
 */
public interface TemplateModel extends ElContext {

    static TemplateModel valueOf(Object dataModel) {
        return new TemplateModelObjectImpl(dataModel);
    }
}
