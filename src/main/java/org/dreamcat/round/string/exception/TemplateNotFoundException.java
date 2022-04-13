package org.dreamcat.round.string.exception;

import java.io.FileNotFoundException;
import lombok.Getter;

/**
 * @author Jerry Will
 * @version 2022-04-13
 */
@Getter
public class TemplateNotFoundException extends FileNotFoundException {

    private final String templateName;

    public TemplateNotFoundException(String templateName, String message) {
        super(message);
        this.templateName = templateName;
    }
}
