package org.dreamcat.round.string.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.dreamcat.round.el.ast.ElNode;
import org.dreamcat.round.el.ast.SnippetAnalyzer;
import org.dreamcat.round.el.exception.CompileException;
import org.dreamcat.round.el.exception.RoundException;
import org.dreamcat.round.el.lex.LexSettings;
import org.dreamcat.round.el.lex.Lexer;
import org.dreamcat.round.el.lex.SimpleTokenStream;
import org.dreamcat.round.el.lex.TokenStream;

/**
 * copy from {@link Lexer}
 *
 * @author Jerry Will
 * @version 2022-05-11
 */
@RequiredArgsConstructor
public class RsLexer {

    private static final Lexer lexer = new Lexer(new LexSettings());

    public RsNodeStream lex(String string) {
        RsNodeStream stream = new RsNodeStream();
        List<RsNode> tokens = stream.tokens;

        int size = string.length();
        int prev = 0, colStart = 0;
        boolean enterSharp = false, enterDollar = false;
        int lineNo = 1, col = 1;
        List<Integer> lineSeps = new ArrayList<>();
        for (int i = 0; i < size; i++, col++) {
            char c = string.charAt(i);
            if (c == '\\') {
                if (i < size - 1) {
                    char nc = string.charAt(++i);
                    if (nc == '\n') {
                        lineSeps.add(i);
                        lineNo++;
                        col = 0;
                    } else col++;
                }
            } else if (c == '#') {
                if (!enterSharp) {
                    if (i < size - 1) {
                        char nc = string.charAt(i+1);
                        if (nc == '{') {
                            enterSharp = true;
                            colStart = ++col;
                            if (i > prev) {
                                stream.addAll(text(string, prev, i, lineSeps, lineNo));
                            }
                            i++;
                            prev = i + 1;
                        }
                    }
                }
            } else if (c == '$') {
                if (!enterDollar) {
                    if (i < size - 1) {
                        char nc = string.charAt(i+1);
                        if (nc == '{') {
                            enterDollar = true;
                            colStart = ++col;
                            if (i > prev) {
                                stream.addAll(text(string, prev, i, lineSeps, lineNo));
                            }
                            i++;
                            prev = i + 1;
                        }
                    }
                }
            } else if (c == '}') {
                if (enterSharp) {
                    enterSharp = false;
                    stream.add(instruction(string.substring(prev, i), lineNo, colStart));
                    prev = i + 1;
                    lineSeps.clear();
                } else if (enterDollar) {
                    enterDollar = false;
                    stream.add(expression(string.substring(prev, i), lineNo));
                    prev = i + 1;
                    lineSeps.clear();
                }
            } else if (c == '\n') {
                lineSeps.add(i);
                lineNo++;
                col = 0;
            }
        }
        stream.addAll(text(string, prev, size, lineSeps, lineNo));
        return stream;
    }

    private List<RsNode> text(String string, int start, int end,
            List<Integer> lineSeps, int lineNo) {
        if (lineSeps.isEmpty()) {
            char[] text = new char[end - start];
            string.getChars(start, end, text, 0);
            return Collections.singletonList(new TextNode(text, lineNo));
        } else {
            List<RsNode> nodes = new ArrayList<>(lineSeps.size() + 1);
            for (int sep : lineSeps) {
                char[] line = new char[sep - start];
                string.getChars(start, sep, line, 0);
                nodes.add(new TextNode(line, lineNo++));
                start = sep + 1;
            }
            char[] line = new char[end - start];
            string.getChars(start, end, line, 0);
            nodes.add(new TextNode(line, lineNo));
            return nodes;
        }
    }

    private InstructionNode instruction(String instruction, int lineNo, int colStart) {
        SimpleTokenStream stream;
        try {
            stream = (SimpleTokenStream) lexer.lex(instruction);
        } catch (RoundException e) {
            throw new CompileException("You has wrong syntax in your template, near at line " + lineNo);
        }
        stream.setFirstLineNo(lineNo);
        stream.setFirstCol(colStart);
        return new InstructionNode(stream);
    }

    private ExpressionNode expression(String expression, int lineNo) {
        TokenStream stream;
        try {
            stream = lexer.lex(expression);
        } catch (RoundException e) {
            throw new CompileException("You has wrong syntax in your template, near at line " + lineNo);
        }
        ElNode astNode = SnippetAnalyzer.analyse(stream);
        return new ExpressionNode(astNode, lineNo);
    }
}
