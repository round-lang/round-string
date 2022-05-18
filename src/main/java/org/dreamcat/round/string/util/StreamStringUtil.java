package org.dreamcat.round.string.util;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

/**
 * @author Jerry Will
 * @version 2022-05-13
 */
public class StreamStringUtil {

    private StreamStringUtil(){}

    public static void copy(List<StringBuilder> sbs, Writer out) throws IOException {
        // avoid copy
        if (out instanceof StringWriter) {
            StringWriter sw = (StringWriter) out;
            for (StringBuilder sb : sbs) {
                sw.getBuffer().append(sb);
                sw.write("\n"); // discard \r\n
            }
        } else {
            for (StringBuilder sb : sbs) {
                out.write(sb.toString()); // copy cost
                out.write("\n"); // discard \r\n
            }
        }
    }
}
