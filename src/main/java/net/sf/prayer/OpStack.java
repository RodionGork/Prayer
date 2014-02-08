package net.sf.prayer;

import java.util.*;

public class OpStack extends Stack<Object> {

    public int popInt() {
        return (Integer) pop();
    }
    
    public String popStr() {
        return (String) pop();
    }
    
    public void pushAll(Object... os) {
        for (Object o : os) {
            push(o);
        }
    }
}

