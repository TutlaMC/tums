package net.tutla.tums.tusan.lang;

import net.tutla.tums.tusan.node.Node;
import net.tutla.tums.tusan.TusanContext;
import net.tutla.tums.tusan.lexer.*;
import net.tutla.tums.tusan.nodes.Statement;
import net.tutla.tums.tusan.nodes.base.Return;

import java.util.ArrayList;
import java.util.List;

public class Tusan {
    private final TusanLanguage lang = new TusanLanguage();
    private final List<Node> nodes = new ArrayList<>();

    private static final List<LexerRule> DEFAULT_LEXER_RULES = List.of(
            new LexerRule(PrebuiltTusanTokenType.STRING, "\"(?:[^\"\\\\]|\\\\.)*\"|'(?:[^'\\\\]|\\\\.)*'"),
            new LexerRule(PrebuiltTusanTokenType.NUMBER, "\\d+(\\.\\d+)?"),
            new LexerRule(PrebuiltTusanTokenType.BOOL, "\\b(true|false)\\b"),
            new LexerRule(PrebuiltTusanTokenType.NOTHING, "\\bnothing\\b"),

            new LexerRule(PrebuiltTusanTokenType.OPERATOR, "[+\\-*/%]"),
            new LexerRule(PrebuiltTusanTokenType.LOGIC, "(?:\\b(?:and|or|not|contains|in)\\b|\\|\\||&&)"),
            new LexerRule(PrebuiltTusanTokenType.COMPARISON, ">=|<=|==|!=|>|<|\\bis\\b"), // ← Fixed name
            new LexerRule(PrebuiltTusanTokenType.EQUAL, "="),

            new LexerRule(PrebuiltTusanTokenType.LEFT_CURLY, "\\{"),
            new LexerRule(PrebuiltTusanTokenType.RIGHT_CURLY, "\\}"),
            new LexerRule(PrebuiltTusanTokenType.LEFT_PAR, "\\("),
            new LexerRule(PrebuiltTusanTokenType.RIGHT_PAR, "\\)"),
            new LexerRule(PrebuiltTusanTokenType.LEFT_SQUARE, "\\["),
            new LexerRule(PrebuiltTusanTokenType.RIGHT_SQUARE, "\\]"),
            new LexerRule(PrebuiltTusanTokenType.SEMICOLON, ";"),
            new LexerRule(PrebuiltTusanTokenType.COLON, ":"),
            new LexerRule(PrebuiltTusanTokenType.COMMA, ",")
    );

    public Tusan(){
        registerAllDefaultRules();
    }

    // registering
    public void registerNode(Node node){
        // TODO: Verify if node already exists
        nodes.add(node);
    }

    public List<Node> getNodes(){
        return nodes;
    }

    public void registerLexerRule(LexerRule rule){
        lang.registerRule(rule);
    }

    private void registerAllDefaultRules(){
        DEFAULT_LEXER_RULES.forEach(this::registerLexerRule);
    }

    public void compile(TusanContext ctx){
        if (!ctx.isFunction){
            System.out.println("================ OUTPUT =============== ee");
        }
        while (ctx.getPos()<= ctx.tokenContext.length()-1){
            Token currentToken = ctx.getCurrentToken();
            if (ctx.interpreter.end){
                return;
            }
            if (currentToken.type == PrebuiltTusanTokenType.ENDSCRIPT){ // how did you get here?
                return;
            } else if (currentToken.type == PrebuiltTusanTokenType.BREAKSTRUCTURE && currentToken.value.equals("return")){
                new Return(ctx.tokenContext.nextToken()).create();
            } else {
                new Statement(currentToken).create();
            }
            if (ctx.tokenContext.getNextToken() == null){
                ctx.interpreter.meetEnd();
            } else {
                Token e = ctx.tokenContext.nextToken();
                if (e.type == PrebuiltTusanTokenType.ENDSCRIPT){
                    ctx.interpreter.meetEnd();
                }
            }

        }
        ctx.interpreter.meetEnd();
    }

    public TusanLanguage getLang(){
        return lang;
    }
}
