package org.dreamcat.round.string.bench;

import static org.dreamcat.common.util.RandomUtil.choose26;
import static org.dreamcat.common.util.RandomUtil.choose36;
import static org.dreamcat.common.util.RandomUtil.choose52;
import static org.dreamcat.common.util.RandomUtil.choose72;
import static org.dreamcat.common.util.RandomUtil.rand;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.dreamcat.common.Timeit;
import org.dreamcat.common.util.ArrayUtil;
import org.dreamcat.common.util.MapUtil;
import org.dreamcat.common.x.plot.plotly.Plotly;
import org.dreamcat.round.string.RoundEngine;
import org.dreamcat.round.string.RoundTemplate;
import org.junit.jupiter.api.Test;

/**
 * @author Jerry Will
 * @version 2022-03-18
 */
class SimpleSpeedTest {

    @Test
    void testOne() throws Exception {
        System.out.println(toStringRs(mapContext));
    }

    @Test
    void testAll() throws Exception {
        System.out.println(toStringRs(mapContext));
        System.out.println(toStringFtl(mapContext));
        System.out.println(toStringVm(mapContext));
    }

    @Test
    void test() {
        String[] stack = new String[]{
                "vm#str", "vm#io", "flt#str", "flt#io", "rs#str", "rs#io"
        };
        System.out.println("    " + Arrays.stream(stack).map(s -> String.format("%12s", s))
                .collect(Collectors.joining()));
        List<Integer> x = new ArrayList<>();
        List<long[]> y = new ArrayList<>();
        for (int i = 1; i <= (1 << 7); i <<= 1) {
            long[] ts = Timeit.ofActions()
                    .addAction(() -> {
                        toStringVm(mapContext);
                    })
                    .addAction(() -> {
                        toWriterVm(mapContext);
                    })
                    .addAction(() -> {
                        toStringFtl(mapContext);
                    })
                    .addAction(() -> {
                        toWriterFtl(mapContext);
                    })
                    .addAction(() -> {
                        toStringRs(mapContext);
                    })
                    .addAction(() -> {
                        toWriterRs(mapContext);
                    })
                    .repeat(i).count(10).skip(2)
                    .run();
            System.out.printf("%04d%s%n", i, Timeit.formatMs(ts, 12));
            x.add(i);
            y.add(ts);
        }

        try {
            Plotly.plotAndOpenStack(x, y, n -> n / 1000_000., stack);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //
    private static String toStringVm(MapContext context) {
        return toWriterVm(context).toString();
    }

    private static Writer toWriterVm(MapContext context) {
        org.apache.velocity.Template template = getVmTemplate("velocity.vm");
        StringWriter stringWriter = new StringWriter();
        template.merge(context, stringWriter);
        return stringWriter;
    }

    private static org.apache.velocity.Template getVmTemplate(String templateName) {
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, path);
        ve.setProperty("runtime.log.logsystem.class",
                "org.apache.velocity.runtime.log.NullLogChute");
        ve.init();
        return ve.getTemplate(templateName, "UTF-8");
    }

    // freemarker
    private static String toStringFtl(MapContext context) throws Exception {
        return toWriterFtl(context).toString();
    }

    private static Writer toWriterFtl(MapContext context) throws Exception {
        freemarker.template.Template template = getFtlTemplate("freemarker.ftl");
        StringWriter stringWriter = new StringWriter();
        template.process(context.map, stringWriter);
        return stringWriter;
    }

    private static freemarker.template.Template getFtlTemplate(
            String templateName) throws Exception {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setDirectoryForTemplateLoading(new File(path));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        return cfg.getTemplate(templateName);
    }

    // round-string
    private static String toStringRs(MapContext context) throws Exception {
        return toWriterRs(context).toString();
    }

    private static Writer toWriterRs(MapContext context) throws Exception {
        RoundTemplate template = getRoundTemplate("round-string.xml");
        StringWriter stringWriter = new StringWriter();
        template.process(context.map, stringWriter);
        return stringWriter;
    }

    private static RoundTemplate getRoundTemplate(String templateName) throws Exception {
        RoundEngine engine = RoundEngine.getEngine();
        engine.getConfig().setDefaultEncoding("UTF-8");
        engine.getConfig().setDirectoryForTemplateLoading(new File(path));
        return engine.getTemplate(templateName);
    }

    private static final String path = "src/test/resources/bench";

    private static final MapContext mapContext = new MapContext(MapUtil.of(
            "mapperName", choose72(6),
            "methodList", ArrayUtil.map(200, i -> MapUtil.of(
                    "sqlType", rand() > 0.5 ? "select" : "insert",
                    "methodName", choose52(8),
                    "type", choose26(8),
                    "desc", choose36(16)
            ), HashMap[]::new)
    ));
}
