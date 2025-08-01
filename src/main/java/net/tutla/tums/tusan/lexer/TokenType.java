package net.tutla.tums.tusan.lexer;

public enum TokenType {
    // types
    STRING,
    NUMBER,
    BOOL,
    NOTHING,
    TYPE,
    EVENT,

    PROPERTY,
    OPERATOR,
    LOGIC,
    COMPARISION,
    EQUAL,

    IDENTIFIER, // undefined keywords basically
    ENDSCRIPT, // end of script
    // keywords
    KEYWORD,
    STRUCTURE,
    EFFECT,

    TIME,
    BREAKSTRUCTURE,
    ENDSTRUCTURE,

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
