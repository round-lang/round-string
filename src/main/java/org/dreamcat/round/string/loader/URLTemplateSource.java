package org.dreamcat.round.string.loader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Jerry Will
 * @version 2022-04-13
 */
public class URLTemplateSource implements TemplateSource {

    private final String name;
    private final URL url;
    private URLConnection connection;
    private InputStream inputStream;

    public URLTemplateSource(String name, URL url, Boolean useCaches) throws IOException {
        this.name = name;
        this.url = url;
        this.connection = url.openConnection();
        if (useCaches != null) {
            this.connection.setUseCaches(useCaches);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getLastModified() {
        if (!(this.connection instanceof JarURLConnection)) {
            long lastModified = connection.getLastModified();
            if (lastModified != -1L) return lastModified;
            if (url.getProtocol().equals("file")) {
                return new File(url.getFile()).lastModified();
            }
            return -1L;
        }

        URL jarURL = ((JarURLConnection) connection).getJarFileURL();
        if (jarURL.getProtocol().equals("file")) {
            return (new File(jarURL.getFile())).lastModified();
        }

        URLConnection jarConn = null;
        try {
            jarConn = jarURL.openConnection();
            return jarConn.getLastModified();
        } catch (IOException e) {
            return -1L;
        } finally {
            if (jarConn != null) {
                try {
                    jarConn.getInputStream().close();
                } catch (IOException e) {
                    // nop
                }
            }
        }
    }

    @Override
    public Reader getReader(String encoding) throws IOException {
        return new InputStreamReader(getInputStream(), encoding);
    }

    private InputStream getInputStream() throws IOException {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                // nop
            }
            connection = url.openConnection();
        }

        inputStream = connection.getInputStream();
        return inputStream;
    }

    @Override
    public void close() throws IOException {
        try {
            if (inputStream != null) {
                inputStream.close();
            } else {
                connection.getInputStream().close();
            }
        } finally {
            inputStream = null;
            connection = null;
        }

    }
}
