package com.lzg.stone;

import com.lzg.stone.ast.ASTList;
import com.lzg.stone.ast.ASTree;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    protected static abstract class Element {
        protected abstract void parse(Lexer lexer, List<ASTree> res)
            throws ParseException;
        protected abstract boolean match(Lexer lexer) throws ParseException;
    }

    protected static class Tree extends Element {
        protected Parser parser;
        protected Tree(Parser p) {
            parser = p;
        }
        protected void parse(Lexer lexer, List<ASTree> res)
            throws ParseException {
            res.add(parser.parse(lexer));
        }
        protected boolean match(Lexer lexer) throws ParseException {
            return parser.match(lexer);
        }
    }

    protected static class OrTree extends Element {
        protected Parser[] parsers;
        protected OrTree(Parser[] p) {
            parsers = p;
        }
        protected void parse(Lexer lexer, List<ASTree> res)
                throws ParseException {
            Parser p = choose(lexer);
            if (p == null) {
                throw new ParseException(lexer.peek(0));
            } else {
                res.add(p.parse(lexer));
            }
        }
        protected boolean match(Lexer lexer) throws ParseException {
            return choose(lexer) != null;
        }
    }

    protected static abstract class Factory {
        protected abstract ASTree make0(Object arg) throws Exception;
        protected ASTree make(Object arg) {
            try {
                return make0(arg);
            } catch (IllegalArgumentException e1) {
                throw e1;
            } catch (Exception e2) {
                throw new RuntimeException(e2);
            }
        }
        protected static Factory getForASTList(Class<? extends ASTree> clazz) {
            Factory f = get(clazz, List.class);
            if (f == null) {
                f = new Factory() {
                    @Override
                    protected ASTree make0(Object arg) throws Exception {
                        List<ASTree> results = (List<ASTree>) arg;
                        if (results.size() == 1) {
                            return results.get(0);
                        } else {
                            return new ASTList(results);
                        }
                    }
                };
                return f;
            }
        }

        public static final String factoryName = "crete";
        protected static Factory get(Class<? extends ASTree> clazz, Class<?> argType) {
            if (clazz == null) {
                return null;
            }
            try {
                final Method m = clazz.getMethod(factoryName,
                        new Class<?>[] {argType});
                return new Factory() {
                    @Override
                    protected ASTree make0(Object arg) throws Exception {
                        return (ASTree)m.invoke(null, arg);
                    }
                };
            } catch (NoSuchMethodException e) {}
            try {
                final Constructor<? extends ASTree> c
                        = clazz.getConstructor(argType);
                return new Factory() {
                    @Override
                    protected ASTree make0(Object arg) throws Exception {
                        return c.newInstance(arg);
                    }
                };
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected List<Element> elements;
    protected Factory factory;

    public Parser(Class<? extends ASTree> clazz) {
        reset(clazz);
    }

    protected Parser(Parser p) {
        elements = p.elements;
        factory = p.factory;
    }

    public ASTree parse(Lexer lexer) throws ParseException {
        ArrayList<ASTree> results = new ArrayList<>();
        for (Element e : elements) {
            e.parse(lexer, results);
        }
        return factory.make(results);
    }

    public Parser reset(Class<? extends ASTree> clazz) {
        elements = new ArrayList<>();
        factory = Factory.getForASTList(clazz);
        return this;
    }
}
