package net.tutla.tums.tusan;

import net.tutla.tums.tusan.Lexer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Interpreter {
    public Object data;
    public String text;
    public String file;
    public List<Token> tokens;

    private static Lexer lexer;

    public Interpreter(){

    }

    public void setup(HashMap<String, Object> data, List<Token> tokens, String text, String file){
        if (data == null){
            this.data = new InterpreterData(null, null, null, null);
        } else {
            this.data = data;
        }
        if (text != null){
            this.text = text;
        }
        if (file != null){
            this.text = readFileContents(file);
            this.file = file;
        } else {
            this.file = "<stdin>";
        }
        if (tokens != null){
            lexer = new Lexer(text, this);
            this.tokens = lexer.classify();
        }

    }

    public void compile(){

    }

    // utils

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
}
