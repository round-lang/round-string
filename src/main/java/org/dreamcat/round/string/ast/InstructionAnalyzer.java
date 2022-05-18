package org.dreamcat.round.string.ast;

import org.dreamcat.round.el.ast.SnippetAnalyzer;
import org.dreamcat.round.el.lex.KeywordToken;
import org.dreamcat.round.el.lex.OperatorToken;
import org.dreamcat.round.el.lex.PunctuationToken;
import org.dreamcat.round.el.lex.Token;
import org.dreamcat.round.el.lex.TokenStream;

/**
 * @author Jerry Will
 * @version 2022-05-11
 */
interface InstructionAnalyzer {

    static RsNode analyse(RsNodeStream stream, TokenStream tokens) {
        Token token = tokens.next();
        if (!token.isIdentifier()) {
            return stream.throwWrongSyntax();
        }
        String identifier = token.getIdentifier();
        if (KeywordToken.END.is(identifier)) {
            return null; // null means end node
        } else if (KeywordToken.FOR.is(identifier)) {
            return analyseFor(stream, tokens);
        } else if (KeywordToken.IF.is(identifier)) {
            return analyseIf(stream, tokens, false);
        } else if (KeywordToken.ELIF.is(identifier)) {
            return analyseIf(stream, tokens, true);
        } else if (KeywordToken.ELSE.is(identifier)) {
            return analyseElse(stream, tokens);
        }
        return stream.throwWrongSyntax();
    }

    static ForNode analyseFor(RsNodeStream stream, TokenStream tokens) {
        ForNode forNode = new ForNode();
        Token token1 = tokens.next(), item, index, list;
        if (PunctuationToken.LEFT_PARENTHESIS.equals(token1)) {
            // for (item, index) in list
            if (!(item = tokens.next()).isIdentifier() ||
                    !PunctuationToken.COMMA.equals(tokens.next()) ||
                    !(index = tokens.next()).isIdentifier() ||
                    !PunctuationToken.RIGHT_PARENTHESIS.equals(tokens.next()) ||
                    !KeywordToken.IN.is(tokens.next().getIdentifier()) ||
                    !(list = tokens.next()).isIdentifier()) {
                return stream.throwWrongSyntax();
            }
            forNode.item = item.getIdentifier();
            forNode.index = index.getIdentifier();
            forNode.list = list.getIdentifier();
        } else {
            // for item in list
            if (!token1.isIdentifier() ||
                    !KeywordToken.IN.is(tokens.next().getIdentifier()) ||
                    !(list = tokens.next()).isIdentifier()) {
                return stream.throwWrongSyntax();
            }
            forNode.item = token1.getIdentifier();
            forNode.list = list.getIdentifier();
        }

        RsNode node;
        while (stream.hasNext()) {
            node = stream.next();
            if (node instanceof TextNode || node instanceof ExpressionNode) {
                forNode.addChild(node);
            } else {
                RsNode child = InstructionAnalyzer.analyse(
                        stream, ((InstructionNode) node).getTokenStream());
                // end
                if (child == null) {
                    return forNode;
                }
                forNode.addChild(child);
            }
        }
        return stream.throwWrongSyntax();
    }

    static IfNode analyseIf(RsNodeStream stream, TokenStream tokens, boolean elseIf) {
        IfNode root = new IfNode();
        root.cond = SnippetAnalyzer.analyse(tokens);

        RsNode node;
        while (stream.hasNext()) {
            node = stream.next();
            if (node instanceof TextNode || node instanceof ExpressionNode) {
                root.addChild(node);
            } else {
                InstructionNode instructionNode = (InstructionNode) node;
                // end of elif/else
                if (instructionNode.end) {
                    return root;
                }
                RsNode child = InstructionAnalyzer.analyse(
                        stream, instructionNode.getTokenStream());
                // elif
                if (child instanceof IfNode) {
                    root.elseIf = (IfNode) child;
                }
                // else
                else if (child instanceof ElseNode) {
                    root.elsePart = (ElseNode) child;
                }
                // end
                else if (child == null) {
                    if (elseIf) {
                        instructionNode.end = true;
                        stream.previous();
                    }
                    return root;
                } else {
                    root.addChild(child);
                }
            }
        }
        return stream.throwWrongSyntax();
    }

    static ElseNode analyseElse(RsNodeStream stream, TokenStream tokens) {
        ElseNode root = new ElseNode();
        RsNode node;
        while (stream.hasNext()) {
            node = stream.next();
            if (node instanceof TextNode || node instanceof ExpressionNode) {
                root.addChild(node);
            } else {
                RsNode child = InstructionAnalyzer.analyse(
                        stream, ((InstructionNode) node).getTokenStream());
                // end
                if (child == null) {
                    return root;
                } else {
                    root.addChild(child);
                }
            }
        }
        return stream.throwWrongSyntax();
    }

    static void checkToken(TokenStream tokens) {
        Token token;
        while (tokens.hasNext()) {
            token = tokens.next();
            if (token.equals(OperatorToken.ASSIGN)) {
                tokens.throwWrongSyntax();
            }
        }
        tokens.reset();
    }
}
