package net.tutla.tums.tusan;

public class Token {

    public TokenType type;
    public String value;
    public Interpreter interpreter;

    public Token(TokenType type, String value, Interpreter interpreter){
        this.type = type;
        this.value = value;
        this.interpreter = interpreter;
    }
}
