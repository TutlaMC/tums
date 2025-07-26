package net.tutla.tums.tusan.lexer;

public enum TokenType {
    // types
    STRING,
    NUMBER,
    BOOL,
    NOTHING,
    TYPE,

    PROPERTY,
    OPERATOR,
    LOGIC,
    COMPARISION,

    IDENTIFIER, // undefined keywords basically
    ENDSCRIPT, // end of script
    // keywords
    KEYWORD,
    STRUCTURE,
    EFFECT,

    TIME,
    BREAKSTRUCTURE,

    // symbols
    LEFT_CURLY,
    RIGHT_CURLY,
    LEFT_PAR,
    RIGHT_PAR,
    LEFT_SQUARE,
    RIGHT_SQUARE,
    SEMICOLON,
    COLON,
    COMMA
}
