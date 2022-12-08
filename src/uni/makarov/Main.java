package uni.makarov;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static int currSymb = 0;

    public static String currToken = "Undefined";

    public static String currLexem = "Undefined";

    public static List<String> keywordList = Arrays.asList("and", "as", "assert", "break", "class", "continue", "def",
            "del", "elif", "else", "except", "False", "finally", "for", "from", "global", "if", "import",
            "in", "is", "lambda", "None", "nonlocal", "not", "or", "pass", "raise", "return", "True",
            "try", "while", "with", "yield");

    public static List<String> operatorList = Arrays.asList("+", "-", "*", "/", "%", "**", "//", ".", "=", "+=", "-=",
            "*=", "/=", "%=", "//=", "**=", "&=", "|=", "^=", ">>=", "<<=", "==", "!=", ">", "<", ">=", "<=", "&",
            "|", "^", "~", "<<", ">>", "!");

    public static void wordState(String text) {
        currToken = "Identifier";
        StringBuilder symbols = new StringBuilder();
        while ((currSymb < text.length()) &&
                ((text.charAt(currSymb) >= 'A' && text.charAt(currSymb) <= 'z')
                        || (text.charAt(currSymb) >= '0' && text.charAt(currSymb) <= '9')
                        || (text.charAt(currSymb) == '_'))){
            symbols.append(text.charAt(currSymb));
            currSymb++;
        }
        currLexem = symbols.toString();
        if(keywordList.contains(currLexem)){
            currToken = "Keyword";
        }
        System.out.println(currLexem + " - " + currToken);
    }
    public static void numberState(String text) {
        StringBuilder symbols = new StringBuilder();
        while ((currSymb < text.length()) &&
                ((text.charAt(currSymb) >= '0' && text.charAt(currSymb) <= '9')
                || (text.charAt(currSymb) == '.'))){
            symbols.append(text.charAt(currSymb));
            currSymb++;
        }
        currLexem = symbols.toString();
        currToken = "Number";
        System.out.println(currLexem + " - " + currToken);
    }
    public static void spaceState(String text) {
        currLexem = "\" \"";
        currToken = "Space";
        currSymb++;
    }
    public static void operatorState(String text) {
        currToken = "Unidentified";
        StringBuilder symbols = new StringBuilder();
        while ((currSymb < text.length()) &&
                (operatorList.contains(String.valueOf(text.charAt(currSymb))))){
            symbols.append(text.charAt(currSymb));
            currSymb++;
        }
        currLexem = symbols.toString();
        if(operatorList.contains(currLexem)){
            currToken = "Operator";
        }
        System.out.println(currLexem + " - " + currToken);
    }
    public static void characterConstState(String text) {
        currToken = "Character/String Const";
        Character ch = text.charAt(currSymb);
        StringBuilder symbols = new StringBuilder();
        if(text.charAt(currSymb) == '"')
        {
            currSymb++;
            ch = text.charAt(currSymb);
            while (currSymb < text.length() && (!ch.equals('"'))){
                symbols.append(text.charAt(currSymb));
                currSymb++;
                ch = text.charAt(currSymb);
            }
            currLexem = symbols.toString();
        }
        if(text.charAt(currSymb) == '\''){
            currSymb++;
            while (currSymb < text.length() && (!ch.equals('\''))) {
                symbols.append(text.charAt(currSymb));
                currSymb++;
            }
            currLexem = symbols.toString();
        }
        System.out.println(currLexem + " - " + currToken);
        currSymb++;
    }
    public static void commentState(String text) {
        currToken = "Comment";
        Character ch = text.charAt(currSymb);
        StringBuilder symbols = new StringBuilder();
        if(text.charAt(currSymb) == '#'){
            ch = text.charAt(currSymb);
            while(currSymb < text.length() && (!ch.equals('\r'))){
                symbols.append(text.charAt(currSymb));
                currSymb++;
                ch = text.charAt(currSymb);
            }
            currLexem = symbols.toString();
        }
    }
    public static void bracketsState(String text) {
        currToken = "Brackets";
        currLexem = Character.toString(text.charAt(currSymb));
        System.out.println(currLexem + " - " + currToken);
        currSymb++;
    }
    public static void delimeterState(String text) {
        currToken = "Delimeter";
        currLexem = Character.toString(text.charAt(currSymb));
        System.out.println(currLexem + " - " + currToken);
        currSymb++;
    }
    public static void escapeState(String text) {
        currToken = "Escape Character";
        if(text.charAt(currSymb) == '\n'){
            currLexem = "\"\\n\"";
        }
        if(text.charAt(currSymb) == '\r'){
            currLexem = "\"\\r\"";
        }
        if(text.charAt(currSymb) == '\t'){
            currLexem = "\"\\t\"";
        }
        currSymb++;
    }
    public static void undefinedState(String text) {
        currLexem = Character.toString(text.charAt(currSymb));
        currToken = "Undefined lexeme";
        currSymb++;
    }

    public static void main(String[] args) throws IOException {
        String text = new String(Files.readAllBytes(Paths.get("test.py")));
        currSymb = 0;
        while (currSymb < text.length()) {
            if((text.charAt(currSymb) >= 'A' && text.charAt(currSymb) <= 'z') || (text.charAt(currSymb) == '_')){
                wordState(text);
                continue;
            }
            if((text.charAt(currSymb) >= '0' && text.charAt(currSymb) <= '9')){
                numberState(text);
                continue;
            }
            if(operatorList.contains(String.valueOf(text.charAt(currSymb)))){
                operatorState(text);
                continue;
            }
            if((text.charAt(currSymb) == ' ')){
                spaceState(text);
                continue;
            }
            if(text.charAt(currSymb) == '\n' || text.charAt(currSymb) == '\r' || text.charAt(currSymb) == '\t'){
                escapeState(text);
                continue;
            }
            if(text.charAt(currSymb) == '(' || text.charAt(currSymb) == ')' || text.charAt(currSymb) == '[' ||
                    text.charAt(currSymb) == ']' || text.charAt(currSymb) == '{' || text.charAt(currSymb) == '}'){
                bracketsState(text);
                continue;
            }
            if(text.charAt(currSymb) == ':' || text.charAt(currSymb) == ';' || text.charAt(currSymb) == ','){
                delimeterState(text);
                continue;
            }
            if(text.charAt(currSymb) == '\'' || text.charAt(currSymb) == '\"'){
                characterConstState(text);
                continue;
            }
            if(text.charAt(currSymb) == '#'){
                commentState(text);
                continue;
            }
            if(true){
                undefinedState(text);
            }
            currSymb++;
        }
    }
}
