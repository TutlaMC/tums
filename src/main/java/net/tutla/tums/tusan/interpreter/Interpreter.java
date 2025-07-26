package net.tutla.tums.tusan.interpreter;

<<<<<<< Updated upstream
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
=======
import net.tutla.tums.tusan.lexer.Lexer;
import net.tutla.tums.tusan.lexer.Token;

import java.util.HashMap;
import java.util.List;
import java.io.*;
>>>>>>> Stashed changes

public class Interpreter {

    private static Lexer lexer;

    public InterpreterData data;
    public String text;
    public String file;
    public List<Token> tokens;


    public Interpreter() {

    }

    public void setup(InterpreterData data, List<Token> tokens, String text, String file){
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


        if (tokens != null){
            lexer = new Lexer(text, this);
            this.tokens = lexer.classify();
        }
    }

    public void compile() {

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
