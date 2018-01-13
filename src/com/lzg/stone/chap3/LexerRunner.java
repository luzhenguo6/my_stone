package com.lzg.stone.chap3;

import com.lzg.stone.CodeDialog;
import com.lzg.stone.Lexer;
import com.lzg.stone.ParseException;
import com.lzg.stone.Token;

public class LexerRunner {
    public static void main(String[] args) throws ParseException {
        Lexer l = new Lexer(new CodeDialog());
        for (Token t; (t = l.read()) != Token.EOF;) {
            System.out.println("=> " + t.getText());
        }
    }
}
