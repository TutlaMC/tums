package net.tutla.tums.tusan.interpreter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import net.tutla.tums.tusan.lang.Tusan;
import net.tutla.tums.tusan.TusanContext;
import net.tutla.tums.tusan.Utils;
import net.tutla.tums.tusan.lexer.*;
import net.tutla.tums.tusan.node.Node;
import net.tutla.tums.tusan.node.Token2NodeMap;
import net.tutla.tums.tusan.tums.HelloNode;
import net.tutla.tums.tusan.tums.TumsTokenType;

public class Interpreter {

    private Tusan tusan = new Tusan();
    private TusanLanguage lang = tusan.getLang();

    private Lexer lexer;
    public Utils util = new Utils();

    public InterpreterData data;
    public TokenManager tokenManager = new TokenManager(this);

    public String text;
    public String file;
    public Object returned;
    public Boolean isFunction = false;

    private Path filePath;

    public Boolean end = false;
    public Boolean caughtError = false;
    public int pos = 0;
    public Token currentToken;


    public Interpreter() {
        tusan.registerLexerRule(new LexerRule(TumsTokenType.HELLO, "\\bhello\\b"));
        tusan.registerNode(HelloNode.class);
    }

    public void setup(InterpreterData data, TokenManager tknmanager, String _text, Path file){
        this.data = data != null ? data : new InterpreterData(null, null, null, null);

        if (_text != null) {
            this.text = _text;
        }
        if (file != null) {
            this.text = readFileContents(file);
            this.file = String.valueOf(file.toAbsolutePath());
            this.filePath = file;
        }
        else {
            this.file = "<stdin>";
        }

        if (tknmanager == null){
            lexer = new Lexer(this.text, this, lang);
            tokenManager.setTokens(lexer.classify());
        } else {
            tokenManager.setTokens(tknmanager.getAll());
            tokenManager.changeTokensParent(this);
        }

        this.currentToken = tokenManager.get(this.pos);
    }

    public void compile() {
        tokenManager.changeTokensParent(this);
        end = false;
        caughtError = false;
        pos = 0;
        currentToken = tokenManager.get(pos);

        /* for (Token t : tokens){
            System.out.print(t.type);
            System.out.print(":");
            System.out.print(t.value);
            System.out.print("\n");
        } */

        // where api testing goes


        /// /////////////////////

        TusanContext ctx = new TusanContext(this);
       tusan.compile(ctx);
    }

    // utils




    public void error(String name, String detail, List<String> notes){
        if (!caughtError){
            System.out.println("================ ERROR ================");
            System.out.println(name+" : "+detail);
            System.out.println("============== POSITION ===============");
            System.out.println(arrowsAtPosition());
            if (notes != null){
                System.out.println("================ NOTES ================");
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
        for (Token i : tokenManager.getAll()){
            npos++;
            if (npos >= pos-2 && npos <= pos+4){
                String tokenStr;
                int width;
                if (i.type == PrebuiltTusanTokenType.STRING){
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



    public void meetEnd(){
        this.end = true;
        if (!isFunction){
            System.out.println("=======================================");
        }
        // System.exit(0);
    }

    public Interpreter clone(){
        Interpreter intr = new Interpreter();
        intr.setup(data, tokenManager, text, filePath);
        return intr;
    }

    public void setLanguage(TusanLanguage lang){
        this.lang = lang;
    }
}
