package net.sf.prayer;

public class Literal implements CharSequence {
    private String string;
    
    public Literal(CharSequence s) {
        string = s.toString();
    }
    
    public char charAt(int i) {
        return string.charAt(i);
    }
    
    public int length() {
        return string.length();
    }
    
    public CharSequence subSequence(int start, int end) {
        return string.substring(start, end);
    }
    
    public String toString() {
        return string;
    }
}

