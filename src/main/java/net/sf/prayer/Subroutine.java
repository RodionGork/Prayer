package net.sf.prayer;

import java.util.*;

public class Subroutine {
    
    private Object[][] lines;
    private String[] args;
    private boolean func;
    private String name;
    
    public Subroutine(String name, Object[][] lines, char[] args, boolean func) {
        this.name = name;
        this.lines = lines;
        this.func = func;
        this.args = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            this.args[i] = Character.toString(args[i]);
        }
    }
    
    public int getLength() {
        return lines.length;
    }
    
    public Object[] getLine(int i) {
        return lines[i];
    }
    
    public String[] getArgs() {
        return args;
    }
    
    public boolean isFunc() {
        return func;
    }
    
    public String getName() {
        return name;
    }
    
}

