package com.lzg.stone;

import com.lzg.stone.ast.ASTList;
import com.lzg.stone.ast.ASTree;

import java.util.List;

public class NegativeExpr extends ASTList {

    public NegativeExpr(List<ASTree> c) {
        super(c);
    }
    public ASTree operand(){
        return child(0);
    }

    @Override
    public String toString() {
        return "-"+operand();
    }

}
