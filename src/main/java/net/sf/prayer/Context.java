package net.sf.prayer;

import java.util.*;

public class Context {

    private OpStack stack;
    private Map<String, Object> globals;
    private ArrayList<Object[]> locals;
    private Object[] curLocals;
    private int callDepth;
    
    public Context() {
        stack = new OpStack();
        globals = new HashMap<>();
        locals = new ArrayList<>();
        callDepth = 0;
    }
    
    public void increaseCallDepth() {
        while (locals.size() <= callDepth) {
            locals.add(new Object[26]);
        }
        curLocals = locals.get(callDepth);
        callDepth++;
    }
    
    public void decreaseCallDepth() {
        callDepth--;
        curLocals = (callDepth > 0) ? locals.get(callDepth - 1) : null;
    }
    
    public OpStack getStack() {
        return stack;
    }
    
    public void assignVar(String name, Object v) {
        if (name.length() > 1) {
            globals.put(name, v);
        } else {
            curLocals[name.charAt(0) - 'a'] = v;
        }
    }
    
    public Object fetchVar(String name) {
        Object res;
        if (name.length() > 1) {
            res = globals.get(name);
        } else {
            res = curLocals[name.charAt(0) - 'a'];
        }
        return res;
    }
    
}

