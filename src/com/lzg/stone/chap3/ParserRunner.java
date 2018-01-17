package com.lzg.stone.chap3;

import com.lzg.stone.BasicParser;
import com.lzg.stone.CodeDialog;
import com.lzg.stone.Lexer;
import com.lzg.stone.Token;
import com.lzg.stone.ast.ASTree;

import java.io.FileReader;
import java.io.Reader;

public class ParserRunner {
    public static void main(String[] args) throws Exception {
        Reader reader = new FileReader("src\\com\\lzg\\stone\\chap3\\code");
        Lexer l = new Lexer(reader);
        BasicParser bp = new BasicParser();
        while (l.peek(0) != Token.EOF) {
            ASTree ast = bp.parse(l);
            System.out.println("=> " + ast.toString());
        }
    }
}
