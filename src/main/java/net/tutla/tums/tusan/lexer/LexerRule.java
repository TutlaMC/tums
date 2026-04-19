package net.tutla.tums.tusan.lexer;

public class LexerRule {
    public final TokenType type;
    public final String regex;
    public TokenGroup group;

    public LexerRule(TokenType type, String regex) {
        this.type = type;
        this.regex = regex;
    }

    public LexerRule setGroup(TokenGroup e) {
        this.group = e;
        return this;
    }

    public TokenType type(){
        return type;
    }

    public String regex(){
        return regex;
    }
}
