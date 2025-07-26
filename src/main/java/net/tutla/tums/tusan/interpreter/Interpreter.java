package net.tutla.tums.tusan.interpreter;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.tutla.tums.tusan.lexer.Lexer;
import net.tutla.tums.tusan.lexer.Token;
import net.tutla.tums.tusan.lexer.TokenType;
import net.tutla.tums.tusan.nodes.Statement;
import net.tutla.tums.tusan.nodes.base.Return;

public class Interpreter {

    private static Lexer lexer;

    public InterpreterData data;
    public String text;
    public String file;
    public List<Token> tokens;
    public Object returned;

    public Boolean end = false;
    public Boolean caughtError = false;
    public int pos = 0;
    public Token currentToken;


    public Interpreter() {

    }

    public void setup(InterpreterData data, List<Token> _tokens, String text, String file){
        this.data = data != null ? data : new InterpreterData(null, null, null, null);

        if (text != null)
            this.text = text;

        if (file != null) {
            this.text = readFileContents(file);
            this.file = file;
        }
        else {
            this.file = "<stdin>";
        }

        if (_tokens == null){
            lexer = new Lexer(text, this);
            this.tokens = lexer.classify();
        } else {
            this.tokens = _tokens;
            this.tokens = change_tokens_parent(this);
        }

        this.currentToken = tokens.get(this.pos);
    }

    public Object compile() {
        end = false;
        caughtError = false;
        System.out.println(tokens);
        for (Token t : tokens){
            System.out.print(t.type);
            System.out.print(":");
            System.out.print(t.value);
            System.out.print("\n");
        }
        System.out.println("======= OUTPUT =======");
        while (pos <= tokens.toArray().length-1){
            if (end){
                return returned;
            }
            if (currentToken.type == TokenType.ENDSCRIPT){ // how did you get here?
                return returned;
            } else if (currentToken.type == TokenType.BREAKSTRUCTURE && currentToken.value.equals("return")){
                this.returned = new Return(currentToken).create();
            } else {
                if (currentToken.type == TokenType.ENDSCRIPT){
                    meetEnd();
                } else {
                    new Statement(currentToken).create();
                }
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
            pos++;
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
        String target = "NOTHING:UNKOWN";
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

    public static String readFileContents(String path) {
        try {
            File nfile = new File(path);
            FileInputStream fis = new FileInputStream(nfile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            byte[] bytes = bis.readAllBytes();
            String contents = new String(bytes);

            bis.close();
            fis.close();
            return contents;
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<Token> change_tokens_parent(Interpreter interpreter){
        List<Token> s = new ArrayList<>();
        for (Token token : this.tokens){
            token.interpreter = this;
            s.add(token);
        }
        return s;
    }

    public void meetEnd(){
        this.end = true;
    }
}
