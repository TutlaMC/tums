package net.tutla.tums.tusan.interpreter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.tutla.tums.tusan.Utils;
import net.tutla.tums.tusan.lexer.Lexer;
import net.tutla.tums.tusan.lexer.Token;
import net.tutla.tums.tusan.lexer.TokenType;
import net.tutla.tums.tusan.nodes.Statement;
import net.tutla.tums.tusan.nodes.base.Return;

public class Interpreter {

    private static Lexer lexer;
    public Utils util = new Utils();

    public InterpreterData data;
    public String text;
    public String file;
    public List<Token> tokens;
    public Object returned;
    public Boolean isFunction = false;

    private Path filePath;

    public Boolean end = false;
    public Boolean caughtError = false;
    public int pos = 0;
    public Token currentToken;


    public Interpreter() {

    }

    public void setup(InterpreterData data, List<Token> _tokens, String _text, Path file){
        this.data = data != null ? data : new InterpreterData(null, null, null, null);

        if (_text != null) {
            this.text = _text;
        }
        if (file != null) {
            this.text = readFileContents(file);
            this.file = String.valueOf(file.toAbsolutePath());
            this.filePath = filePath;
        }
        else {
            this.file = "<stdin>";
        }

        if (_tokens == null){
            lexer = new Lexer(this.text, this);
            this.tokens = lexer.classify();
        } else {
            this.tokens = _tokens;
            this.tokens = changeTokensParent(this);
        }

        this.currentToken = tokens.get(this.pos);
    }

    public Object compile() {
        changeTokensParent(this);
        end = false;
        caughtError = false;
        pos = 0;
        currentToken = tokens.get(pos);

        /* for (Token t : tokens){
            System.out.print(t.type);
            System.out.print(":");
            System.out.print(t.value);
            System.out.print("\n");
        } */
        if (!isFunction){
            System.out.println("================ OUTPUT ===============");
        }
        while (pos <= tokens.toArray().length-1){
            if (end){
                return returned;
            }
            if (currentToken.type == TokenType.ENDSCRIPT){ // how did you get here?
                return returned;
            } else if (currentToken.type == TokenType.BREAKSTRUCTURE && currentToken.value.equals("return")){
                new Return(nextToken()).create();
            } else {
                new Statement(currentToken).create();
            }
            if (getNextToken() == null){
                meetEnd();
            } else {
                Token e = nextToken();
                if (e.type == TokenType.ENDSCRIPT){
                    meetEnd();
                }
            }

        }
        meetEnd();

        return returned;
    }

    // utils

    public Token getNextToken(){
        if (pos >= tokens.toArray().length-1){
            return null;
        } else {
            return tokens.get(pos+1);
        }
    }

    public Token nextToken(){
        Token nxt = getNextToken();
        if (nxt != null){
            this.pos++;
            currentToken = tokens.get(pos);
            return nxt;
        } else {
            error("UnfinishedExpression", "Unfinished expression at ENDSCRIPT", null);
        }
        return null;
    }

    public Token expectTokenType(TokenType token){
        Token nxt = nextToken();
        if (nxt.type == token){
            return nxt;
        } else {
            error("UnexpectedToken", "Expected "+token.name()+" got "+nxt.type.name(), null);
            return null;
        }
    }

    public Token expectToken(TokenType token, String name){
        Token nxt = nextToken();
        if (nxt.type == token && nxt.value.equals(name)){
            return nxt;
        } else {
            error("UnexpectedToken", "Expected "+token.name()+":"+name+" got "+nxt.type.name()+":"+nxt.value, null);
            return null;
        }
    }

    public Token expectTokenClassic(String tokenTypes) {
        String[] types = tokenTypes.replace(" ", "").split("\\|");
        Token nextTkn = getNextToken();

        for (String t : types) {
            if (t.contains(":")) {
                String expectedValue = t.split(":")[1];
                if (expectedValue.equals(nextTkn.value)) {
                    return nextToken();
                }
            } else {
                if (nextTkn.type.toString().equalsIgnoreCase(t)) {
                    return nextToken();
                }
            }
        }

        if (Arrays.asList(types).contains("IDENTIFIER")) {
            error("UnexpectedToken", "Expected token " + Arrays.toString(types) + ", got " + nextTkn,
                    List.of("Possible Fix: You might have entered a keyword as a variable name, try renaming it"));
        } else {
            error("UnexpectedToken", "Expected token " + Arrays.toString(types) + ", got " + nextTkn, null);
        }

        return null;
    }


    public void error(String name, String detail, List<String> notes){
        if (!caughtError){
            System.out.println("================ ERROR ================");
            System.out.println(name+" : "+detail);
            System.out.println("============== POSITION ===============");
            System.out.println(arrowsAtPosition());
            System.out.println("================ NOTES ================");
            if (notes != null){
                for (String note : notes){
                    System.out.println(note);
                }
            }
            System.out.println("=======================================");
            meetEnd();
        }
        caughtError = true;
    }

    public String arrowsAtPosition(){
        StringBuilder recreated = new StringBuilder();
        StringBuilder arrows = new StringBuilder();
        int npos = 0;
        String target = "NOTHING:UNKNOWN";
        for (Token i : tokens){
            npos++;
            if (npos >= pos-2 && npos <= pos+4){
                String tokenStr;
                int width;
                if (i.type == TokenType.STRING){
                    tokenStr = " \"" + i.value + "\"";
                    width = i.value.length() + 3;
                } else {
                    tokenStr = " "+i.value;
                    width = i.value.length()+1;
                }

                recreated.append(tokenStr);
                String tt;
                if (npos == pos+1){
                    tt = "^";
                    target = i.value;
                } else {
                    tt = " ";
                }
                arrows.append(tt.repeat(width));
            }
        }
        recreated.append("\t\t<----- "+target).append("\n").append(arrows);
        return recreated.toString();
    }

    public static String readFileContents(Path path) {
        try {
            return Files.readString(path);
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<Token> changeTokensParent(Interpreter interpreter){
        List<Token> s = new ArrayList<>();
        for (Token token : this.tokens){
            token.interpreter = interpreter;
            s.add(token);
        }
        return s;
    }

    public void meetEnd(){
        this.end = true;
        if (!isFunction){
            System.out.println("=======================================");
        }
        // System.exit(0);
    }

    public Interpreter clone(){
        Interpreter intr = new Interpreter();
        intr.setup(data, tokens, text, filePath);
        return intr;
    }
}
