package net.tutla.tums.tusan.lexer;

import net.tutla.tums.tusan.Utils;
import net.tutla.tums.tusan.interpreter.Interpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    public String text;
    private final Interpreter interpreter;

    private TusanLanguage lang;

    private int pos = 0;
    private final StringBuilder currentToken = new StringBuilder();
    public List<Token> tokens = new ArrayList<>();

    // Keyword Definitions

    public final List<String> structures = Arrays.asList("if","on","loop","while", "function");
    public final List<String> effects = Arrays.asList("print", "set", "wait");
    public final List<String> keywords = Arrays.asList("of", "else", "elseif", "then", "to", "do", "as", "times", "items", "characters", "all");
    public final List<String> timeReprs = Arrays.asList("milliseconds","seconds","minutes","hours","days","weeks","months","years","millisecond","second","minute","hour","day","week","month","year");
    public final List<String> types = Utils.getTypeNames();


    // event mappings are in tusan.Utils

    public Lexer(String text, Interpreter interpreter, TusanLanguage lang) {
        this.text = text;
        this.interpreter = interpreter;
        this.lang = lang;
    }

    private void register(TokenType name, String value) { // adds token
        tokens.add(new Token(name, value, interpreter));
        currentToken.setLength(0);
    }

    public List<Token> classify() {
        Matcher matcher = lang.match(text);

        while (matcher.find()) {
            boolean matched = false;
            for (int i = 0; i < lang.getRules().size(); i++) {
                LexerRule rule = lang.getRules().get(i);
                if (matcher.group("G" + i) != null) {
                    String value = matcher.group("G" + i);
                    register(rule.type(), value);
                    matched = true;
                    break;
                }
            }

            if (!matched) {
                if (matcher.group("IDENTIFIER") != null) {
                    String value = matcher.group("IDENTIFIER");

                    if (keywords.contains(value)) {
                        register(TokenType.KEYWORD, value);
                    } else if (effects.contains(value)) {
                        register(TokenType.EFFECT, value);
                    } else if (structures.contains(value)) {
                        register(TokenType.STRUCTURE, value);
                    } else if (value.equals("end")) {
                        register(TokenType.ENDSTRUCTURE, value);
                    } else if (Arrays.asList("return","break").contains(value)) {
                        register(TokenType.BREAKSTRUCTURE, value);
                    } else if (types != null && types.contains(value)) {
                        register(TokenType.TYPE, value);
                    } else if (Utils.isEventType(value)) {
                        register(TokenType.EVENT, value);
                    } else if (timeReprs.contains(value)) {
                        String timeValue = value.endsWith("s") ?
                                value.substring(0, value.length() - 1) : value;
                        register(TokenType.TIME, timeValue);
                    } else {
                        register(TokenType.IDENTIFIER, value);
                    }
                } else if (matcher.group("WHITESPACE") != null) {
                }
            }
        }

        register(TokenType.ENDSCRIPT, "");
        return tokens;
    }
}
