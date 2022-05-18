package org.dreamcat.round.string;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.Data;
import org.dreamcat.round.string.loader.ClassTemplateLoader;
import org.dreamcat.round.string.loader.FileTemplateLoader;
import org.dreamcat.round.string.loader.TemplateLoader;

/**
 * @author Jerry Will
 * @version 2022-03-18
 */
@Data
public class RoundConfig {

    private boolean ignoreMissing; // avoid TemplateNotFoundException
    private String defaultEncoding = StandardCharsets.UTF_8.toString();
    private TemplateLoader templateLoader;

    public void setDirectoryForTemplateLoading(File dir) throws IOException {
        this.setTemplateLoader(new FileTemplateLoader(dir));
    }

    public void setClassForTemplateLoading(Class<?> resourceLoaderClass, String basePackagePath) {
        this.setTemplateLoader(new ClassTemplateLoader(resourceLoaderClass, basePackagePath));
    }

    public void setClassLoaderForTemplateLoading(ClassLoader classLoader, String basePackagePath) {
        this.setTemplateLoader(new ClassTemplateLoader(classLoader, basePackagePath));
    }
}
