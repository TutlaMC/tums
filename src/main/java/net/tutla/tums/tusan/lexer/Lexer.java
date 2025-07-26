package net.tutla.tums.tusan.lexer;

import net.tutla.tums.tusan.interpreter.Interpreter;

import java.util.ArrayList;
import java.util.List;

public class Lexer {

    public String text;
    private final Interpreter interpreter;

    private int pos = 0;
    private final StringBuilder currentToken = new StringBuilder();
    public List<Token> tokens = new ArrayList<>();

    public Lexer(String text, Interpreter interpreter) {
        this.text = text;
        this.interpreter = interpreter;
    }

    private void register(TokenType name, String value) { // adds token
        tokens.add(new Token(name, value, interpreter));
        currentToken.setLength(0);
    }

    public List<Token> classify() {
        String[] symbols = {"(", ")", "{", "}", "[", "]", ",", ";", ":"};
        for (String sym : symbols){
            text = text.replace(sym, " " + sym + " ");
        }
        text = text.replace("'s ", " 's ") + "\n";

        boolean inString = false;
        boolean inComment = false;
        boolean inNumber = false;
        int hexCount = 0;
        String startQuoteType = "";


        while (pos < text.length()) {
            char j = text.charAt(pos);

            if (inString) { // string checking thing, i yoinked the code for tusan so its pretty shit so it's not worth touching
                if (String.valueOf(j).equals(startQuoteType)) {
                    inString = false;
                    register(TokenType.STRING, currentToken.toString().replace("\\n", "\n"));
                } else {
                    if (startQuoteType.equals("'") && currentToken.toString().equals("s ")) {
                        inString = false;
                        register(TokenType.PROPERTY, "'s ");
                    }
                    currentToken.append(j);
                }

            } else if (inComment) {
                if (j == '\n') {
                    inComment = false;
                    currentToken.setLength(0);
                }

            } else if (inNumber) {
                if (Character.isDigit(j) || j == '.') {
                    currentToken.append(j);
                } else if ("+-*/%".indexOf(j) != -1) {
                    inNumber = false;
                    register(TokenType.NUMBER, currentToken.toString());
                    pos--;
                } else if (Character.isWhitespace(j)) {
                    inNumber = false;
                    register(TokenType.NUMBER, currentToken.toString());
                    currentToken.setLength(0);
                }

            } else if (hexCount > 0) {
                if ("0123456789abcdefABCDEF".indexOf(j) != -1) {
                    hexCount++;
                    if (hexCount == 7) {
                        register(TokenType.NUMBER, currentToken.toString() + j);
                        hexCount = 0;
                    } else {
                        currentToken.append(j);
                    }
                } else {
                    hexCount = 0;
                    currentToken.setLength(0);
                }

            } else {
                if ("(){}[],;:".indexOf(j) != -1) {
                    register(TokenType.SYMBOL, String.valueOf(j));
                    currentToken.setLength(0);

                } else if ("+-*/%".indexOf(j) != -1) {
                    if (pos + 1 < text.length() && Character.isDigit(text.charAt(pos + 1)) && (pos == 0 || !Character.isDigit(text.charAt(pos - 1)))) {
                        currentToken.setLength(0);
                        currentToken.append(j);
                        inNumber = true;
                    } else {
                        register(TokenType.OPERATOR, String.valueOf(j));
                    }

                } else if (j == '#') {
                    if (pos + 6 < text.length()) {
                        String hex = text.substring(pos + 1, pos + 7);
                        if (hex.matches("[0-9a-fA-F]{6}")) {
                            hexCount = 1;
                            currentToken.setLength(0);
                            currentToken.append(j);
                        } else {
                            inComment = true;
                            currentToken.setLength(0);
                        }
                    }

                } else if (j == '\'' || j == '"') {
                    inString = true;
                    startQuoteType = String.valueOf(j);
                    currentToken.setLength(0);

                } else if (Character.isDigit(j)) {
                    inNumber = true;
                    currentToken.setLength(0);
                    currentToken.append(j);

                } else if (Character.isWhitespace(j) || pos == text.length() - 1) {
                    if (pos == text.length() - 1 && !Character.isWhitespace(j)) {
                        currentToken.append(j);
                    }

                    String tok = currentToken.toString().trim();
                    if (!tok.isEmpty()) {
                        if (tok.matches("\\d+(\\.\\d+)?")) {
                            register(TokenType.NUMBER, tok);
                        } else if (tok.equals("true") || tok.equals("false")) {
                            register(TokenType.BOOLEAN, tok);
                        } else if (tok.equals("nothing")) {
                            register(TokenType.NOTHING, tok);
                        } else {
                            register(TokenType.IDENTIFIER, tok);
                        }
                    }
                    currentToken.setLength(0);

                } else {
                    currentToken.append(j);
                }
            }
            pos++;
        }

        register(TokenType.ENDSCRIPT, "");
        return tokens;
    }
}
