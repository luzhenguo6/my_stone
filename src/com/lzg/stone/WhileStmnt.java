package com.lzg.stone;

import com.lzg.stone.ast.ASTList;
import com.lzg.stone.ast.ASTree;

import java.util.List;

public class WhileStmnt extends ASTList {

    public WhileStmnt(List<ASTree> lis) {
        super(lis);
    }

    public ASTree condition() {
        return child(0);
    }

    public ASTree body() {
        return child(1);
    }

    public String toString() {
        return "(while " + condition() + " " + body() + ")";
    }

}